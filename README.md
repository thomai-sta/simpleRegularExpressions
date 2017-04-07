# simpleRegularExpressions

Implementation of a Simple Regular Expression parser and matcher.
The regex can contain:
- [a-z] lower-case latin characters
- '*' zero or more matches of the preceding expression
- '|' alternative expressions
- '(', ')' grouping expressions

The implementation follows the structure of Russ Cox's article (https://swtch.com/~rsc/regexp/regexp1.html) [suggested reading material], who implements Thompson's Construction Algorithm.
