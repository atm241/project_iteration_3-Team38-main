package edu.umn.cs.csci3081w.project.webserver;

import edu.umn.cs.csci3081w.project.model.BusData;
import edu.umn.cs.csci3081w.project.model.RouteData;
import java.util.ArrayList;
import java.util.List;

public class MyWebServer implements WebInterface {

  public List<BusData> busses;
  public List<RouteData> routes;

  public MyWebServer() {
    busses = new ArrayList<BusData>();
    routes = new ArrayList<RouteData>();
  }

  /**
   * Updates a bus in the web server.
   * @param tempData incoming data
   * @param deleted if bus was deleted
   */
  public void updateBus(BusData tempData, boolean deleted) {
    BusData found = null;
    for (BusData bd : busses) {
      if (bd.getId().equals(tempData.getId())) {
        found = bd;
      }
    }
    if (found != null) {
      if (deleted) {
        busses.remove(found);
        return;
      }
      int index = busses.indexOf(found);
      busses.get(index).setId(tempData.getId());
      busses.get(index).setPosition(tempData.getPosition());
      busses.get(index).setNumPassengers(tempData.getNumPassengers());
      busses.get(index).setCapacity(tempData.getCapacity());
    } else {
      busses.add(tempData);
    }
  }

  /**
   * Updates route in the web server.
   * @param tempData incoming data
   * @param deleted if route was deleted
   */
  public void updateRoute(RouteData tempData, boolean deleted) {
    RouteData found = null;
    for (RouteData rd : routes) {
      if (rd.getId().equals(tempData.getId())) {
        found = rd;
      }
    }
    if (found != null) {
      if (deleted) {
        routes.remove(found);
        return;
      }
      int index = routes.indexOf(found);
      routes.get(index).setId(tempData.getId());
      routes.get(index).setStops(tempData.getStops());
    } else {
      routes.add(tempData);
    }
  }

  /**
   * For testing purpose.
   * @param bd busdata
   */
  public void addBusses(BusData bd) {
    busses.add(bd);
  }
}
