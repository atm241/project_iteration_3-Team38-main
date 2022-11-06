package edu.umn.cs.csci3081w.project.model;

public abstract class BusDecorator extends BusData {

  public BusDecorator(BusData busData) {
    super(busData.getId(), busData.getPosition(), busData.getNumPassengers(),
        busData.getCapacity());
  }
}
