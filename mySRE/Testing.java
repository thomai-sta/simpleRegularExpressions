package mySRE;

import java.util.ArrayList;

public class Testing
{

  public static void main(String[] args)
  {
    String regex = new String();
    ArrayList<String> matched = new ArrayList<String>();
    if (args.length < 2)
    {
      System.err.println("You need to give a regex and a string to match...");
      return;
    }
    else
    {
      /// Create NFA
      SRE tester = new SRE();
      regex = args[0];
      tester.parse(regex);

      for (int i = 1; i < args.length; i++)
      {
        if (!args[i].equals(args[i].toLowerCase()))
        {
          /// A bit useless. If the string contains uppercase, it will not be
          /// matched anyway
          System.err.println("Ignoring " + args[i]);
        }
        else
        {
          if (tester.matches(args[i]))
          {
            if (args[i].length() == 0)
            {
              matched.add("the empty string");
            }
            else
            {
              matched.add(args[i]);
            }
          }
        }
      }
    }

    /// Presenting stuff!!
    String message = new String();
    message += "Regular expression " + regex + " matches ";
    if (matched.size() != 0)
    {
      for (int i = 0; i < matched.size(); i++)
      {
        if (i == 0)
        {
          message += matched.get(i);
        }
        else if (i == matched.size() - 1)
        {
          message += " and " + matched.get(i);
        }
        else
        {
          message += ", " + matched.get(i);
        }
      }
    }
    else
    {
      message += "none of the given strings";
    }
    System.out.println(message);
  }

}