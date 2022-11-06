package edu.umn.cs.csci3081w.project.model;

import java.util.Random;

public class RandomConcreteBusCreator extends BusCreator {

  private Random random;

  /**
   * Creates a random bus creator.
   */
  public RandomConcreteBusCreator() {
    random = new Random();
  }

  /**
   * Creates the bus type randomly.
   *
   * @param name     parameter for the name of the bus
   * @param outbound parameter for outbound route
   * @param inbound  parameter for inbound route
   * @param speed    parameter for bus speed
   * @return created bus
   */
  public Bus createBus(String name, Route outbound, Route inbound, double speed) {
    int busType = random.nextInt(3);
    if (busType == 0) {
      return new SmallBus(name, outbound, inbound, speed);
    } else if (busType == 1) {
      return new RegularBus(name, outbound, inbound, speed);
    } else {
      return new LargeBus(name, outbound, inbound, speed);
    }
  }
}
