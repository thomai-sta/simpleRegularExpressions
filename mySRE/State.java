package mySRE;

/**
 * A state of the NFA. It is defined by:
 *   -  its status, which can be a letter (a-z), the label "split" for when the
 *      state leads to two different states and the "final" for the final state
 *      of the NFA
 *   -  its "next" states (first and second), to which the current state points
 */

public class State
{
  String status; /// a-z, "split", "final"
  State nextFirst, nextSecond; /// max two next states in case of a split

  public State(String status)
  {
    this.status = status;
    nextFirst = null;
    nextSecond = null;
  }

  public State(String status, State nextFirst)
  {
    this.status = status;
    this.nextFirst = nextFirst;
    nextSecond = null;
  }

  public State(String status, State nextFirst, State nextSecond)
  {
    this.status = status;
    this.nextFirst = nextFirst;
    this.nextSecond = nextSecond;
  }

}