package edu.umn.cs.csci3081w.project.model;

import com.google.gson.JsonObject;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Bus implements BusObserver {
  private PassengerUnloader unloader;
  private PassengerLoader loader;
  private List<Passenger> passengers;
  private int passengerMaxCapacity;
  private String name;
  //the speed is in distance over a time unit
  private double speed;
  private Route outgoingRoute;
  private Route incomingRoute;
  private double distanceRemaining;
  private Stop nextStop;
  private BusData busData;
  private ConcreteBusSubject concreteBusSubject;

  /**
   * Constructor for a bus.
   *
   * @param name name of bus
   * @param out  outbound route
   * @param in inbound route
   * @param capacity capacity of bus
   * @param speed speed of bus
   */
  public Bus(String name, Route out, Route in, int capacity, double speed) {
    this.name = name;
    this.outgoingRoute = out;
    this.incomingRoute = in;
    this.passengerMaxCapacity = capacity;
    this.speed = speed;
    this.distanceRemaining = 0;
    this.nextStop = out.getDestinationStop();
    this.unloader = new PassengerUnloader();
    this.loader = new PassengerLoader();
    this.passengers = new ArrayList<Passenger>();
    this.busData = new BusData();
  }

  /**
   * Report statistics for the bus.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Bus Info Start####");
    out.println("Name: " + name);
    out.println("Speed: " + speed);
    out.println("Distance to next stop: " + distanceRemaining);
    out.println("****Passengers Info Start****");
    out.println("Num of passengers: " + passengers.size());
    for (Passenger pass : passengers) {
      pass.report(out);
    }
    out.println("****Passengers Info End****");
    out.println("####Bus Info End####");
  }

  public boolean isTripComplete() {
    return outgoingRoute.isAtEnd() && incomingRoute.isAtEnd();
  }

  public boolean loadPassenger(Passenger newPassenger) {
    return loader.loadPassenger(newPassenger, passengerMaxCapacity, passengers);
  }

  /**
   * Moves the bus on its route.
   *
   * @return if the bus moved
   */
  public boolean move() {
    // update passengers FIRST
    // new passengers will get "updated" when getting on the bus
    for (Passenger passenger : passengers) {
      passenger.pasUpdate();
    }
    //actually move
    double speed = updateDistance();
    if (!isTripComplete() && distanceRemaining <= 0) {
      //load & unload
      handleBusStop();

      //switch to next stop
      toNextStop();
    }
    return (speed > 0);
  }

  public void update() {
    move();
    updateBusData();
  }

  /**
   * Updates bus data with new details.
   */
  public void updateBusData() {
    busData.setId(name);
    // Get the correct route and early exit
    Route currentRoute = outgoingRoute;

    if (outgoingRoute.isAtEnd()) {
      currentRoute = incomingRoute;
    }

    if (!outgoingRoute.isAtEnd() && !incomingRoute.isAtEnd()) {
      busData = new MaroonBusDecorator(busData);
    } else {
      busData = new GoldBusDecorator(busData);
    }

    Stop prevStop = currentRoute.prevStop();
    Stop nextStop = currentRoute.getDestinationStop();
    double distanceBetween = currentRoute.getNextStopDistance();
    // the ratio shows us how far from the previous stop are we in a ratio from 0 to 1
    double ratio;
    // check if we are at the first stop
    if (distanceBetween - 0.00001 < 0) {
      ratio = 1;
    } else {
      ratio = distanceRemaining / distanceBetween;
      if (ratio < 0) {
        ratio = 0;
        distanceRemaining = 0;
      }
    }
    Position p = new Position(0.0, 0.0);
    p.setXcoordLoc(nextStop.getLongitude() * (1 - ratio) + prevStop.getLongitude() * ratio);
    p.setYcoordLoc(nextStop.getLatitude() * (1 - ratio) + prevStop.getLatitude() * ratio);
    busData.setPosition(p);
    busData.setNumPassengers(passengers.size());
    busData.setCapacity(passengerMaxCapacity);
  }

  /**
   * Retrieves the current bus information and send the information to the visualization module.
   *
   */
  public void displayInfo() {
    JsonObject data = new JsonObject();
    data.addProperty("command", "observeBus");
    String text = "";
    text += "Bus " + busData.getId() + System.lineSeparator();
    text += "-----------------------------" + System.lineSeparator();
    text += "  * Position: (" + busData.getPosition().getXcoordLoc() + ","
        + busData.getPosition().getYcoordLoc()
        + ")" + System.lineSeparator();
    text += "  * Passengers: " + busData.getNumPassengers() + System.lineSeparator();
    text += "  * Capacity: " + busData.getCapacity() + System.lineSeparator();
    data.addProperty("text", text);
    concreteBusSubject.getSession().sendJson(data);
  }

  public BusData getBusData() {
    return busData;
  }

  public String getName() {
    return name;
  }

  public Stop getNextStop() {
    return nextStop;
  }

  public long getNumPassengers() {
    return passengers.size();
  }

  public int getCapacity() {
    return passengerMaxCapacity;
  }

  public Route getOutgoingRoute() {
    return outgoingRoute;
  }

  public Route getIncomingRoute() {
    return incomingRoute;
  }

  public double getSpeed() {
    return speed;
  }

  public void setConcreteBusSubject(
      ConcreteBusSubject concreteBusSubject) {
    this.concreteBusSubject = concreteBusSubject;
  }

  private int unloadPassengers() {
    return unloader.unloadPassengers(passengers, nextStop);
  }

  private void handleBusStop() {
    // This function handles arrival at a bus stop
    int passengersHandled = 0;
    // unloading passengers
    passengersHandled += unloadPassengers();
    // loading passengers
    passengersHandled += nextStop.loadPassengers(this);

    distanceRemaining = 0;
  }

  private void toNextStop() {
    //current stop
    currentRoute().toNextStop();
    if (!isTripComplete()) {
      // it's important we call currentRoute() again,
      // as nextStop() may have caused it to change.
      nextStop = currentRoute().getDestinationStop();
      distanceRemaining +=
          currentRoute().getNextStopDistance();
      // note, if distanceRemaining was negative because we
      // had extra time left over, that extra time is
      // effectively counted towards the next stop
    } else {
      nextStop = null;
      distanceRemaining = 999;
    }
  }

  private double updateDistance() {
    // Updates the distance remaining and returns the effective speed of the bus
    // Bus does not move if speed is negative or bus is at end of route
    if (isTripComplete()) {
      return 0;
    }
    if (speed < 0) {
      return 0;
    }
    distanceRemaining -= speed;
    return speed;
  }

  private Route currentRoute() {
    // Figure out if we're on the outgoing or incoming route
    if (!outgoingRoute.isAtEnd()) {
      return outgoingRoute;
    }
    return incomingRoute;
  }
}
