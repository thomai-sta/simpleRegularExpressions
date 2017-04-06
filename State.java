package mySRE;

public class State
{
  private String status; /// a-z, "split", "final", "dangling"
  State next, next_1; /// max two next states in case of a split

  public State(String status)
  {
    this.status = status;
  }


}