package mySRE;

public class Fragment
{
  State beginning;
  LinkedList<State> dangling = new LinkedList<State>();

  public Fragment(State beginning, State out)
  {
    this.beginning = beginning;
    dangling.add(out);
  }


}