package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;

public class InitRoutesCommand extends MyWebServerCommand {

  private ConfigManager cm;

  public InitRoutesCommand(ConfigManager configManager) {
    this.cm = configManager;
  }

  /**
   * Initializes the routes information for the simulation.
   *
   * @param session current simulation session
   * @param command the initialize routes command content
   * @param state the state of the simulation session
   */
  @Override
  public void execute(MyWebServerSession session, JsonObject command,
                      MyWebServerSessionState state) {
    int numRoutes = cm.getRoutes().size();
    JsonObject data = new JsonObject();
    data.addProperty("command", "initRoutes");
    data.addProperty("numRoutes", numRoutes);  // Original code had it switch to double
    session.sendJson(data);
  }

}
