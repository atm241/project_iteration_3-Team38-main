package edu.umn.cs.csci3081w.project.model;

public class BusDeploymentStrategyEvening implements BusDeploymentStrategy {
  private int count = 0;

  /**
   * Returns busses in the following order: small, regular, large, small,
   * regular, large, etc. (the sequence keeps repeating).
   *
   * @param name     parameter for the name of the bus
   * @param outbound parameter for outbound route
   * @param inbound  parameter for inbound route
   * @param speed    parameter for bus speed
   * @return the next bus according to the strategy
   */
  public Bus getNextBus(String name, Route outbound, Route inbound, double speed) {
    int busType = count % 3;
    count++;
    if (busType == 0) {
      return new SmallBus(name, outbound, inbound, speed);
    } else if (busType == 1) {
      return new RegularBus(name, outbound, inbound, speed);
    } else {
      return new LargeBus(name, outbound, inbound, speed);
    }
  }
}
