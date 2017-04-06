package mySRE;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class RegexProcessor
{

  /** Operators precedence map. */
  private final HashMap<Character, Integer> precedenceMap =
            new HashMap<Character, Integer>();
  static
  {
    // precedenceMap.put('(', 1);
    precedenceMap.put('|', 1);
    precedenceMap.put('.', 2);
    precedenceMap.put('*', 3);
  };

  public Integer getPrecedence(Character c)
  {
    return precedenceMap.get(c);
  }

  /**
   * Replace parentheses with '.' as the explicit concatenation operator.
   */
  private static String transformRegex(String sre)
  {
    String res = new String();
    for (int i = 0; i < sre.length(); i++)
    {
      Character c1 = sre.charAt(i);

      if (i + 1 < sre.length())
      {
        Character c2 = sre.charAt(i + 1);

        /// Remove parentheses
        if (!c1.equals('(') && !c1.equals(')'))
        {
          res += c1;
        }

        if (!c1.equals('(') && !c1.equals('|') &&
            !c2.equals(')') && !c2.equals('|') && !c2.equals('*') )
        {
          res += '.';
        }
      }
    }
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
      if (!c.equals('|') && !c.equals('.') && !c.equals('*'))
      {
        /// We have an operand. Simply append to the result.
        result += c;
      }
      else
      {
        /// We have an operator. We need to check precedence.
        /// While the top operator has higher precedence, it's added to the
        /// result before the current operator.
        while (!operators.empty())
        {
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
      }
    }

    while (operators.size() > 0)
    {
      result += operators.pop();
    }

    return result;
  }
}