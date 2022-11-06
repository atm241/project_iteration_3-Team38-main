package edu.umn.cs.csci3081w.project.model;

import java.util.ArrayList;
import java.util.List;

public abstract class StopSubject {
  protected List<StopObserver> stopObservers;

  /**
   * Create a stop subject and initialize observers to be empty.
   *
   */
  public StopSubject() {
    stopObservers = new ArrayList<StopObserver>();
  }

  public abstract void registerStopObserver(StopObserver stopObserver);

  public abstract void notifyStopObservers();
}
