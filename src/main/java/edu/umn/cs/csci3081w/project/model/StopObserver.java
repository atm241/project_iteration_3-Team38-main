package edu.umn.cs.csci3081w.project.model;

import edu.umn.cs.csci3081w.project.webserver.MyWebServerSession;

public interface StopObserver {
  public void displayInfo();

  public void setConcreteStopSubject(ConcreteStopSubject concreteStopSubject);
}
