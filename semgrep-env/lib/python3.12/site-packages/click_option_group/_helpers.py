import random
import string
from typing import Callable, List, NoReturn, Tuple, TypeVar

import click

F = TypeVar("F", bound=Callable)

FAKE_OPT_NAME_LEN = 30


def get_callback_and_params(func) -> Tuple[Callable, List[click.Option]]:
    """Returns callback function and its parameters list

    :param func: decorated function or click Command
    :return: (callback, params)
    """
    if isinstance(func, click.Command):
        params = func.params
        func = func.callback
    else:
        params = getattr(func, "__click_params__", [])

    func = resolve_wrappers(func)
    return func, params


def get_fake_option_name(name_len: int = FAKE_OPT_NAME_LEN, prefix: str = "fake") -> str:
    return f"--{prefix}-" + "".join(random.choices(string.ascii_lowercase, k=name_len))


def raise_mixing_decorators_error(wrong_option: click.Option, callback: Callable) -> NoReturn:
    error_hint = wrong_option.opts or [wrong_option.name]

    msg = f"Grouped options must not be mixed with regular parameters while adding by decorator. Check decorator position for {error_hint} option in '{callback.__name__}'."
    raise TypeError(msg)


def resolve_wrappers(f: F) -> F:
    """Get the underlying function behind any level of function wrappers."""
    return resolve_wrappers(f.__wrapped__) if hasattr(f, "__wrapped__") else f
