package edu.umn.cs.csci3081w.project.model;

public interface BusDeploymentStrategy {
  public Bus getNextBus(String name, Route outbound, Route inbound, double speed);
}
