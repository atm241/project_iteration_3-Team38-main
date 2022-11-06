package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerLoaderTest {

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
   * Test loading of passenger to a empty list.
   */
  @Test
  public void testLoadPassengerToEmptyList() {
    PassengerLoader testPassLoader = new PassengerLoader();
    Passenger passenger = new Passenger(1, "Goldy");
    List<Passenger> passengers = new ArrayList<>();
    boolean passLoaded = testPassLoader.loadPassenger(passenger, 5, passengers);
    assertTrue(passLoaded);
  }
}
