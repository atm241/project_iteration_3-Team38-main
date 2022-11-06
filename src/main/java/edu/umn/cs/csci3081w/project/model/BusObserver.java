package edu.umn.cs.csci3081w.project.model;

public interface BusObserver {
  public void displayInfo();

  public void setConcreteBusSubject(ConcreteBusSubject concreteBusSubject);
}
