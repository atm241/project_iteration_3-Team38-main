package edu.umn.cs.csci3081w.project.model;

public class BusDeploymentStrategyNight implements BusDeploymentStrategy {

  /**
   * Returns small busses.
   *
   * @param name parameter for the name of the bus
   * @param outbound parameter for outbound route
   * @param inbound parameter for inbound route
   * @param speed parameter for bus speed
   * @return the next bus according to the strategy
   */
  public Bus getNextBus(String name, Route outbound, Route inbound, double speed) {
    return new SmallBus(name, outbound, inbound, speed);
  }
}
