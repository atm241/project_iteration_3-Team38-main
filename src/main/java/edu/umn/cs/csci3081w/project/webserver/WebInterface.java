package edu.umn.cs.csci3081w.project.webserver;

import edu.umn.cs.csci3081w.project.model.BusData;
import edu.umn.cs.csci3081w.project.model.RouteData;

public interface WebInterface {

  public void updateBus(BusData bus, boolean deleted);

  public void updateRoute(RouteData route, boolean deleted);
}
