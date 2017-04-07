// Simple Regular Expressions
//
// The simple regular expressions (SRE) we are here concerned with here are a
// subset of the regular expressions implemented in most modern programming
// languages.
// They can be specified using only the following characters:
//   - 'a' 'b' 'c' ... 'z' (the 26 lower-case latin letters)
//   - '*'      : for zero or more matches of the preceding expression
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

package mySRE;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Stack;
import java.util.LinkedList;


public final class SRE
{
  static State start;

  /**
   * Converts the SRE into a non-deterministic finite automaton (NFA).
   * This implementation
   * It follows the representation standards defined by Russ Cox in
   * https://swtch.com/~rsc/regexp/regexp1.html, who in turns implements the
   * Thompson's construction algorithm.
   * This implementation needs the regex to be in postfix notation.
   * @param SRE string in infix notation
   */
  public static void parse(String sre)
  {
    isValid(sre);
    /// First convert SRE to postfix notation
    RegexProcessor processor = new RegexProcessor();
    String regexPost = processor.convertToPostfix(sre);
    Stack<Fragment> fragments = new Stack<Fragment>();

    /// Start building the NFA
    for (int i = 0; i < regexPost.length(); i++)
    {
      Character c = regexPost.charAt(i);
      switch (c)
      {
        case '*':
          /// Zero or any number of repetitions
          Fragment toBeStarred = fragments.pop();
          /// Create a split state to be put before the fragment. Its nextFirst
          /// will lead to the beginning of this fragment. Its nextSecond will
          /// be dangling, to implement the "zero-times"
          State splitState = new State("split", toBeStarred.beginning);
          /// All dangling nodes of this fragment will be directed to the split
          /// state in the beginning of the fragment to implement the repetition
          for (State dangling : toBeStarred.danglingStates)
          {
            if (dangling.nextFirst == null)
            {
              dangling.nextFirst = splitState;
            }
            else
            {
              /// If it is a split state
              dangling.nextSecond = splitState;
            }
          }

          fragments.push(new Fragment(splitState, splitState));

          break;
        case '.':
          /// Concatenate
          Fragment last = fragments.pop();
          Fragment secondToLast = fragments.pop();
          /// All dangling nodes of the second to last fragment will be directed
          /// to the beginning of the last
          for (State dangling : secondToLast.danglingStates)
          {
            if (dangling.nextFirst == null)
            {
              dangling.nextFirst = last.beginning;
            }
            else
            {
              dangling.nextSecond = last.beginning;
            }
          }

          /// New fragment has the beginning of the second to last and the
          /// dangling states of the last
          fragments.push(new Fragment(secondToLast.beginning,
                                      last.danglingStates));

          break;
        case '|':
          /// or
          Fragment f1 = fragments.pop();
          Fragment f2 = fragments.pop();
          /// Create a split state to be located before the fragments. Its next
          /// states will lead to the beginnings of f1 and f2.
          State splitStateOr = new State("split", f1.beginning, f2.beginning);

          /// The dangling states of the two fragments are appended
          LinkedList<State> newDanglingStates = new LinkedList<State>();
          for (State dangling : f1.danglingStates)
          {
            newDanglingStates.add(dangling);
          }
          for (State dangling : f2.danglingStates)
          {
            newDanglingStates.add(dangling);
          }

          fragments.push(new Fragment(splitStateOr, newDanglingStates));

          break;
        default:
          /// Letter. It becomes a state and a fragment with itself as dangling
          State sNew = new State(String.valueOf(c));
          Fragment fSingle = new Fragment(sNew, sNew);
          fragments.push(fSingle);
          break;
      }
    }

    /// At this point there should be only one fragment left, which is the
    /// entire NFA. We simply add a final state at the end and we keep the
    /// beginning.
    Fragment nfa = fragments.pop();
    if (!fragments.isEmpty())
    {
      System.err.println("SOMETHING WENT WRONG WHILE BUILDING THE NFA...");
      System.exit(-1);
    }

    State finalState = new State("final");
    for (State dangling : nfa.danglingStates)
    {
      if (dangling.nextFirst == null)
      {
        dangling.nextFirst = finalState;
      }
      else
      {
        dangling.nextSecond = finalState;
      }
    }

    start = nfa.beginning;
  }


  /**
   * Checks if the given regex contains only lowercase latin letters and only
   * the sumbols: '*', '.' and '|'.
   * @param string containing regex
   */
  public static void isValid(String regex)
  {
    if (!regex.equals(regex.toLowerCase()))
    {
      System.err.println("Regex contains uppercase letters, which are not permitted...");
      System.exit(-1);
    }

    for (Character c : regex.toCharArray())
    {
      if ("abcdefghijklmnopqrstuvwxyz*|()".indexOf(c) == -1)
      {
        System.err.println("'" + c + "' is not permitted. Only lower-case latin letters and the signs '*', '|' and '.' are permitted.");
        System.exit(-1);
      }
    }
  }


  /**
   * Traverses the NFA and checks if the given string is a valid string,
   * according to this NFA.
   * For each possible path we keep track of the state we are located and the
   * string characters that are consumed.
   * If we reach the final state having consumed the entire string, then it is
   * a match.
   * @param string to be matched
   * @return boolean match or not
   */
  public static boolean matches(String s)
  {
    ConcurrentLinkedQueue<State> states = new ConcurrentLinkedQueue<State>();
    ConcurrentLinkedQueue<Integer> consumed = new ConcurrentLinkedQueue<Integer>();

    states.add(start);
    consumed.add(0);

    State currentState = new State();
    int consumedChars;
    Character c;

    while(states.size() != 0)
    {
      currentState = states.poll();
      consumedChars = consumed.poll();

      if (currentState.status.equals("split"))
      {
        /// Move to the two next states without consuming a character
        states.add(currentState.nextFirst);
        consumed.add(consumedChars);
        states.add(currentState.nextSecond);
        consumed.add(consumedChars);
      }
      else if (currentState.status.equals("final") &&
               consumedChars == s.length())
      {
        return true;
      }
      else
      {
        /// We are at a letter state
        if (consumedChars < s.length())
        {
          c = s.charAt(consumedChars);
          if (currentState.status.equals(String.valueOf(c)))
          {
            states.add(currentState.nextFirst);   /// Only split states have two next
            consumed.add(consumedChars + 1);
          }
        }
      }
    }

    return false;
  }
}
