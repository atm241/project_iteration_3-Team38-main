package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.Bus;
import edu.umn.cs.csci3081w.project.model.ConcreteBusSubject;
import edu.umn.cs.csci3081w.project.model.ConcreteStopSubject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.RegularBus;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import edu.umn.cs.csci3081w.project.model.TestUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class VisualizationSimulatorTest {

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Test command for initializing the simulation.
   */
  @Test
  public void testSimulationInitialization() {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    ConfigManager cm = myWebServerSessionSpy.getConfig();
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    VisualizationSimulator visTest = new VisualizationSimulator(myWS, cm, myWebServerSessionSpy);
    LocalDateTime dateTime = LocalDateTime.of(2020, Month.APRIL, 10, 12, 30);
    visTest.setConcreteBusCreator(dateTime);
    Stop stop1 = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop1);
    stopsOut.add(stop2);
    stopsOut.add(stop3);
    List<Double> distancesOut = new ArrayList<Double>();
    distancesOut.add(0.9712663713083954);
    distancesOut.add(0.961379387775189);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(.15);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.0);
    Route testOutRoute = TestUtils.createRouteGivenData(stopsOut, distancesOut, probabilitiesOut);
    List<Stop> stopsIn = new ArrayList<>();
    stopsIn.add(stop3);
    stopsIn.add(stop2);
    stopsIn.add(stop1);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.961379387775189);
    distancesIn.add(0.9712663713083954);
    List<Double> probabilitiesIn = new ArrayList<>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.0);
    Route testInRoute = TestUtils.createRouteGivenData(stopsIn, distancesIn, probabilitiesIn);
    Bus testBus1 = visTest.getSimulationConcreteBusCreator().createBus("regular",
        testOutRoute, testInRoute, 1);
    assertTrue(testBus1 instanceof RegularBus);
  }

  /**
   * Test starting the simulation.
   */
  @Test
  public void testSimulationStart() {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    ConfigManager cm = myWebServerSessionSpy.getConfig();
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    VisualizationSimulator visTest = new VisualizationSimulator(myWS, cm, myWebServerSessionSpy);
    LocalDateTime dateTime = LocalDateTime.of(2020, Month.APRIL, 10, 12, 30);
    visTest.setConcreteBusCreator(dateTime);
    int numTimeStepsParam = 1;
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "start");
    commandFromClient.addProperty("numTimeSteps", numTimeStepsParam);
    JsonArray a = new JsonArray();
    a.add(1);
    a.add(1);
    commandFromClient.add("timeBetweenBusses", a);
    try {
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      myWebServerSessionSpy.onMessage(commandFromClient.toString());
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "Time between busses for route 0: 1" + System.lineSeparator()
          + "Time between busses for route 1: 1" + System.lineSeparator()
          + "Number of time steps for simulation is: 1" + System.lineSeparator()
          + "Starting simulation" + System.lineSeparator();
      assertEquals("", data);
      final Charset charset2 = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
      PrintStream testStream2 = new PrintStream(outputStream2, true, charset2.name());
      myWebServerSessionSpy.onClose(sessionDummy);
      String data2 = new String(outputStream2.toByteArray(), charset);
      testStream2.close();
      outputStream2.close();
      String strToCompare2 =
          "session closed" + System.lineSeparator();
      assertNotNull(data2);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test pause the simulation.
   */
  @Test
  public void testSimulationPause() {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    ConfigManager cm = myWebServerSessionSpy.getConfig();
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    VisualizationSimulator visTest = new VisualizationSimulator(myWS, cm, myWebServerSessionSpy);
    LocalDateTime dateTime = LocalDateTime.of(2020, Month.APRIL, 10, 12, 30);
    visTest.setConcreteBusCreator(dateTime);
    List<Integer> busStartTimingsParam = new ArrayList<Integer>();
    busStartTimingsParam.add(1);
    busStartTimingsParam.add(1);
    int numTimeStepsParam = 1;
    visTest.start(busStartTimingsParam, numTimeStepsParam);
    visTest.togglePause();
    visTest.update();
    assertTrue(visTest.getPause());
    visTest.togglePause();
    assertFalse(visTest.getPause());
  }

  /**
   * Test update the simulation.
   */
  @Test
  public void testSimulationUpdate() {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    ConfigManager cm = myWebServerSessionSpy.getConfig();
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    VisualizationSimulator visTest = new VisualizationSimulator(myWS, cm, myWebServerSessionSpy);
    LocalDateTime dateTime = LocalDateTime.of(2020, Month.APRIL, 10, 12, 30);
    visTest.setConcreteBusCreator(dateTime);
    List<Integer> busStartTimingsParam = new ArrayList<Integer>();
    busStartTimingsParam.add(1);
    busStartTimingsParam.add(1);
    int numTimeStepsParam = 1;
    visTest.start(busStartTimingsParam, numTimeStepsParam);
    try {
      final Charset charset2 = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
      PrintStream testStream2 = new PrintStream(outputStream2, true, charset2.name());
      visTest.update();
      String data2 = new String(outputStream2.toByteArray(), charset2);
      testStream2.close();
      outputStream2.close();
      assertNotNull(data2);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test getRoute command.
   */
  @Test
  public void testSimulationGetRoute() {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    ConfigManager cm = myWebServerSessionSpy.getConfig();
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    VisualizationSimulator visTest = new VisualizationSimulator(myWS, cm, myWebServerSessionSpy);
    LocalDateTime dateTime = LocalDateTime.of(2020, Month.APRIL, 10, 12, 30);
    visTest.setConcreteBusCreator(dateTime);
    int numTimeStepsParam = 1;
    List<Integer> busStartTimingsParam = new ArrayList<Integer>();
    busStartTimingsParam.add(1);
    busStartTimingsParam.add(1);
    JsonObject commandFromClient = new JsonObject();
    visTest.start(busStartTimingsParam, numTimeStepsParam);
    commandFromClient.addProperty("command", "getRoutes");
    myWebServerSessionSpy.onMessage(commandFromClient.toString());
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject commandToClient = messageCaptor.getValue();
    assertEquals("updateRoutes", commandToClient.get("command").getAsString());
  }

  /**
   * Test getBus command.
   */
  @Test
  public void testSimulationGetBus() {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    ConfigManager cm = myWebServerSessionSpy.getConfig();
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    VisualizationSimulator visTest = new VisualizationSimulator(myWS, cm, myWebServerSessionSpy);
    LocalDateTime dateTime = LocalDateTime.of(2020, Month.APRIL, 10, 12, 30);
    visTest.setConcreteBusCreator(dateTime);
    int numTimeStepsParam = 1;
    List<Integer> busStartTimingsParam = new ArrayList<Integer>();
    busStartTimingsParam.add(1);
    busStartTimingsParam.add(1);
    Bus testBus = TestUtils.createBus();
    ConcreteBusSubject testConcreteBus = new ConcreteBusSubject(myWebServerSessionSpy);
    testBus.setConcreteBusSubject(testConcreteBus);
    visTest.addBus(testBus);
    visTest.setBusSubject(testConcreteBus);
    myWS.addBusses(testBus.getBusData());
    visTest.addBusObserver(testBus.getBusData().getId());
    JsonObject commandFromClient = new JsonObject();
    visTest.start(busStartTimingsParam, numTimeStepsParam);
    commandFromClient.addProperty("command", "getBusses");
    myWebServerSessionSpy.onMessage(commandFromClient.toString());
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject commandToClient = messageCaptor.getValue();
    assertEquals("updateBusses", commandToClient.get("command").getAsString());
  }

  /**
   * Test BusObserver.
   */
  @Test
  public void testBusObserver() {
    Bus testBus = TestUtils.createBus();
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);

    ConcreteBusSubject testConcreteBus = new ConcreteBusSubject(myWebServerSessionSpy);
    testBus.setConcreteBusSubject(testConcreteBus);
    ConfigManager cm = myWebServerSessionSpy.getConfig();
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    VisualizationSimulator visTest = new VisualizationSimulator(myWS, cm, myWebServerSessionSpy);
    visTest.addBus(testBus);
    visTest.setBusSubject(testConcreteBus);
    visTest.addBusObserver(testBus.getBusData().getId());
    testConcreteBus.notifyBusObservers();

    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject captureData = messageCaptor.getValue();
    String command = captureData.get("command").getAsString();
    assertEquals("observeBus", command);
    String text = captureData.get("text").getAsString();

    assertTrue(text != null);
  }

  /**
   * Test add stop observer.
   */
  @Test
  public void testStopObserver() {
    Stop testStop = TestUtils.createStop();
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);

    ConcreteStopSubject concreteStopSubject = new ConcreteStopSubject(myWebServerSessionSpy);
    concreteStopSubject.registerStopObserver(testStop);
    ConfigManager cm = myWebServerSessionSpy.getConfig();
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    VisualizationSimulator visTest = new VisualizationSimulator(myWS, cm, myWebServerSessionSpy);
    Route route = TestUtils.createRoute();
    visTest.addRoute(route);
    visTest.setStopSubject(concreteStopSubject);
    visTest.addStopObserver(String.valueOf(testStop.getId()));
    concreteStopSubject.notifyStopObservers();
    testStop.setConcreteStopSubject(concreteStopSubject);

    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject captureData = messageCaptor.getValue();
    String command = captureData.get("command").getAsString();
    assertEquals("observeStop", command);
    String text = captureData.get("text").getAsString();

    assertTrue(text != null);
  }
}
