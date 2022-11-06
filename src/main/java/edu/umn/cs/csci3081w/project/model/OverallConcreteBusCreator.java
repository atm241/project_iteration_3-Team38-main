package edu.umn.cs.csci3081w.project.model;

public class OverallConcreteBusCreator extends BusCreator {
  private BusCreator currentConcreteBusCreator;

  /**
   * Creates a bus according to the current simulation strategy.
   *
   * @param name     parameter for the name of the bus
   * @param outbound parameter for outbound route
   * @param inbound  parameter for inbound route
   * @param speed    parameter for bus speed
   * @return created bus
   */
  public Bus createBus(String name, Route outbound, Route inbound, double speed) {
    return currentConcreteBusCreator.createBus(name, outbound, inbound, speed);
  }

  /**
   * Sets the concrete bus creator for the simulation.
   *
   * @param day  value representing the day of the simulation
   * @param time value representing the time of the simulation
   */
  public void setCurrentConcreteBusCreator(int day, int time) {
    if (day == 1 || day == 15) {
      currentConcreteBusCreator = new RandomConcreteBusCreator();
    } else {
      TimeOfDayConcreteBusCreator timeOfDayConcreteBusCreator =
          new TimeOfDayConcreteBusCreator();
      timeOfDayConcreteBusCreator.setStrategy(time);
      currentConcreteBusCreator = timeOfDayConcreteBusCreator;
    }
  }

  public BusCreator getCurrentConcreteBusCreator() {
    return currentConcreteBusCreator;
  }
}
