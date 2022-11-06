package edu.umn.cs.csci3081w.project.model;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

  /**
   * Create a bus with outgoing and incoming routes and three stops per route.
   */
  public static Bus createBus() {
    Stop stop1 = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop1);
    stopsOut.add(stop2);
    stopsOut.add(stop3);
    List<Double> distancesOut = new ArrayList<Double>();
    distancesOut.add(0.9712663713083954);
    distancesOut.add(0.961379387775189);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(.15);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.0);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(probabilitiesOut, stopsOut);
    Route testRouteOut = new Route("testRouteOut", stopsOut, distancesOut, 3, generatorOut);
    List<Stop> stopsIn = new ArrayList<>();
    stopsIn.add(stop3);
    stopsIn.add(stop2);
    stopsIn.add(stop1);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.961379387775189);
    distancesIn.add(0.9712663713083954);
    List<Double> probabilitiesIn = new ArrayList<>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.0);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(probabilitiesIn, stopsIn);
    Route testRouteIn = new Route("testRouteIn", stopsIn, distancesIn, 3, generatorIn);
    Bus bus = new Bus("TestBus", testRouteOut, testRouteIn, 5, 1);
    bus.getBusData().setId(bus.getName());
    return bus;
  }

  /**
   * Create a small bus with outgoing and incoming routes and three stops per route.
   */
  public static SmallBus createSmallBus() {
    Stop stop1 = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop1);
    stopsOut.add(stop2);
    stopsOut.add(stop3);
    List<Double> distancesOut = new ArrayList<Double>();
    distancesOut.add(0.9712663713083954);
    distancesOut.add(0.961379387775189);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(.15);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.0);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(probabilitiesOut, stopsOut);
    Route testRouteOut = new Route("testRouteOut", stopsOut, distancesOut, 3, generatorOut);
    List<Stop> stopsIn = new ArrayList<>();
    stopsIn.add(stop3);
    stopsIn.add(stop2);
    stopsIn.add(stop1);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.961379387775189);
    distancesIn.add(0.9712663713083954);
    List<Double> probabilitiesIn = new ArrayList<>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.0);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(probabilitiesIn, stopsIn);
    Route testRouteIn = new Route("testRouteIn", stopsIn, distancesIn, 3, generatorIn);
    SmallBus bus = new SmallBus("TestBus", testRouteOut, testRouteIn, 1);
    bus.getBusData().setId(bus.getName());
    return bus;
  }

  /**
   * Create a route with three stops.
   */
  public static Route createRoute() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stops = new ArrayList<>(); //for route
    stops.add(stop);
    stops.add(stop2);
    stops.add(stop3);
    List<Double> distances = new ArrayList<>();
    distances.add(0.9712663713083954);
    distances.add(0.961379387775189);
    List<Double> probabilities = new ArrayList<>();
    probabilities.add(.15);
    probabilities.add(0.3);
    probabilities.add(.0);
    PassengerGenerator generator = new RandomPassengerGenerator(probabilities, stops);
    return new Route("TestRoute", stops, distances, 3, generator);
  }

  /**
   * Create a stop.
   */
  public static Stop createStop() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    return stop;
  }

  /**
   * Create a route given the data.
   */
  public static Route createRouteGivenData(List<Stop> stops, List<Double> distances,
                                           List<Double> probabilities) {
    PassengerGenerator generator = new RandomPassengerGenerator(probabilities, stops);
    return new Route("TestRoute", stops, distances, stops.size(), generator);
  }

}
