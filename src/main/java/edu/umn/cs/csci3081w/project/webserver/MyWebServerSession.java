package edu.umn.cs.csci3081w.project.webserver;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
    value = "/simulator",
    subprotocols = {"web_server"}
)
public class MyWebServerSession {

  private Session session;
  private MyWebServerSessionState state;
  private MyWebServer myWS;
  private ConfigManager cm;
  private VisualizationSimulator mySim;

  public MyWebServerSession() {
    System.out.println("class loaded " + this.getClass());
  }


  /**
   * Function which runs when client connection is made.
   * @param session session object
   */
  @OnOpen
  public void onOpen(Session session) {
    System.out.println("session opened");
    //save session object
    this.session = session;
    state = new MyWebServerSessionState();
    myWS = new MyWebServer();
    cm = new ConfigManager();
    cm.readConfig(getClass().getClassLoader().getResource("config.txt").getFile());
    for (Route r : cm.getRoutes()) {
      System.out.println(r.getName());
      System.out.println(r.getStops().size());
      for (Stop s : r.getStops()) {
        System.out.println(s.getLongitude());
      }
    }
    mySim = new VisualizationSimulator(myWS, cm, this);
    state.getCommands().put("getRoutes", new GetRoutesCommand(myWS));
    state.getCommands().put("getBusses", new GetBussesCommand(myWS));
    state.getCommands().put("start", new StartCommand(mySim));
    state.getCommands().put("update", new UpdateCommand(mySim));
    state.getCommands().put("initRoutes", new InitRoutesCommand(cm));
    state.getCommands().put("pause", new PauseCommand(mySim));
    state.getCommands().put("listenBus", new ListenBusCommand(mySim));
    state.getCommands().put("listenStop", new ListenStopCommand(mySim));
  }


  /**
   * Function which executes when a simulation command is received from the client.
   * @param message incoming message
   */
  @OnMessage
  public void onMessage(String message) {
    JsonObject commandJson = JsonParser.parseString(message).getAsJsonObject();
    String command = commandJson.get("command").getAsString();
    if (command != null) {
      if (state.getCommands().keySet().contains(command)) {
        MyWebServerCommand myC = state.getCommands().get(command);
        myC.execute(this, commandJson, state);
      }
    }
  }

  /**
   * Sends simulation information to the client.
   * @param message incoming data
   */
  public void sendJson(JsonObject message) {
    try {
      session.getBasicRemote().sendText(message.toString());
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @OnError
  public void onError(Throwable e) {
    e.printStackTrace();
  }

  /**
   * Runs when session is closed by the client.
   * @param session session object
   */
  @OnClose
  public void onClose(Session session) {
    System.out.println("session closed");
    //make session null as the session is closed
    this.session = null;
  }

  /**
   * For testing purpose.
   * @return ConfigManager cm
   */
  public ConfigManager getConfig() {
    return cm;
  }

  /**
   * For testing purpose.
   * @return myWS
   */
  public MyWebServer getMyWS() {
    return myWS;
  }
}
