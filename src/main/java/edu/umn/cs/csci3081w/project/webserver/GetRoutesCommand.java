package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.RouteData;
import java.util.List;

public class GetRoutesCommand extends MyWebServerCommand {

  private MyWebServer myWS;

  public GetRoutesCommand(MyWebServer ws) {
    this.myWS = ws;
  }

  /**
   * Retrieves routes information from the simulation.
   *
   * @param session current simulation session
   * @param command the get routes command content
   * @param state the state of the simulation session
   */
  @Override
  public void execute(MyWebServerSession session, JsonObject command,
                      MyWebServerSessionState state) {
    command = null;
    state = null;
    List<RouteData> routes = myWS.routes;
    JsonObject data = new JsonObject();
    data.addProperty("command", "updateRoutes");
    JsonArray routesArray = new JsonArray();
    for (int i = 0; i < routes.size(); i++) {
      JsonObject r = new JsonObject();
      r.addProperty("id", routes.get(i).getId());
      JsonArray stopArray = new JsonArray();
      for (int j = 0; j < (routes.get(i).getStops().size()); j++) {
        JsonObject stopStruct = new JsonObject();
        stopStruct.addProperty("id", routes.get(i).getStops().get(j).getId());
        stopStruct.addProperty("numPeople", routes.get(i).getStops().get(j).getNumPeople());
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("x", routes.get(i).getStops().get(j).getPosition().getXcoordLoc());
        jsonObj.addProperty("y", routes.get(i).getStops().get(j).getPosition().getYcoordLoc());
        stopStruct.add("position", jsonObj);
        stopArray.add(stopStruct);
      }
      r.add("stops", stopArray);
      routesArray.add(r);
    }
    data.add("routes", routesArray);
    session.sendJson(data);
  }
}

