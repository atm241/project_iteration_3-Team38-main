package edu.umn.cs.csci3081w.project.model;

public abstract class BusCreator {

  public abstract Bus createBus(String name, Route outbound, Route inbound, double speed);

}
