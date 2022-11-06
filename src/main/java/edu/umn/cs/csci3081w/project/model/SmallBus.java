package edu.umn.cs.csci3081w.project.model;

public class SmallBus extends Bus {
  public SmallBus(String name, Route outbound, Route inbound, double speed) {
    super(name, outbound, inbound, 30, speed);
  }
}