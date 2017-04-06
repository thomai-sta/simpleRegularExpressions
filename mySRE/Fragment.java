package mySRE;

import java.util.LinkedList;


/**
 * Simulates a partial NFA. It is defined by the first state (beginning) and a
 * list of "final" states that wait to get connected (dangling states)
 */

public class Fragment
{
  State beginning;
  LinkedList<State> danglingStates = new LinkedList<State>();

  public Fragment(State beginning, State danglingState)
  {
    this.beginning = beginning;
    danglingStates.add(danglingState);
  }

  public Fragment(State beginning, LinkedList<State> danglingStates)
  {
    this.beginning = beginning;
    this.danglingStates = danglingStates;
  }
}