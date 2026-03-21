
from __future__ import annotations

if False:  # MYPY
    from typing import Any, Dict, List, Union, Optional, Iterator  # NOQA


merge_attrib = '_yaml_merge'


class MergeValue:
    attrib = merge_attrib

    def __init__(self) -> None:
        self.value: List[Any] = []
        self.sequence = None
        self.merge_pos: Optional[int] = None  # position of merge in the mapping

    def __getitem__(self, index: Any) -> Any:
        return self.value[index]

    def __setitem__(self, index: Any, val: Any) -> None:
        self.value[index] = val

    def __repr__(self) -> Any:
        return f'MergeValue({self.value!r})'

    def __len__(self) -> Any:
        return len(self.value)

    def append(self, elem: Any) -> Any:
        self.value.append(elem)

    def extend(self, elements: Any) -> None:
        self.value.extend(elements)

    def set_sequence(self, seq: Any) -> None:
        # print('mergevalue.set_sequence node', node.anchor)
        self.sequence = seq
