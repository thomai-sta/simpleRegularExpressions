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


public final class SRE
{
  State start;


  /**
   * Convert regex into a nondeterministic finite automaton (NFA)
   */
  public static State parse(String sre)
  {
    /// The sre string is already converted to postfix notation
    Stack<Fragment> fragments = new Stack<Fragment>();
    for (int i = 0; i < postSre.length(); i++)
    {
      switch (postSre.charAt(i))
      {
        default:
          /// character a-z
          State s(postSre.charAt(i));
          State dangling("dangling");
          s.next = dangling;
          s.next_1 = null;

          Fragment f = new Fragment(s, s.next);
          fragments.push(f);
          break;
        case ".":
          /// concatenate upper two fragments in stack
          Fragment e2 = fragments.pop();
          Fragment e1 = fragments.pop();
          Fragment patched = connectFragments(e1, e2);
          fragments.push(patched);
          break;
        case "|":
          /// OR
          Fragment e2 = fragments.pop();
          Fragment e1 = fragments.pop();
          State s("split");
          s.next = e1.beginning;
          s.next_1 = e2.beginning;

          /// Concatenate dangling lists
          LinkedList<State> danglingE1 = e1.dangling;
          for (State d : e2.dangling)
          {
            danglingE1.add(d);
          }
          Fragment split(s, danglingE1);
          fragments.push(split);
          break;
        case "*":
          /// Zero or more
          e = fragments.pop();
          State s("split");
          s.next = e.start;

          State s1("dangling");
          s.next_1 = s1;

          Fragment e2(s, s.next_1);
          Fragment f = connectFragments(e, e2);
          fragments.push(f);
          break;
      }

    }
    /// If regex is valid we should have one fragment left. It only needs
    /// a final state to be appended
    State finalState("final");
    Fragment e1 = fragments.pop();
    Fragment e2 = Fragment(finalState, null);
    Fragment nfa = connectFragments(e1, e2);

    start = nfa.beginning;

    return start;
  }



  public Fragment connectFragments(Fragment e1, Fragment e2)
  {
    for (State s : e1.dangling)
    {
      s.next = e2.beginning;
    }

    Fragment patchedFragment = new Fragment(e1.beginning, e2.dangling);
    return patchedFragment;
  }

  public boolean matches(String s)
  {
    // TODO: implement the SRE checker
    throw new UnsupportedOperationException();
  }

  public static void main(String[] args)
  {
    String regex = new String();
    String toMatch = new String();
    if (args.length() < 2)
    {
      System.out.println("You need to give a regex and a string to match");
      return;
    }
    else
    {
      regex = args[0];
      toMatch = args[1];
    }
    RegexProcessor processor;
    String regexDot = processor.transformRegex(regex);
    String regexPost = processor.convertToPostfix(regexDot);



  }

}
