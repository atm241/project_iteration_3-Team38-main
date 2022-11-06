package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PositionTest {

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
   * Test position constructor with parameters.
   */
  @Test
  public void testConstructorWithParameters() {
    Position testPosition = new Position(44.972392, -93.243774);
    assertEquals(44.972392, testPosition.getXcoordLoc());
    assertEquals(-93.243774, testPosition.getYcoordLoc());
  }

  /**
   * Test position constructor with no parameters.
   */
  @Test
  public void testConstructorWithNoParameters() {
    Position testPosition = new Position();
    assertEquals(0, testPosition.getXcoordLoc());
    assertEquals(0, testPosition.getYcoordLoc());
  }

}
