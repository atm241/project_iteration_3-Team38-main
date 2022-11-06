package edu.umn.cs.csci3081w.project.webserver;

import java.io.File;
import java.io.FileWriter;

public class Printer {
  private static Printer instance;

  public Printer() { }

  /**
   * Gets the instance of the printer.
   * @return the printer instance
   */
  public static Printer getInstance() {
    if (instance == null) {
      instance = new Printer();
    }
    File f = new File("SimData.csv");
    if (f.exists()) {
      f.delete();
    }
    return instance;
  }

  /**
   * Prints information.
   * @param txt the string that will be used to print the info
   */
  public void printInfo(String txt) {
    try {
      FileWriter csvWriter = new FileWriter("SimData.csv", true);
      csvWriter.append(txt);
      csvWriter.flush();
      csvWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
