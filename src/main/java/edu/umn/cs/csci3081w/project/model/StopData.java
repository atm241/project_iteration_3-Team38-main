package edu.umn.cs.csci3081w.project.model;

public class StopData {
  private String id;
  private Position position;
  private int numPeople;

  /**
   * Stop data constructor.
   *
   * @param id stop id
   * @param numPeople Number of people
   * @param position stop position
   */
  public StopData(String id, Position position, int numPeople) {
    this.id = id;
    this.position = position;
    this.numPeople = numPeople;
  }

  /**
   * Default stop data constructor.
   */
  public StopData() {
    this.id = "";
    this.position = new Position();
    this.numPeople = 0;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public int getNumPeople() {
    return numPeople;
  }

  public void setNumPeople(int numPeople) {
    this.numPeople = numPeople;
  }
}
