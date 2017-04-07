# simpleRegularExpressions

Implementation of a Simple Regular Expression parser and matcher.
The regex can contain:
- [a-z] lower-case latin characters
- '*' zero or more matches of the preceding expression
- '|' alternative expressions
- '(', ')' grouping expressions

The implementation follows the structure of Russ Cox's article (https://swtch.com/~rsc/regexp/regexp1.html) [suggested reading material], who implements Thompson's Construction Algorithm.

The regex is first converted from infix to postfix notation and then it is used to produce a non-deterministic finite automaton (NFA). The NFA will then accept or reject a string that needs to be matched.


To compile: run "compile.sh" script.
To execute: run "run_mySRE.sh" script
To give input: edit "run_mySRE.sh" script. The command should look like: 
      java -cp classes mySRE.Testing "regex" "string1" "string2" "string3" "string4"...
