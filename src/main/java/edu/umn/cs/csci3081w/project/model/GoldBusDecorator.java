package edu.umn.cs.csci3081w.project.model;

public class GoldBusDecorator extends BusDecorator {

  /**
   * For bus on inbound.
   * red=255, green=215, blue=0, and alpha=255.
   * @param busData Used for the constructor
   */
  public GoldBusDecorator(BusData busData) {
    super(busData);
    color.addProperty("red", 255);
    color.addProperty("green", 215);
    color.addProperty("blue", 0);
    color.addProperty("alpha", 255);
  }
}
