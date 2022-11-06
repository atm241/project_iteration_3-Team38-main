package edu.umn.cs.csci3081w.project.model;

public class TimeOfDayConcreteBusCreator extends BusCreator {

  private static final int MORNING_START_TIME = 5;
  private static final int DAY_START_TIME = 8;
  private static final int EVENING_START_TIME = 16;
  private static final int NIGHT_START_TIME = 21;

  private BusDeploymentStrategy strategy;

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
    return strategy.getNextBus(name, outbound, inbound, speed);
  }

  /**
   * Sets the deployment strategy based on simulation time.
   *
   * @param time current time of the simulation
   */
  public void setStrategy(int time) {
    if ((time >= MORNING_START_TIME) && (time < DAY_START_TIME)) {
      this.strategy = new BusDeploymentStrategyMorning();
    } else if ((time >= DAY_START_TIME) && (time < EVENING_START_TIME)) {
      this.strategy = new BusDeploymentStrategyDay();
    } else if ((time >= EVENING_START_TIME) && (time < NIGHT_START_TIME)) {
      this.strategy = new BusDeploymentStrategyEvening();
    } else {
      this.strategy = new BusDeploymentStrategyNight();
    }
  }

  public BusDeploymentStrategy getStrategy() {
    return strategy;
  }

}
