import sys

PY2 = (sys.version_info[0] == 2)

if PY2:
    from thread import get_ident
else:
    from _thread import get_ident


def recursive_repr(fillvalue='...'):
    'Decorator to make a repr function return fillvalue for a recursive call'

    def decorating_function(user_function):
        repr_running = set()

        def wrapper(self):
            key = id(self), get_ident()
            if key in repr_running:
                return fillvalue
            repr_running.add(key)
            try:
                result = user_function(self)
            finally:
                repr_running.discard(key)
            return result

        # Can't use functools.wraps() here because of bootstrap issues
        wrapper.__module__ = getattr(user_function, '__module__')
        wrapper.__doc__ = getattr(user_function, '__doc__')
        wrapper.__name__ = getattr(user_function, '__name__')
        if not PY2:
            wrapper.__qualname__ = getattr(user_function, '__qualname__')
            wrapper.__annotations__ = getattr(user_function, '__annotations__', {})
        return wrapper

    return decorating_function
