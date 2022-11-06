package edu.umn.cs.csci3081w.project.webserver;


import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;

public class ConfigManager {

  private List<Route> routes;

  public ConfigManager() {
    routes = new ArrayList<Route>();
  }

  /**
   * This method reads the configuration file.
   * This method reads the configuration file, which contains information
   * about stops and routes.
   *
   * @param fileName the file name of the configuration file.
   */
  public void readConfig(String fileName) {
    File configFile = FileUtils.getFile(fileName);
    try {
      List<Stop> stops = new ArrayList<Stop>();
      List<Double> distances = new ArrayList<Double>();
      double oldLat = 0;
      double oldLon = 0;
      List<Double> currProbabilities = new ArrayList<Double>();
      String currGeneralName = "";
      String currRouteName = "";
      List<String> stopNames = new ArrayList<String>();
      int stopId = 10;
      Scanner scanner = new Scanner(configFile);
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] splits = line.split(",");
        String chunk = splits[0];
        if (chunk.equals("ROUTE_GENERAL")) {
          currGeneralName = splits[1];
        } else if (chunk.equals("ROUTE")) {
          // If we are coming to a route besides our first one, save all our
          // data and init variables for next route
          if (stops.size() > 0) {
            // Convert our variables into the necessary raw memory
            int numStops = stops.size();
            List<Stop> rawStops = new ArrayList<Stop>();
            rawStops.addAll(stops);
            int numDists = distances.size();
            List<Double> rawDists = new ArrayList<Double>();
            rawDists.addAll(distances);
            this.routes.add(
                new Route(currGeneralName + " " + currRouteName, rawStops, rawDists, stops.size(),
                    new RandomPassengerGenerator(currProbabilities, stops)));
            stops.clear();
            distances.clear();
            currProbabilities.clear();
          }
          oldLat = 0;
          oldLon = 0;
          currRouteName = splits[1];
        } else if (chunk.equals("STOP")) {
          String stopName = splits[1];
          stopName = stopName.replace(" ", "");
          if (stopNames.contains(stopName)) {
            continue;
          } else {
            int id = stopId;
            stopId++;
            String latString;
            latString = splits[2];
            double latitude = Double.valueOf(latString);
            String lonString;
            lonString = splits[3];
            double longitude = Double.valueOf(lonString);
            stops.add(new Stop(id, latitude, longitude));
            // Need to turn these lat and long into real-world distances
            // This means moving 1 speed in a time click moves 1 mile.
            // That's a bit far, so I multiply * 2 so that a speed of 1 moves 1/2 mile
            latitude *= 69 * 2;
            longitude *= 55 * 2;
            // Grabbing last element from list is hard, so cache position instead
            if (stops.size() > 1) {
              double dist = Math.sqrt((latitude - oldLat) * (latitude - oldLat)
                      + (longitude - oldLon) * (longitude - oldLon));
              distances.add(dist);
            }
            oldLat = latitude;
            oldLon = longitude;
            String probString;
            probString = splits[4];
            double probability = Double.valueOf(probString);
            currProbabilities.add(probability);
          }
        }
      }
      // Generatre our last route
      if (stops.size() > 0) {
        int numStops = stops.size();
        List<Stop> rawStops = new ArrayList<>();
        rawStops.addAll(stops);
        int numDists = distances.size();
        List<Double> rawDists = new ArrayList<>();
        rawDists.addAll(distances);
        this.routes.add(
            new Route(currGeneralName + " " + currRouteName, rawStops, rawDists, stops.size(),
                new RandomPassengerGenerator(currProbabilities, stops)));
      }
      scanner.close();
      currProbabilities.clear();
      stops.clear();
      distances.clear();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Route> getRoutes() {
    return routes;
  }
}