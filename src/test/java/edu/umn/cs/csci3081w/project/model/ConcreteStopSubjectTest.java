package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.webserver.MyWebServerSession;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ConcreteStopSubjectTest {

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
   * Test constructor with parameters.
   */
  @Test
  public void testConstructorWithParameters() {
    ConcreteStopSubject concreteStopSubject = new ConcreteStopSubject(null);
    assertEquals(null, concreteStopSubject.getSession());
    assertEquals(0, concreteStopSubject.stopObservers.size());
  }

  /**
   * Test registering of stop observer.
   */
  @Test
  public void testRegisterStopObserver() {
    ConcreteStopSubject concreteStopSubject = new ConcreteStopSubject(null);
    concreteStopSubject.registerStopObserver(TestUtils.createStop());
    assertEquals(1, concreteStopSubject.stopObservers.size());
  }

  /**
   * Test notify stop observers.
   */
  @Test
  public void testNotifyStopObserver() {
    Stop testStop = TestUtils.createStop();
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);

    ConcreteStopSubject concreteStopSubject = new ConcreteStopSubject(myWebServerSessionSpy);
    concreteStopSubject.registerStopObserver(testStop);
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
