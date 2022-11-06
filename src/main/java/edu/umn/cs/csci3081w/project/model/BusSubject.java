package edu.umn.cs.csci3081w.project.model;

import java.util.ArrayList;
import java.util.List;

public abstract class BusSubject {
  protected List<BusObserver> busObservers;

  /**
   * Create a bus subject and initialize observers to be empty.
   *
   */
  public BusSubject() {
    busObservers = new ArrayList<BusObserver>();
  }

  public abstract void registerBusObserver(BusObserver busObserver);

  public abstract void notifyBusObservers();
}
