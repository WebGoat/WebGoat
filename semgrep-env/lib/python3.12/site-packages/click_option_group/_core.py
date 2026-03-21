import collections
import inspect
import weakref
from typing import (
    Any,
    Callable,
    Dict,
    List,
    Mapping,
    Optional,
    Sequence,
    Set,
    Tuple,
    Union,
)

import click
from click.core import augment_usage_errors

from ._helpers import (
    get_callback_and_params,
    get_fake_option_name,
    raise_mixing_decorators_error,
    resolve_wrappers,
)

FC = Union[Callable, click.Command]


class GroupedOption(click.Option):
    """Represents grouped (related) optional values

    The class should be used only with `OptionGroup` class for creating grouped options.

    :param param_decls: option declaration tuple
    :param group: `OptionGroup` instance (the group for this option)
    :param attrs: additional option attributes
    """

    def __init__(
        self,
        param_decls: Optional[Sequence[str]] = None,
        *,
        group: "OptionGroup",
        **attrs: Any,
    ):
        super().__init__(param_decls, **attrs)

        for attr in group.forbidden_option_attrs:
            if attr in attrs:
                msg = f"'{attr}' attribute is not allowed for '{type(group).__name__}' option `{self.name}'."
                raise TypeError(msg)

        self.__group = group

    @property
    def group(self) -> "OptionGroup":
        """Returns the reference to the group for this option

        :return: `OptionGroup` the group instance for this option
        """
        return self.__group

    def handle_parse_result(
        self,
        ctx: click.Context,
        opts: Mapping[str, Any],
        args: List[str],
    ) -> Tuple[Any, List[str]]:
        with augment_usage_errors(ctx, param=self):
            if not ctx.resilient_parsing:
                self.group.handle_parse_result(self, ctx, opts)
        return super().handle_parse_result(ctx, opts, args)

    def get_help_record(self, ctx: click.Context) -> Optional[Tuple[str, str]]:
        help_record = super().get_help_record(ctx)
        if help_record is None:
            # this happens if the option is hidden
            return help_record

        opts, opt_help = help_record

        formatter = ctx.make_formatter()
        with formatter.indentation():
            indent = " " * formatter.current_indent
            return f"{indent}{opts}", opt_help


class _GroupTitleFakeOption(click.Option):
    """The helper `Option` class to display option group title in help"""

    def __init__(
        self,
        param_decls: Optional[Sequence[str]] = None,
        *,
        group: "OptionGroup",
        **attrs: Any,
    ) -> None:
        self.__group = group
        super().__init__(param_decls, hidden=True, expose_value=False, help=group.help, **attrs)

        # We remove parsed opts for the fake options just in case.
        # For example it is workaround for correct click-repl autocomplete
        self.opts = []
        self.secondary_opts = []

    def get_help_record(self, ctx: click.Context) -> Optional[Tuple[str, str]]:
        return self.__group.get_help_record(ctx)


class OptionGroup:
    """Option group manages grouped (related) options

    The class is used for creating the groups of options. The class can de used as based class to implement
    specific behavior for grouped options.

    :param name: the group name. If it is not set the default group name will be used
    :param help: the group help text or None
    """

    def __init__(
        self,
        name: Optional[str] = None,
        *,
        hidden: bool = False,
        help: Optional[str] = None,
    ) -> None:
        self._name = name if name else ""
        self._help = inspect.cleandoc(help if help else "")
        self._hidden = hidden

        self._options: Mapping[Any, Any] = collections.defaultdict(weakref.WeakValueDictionary)
        self._group_title_options = weakref.WeakValueDictionary()

    @property
    def name(self) -> str:
        """Returns the group name or empty string if it was not set

        :return: group name
        """
        return self._name

    @property
    def help(self) -> str:
        """Returns the group help or empty string if it was not set

        :return: group help
        """
        return self._help

    @property
    def name_extra(self) -> List[str]:
        """Returns extra name attributes for the group"""
        return []

    @property
    def forbidden_option_attrs(self) -> List[str]:
        """Returns the list of forbidden option attributes for the group"""
        return []

    def get_help_record(self, ctx: click.Context) -> Optional[Tuple[str, str]]:
        """Returns the help record for the group

        :param ctx: Click Context object
        :return: the tuple of two fileds: `(name, help)`
        """
        if all(o.hidden for o in self.get_options(ctx).values()):
            return None

        name = self.name
        help_ = self.help if self.help else ""

        extra = ", ".join(self.name_extra)
        if extra:
            extra = f"[{extra}]"

        if name:
            name = f"{name}: {extra}"
        elif extra:
            name = f"{extra}:"

        if not name and not help_:
            return None

        return name, help_

    def option(self, *param_decls: str, **attrs: Any) -> Callable:
        """Decorator attaches a grouped option to the command

        The decorator is used for adding options to the group and to the Click-command
        """

        def decorator(func: FC) -> FC:
            option_attrs = attrs.copy()
            option_attrs.setdefault("cls", GroupedOption)
            if self._hidden:
                option_attrs.setdefault("hidden", self._hidden)

            if not issubclass(option_attrs["cls"], GroupedOption):
                msg = "'cls' argument must be a subclass of 'GroupedOption' class."
                raise TypeError(msg)

            self._check_mixing_decorators(func)
            func = click.option(*param_decls, group=self, **option_attrs)(func)
            self._option_memo(func)

            # Add the fake invisible option to use for print nice title help for grouped options
            self._add_title_fake_option(func)

            return func

        return decorator

    def get_options(self, ctx: click.Context) -> Dict[str, GroupedOption]:
        """Returns the dictionary with group options"""
        return self._options.get(resolve_wrappers(ctx.command.callback), {})

    def get_option_names(self, ctx: click.Context) -> List[str]:
        """Returns the list with option names ordered by addition in the group"""
        return list(reversed(list(self.get_options(ctx))))

    def get_error_hint(self, ctx: click.Context, option_names: Optional[Set[str]] = None) -> str:
        options = self.get_options(ctx)
        text = ""

        for name, opt in reversed(list(options.items())):
            if option_names and name not in option_names:
                continue
            text += f"  {opt.get_error_hint(ctx)}\n"

        if text:
            text = text[:-1]

        return text

    def handle_parse_result(self, option: GroupedOption, ctx: click.Context, opts: Mapping[str, Any]) -> None:
        """The method should be used for adding specific behavior and relation for options in the group"""

    def _check_mixing_decorators(self, func: Callable) -> None:
        func, params = get_callback_and_params(func)

        if not params or func not in self._options:
            return

        last_param = params[-1]
        title_option = self._group_title_options[func]
        options = self._options[func]

        if last_param.name != title_option.name and last_param.name not in options:
            raise_mixing_decorators_error(last_param, func)

    def _add_title_fake_option(self, func: FC) -> None:
        callback, params = get_callback_and_params(func)

        if callback not in self._group_title_options:
            func = click.option(get_fake_option_name(), group=self, cls=_GroupTitleFakeOption)(func)

            _, params = get_callback_and_params(func)
            self._group_title_options[callback] = params[-1]

        title_option = self._group_title_options[callback]
        last_option = params[-1]

        if title_option.name != last_option.name:
            # Hold title fake option on the top of the option group
            title_index = params.index(title_option)
            params[-1], params[title_index] = params[title_index], params[-1]

    def _option_memo(self, func: Callable) -> None:
        func, params = get_callback_and_params(func)
        option = params[-1]
        self._options[func][option.name] = option

    def _group_name_str(self) -> str:
        return f"'{self.name}'" if self.name else "the"


class RequiredAnyOptionGroup(OptionGroup):
    """Option group with required any options of this group

    `RequiredAnyOptionGroup` defines the behavior: At least one option from the group must be set.
    """

    @property
    def forbidden_option_attrs(self) -> List[str]:
        return ["required"]

    @property
    def name_extra(self) -> List[str]:
        return [*super().name_extra, "required_any"]

    def handle_parse_result(self, option: GroupedOption, ctx: click.Context, opts: Mapping[str, Any]) -> None:
        if option.name in opts:
            return

        if all(o.hidden for o in self.get_options(ctx).values()):
            cls_name = self.__class__.__name__
            group_name = self._group_name_str()

            msg = f"Need at least one non-hidden option in {group_name} option group ({cls_name})."
            raise TypeError(msg)

        option_names = set(self.get_options(ctx))

        if not option_names.intersection(opts):
            group_name = self._group_name_str()
            option_info = self.get_error_hint(ctx)

            msg = f"At least one of the following options from {group_name} option group is required:\n{option_info}"
            raise click.UsageError(
                msg,
                ctx=ctx,
            )


class RequiredAllOptionGroup(OptionGroup):
    """Option group with required all options of this group

    `RequiredAllOptionGroup` defines the behavior: All options from the group must be set.
    """

    @property
    def forbidden_option_attrs(self) -> List[str]:
        return ["required", "hidden"]

    @property
    def name_extra(self) -> List[str]:
        return [*super().name_extra, "required_all"]

    def handle_parse_result(self, option: GroupedOption, ctx: click.Context, opts: Mapping[str, Any]) -> None:
        option_names = set(self.get_options(ctx))

        if not option_names.issubset(opts):
            group_name = self._group_name_str()
            required_names = option_names.difference(option_names.intersection(opts))
            option_info = self.get_error_hint(ctx, required_names)

            msg = f"Missing required options from {group_name} option group:\n{option_info}"
            raise click.UsageError(
                msg,
                ctx=ctx,
            )


class MutuallyExclusiveOptionGroup(OptionGroup):
    """Option group with mutually exclusive behavior for grouped options

    `MutuallyExclusiveOptionGroup` defines the behavior:
        - Only one or none option from the group must be set
    """

    @property
    def forbidden_option_attrs(self) -> List[str]:
        return ["required"]

    @property
    def name_extra(self) -> List[str]:
        return [*super().name_extra, "mutually_exclusive"]

    def handle_parse_result(self, option: GroupedOption, ctx: click.Context, opts: Mapping[str, Any]) -> None:
        option_names = set(self.get_options(ctx))
        given_option_names = option_names.intersection(opts)
        given_option_count = len(given_option_names)

        if given_option_count > 1:
            group_name = self._group_name_str()
            option_info = self.get_error_hint(ctx, given_option_names)

            msg = f"Mutually exclusive options from {group_name} option group cannot be used at the same time:\n{option_info}"
            raise click.UsageError(
                msg,
                ctx=ctx,
            )


class RequiredMutuallyExclusiveOptionGroup(MutuallyExclusiveOptionGroup):
    """Option group with required and mutually exclusive behavior for grouped options

    `RequiredMutuallyExclusiveOptionGroup` defines the behavior:
        - Only one required option from the group must be set
    """

    @property
    def name_extra(self) -> List[str]:
        return [*super().name_extra, "required"]

    def handle_parse_result(self, option: GroupedOption, ctx: click.Context, opts: Mapping[str, Any]) -> None:
        super().handle_parse_result(option, ctx, opts)

        option_names = set(self.get_option_names(ctx))
        given_option_names = option_names.intersection(opts)

        if len(given_option_names) == 0:
            group_name = self._group_name_str()
            option_info = self.get_error_hint(ctx)

            msg = (
                f"Missing one of the required mutually exclusive options from {group_name} option group:\n{option_info}"
            )
            raise click.UsageError(
                msg,
                ctx=ctx,
            )


class AllOptionGroup(OptionGroup):
    """Option group with required all/none options of this group

    `AllOptionGroup` defines the behavior:
        - All options from the group must be set or None must be set
    """

    @property
    def forbidden_option_attrs(self) -> List[str]:
        return ["required", "hidden"]

    @property
    def name_extra(self) -> List[str]:
        return [*super().name_extra, "all_or_none"]

    def handle_parse_result(self, option: GroupedOption, ctx: click.Context, opts: Mapping[str, Any]) -> None:
        option_names = set(self.get_options(ctx))

        if not option_names.isdisjoint(opts) and option_names.intersection(opts) != option_names:
            group_name = self._group_name_str()
            option_info = self.get_error_hint(ctx)

            msg = f"All options from {group_name} option group should be specified or none should be specified. Missing required options:\n{option_info}"
            raise click.UsageError(
                msg,
                ctx=ctx,
            )
