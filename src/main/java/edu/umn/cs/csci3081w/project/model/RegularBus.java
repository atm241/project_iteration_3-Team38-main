package edu.umn.cs.csci3081w.project.model;

public class RegularBus extends Bus {
  public RegularBus(String name, Route outbound, Route inbound, double speed) {
    super(name, outbound, inbound, 60, speed);
  }
}
