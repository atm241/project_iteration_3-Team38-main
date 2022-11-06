package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StartCommand extends MyWebServerCommand {

  private VisualizationSimulator mySim;
  private List<Integer> timeBetweenBusses;
  private int numTimeSteps;

  /**
   * Start simulation constructor functionality.
   * @param sim simulation object
   */
  public StartCommand(VisualizationSimulator sim) {
    this.mySim = sim;
    this.timeBetweenBusses = new ArrayList<Integer>();
    this.numTimeSteps = 10;
  }

  /**
   * This method starts the simulation.
   *
   * @param session current simulation session
   * @param command the content of the start simulations content
   * @param state the state of the simulation session
   */
  @Override
  public void execute(MyWebServerSession session, JsonObject command,
                      MyWebServerSessionState state) {
    timeBetweenBusses.clear();
    numTimeSteps = command.get("numTimeSteps").getAsInt();
    JsonArray arr = command.getAsJsonArray("timeBetweenBusses");
    for (int i = 0; i < arr.size(); i++) {
      timeBetweenBusses.add(arr.get(i).getAsInt());
    }
    for (int i = 0; i < timeBetweenBusses.size(); i++) {
      System.out.println("Time between busses for route  " + i + ": " + timeBetweenBusses.get(i));
    }
    System.out.println("Number of time steps for simulation is: " + numTimeSteps);
    System.out.println("Starting simulation");
    mySim.setConcreteBusCreator(getCurrentSimulationTime());
    mySim.start(timeBetweenBusses, numTimeSteps);
  }

  /**
   * Retrieves the current time for the simulation.
   *
   * @return current simulation time
   */
  public LocalDateTime getCurrentSimulationTime() {
    return LocalDateTime.now();
  }

}
