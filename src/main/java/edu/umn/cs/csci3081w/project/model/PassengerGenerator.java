package edu.umn.cs.csci3081w.project.model;

import java.util.ArrayList;
import java.util.List;

public abstract class PassengerGenerator {
  private List<Double> probs;
  private List<Stop> stops;

  /**
   * Constructor for Abstract class.
   *
   * @param stops list of stops
   * @param l list of probabilities
   */
  public PassengerGenerator(List<Double> l, List<Stop> stops) {
    this.probs = new ArrayList<>();
    this.stops = new ArrayList<>();
    for (Double prob : l) {
      this.probs.add(prob);
    }
    for (Stop s : stops) {
      this.stops.add(s);
    }
  }

  public abstract int generatePassengers();

  public List<Double> getProbs() {
    return probs;
  }

  public void setProbs(List<Double> probs) {
    this.probs = probs;
  }

  public List<Stop> getStops() {
    return stops;
  }

  public void setStops(List<Stop> stops) {
    this.stops = stops;
  }
}