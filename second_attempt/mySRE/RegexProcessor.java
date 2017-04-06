package mySRE;

import java.util.HashMap;
import java.util.Stack;


public class RegexProcessor
{

  /** Operators precedence map. */
  private static final HashMap<Character, Integer> precedenceMap =
            new HashMap<Character, Integer>();
  static
  {
    precedenceMap.put('(', 1);
    precedenceMap.put('|', 2);
    precedenceMap.put('.', 3);
    precedenceMap.put('*', 4);
  };

  public Integer getPrecedence(Character c)
  {
    if (!precedenceMap.containsKey(c))
    {
      /// Letters get the highest precedence
      return 5;
    }
    return precedenceMap.get(c);
  }

  /**
   * Replace parentheses with '.' as the explicit concatenation operator.
   */
  public static String transformRegex(String sre)
  {
    String res = new String();
    for (int i = 0; i < sre.length(); i++)
    {
      Character c1 = sre.charAt(i);

      if (i + 1 < sre.length())
      {
        Character c2 = sre.charAt(i + 1);

        res += c1;

        if (!c1.equals('(') && !c1.equals('|') &&
            !c2.equals(')') && !c2.equals('|') && !c2.equals('*') )
        {
          res += '.';
        }
      }
    }
    Character last = sre.charAt(sre.length() - 1);
    res += sre.charAt(sre.length() - 1);
    return res;
  }


  /**
   * Convert regular expression from infix to postfix notation using
   * Shunting-yard algorithm.
   *
   * @param regex infix notation
   * @return postfix notation
   */
  public String convertToPostfix(String sre)
  {
    String result = new String();

    Stack<Character> operators = new Stack<Character>();

    String sreDots = transformRegex(sre);

    for (Character c : sreDots.toCharArray())
    {
      switch (c)
      {
        case '(':
          operators.push(c);
          break;
        case ')':
          while(!operators.peek().equals('('))
          {
            result += operators.pop();
          }
          operators.pop(); // Remove parenthesis
          break;
        default:
          while(!operators.empty())
          {
            /// We have an operator. We need to check precedence.
            /// While the top operator has higher precedence, it's added to the
            /// result before the current operator.
            if (getPrecedence(operators.peek()) >= getPrecedence(c))
            {
              result += operators.pop();
            }
            else
            {
              break;
            }
          }
          operators.push(c);
          break;
      }
    }
    while (!operators.empty())
    {
      result += operators.pop();
    }

    return result;
  }
}