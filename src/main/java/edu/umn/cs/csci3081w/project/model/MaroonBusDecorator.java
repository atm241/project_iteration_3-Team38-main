package edu.umn.cs.csci3081w.project.model;

public class MaroonBusDecorator extends BusDecorator {

  /**
   * For bus on outbound.
   * red=128, green=0, blue=0, and alpha=255.
   * @param busData Used for the constructor
   */
  public MaroonBusDecorator(BusData busData) {
    super(busData);
    color.addProperty("red", 128);
    color.addProperty("green", 0);
    color.addProperty("blue", 0);
    color.addProperty("alpha", 255);
  }

}
