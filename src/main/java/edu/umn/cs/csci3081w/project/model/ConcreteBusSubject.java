package edu.umn.cs.csci3081w.project.model;

import edu.umn.cs.csci3081w.project.webserver.MyWebServerSession;
import java.util.ArrayList;
import java.util.List;

public class ConcreteBusSubject extends BusSubject {
  private MyWebServerSession session;

  /**
   * Create a concrete bus subject.
   *
   * @param session parameter to communicate with the visualization module
   */
  public ConcreteBusSubject(MyWebServerSession session) {
    super();
    this.session = session;
  }

  /**
   * Register the bus as the observer.
   *
   * @param busObserver bus that is going to become the only observer
   */
  public void registerBusObserver(BusObserver busObserver) {
    busObservers.clear();
    busObserver.setConcreteBusSubject(this);
    busObservers.add(busObserver);
  }

  /**
   * Notify the observers that they should display new info.
   */
  public void notifyBusObservers() {
    for (BusObserver busObserver : busObservers) {
      busObserver.displayInfo();
    }
  }

  public MyWebServerSession getSession() {
    return session;
  }
}
