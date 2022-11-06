package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StopDataTest {

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
   * Test stop data constructor with no parameters.
   */
  @Test
  public void testConstructorWithNoParameters() {
    StopData testStopData = new StopData();
    assertEquals("", testStopData.getId());
    assertEquals(0, testStopData.getPosition().getXcoordLoc());
    assertEquals(0, testStopData.getPosition().getXcoordLoc());
    assertEquals(0, testStopData.getNumPeople());
  }

  /**
   * Test stop data constructor with parameters.
   */
  @Test
  public void testConstructorWithParameters() {
    StopData testStopData = new StopData("0", new Position(44.972392, -93.243774), 5);
    assertEquals("0", testStopData.getId());
    assertEquals(44.972392, testStopData.getPosition().getXcoordLoc());
    assertEquals(-93.243774, testStopData.getPosition().getYcoordLoc());
    assertEquals(5, testStopData.getNumPeople());
  }

}
