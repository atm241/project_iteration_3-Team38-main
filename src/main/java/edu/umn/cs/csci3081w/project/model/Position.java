package edu.umn.cs.csci3081w.project.model;

public class Position {

  private double xcoordLoc;
  private double ycoordLoc;

  public Position(double xcoordLoc, double ycoordLoc) {
    this.xcoordLoc = xcoordLoc;
    this.ycoordLoc = ycoordLoc;
  }

  public Position() {
    this.xcoordLoc = 0.0;
    this.ycoordLoc = 0.0;
  }

  public double getXcoordLoc() {
    return xcoordLoc;
  }

  public void setXcoordLoc(double xcoordLoc) {
    this.xcoordLoc = xcoordLoc;
  }

  public double getYcoordLoc() {
    return ycoordLoc;
  }

  public void setYcoordLoc(double ycoordLoc) {
    this.ycoordLoc = ycoordLoc;
  }

}
