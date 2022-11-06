package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;

public class ListenStopCommand extends MyWebServerCommand {
  private VisualizationSimulator visSim;

  public ListenStopCommand(VisualizationSimulator visSim) {
    this.visSim = visSim;
  }

  /**
   * Mark the stop as an observer.
   *
   * @param session object representing the simulation session
   * @param command object containing the original command
   * @param state object containing the state of the web server
   */
  @Override
  public void execute(MyWebServerSession session, JsonObject command,
                      MyWebServerSessionState state) {
    String id = command.get("id").getAsString();
    visSim.addStopObserver(id);
  }
}