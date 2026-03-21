import collections
import inspect
import warnings
from typing import Any, Callable, Dict, List, NamedTuple, Optional, Tuple, Type, TypeVar

import click

from ._core import OptionGroup
from ._helpers import (
    get_callback_and_params,
    raise_mixing_decorators_error,
)

T = TypeVar("T")
F = TypeVar("F", bound=Callable)


class OptionStackItem(NamedTuple):
    param_decls: Tuple[str, ...]
    attrs: Dict[str, Any]
    param_count: int


class _NotAttachedOption(click.Option):
    """The helper class to catch grouped options which were not attached to the group

    Raises TypeError if not attached options exist.
    """

    def __init__(self, param_decls=None, *, all_not_attached_options, **attrs):
        super().__init__(param_decls, expose_value=False, hidden=False, is_eager=True, **attrs)
        self._all_not_attached_options = all_not_attached_options

    def handle_parse_result(self, ctx, opts, args):
        options_error_hint = ""
        for option in reversed(self._all_not_attached_options[ctx.command.callback]):
            options_error_hint += f"  {option.get_error_hint(ctx)}\n"
        options_error_hint = options_error_hint[:-1]

        msg = f"Missing option group decorator in '{ctx.command.name}' command for the following grouped options:\n{options_error_hint}\n"
        raise TypeError(msg)


class _OptGroup:
    """A helper class to manage creating groups and group options via decorators

    The class provides two decorator-methods: `group`/`__call__` and `option`.
    These decorators should be used for adding grouped options. The class have
    single global instance `optgroup` that should be used in most cases.

    The example of usage::

        ...
        @optgroup('Group 1', help='option group 1')
        @optgroup.option('--foo')
        @optgroup.option('--bar')
        @optgroup.group('Group 2', help='option group 2')
        @optgroup.option('--spam')
        ...
    """

    def __init__(self) -> None:
        self._decorating_state: Dict[Callable, List[OptionStackItem]] = collections.defaultdict(list)
        self._not_attached_options: Dict[Callable, List[click.Option]] = collections.defaultdict(list)
        self._outer_frame_index = 1

    def __call__(
        self,
        name: Optional[str] = None,
        *,
        help: Optional[str] = None,
        cls: Optional[Type[OptionGroup]] = None,
        **attrs,
    ):
        """Creates a new group and collects its options

        Creates the option group and registers all grouped options
        which were added by `option` decorator.

        :param name: Group name or None for default name
        :param help: Group help or None for empty help
        :param cls: Option group class that should be inherited from `OptionGroup` class
        :param attrs: Additional parameters of option group class
        """
        try:
            self._outer_frame_index = 2
            return self.group(name, help=help, cls=cls, **attrs)
        finally:
            self._outer_frame_index = 1

    def group(
        self,
        name: Optional[str] = None,
        *,
        help: Optional[str] = None,
        cls: Optional[Type[OptionGroup]] = None,
        **attrs: Any,
    ) -> Callable[[F], F]:
        """The decorator creates a new group and collects its options

        Creates the option group and registers all grouped options
        which were added by `option` decorator.

        :param name: Group name or None for default name
        :param help: Group help or None for empty help
        :param cls: Option group class that should be inherited from `OptionGroup` class
        :param attrs: Additional parameters of option group class
        """

        if not cls:
            cls = OptionGroup
        elif not issubclass(cls, OptionGroup):
            msg = "'cls' must be a subclass of 'OptionGroup' class."
            raise TypeError(msg)

        def decorator(func: F) -> F:
            callback, params = get_callback_and_params(func)

            if callback not in self._decorating_state:
                frame = inspect.getouterframes(inspect.currentframe())[self._outer_frame_index]
                lineno = frame.lineno

                with_name = f' "{name}"' if name else ""
                warnings.warn(
                    (
                        f"The empty option group{with_name} was found (line {lineno}) "
                        f'for "{callback.__name__}". The group will not be added.'
                    ),
                    RuntimeWarning,
                    stacklevel=2,
                )
                return func

            option_stack = self._decorating_state.pop(callback)

            [params.remove(opt) for opt in self._not_attached_options.pop(callback)]
            self._check_mixing_decorators(callback, option_stack, self._filter_not_attached(params))

            attrs["help"] = help

            try:
                option_group = cls(name, **attrs)
            except TypeError as err:
                message = str(err).replace("__init__()", f"'{cls.__name__}' constructor")
                raise TypeError(message) from err

            for item in option_stack:
                func = option_group.option(*item.param_decls, **item.attrs)(func)

            return func

        return decorator

    def option(self, *param_decls: str, **attrs: Any) -> Callable[[F], F]:
        """The decorator adds a new option to the group

        The decorator is lazy. It adds option decls and attrs.
        All options will be registered by `group` decorator.

        :param param_decls: option declaration tuple
        :param attrs: additional option attributes and parameters
        """

        def decorator(func: F) -> F:
            callback, params = get_callback_and_params(func)

            option_stack = self._decorating_state[callback]
            params = self._filter_not_attached(params)

            self._check_mixing_decorators(callback, option_stack, params)
            self._add_not_attached_option(func, param_decls)
            option_stack.append(OptionStackItem(param_decls, attrs, len(params)))

            return func

        return decorator

    def help_option(self, *param_decls: str, **attrs: Any) -> Callable[[F], F]:
        """This decorator adds a help option to the group, which prints
        the command's help text and exits.
        """
        if not param_decls:
            param_decls = ("--help",)

        attrs.setdefault("is_flag", True)
        attrs.setdefault("is_eager", True)
        attrs.setdefault("expose_value", False)
        attrs.setdefault("help", "Show this message and exit.")

        if "callback" not in attrs:

            def callback(ctx, _, value):
                if not value or ctx.resilient_parsing:
                    return
                click.echo(ctx.get_help(), color=ctx.color)
                ctx.exit()

            attrs["callback"] = callback

        return self.option(*param_decls, **attrs)

    def _add_not_attached_option(self, func, param_decls) -> None:
        click.option(
            *param_decls,
            all_not_attached_options=self._not_attached_options,
            cls=_NotAttachedOption,
        )(func)

        callback, params = get_callback_and_params(func)
        self._not_attached_options[callback].append(params[-1])

    @staticmethod
    def _filter_not_attached(options: List[T]) -> List[T]:
        return [opt for opt in options if not isinstance(opt, _NotAttachedOption)]

    @staticmethod
    def _check_mixing_decorators(callback, options_stack, params):
        if options_stack:
            last_state = options_stack[-1]

            if len(params) > last_state.param_count:
                raise_mixing_decorators_error(params[-1], callback)


optgroup = _OptGroup()
"""Provides decorators for creating option groups and adding grouped options

Decorators:
    - `group` is used for creating an option group
    - `option` is used for adding options to a group

Example::

    @optgroup.group('Group 1', help='option group 1')
    @optgroup.option('--foo')
    @optgroup.option('--bar')
    @optgroup.group('Group 2', help='option group 2')
    @optgroup.option('--spam')
"""
