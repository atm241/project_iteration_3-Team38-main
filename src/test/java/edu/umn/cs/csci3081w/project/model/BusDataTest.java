package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BusDataTest {

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
   * Test bus data constructor with parameters.
   */
  @Test
  public void testConstructorWithParameters() {
    BusData testBusData = new BusData("0", new Position(44.972392, -93.243774), 0, 30);
    assertEquals("0", testBusData.getId());
    assertEquals(44.972392, testBusData.getPosition().getXcoordLoc());
    assertEquals(-93.243774, testBusData.getPosition().getYcoordLoc());
    assertEquals(0, testBusData.getNumPassengers());
    assertEquals(30, testBusData.getCapacity());
  }

  /**
   * Test bus data constructor with no parameters.
   */
  @Test
  public void testConstructorWithNoParameters() {
    BusData testBusData = new BusData();
    assertEquals(null, testBusData.getId());
    assertEquals(0, testBusData.getPosition().getXcoordLoc());
    assertEquals(0, testBusData.getPosition().getYcoordLoc());
    assertEquals(0, testBusData.getNumPassengers());
    assertEquals(0, testBusData.getCapacity());
  }

}
