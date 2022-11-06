package edu.umn.cs.csci3081w.project.model;

import java.util.ArrayList;
import java.util.List;

public class RouteData {
  private String id;
  private List<StopData> stops;

  public RouteData() {
    this.id = "";
    this.stops = new ArrayList<StopData>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<StopData> getStops() {
    return stops;
  }

  public void setStops(List<StopData> stops) {
    this.stops = stops;
  }
}
