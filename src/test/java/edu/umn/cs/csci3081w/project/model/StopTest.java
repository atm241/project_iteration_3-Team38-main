package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.webserver.MyWebServerSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class StopTest {

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
   * Testing state after using constructor.
   */
  @Test
  public void testConstructorNormal() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    assertEquals(0, stop.getId());
    assertEquals(44.972392, stop.getLongitude());
    assertEquals(-93.243774, stop.getLatitude());
    assertEquals(0, stop.getNumPassengersPresent());
  }

  /**
   * Testing number of passengers.
   */
  @Test
  public void testGetNumPassengerPresent() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    Passenger passenger = new Passenger(1, "Goldy");
    stop.addPassengers(passenger);
    Passenger passenger1 = new Passenger(2, "President");
    stop.addPassengers(passenger1);
    assertEquals(2, stop.getNumPassengersPresent());
  }

  /**
   * Testing state of stop after adding passenger.
   */
  @Test
  public void testAddPassengers() {
    Passenger passenger = new Passenger(1, "Goldy");
    Stop stop = new Stop(0, 44.972392, -93.243774);
    int added = stop.addPassengers(passenger);
    assertEquals(1, added);
    assertEquals(1, stop.getNumPassengersPresent());
  }

  /**
   * Tests if passengers can be loaded onto an empty bus correctly from an empty stop
   * nothing should happen, loadPassengers returns 0.
   */
  @Test
  public void testLoadPassengersEmptyStopEmptyBus() {
    Bus testBus = TestUtils.createBus();
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(0, passengersLoaded);
    long passengersOnBus = testBus.getNumPassengers();
    assertEquals(0, passengersOnBus);
  }

  /**
   * Tests if passengers can be loaded onto an full bus correctly from an empty stop
   * nothing should happen, loadPassengers returns 0.
   */
  @Test
  public void testLoadPassengersEmptyStopFullBus() {
    Bus testBus = TestUtils.createBus();
    Passenger passenger = new Passenger(1, "Goldy");
    Passenger passenger2 = new Passenger(1, "Gopher");
    Passenger passenger3 = new Passenger(1, "The Entire School");
    Passenger passenger4 = new Passenger(1, "Walked onto");
    Passenger passenger5 = new Passenger(1, "This Bus");
    testBus.loadPassenger(passenger);
    testBus.loadPassenger(passenger2);
    testBus.loadPassenger(passenger3);
    testBus.loadPassenger(passenger4);
    testBus.loadPassenger(passenger5);
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(0, passengersLoaded);
    long passengersOnBus = testBus.getNumPassengers();
    assertEquals(5, passengersOnBus);
  }

  /**
   * Tests if passengers can be loaded onto a full bus correctly from a non-empty stop
   * nothing should happen, loadPassengers returns 0.
   */
  @Test
  public void testLoadPassengersNonEmptyStopFullBus() {
    Bus testBus = TestUtils.createBus();
    Passenger passenger = new Passenger(1, "Goldy");
    Passenger passenger2 = new Passenger(1, "Gopher");
    Passenger passenger3 = new Passenger(1, "The Entire School");
    Passenger passenger4 = new Passenger(1, "Walked onto");
    Passenger passenger5 = new Passenger(1, "This Bus");
    testBus.loadPassenger(passenger);
    testBus.loadPassenger(passenger2);
    testBus.loadPassenger(passenger3);
    testBus.loadPassenger(passenger4);
    testBus.loadPassenger(passenger5);
    Passenger passenger6 = new Passenger(1, "I never leave the stop");
    testBus.getNextStop().addPassengers(passenger6);
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(0, passengersLoaded);
    long passengersOnBus = testBus.getNumPassengers();
    assertEquals(5, passengersOnBus);
  }

  /**
   * Tests if passengers can be loaded onto a bus with space correctly from a stop
   * loadPassengers returns the number of passengers at stop.
   */
  @Test
  public void testLoadPassengersNonEmptyStopEmptyBus() {
    Bus testBus = TestUtils.createBus();
    Passenger passenger = new Passenger(1, "I never leave the stop");
    testBus.getNextStop().addPassengers(passenger);
    long passengersOnBusStart = testBus.getNumPassengers();
    assertEquals(0, passengersOnBusStart);
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(1, passengersLoaded);
    long passengersOnBusEnd = testBus.getNumPassengers();
    assertEquals(1, passengersOnBusEnd);
  }

  /**
   * Test simulation update on non-empty stop.
   */
  @Test
  public void testSimulationUpdateOnNonEmptyStop() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    Passenger passenger = new Passenger(1, "Goldy");
    stop.addPassengers(passenger);
    int initialWait = passenger.getTotalWait();
    assertEquals(0, initialWait);
    stop.update();
    int updatedWait = passenger.getTotalWait();
    assertEquals(1, updatedWait);
  }


  /**
   * Testing reporting functionality with no passenger.
   */
  @Test
  public void testStopReportNoPassengers() {
    try {
      Stop stop = new Stop(0, 44.972392, -93.243774);
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      stop.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Stop Info Start####" + System.lineSeparator()
              + "ID: 0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num passengers waiting: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Stop Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Testing reporting functionality with passenger.
   */
  @Test
  public void testStopReportWithPassenger() {
    try {
      Stop stop = new Stop(0, 44.972392, -93.243774);
      Passenger passenger = new Passenger(1, "Goldy");
      stop.addPassengers(passenger);
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      stop.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Stop Info Start####" + System.lineSeparator()
              + "ID: 0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num passengers waiting: 1" + System.lineSeparator()
              + "####Passenger Info Start####" + System.lineSeparator()
              + "Name: Goldy" + System.lineSeparator()
              + "Destination: 1" + System.lineSeparator()
              + "Total wait: 0" + System.lineSeparator()
              + "Wait at stop: 0" + System.lineSeparator()
              + "Time on bus: 0" + System.lineSeparator()
              + "####Passenger Info End####" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Stop Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Testing display info.
   */
  @Test
  public void testDisplayInfo() {
    Stop testStop = new Stop(0, 44.972392, -93.243774);
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    ConcreteStopSubject testConcreteStop = new ConcreteStopSubject(myWebServerSessionSpy);
    testStop.setConcreteStopSubject(testConcreteStop);
    testStop.displayInfo();
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject captureData = messageCaptor.getValue();
    String command = captureData.get("command").getAsString();
    assertEquals("observeStop", command);
    String text = captureData.get("text").getAsString();
    String strToCompare =
        "Stop 0" + System.lineSeparator()
            + "-----------------------------" + System.lineSeparator()
            + "  * Position: (44.972392,-93.243774)" + System.lineSeparator()
            + "  * Number of People: 0" + System.lineSeparator();
    assertEquals(strToCompare, text);
  }
}
