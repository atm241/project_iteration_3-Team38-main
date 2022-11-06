package edu.umn.cs.csci3081w.project.webserver;

import java.util.HashMap;
import java.util.Map;

public class MyWebServerSessionState {

  private Map<String, MyWebServerCommand> commands;

  public MyWebServerSessionState() {
    this.commands = new HashMap<String, MyWebServerCommand>();
  }

  public Map<String, MyWebServerCommand> getCommands() {
    return commands;
  }
}
