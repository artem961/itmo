import json
import yaml
from lark import Lark, Transformer

# Грамматика для JSON
json_grammar = r"""
    ?value: dict
          | list
          | string
          | SIGNED_NUMBER      -> number
          | "true"             -> true
          | "false"            -> false
          | "null"             -> null

    list : "[" [value ("," value)*] "]"

    dict : "{" [pair ("," pair)*] "}"
    pair : string ":" value

    string : ESCAPED_STRING

    %import common.ESCAPED_STRING
    %import common.SIGNED_NUMBER
    %import common.WS
    %ignore WS
    """


class tree_to_dict(Transformer):
    def string(self, s):
        (s,) = s
        return s[1:-1]

    def number(self, n):
        (n,) = n
        return float(n)

    list = list
    pair = tuple
    dict = dict

    def true(self, _):
        return True

    def false(self, _):
        return False

    def null(self, _):
        return None


def json_to_yaml(json_str: str) -> str:
    parser = Lark(json_grammar, start='value', parser="lalr")
    data = parser.parse(json_str)
    data = tree_to_dict().transform(data)
    out = yaml.dump(data, allow_unicode=True)
    return out
