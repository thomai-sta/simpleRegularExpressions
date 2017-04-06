package mySRE;

import java.util.HashMap;
import java.util.Stack;


public class RegexProcessor
{

  private static final HashMap<Character, Integer> precedenceMap = new HashMap<Character, Integer>();
  static
  {
    precedenceMap.put('(', 1);
    precedenceMap.put('|', 2);
    precedenceMap.put('.', 3);
    precedenceMap.put('*', 4);
  };

  private static Integer maxPrecedence = 5;



  /**
   * Returns the precedence of the given character.
   * @param character c
   * @return character's precedence
   */
  public Integer getPrecedence(Character c)
  {
    if (!precedenceMap.containsKey(c))
    {
      /// Letters get the highest precedence
      return maxPrecedence;
    }
    return precedenceMap.get(c);
  }

  /**
   * Converts a regular expression from "user-friendly" notation to postfix
   * notation using the Shunting-yard algorithm.
   *
   * @param regex "user-friendly" notation
   * @return regex postfix notation
   */
  public String convertToPostfix(String sre)
  {
    /// First insert explicit concatenation '.' signs.
    String sreDots = new String();
    for (int i = 0; i < sre.length(); i++)
    {
      Character first = sre.charAt(i);

      if (i + 1 < sre.length())
      {
        Character second = sre.charAt(i + 1);

        sreDots += first;

        /// Between every pair of cosecutive letters, add a '.'
        if (!first.equals('(') && !first.equals('|') &&
            !second.equals(')') && !second.equals('|') && !second.equals('*') )
        {
          sreDots += '.';
        }
      }
    }
    Character last = sre.charAt(sre.length() - 1);
    sreDots += sre.charAt(sre.length() - 1);

    /// Now convert to postfix notation
    String result = new String();

    Stack<Character> operators = new Stack<Character>();

    for (int i = 0; i < sreDots.length(); i++)
    {
      Character c = sreDots.charAt(i);
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