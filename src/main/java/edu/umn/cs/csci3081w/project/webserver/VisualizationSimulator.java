package edu.umn.cs.csci3081w.project.webserver;

import edu.umn.cs.csci3081w.project.model.Bus;
import edu.umn.cs.csci3081w.project.model.BusCreator;
import edu.umn.cs.csci3081w.project.model.BusSubject;
import edu.umn.cs.csci3081w.project.model.ConcreteBusSubject;
import edu.umn.cs.csci3081w.project.model.ConcreteStopSubject;
import edu.umn.cs.csci3081w.project.model.OverallConcreteBusCreator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import edu.umn.cs.csci3081w.project.model.StopSubject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VisualizationSimulator {

  private WebInterface webInterface;
  private ConfigManager configManager;
  private List<Integer> busStartTimings;
  private List<Integer> timeSinceLastBus;
  private int numTimeSteps = 0;
  private int simulationTimeElapsed = 0;
  private List<Route> prototypeRoutes;
  private List<Bus> busses;
  private int busId = 1000;
  private boolean paused = false;
  private Random rand;
  private BusSubject busSubject;
  private StopSubject stopSubject;
  private BusCreator simulationConcreteBusCreator;
  private Printer printer;

  /**
   * Constructor for Simulation.
   *
   * @param webI    MWS object
   * @param configM config object
   * @param session session object
   */
  public VisualizationSimulator(MyWebServer webI, ConfigManager configM,
                                MyWebServerSession session) {
    this.webInterface = webI;
    this.configManager = configM;
    //initialize these lists so that we do not get a null pointer
    this.busStartTimings = new ArrayList<Integer>();
    this.prototypeRoutes = new ArrayList<Route>();
    this.busses = new ArrayList<Bus>();
    this.timeSinceLastBus = new ArrayList<Integer>();
    this.rand = new Random();
    this.busSubject = new ConcreteBusSubject(session);
    this.stopSubject = new ConcreteStopSubject(session);
  }

  /**
   * Sets the bus creator for the simulation.
   *
   * @param currentSimulationTime time when the simulation was started.
   */
  public void setConcreteBusCreator(LocalDateTime currentSimulationTime) {
    int day = currentSimulationTime.getDayOfMonth();
    int time = currentSimulationTime.getHour();
    OverallConcreteBusCreator overallBusCreator = new OverallConcreteBusCreator();
    overallBusCreator.setCurrentConcreteBusCreator(day, time);
    this.simulationConcreteBusCreator = overallBusCreator;
  }

  /**
   * Starts the simulation.
   *
   * @param busStartTimingsParam start timings of bus
   * @param numTimeStepsParam    number of time steps
   */
  public void start(List<Integer> busStartTimingsParam, int numTimeStepsParam) {
    printer = Printer.getInstance();
    this.busStartTimings = busStartTimingsParam;
    this.numTimeSteps = numTimeStepsParam;
    for (int i = 0; i < busStartTimings.size(); i++) {
      this.timeSinceLastBus.add(i, 0);
    }
    simulationTimeElapsed = 0;
    prototypeRoutes = configManager.getRoutes();
    for (int i = 0; i < prototypeRoutes.size(); i++) {
      prototypeRoutes.get(i).report(System.out);
      prototypeRoutes.get(i).updateRouteData();
      webInterface.updateRoute(prototypeRoutes.get(i).getRouteData(), false);
    }
  }

  /**
   * Toggles the pause state of the simulation.
   */
  public void togglePause() {
    paused = !paused;
  }

  /**
   * Updates the simulation at each step.
   */
  public void update() {
    if (!paused) {
      simulationTimeElapsed++;
      System.out.println("~~~~The simulation time is now"
          + "at time step "
          + simulationTimeElapsed + "~~~~");
      // Check if we need to generate new busses
      for (int i = 0; i < timeSinceLastBus.size(); i++) {
        // Check if we need to make a new bus
        if (timeSinceLastBus.get(i) <= 0) {
          Route outbound = prototypeRoutes.get(2 * i);
          Route inbound = prototypeRoutes.get(2 * i + 1);
          busses
              .add(this.simulationConcreteBusCreator.createBus(String.valueOf(busId),
                  outbound.shallowCopy(), inbound.shallowCopy(), 1));
          busId++;
          timeSinceLastBus.set(i, busStartTimings.get(i));
          timeSinceLastBus.set(i, timeSinceLastBus.get(i) - 1);
        } else {
          timeSinceLastBus.set(i, timeSinceLastBus.get(i) - 1);
        }
      }
      // Update busses
      for (int i = busses.size() - 1; i >= 0; i--) {
        busses.get(i).update();
        if (busses.get(i).isTripComplete()) {
          webInterface.updateBus(busses.get(i).getBusData(), true);
          busses.remove(i);
          continue;
        }

        //BUS, SIMULATION_TIME_ELAPSED, ID, POSITION_X, POSITION_Y, NUM_PASS, CAP
        String busSimInfo = "BUS,"
            + simulationTimeElapsed
            + busses.get(i).getBusData().getId() + ","
            + busses.get(i).getBusData().getPosition().getXcoordLoc() + ","
            + busses.get(i).getBusData().getPosition().getYcoordLoc() + ","
            + busses.get(i).getBusData().getNumPassengers() + ","
            + busses.get(i).getBusData().getCapacity() + "\n";

        printer.printInfo(busSimInfo);
        webInterface.updateBus(busses.get(i).getBusData(), false);
        busses.get(i).report(System.out);
      }
      // Update routes
      for (int i = 0; i < prototypeRoutes.size(); i++) {
        prototypeRoutes.get(i).update();
        webInterface.updateRoute(prototypeRoutes.get(i).getRouteData(), false);
        prototypeRoutes.get(i).report(System.out);
        //      STOP, SIMULATION_TIME_ELAPSED, ID, POSITION_X, POSITION_Y, NUM_PASS
        for (Route route : prototypeRoutes) {
          List<Stop> stops = route.getStops();
          for (Stop stop : stops) {
            String stopSimInfo = "STOP,"
                + simulationTimeElapsed + ","
                + stop.getId() + ","
                + stop.getLongitude() + ","
                + stop.getLatitude() + ","
                + stop.getNumPassengersPresent() + "\n";
            printer.printInfo(stopSimInfo);
          }
        }
      }
      busSubject.notifyBusObservers();
      stopSubject.notifyStopObservers();
    }
  }

  /**
   * Identifies the bus that should be an observer and notifies the subject.
   *
   * @param id identifier of the bus that is an observer of the simulation
   */
  public void addBusObserver(String id) {
    for (Bus bus : busses) {
      if (bus.getName().equals(id)) {
        busSubject.registerBusObserver(bus);
      }
    }
  }

  /**
   * Identifies the stop that should be an observer and notifies the subject.
   *
   * @param id identifier of the stop that is an observer of the simulation
   */
  public void addStopObserver(String id) {
    for (Route route : prototypeRoutes) {
      List<Stop> stops = route.getStops();
      for (Stop stop : stops) {
        String stopId = stop.getId() + "";
        if (stopId.equals(id)) {
          stopSubject.registerStopObserver(stop);
        }
      }
    }
  }

  /**
   * For testing purpose.
   * @return simulationConcreteBusCreator
   */
  public BusCreator getSimulationConcreteBusCreator() {
    return simulationConcreteBusCreator;
  }

  /**
   * For testing purpose.
   * @return pause
   */
  public boolean getPause() {
    return paused;
  }

  /**
   * For testing purpose.
   * @param bus The bus to be added
   */
  public void addBus(Bus bus) {
    this.busses.add(bus);
  }

  /**
   * For testing purpose.
   * @param bus the bus to set
   */
  public void setBusSubject(ConcreteBusSubject bus) {
    this.busSubject = bus;
  }

  /**
   * For testing purpose.
   * @param route The route to add
   */
  public void addRoute(Route route) {
    this.prototypeRoutes.add(route);
  }

  /**
   * For testing purpose.
   * @param stop the stop to set
   */
  public void setStopSubject(ConcreteStopSubject stop) {
    this.stopSubject = stop;
  }
}
