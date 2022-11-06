package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RouteTest {

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Test route constructor.
   */
  @Test
  public void testRouteConstructorNormal() {
    Route testRoute = TestUtils.createRoute();
    assertEquals("TestRoute", testRoute.getName());
    assertEquals(0, testRoute.getDestinationStopIndex());
    assertEquals(0.0, testRoute.getNextStopDistance());
    assertEquals(1.9326457590835844, testRoute.getTotalRouteDistance());
    RouteData testRouteData = testRoute.getRouteData();
    assertEquals("", testRouteData.getId());
    assertTrue(testRouteData.getStops().isEmpty());
  }

  /**
   * Test shallow copy of a route.
   */
  @Test
  public void testShallowCopy() {
    Route testRoute = TestUtils.createRoute();
    Route shallowCopyRoute = testRoute.shallowCopy();
    assertEquals(testRoute.getName(), shallowCopyRoute.getName());
    assertEquals(testRoute.getDestinationStopIndex(), shallowCopyRoute.getDestinationStopIndex());
    assertEquals(testRoute.getNextStopDistance(), shallowCopyRoute.getNextStopDistance());
    assertEquals(testRoute.getTotalRouteDistance(), shallowCopyRoute.getTotalRouteDistance());
    assertTrue(testRoute.getStops().get(0) == shallowCopyRoute.getStops().get(0));
    assertTrue(testRoute.getStops().get(1) == shallowCopyRoute.getStops().get(1));
    assertTrue(testRoute.getStops().get(2) == shallowCopyRoute.getStops().get(2));
  }

  /**
   * Test update route.
   */
  @Test
  public void testUpdateRoute() {
    try {
      Stop stop = new Stop(0, 44.972392, -93.243774); //stops for routes
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
      Route testRoute = TestUtils.createRouteGivenData(stops, distances, probabilities);
      Passenger passenger = new Passenger(1, "Goldy");
      stop.addPassengers(passenger);
      int initialWait = passenger.getTotalWait();
      assertEquals(0, initialWait);
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      System.setOut(testStream);
      testRoute.update();
      int updatedWait = passenger.getTotalWait();
      assertEquals(1, updatedWait);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test report route info.
   */
  @Test
  public void testRouteReportWithStops() throws IOException {
    Route testRoute = TestUtils.createRoute();
    final Charset charset = StandardCharsets.UTF_8;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream testStream = new PrintStream(outputStream, true, charset.name());
    testRoute.report(testStream);
    String data = new String(outputStream.toByteArray(), charset);
    testStream.close();
    outputStream.close();
    String strToCompare =
        "####Route Info Start####" + System.lineSeparator()
            + "Name: TestRoute" + System.lineSeparator()
            + "Num stops: 3" + System.lineSeparator()
            + "****Stops Info Start****" + System.lineSeparator()
            + "++++Next Stop Info Start++++" + System.lineSeparator()
            + "####Stop Info Start####" + System.lineSeparator()
            + "ID: 0" + System.lineSeparator()
            + "****Passengers Info Start****" + System.lineSeparator()
            + "Num passengers waiting: 0" + System.lineSeparator()
            + "****Passengers Info End****" + System.lineSeparator()
            + "####Stop Info End####" + System.lineSeparator()
            + "++++Next Stop Info End++++" + System.lineSeparator()
            + "####Stop Info Start####" + System.lineSeparator()
            + "ID: 1" + System.lineSeparator()
            + "****Passengers Info Start****" + System.lineSeparator()
            + "Num passengers waiting: 0" + System.lineSeparator()
            + "****Passengers Info End****" + System.lineSeparator()
            + "####Stop Info End####" + System.lineSeparator()
            + "####Stop Info Start####" + System.lineSeparator()
            + "ID: 2" + System.lineSeparator()
            + "****Passengers Info Start****" + System.lineSeparator()
            + "Num passengers waiting: 0" + System.lineSeparator()
            + "****Passengers Info End****" + System.lineSeparator()
            + "####Stop Info End####" + System.lineSeparator()
            + "****Stops Info End****" + System.lineSeparator()
            + "####Route Info End####" + System.lineSeparator();
    assertEquals(strToCompare, data);
    assertEquals(strToCompare, data);
  }


  /**
   * Test route is not at the end.
   */
  @Test
  public void testRouteNotAtEnd() {
    Route testRoute = TestUtils.createRoute();
    boolean atEnd = testRoute.isAtEnd();
    assertFalse(atEnd);
  }

  /**
   * Test previous stop at beginning.
   */
  @Test
  public void testPreviousStopAtBeginning() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stops = new ArrayList<>();
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
    Route testRoute = TestUtils.createRouteGivenData(stops, distances, probabilities);
    Stop prevStop = testRoute.prevStop();
    assertEquals(stop, prevStop);
  }

  /**
   * Test previous stop not at beginning.
   */
  @Test
  public void testPreviousStopNotAtBeginning() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    List<Stop> stops = new ArrayList<>();
    stops.add(stop);
    List<Double> distances = new ArrayList<>();
    distances.add(0.9712663713083954);
    distances.add(0.961379387775189);
    List<Double> probabilities = new ArrayList<>();
    probabilities.add(.15);
    probabilities.add(0.3);
    probabilities.add(.0);
    Route testRoute = TestUtils.createRouteGivenData(stops, distances, probabilities);
    testRoute.toNextStop();
    testRoute.toNextStop();
    Stop prevStop = testRoute.prevStop();
    assertTrue(testRoute.isAtEnd());
    assertEquals(stop, prevStop);
  }

  /**
   * Test to next stop at beginning.
   */
  @Test
  public void testNextStopAtBeginning() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stops = new ArrayList<>();
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
    Route testRoute = TestUtils.createRouteGivenData(stops, distances, probabilities);
    testRoute.toNextStop();
    int destinationStopIndex = testRoute.getDestinationStopIndex();
    Stop nextStop = testRoute.getDestinationStop();
    assertEquals(stop2, nextStop);
    assertEquals(1, destinationStopIndex);
  }

  /**
   * Test total route distance.
   */
  @Test
  public void testGetTotalRouteDistance() {
    //Check if stop prints out the correct thing - use streams
    Route testRoute = TestUtils.createRoute();
    double dist = testRoute.getTotalRouteDistance();
    assertEquals(1.9326457590835844, dist);
  }

  /**
   * Test get next stop at the beginning.
   */
  @Test
  public void testGetNextStopDistanceAtBeginning() {
    //Check if stop prints out the correct thing - use streams
    Route testRoute = TestUtils.createRoute();
    double dist = testRoute.getNextStopDistance();
    assertEquals(0.0, dist);
  }

  /**
   * Test generate passenger.
   */
  @Test
  public void testGeneratePassengers() {
    Route testRoute = TestUtils.createRoute();
    testRoute.toNextStop();
    int numPass = testRoute.generateNewPassengers();
    assertEquals(2, numPass);
  }

  /**
   * Test route data is when not updated.
   */
  @Test
  public void testGetRouteDataNotUpdated() {
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
    Route testRoute = TestUtils.createRouteGivenData(stops, distances, probabilities);
    RouteData testRouteData = testRoute.getRouteData();
    assertEquals("", testRouteData.getId());
    assertTrue(testRouteData.getStops().isEmpty());
  }
}
