package edu.umn.cs.csci3081w.project.model;

public class LargeBus extends Bus {
  public LargeBus(String name, Route outbound, Route inbound, double speed) {
    super(name, outbound, inbound, 90, speed);
  }
}
