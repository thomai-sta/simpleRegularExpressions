package mySRE;

// Simple Regular Expressions
//
// The simple regular expressions (SRE) we are here concerned with here are a subset
// of the regular expressions implemented in most modern programming languages.
// They can be specified using only the following characters:
//   - 'a' 'b' 'c' ... 'z' (the 26 lower-case latin letters)
//   - '*'      : for expressing zero of more matches of the preceding expression
//   - '|'      : for alternative expressions
//   - '(' ')'  : for grouping expressions
//
// Examples:
//   a  matches a
//   ab matches ab
//   a(b|c) matches ab and ac
//   a(b|c)* matches a, ab, abc, abbbbb, abbccc, ...
//   a*(b|c)* matches the empty string, a, ab, aaaac, b, c, abbcb, ...
//   ((((a)))) matches a
//   ((((a))*)) matches the empty string, a, aa, ...
//
// We call the set of strings, matching a given expression, its language.
//
// The task is to implement a method to check if a given string is in the language
// of a given expression.
//
// The solution must be implemented using Java SE (any version is acceptable).
// It may not be implemented in terms of the java.util.regex package,
// the method String.matches(String), nor any external library providing similar functionality.
// It may span multiple classes and files.

import java.util.Stack;



public final class SRE {

  String srePostFix;

  public static SRE parse(String sre)
  {
    RegexProcessor processor = new RegexProcessor();
    srePostFix = processor.convertToPostfix(sre);
  }

  public boolean matches(String s)
  {
    Stack<String> regProcessed = new Stack<String>();

    int regIdx = 0, strIdx = 0;

    while (regIdx < srePostFix.length() && strIdx < s.length())
    {

    }

  }

}
