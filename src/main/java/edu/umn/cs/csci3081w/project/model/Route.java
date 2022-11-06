package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Route {
  private PassengerGenerator generator;
  private List<Stop> stops = new ArrayList<Stop>();
  private List<Double> distancesBetween = new ArrayList<Double>();
  private String name;
  private int numStops;
  private int destinationStopIndex;
  private Stop destinationStop;
  private RouteData routeData;

  /**
   * Route constructor.
   *
   * @param name name of route
   * @param stops stops on this route
   * @param distances distances between stops of this route
   * @param numStops Number of stops on this route
   * @param generator Passenger generating object
   */
  public Route(String name, List<Stop> stops, List<Double> distances, int numStops,
               PassengerGenerator generator) {
    for (int i = 0; i < numStops; i++) {
      this.stops.add(stops.get(i));
    }
    for (int i = 0; i < numStops - 1; i++) {
      this.distancesBetween.add(distances.get(i));
    }
    this.name = name;
    this.generator = generator;
    this.numStops = numStops;
    this.destinationStopIndex = 0;
    this.destinationStop = stops.get(0);
    this.routeData = new RouteData();


  }

  public int getDestinationStopIndex() {
    return destinationStopIndex;
  }

  public String getName() {
    return name;
  }

  public List<Stop> getStops() {
    return stops;
  }

  public RouteData getRouteData() {
    return routeData;
  }

  public void setRouteData(RouteData routeData) {
    this.routeData = routeData;
  }

  /**
   * Shallow copies a route.
   * This method shallow copies a route as stops are shared
   * across copied routes
   *
   * @return Copy of route
   */
  public Route shallowCopy() {
    List<Stop> stops = new ArrayList<Stop>();
    for (int i = 0; i < this.stops.size(); i++) {
      stops.add(this.stops.get(i));
    }
    List<Double> distances = new ArrayList<Double>();
    for (int i = 0; i < numStops - 1; i++) {
      distances.add(this.distancesBetween.get(i));
    }
    Route shallowCopy = new Route(this.name, stops, distances, this.numStops, this.generator);
    shallowCopy.setRouteData(this.getRouteData());
    return shallowCopy;
  }

  /**
   * Updates and generates passengers on the route.
   */
  public void update() {
    generateNewPassengers();
    Iterator<Stop> stopIter = this.stops.iterator();
    while (stopIter.hasNext()) {
      stopIter.next().update();
    }
    updateRouteData();
  }

  /**
   * Report statistics for the route.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Route Info Start####");
    out.println("Name: " + this.name);
    out.println("Num stops: " + this.numStops);
    int stopCounter = 0;
    Iterator<Stop> stopIter = this.stops.iterator();
    out.println("****Stops Info Start****");
    //calling all Stop's report methods
    while (stopIter.hasNext()) {
      if (stopCounter == this.destinationStopIndex) {
        out.println("++++Next Stop Info Start++++");
      }
      stopIter.next().report(out);
      if (stopCounter == this.destinationStopIndex) {
        out.println("++++Next Stop Info End++++");
      }
      stopCounter++;
    }
    out.println("****Stops Info End****");
    out.println("####Route Info End####");
  }

  public boolean isAtEnd() {
    return destinationStopIndex >= numStops;
  }

  /**
   * Returns previous stop.
   *
   * @return previous stop
   */
  public Stop prevStop() {
    if (destinationStopIndex == 0) {
      return this.stops.get(0);
    } else if (destinationStopIndex < numStops) {
      return this.stops.get(destinationStopIndex - 1);
    } else {
      return this.stops.get(stops.size() - 1);
    }
  }

  /**
   * Updates destinationStop to next stop.
   */
  public void toNextStop() {
    destinationStopIndex++;
    if (destinationStopIndex < numStops) {
      destinationStop = stops.get(destinationStopIndex);
    } else {
      destinationStop = stops.get(stops.size() - 1);
    }
  }

  /**
   * Returns destination stop.
   *
   * @return destination Stop
   */
  public Stop getDestinationStop() {
    return destinationStop;
  }

  /**
   * Returns total route distance.
   *
   * @return distance
   */
  public Double getTotalRouteDistance() {
    double distancesDouble = 0;
    for (int i = 0; i < numStops - 1; i++) {
      distancesDouble += distancesBetween.get(i);
    }
    return distancesDouble;
  }

  /**
   * Computes distance to next stop.
   *
   * @return distance
   */
  public Double getNextStopDistance() {
    if (destinationStopIndex > 0) {
      return distancesBetween.get(destinationStopIndex - 1);
    } else {
      return 0.0;
    }
  }

  /**
   * Returns and generates passengers.
   *
   * @return number of generated passengers
   */
  public int generateNewPassengers() {
    // returning number of passengers added by generator
    return this.generator.generatePassengers();
  }

  /**
   * Updates route data class.
   */
  public void updateRouteData() {
    routeData.setId(name);
    List<StopData> stopDataList = new ArrayList<StopData>();
    Iterator<Stop> stopIter = this.stops.iterator();
    while (stopIter.hasNext()) {
      Stop currentStop = stopIter.next();
      StopData stopData = new StopData();
      stopData.setId(String.valueOf(currentStop.getId()));
      Position c = new Position();
      c.setXcoordLoc(currentStop.getLongitude());
      c.setYcoordLoc(currentStop.getLatitude());
      stopData.setPosition(c);
      stopData.setNumPeople(currentStop.getNumPassengersPresent());
      stopDataList.add(stopData);
    }
    routeData.setStops(stopDataList);
  }
}
