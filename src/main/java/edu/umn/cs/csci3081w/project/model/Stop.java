package edu.umn.cs.csci3081w.project.model;

import com.google.gson.JsonObject;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Stop implements StopObserver {
  private int id;
  private List<Passenger> passengers;
  private double longitude;
  private double latitude;
  private ConcreteStopSubject concreteStopSubject;

  public double getLongitude() {
    return longitude;
  }

  public List<Passenger> getPassengers() {
    return passengers;
  }

  public double getLatitude() {
    return latitude;
  }

  public int getNumPassengersPresent() {
    return passengers.size();
  }

  /**
   * Constructor for a stop.
   *
   * @param id stop id
   * @param longitude longitude
   * @param latitude latitude
   */
  public Stop(int id, double longitude, double latitude) {
    this.id = id;
    this.longitude = longitude;
    this.latitude = latitude;
    passengers = new ArrayList<>();

  }

  /**
   * Loads passengers on bus.
   *
   * @param bus Bus to be loaded to
   * @return number of loaded passengers
   */
  public int loadPassengers(Bus bus) {
    int passengersAdded = 0;
    while (!passengers.isEmpty() && bus.loadPassenger(passengers.get(0))) {
      // passenger is ON the bus and passenger have left the stop
      passengers.remove(0);
      passengersAdded++;
    }
    return passengersAdded;
  }

  /**
   * Adds passenger to passengers list at stop.
   *
   * @param pass to be added
   * @return number of added passengers
   */
  public int addPassengers(Passenger pass) {
    // we're using int here to aid potential future work:
    //  we may modify this to allow more multiple adds
    //  in a single call. For the moment, the var is
    //  used as a flag: 0 - fail; 1 - pass
    int passengersAddedToStop = 0;
    passengers.add(pass);
    passengersAddedToStop++;
    return passengersAddedToStop;
  }

  /**
   * Updates stop.
   */
  public void update() {
    Iterator<Passenger> passIter = passengers.iterator();
    while (passIter.hasNext()) {
      passIter.next().pasUpdate();
    }
  }

  public int getId() {
    return id;
  }

  /**
   * Report function for stop.
   *
   * @param out printstream object
   */
  public void report(PrintStream out) {
    out.println("####Stop Info Start####");
    out.println("ID: " + id);
    out.println("****Passengers Info Start****");
    out.println("Num passengers waiting: " + passengers.size());
    Iterator<Passenger> passIter = passengers.iterator();
    while (passIter.hasNext()) {
      passIter.next().report(out);
    }
    out.println("****Passengers Info End****");
    out.println("####Stop Info End####");
  }

  /**
   * Retrieves the current stop information and send the information to the visualization module.
   *
   */
  public void displayInfo() {
    JsonObject data = new JsonObject();
    data.addProperty("command", "observeStop");
    String text = "";
    text += "Stop " + id + System.lineSeparator();
    text += "-----------------------------" + System.lineSeparator();
    text += "  * Position: (" + longitude + ","
        + latitude + ")" + System.lineSeparator();
    text += "  * Number of People: " + getNumPassengersPresent() + System.lineSeparator();
    data.addProperty("text", text);
    concreteStopSubject.getSession().sendJson(data);
  }

  @Override
  public void setConcreteStopSubject(
      ConcreteStopSubject concreteStopSubject) {
    this.concreteStopSubject = concreteStopSubject;
  }
}
