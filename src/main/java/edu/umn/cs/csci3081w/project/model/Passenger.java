package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;

public class Passenger {
  private static int COUNT;
  private String name;
  private int destinationStopId;
  private int waitAtStop;
  private int timeOnBus;
  private int id;

  /**
   * Constructor for passenger.
   *
   * @param name name of passenger
   * @param destinationStopId destination stop id
   */
  public Passenger(int destinationStopId, String name) {
    this.name = name;
    this.destinationStopId = destinationStopId;
    this.waitAtStop = 0;
    this.timeOnBus = 0;
    this.id = Passenger.COUNT;
    Passenger.COUNT++;
  }

  /**
   * Updates time variables for passenger.
   */
  public void pasUpdate() {
    if (isOnBus()) {
      timeOnBus++;
    } else {
      waitAtStop++;
    }
  }

  public void getOnBus() {
    timeOnBus = 1;
  }

  public int getTotalWait() {
    return waitAtStop + timeOnBus;
  }

  public boolean isOnBus() {
    return timeOnBus > 0;
  }

  public int getDestination() {
    return destinationStopId;
  }

  public String getName() {
    return name;
  }

  /**
   * Report statistics for passenger.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Passenger Info Start####");
    out.println("Name: " + name);
    out.println("Destination: " + destinationStopId);
    out.println("Total wait: " + getTotalWait());
    out.println("Wait at stop: " + waitAtStop);
    out.println("Time on bus: " + timeOnBus);
    out.println("####Passenger Info End####");
  }

}
