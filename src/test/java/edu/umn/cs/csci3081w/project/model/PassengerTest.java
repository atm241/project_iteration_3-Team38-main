package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerTest {

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
   * Test passenger constructor.
   */
  @Test
  public void testPassengerConstructor() {
    Passenger passenger = new Passenger(1, "Goldy");
    assertEquals(0, passenger.getTotalWait());
    assertEquals(1, passenger.getDestination());
  }

  /**
   * Test passenger update.
   */
  @Test
  public void testPassengerUpdate() {
    Passenger passenger = new Passenger(1, "Goldy");
    int initialWait = passenger.getTotalWait();
    assertEquals(0, initialWait);
    passenger.pasUpdate();
    int updatedWait = passenger.getTotalWait();
    assertEquals(1, updatedWait);
  }

  /**
   * Test passenger on bus update.
   */
  @Test
  public void testPassengerUpdateOnBus() {
    Passenger passenger = new Passenger(1, "Goldy");
    int initialWait = passenger.getTotalWait();
    assertEquals(0, initialWait);
    passenger.getOnBus();
    passenger.pasUpdate();
    int updatedWait = passenger.getTotalWait();
    assertEquals(2, updatedWait);
  }

  /**
   * Test passenger get on bus.
   */
  @Test
  public void testGetOnBus() {
    Passenger passenger = new Passenger(1, "Goldy");
    int initialWait = passenger.getTotalWait();
    assertEquals(0, initialWait);
    passenger.getOnBus();
    int updatedWait = passenger.getTotalWait();
    assertEquals(1, updatedWait);
  }

  /**
   * Test passenger report function.
   */
  @Test
  public void testReport() {
    try {
      Passenger passenger = new Passenger(1, "Goldy");
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      passenger.report(testStream);
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Passenger Info Start####" + System.lineSeparator()
              + "Name: Goldy" + System.lineSeparator()
              + "Destination: 1" + System.lineSeparator()
              + "Total wait: 0" + System.lineSeparator()
              + "Wait at stop: 0" + System.lineSeparator()
              + "Time on bus: 0" + System.lineSeparator()
              + "####Passenger Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
    } catch (IOException ioe) {
      fail();
    }
  }
}
