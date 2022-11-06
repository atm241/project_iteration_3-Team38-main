package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;

public class PauseCommand extends MyWebServerCommand {
  private VisualizationSimulator visSim;

  public PauseCommand(VisualizationSimulator visSim) {
    this.visSim = visSim;
  }

  /**
   * Tells the simulator to pause the simulation.
   *
   * @param session object representing the simulation session
   * @param command object containing the original command
   * @param state object containing the state of the web server
   */
  @Override
  public void execute(MyWebServerSession session, JsonObject command,
                      MyWebServerSessionState state) {
    visSim.togglePause();
  }
}