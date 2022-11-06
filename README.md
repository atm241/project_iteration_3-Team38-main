# Visual Transit Simulator: Project Iteration 3

## The Visual Transit Simulator Software

In the project iterations, you will be working on a visual transit simulator (VTS) software. The current VTS software models bus transit around the University of Minnesota campus. The software simulates the behavior of busses and passengers on campus. Specifically, the busses go along a route, make stops, and pick up/drop off passengers. The simulation operates over a certain number of time units. In each time unit, the VTS software updates the state of the simulation by creating passengers at stops, moving busses along the routes, allowing a bus to pick up passengers at a stop, etc. The simulation is configured using a *configuration* file that specifies the simulation routes, the stops of the routes, and how likely it is that a passenger will show up at a certain stop in each time unit. Routes must be defined in pairs, that is, there should be both an outgoing and incoming route and the routes should be specified one after the other. The ending stop of the outgoing route should be at the same location as the starting stop of the incoming route and the ending stop of the incoming route should be at the same location as the starting stop of the outgoing route. However, stops between the starting and ending stops of outgoing and incoming routes can be at different locations. After a bus has passed a stop, it is possible for passengers to show up at stops that the bus has already passed. For this reason, the simulator supports the creation of multiple busses and these busses will go and pick up the new passengers. Each bus has its own understanding of its own route, but the stops have relationships with multiple buses. Buses do not make more than one trip through their routes. When a bus finish both of its routes (outbound and inbound), the bus exits the simulation.

The VTS software is divided into two main modules: the *visualization module* and the *simulator module*. The visualization module displays the state of the simulation in a browser, while the simulator module performs the simulation. The visualization module is a web client application that runs in a browser and it is written in Javascript and HTML. The visualization module code is inside the `<dir>/src/main/webapp/web_graphics` directory of this repo (where `<dir>` is the directory of the repo). The simulator module is a web server application written in Java. The simulator module code is inside the `<dir>/src/main/java/edu/umn/cs/csci3081w/project` directory. The simulator module is divided into two parts: *domain classes* and the *web server*. The domain classes model real-world entities (e.g., the concept of a bus) and the code is inside the `<dir>/src/main/java/edu/umn/cs/csci3081w/project/model` directory. The web server includes the code that orchestrates the simulation and is inside the `<dir>/src/main/java/edu/umn/cs/csci3081w/project/webserver` directory. The visualization module and the simulator module communicate with each other using [websockets](https://www.baeldung.com/java-websockets). In the project iterations, you will largely focus on the code of the simulator module.

The user of the VTS software interacts with the visualization module using the browser and can specific how long the simulation will run (i.e., how many time units) and how often new busses will be added to a route in the simulation. The users also specifies when to start the simulation. The image below depicts the graphical user interface (GUI) of the VTS software. 

![GUI of the VTS Software](https://www-users.cs.umn.edu/~mfazzini/teaching/csci3081w/vs_iteration_2.png)

### VTS Software Details

#### Simulation Configuration
The simulation is based on the `<dir>/src/main/resources/config.txt` configuration file. The following excerpt of the configration file defines a route.

```
ROUTE, East Bound

STOP, Blegen Hall, 44.972392, -93.243774, .15
STOP, Coffman, 44.973580, -93.235071, .3
STOP, Oak Street at University Avenue, 44.975392, -93.226632, .025
STOP, Transitway at 23rd Avenue SE, 44.975837, -93.222174, .05
STOP, Transitway at Commonwealth Avenue, 44.980753, -93.180669, .05
STOP, State Fairgrounds Lot S-108, 44.983375, -93.178810, .01
STOP, Buford at Gortner Avenue, 44.984540, -93.181692, .01
STOP, St. Paul Student Center, 44.984630, -93.186352, 0

ROUTE, West Bound

STOP, St. Paul Student Center, 44.984630, -93.186352, .35
STOP, Buford at Gortner Avenue, 44.984482, -93.181657, .05
STOP, State Fairgrounds Lot S-108, 44.983703, -93.178846, .01
STOP, Transitway at Commonwealth Avenue, 44.980663, -93.180808, .01
STOP, Thompson Center & 23rd Avenue SE, 44.976397, -93.221801, .025
STOP, Ridder Arena, 44.978058, -93.229176, .05
STOP, Pleasant Street at Jones-Eddy Circle, 44.978366, -93.236038, .1
STOP, Bruininks Hall, 44.974549, -93.236927, .3
STOP, Blegen Hall, 44.972638, -93.243591, 0

ROUTE, East Bound Express

STOP, Blegen Hall, 44.972392, -93.243774, .15
STOP, Transitway at Commonwealth Avenue, 44.980753, -93.180669, .05
STOP, St. Paul Student Center, 44.984630, -93.186352, 0

ROUTE, West Bound Express

STOP, St. Paul Student Center, 44.984630, -93.186352, .35
STOP, Thompson Center & 23rd Avenue SE, 44.976397, -93.221801, .025
STOP, Blegen Hall, 44.972638, -93.243591, 0
```
The line `ROUTE, East Bound` defines a the outgoing route. The lines after the line that defines a route are stops for the route. Each stop has a name, a longitude, a latitude, and the probability to generate a passenger at the stop. For example, for `STOP, Blegen Hall, 44.972392, -93.243774, .15`, `Blegen Hall` is the name of the route, `44.972392` is the longitude, `-93.243774` is the latitude, and `.15` (i.e., `0.15`) is the probability to generate a passenger at the stop.

#### Running the VTS Software
To run the VTS software, you have to first start the simulator module and then start the visualization module. To start the simulator module, go to `<dir>` and run `./gradlew appRun`. To start the visualization module, open a browser and paste this link `http://localhost:7777/project/web_graphics/project.html` in its search bar. To stop the simulator module, press the enter/return key in the terminal where you started the module. To stop the visualization module, close the tab of browser where you started the module. In rare occasions, you might experience some issues in starting the simulator module because a previous instance of the module is still running. To kill old instances, run `ps aux | grep gretty | awk '{print $2}' | xargs -L 1 kill` and this command will terminate previous instances. (The comman is killing the web server container running the simulator module.) The command works on VOLE3D and on the class VM.

#### Simulation Workflow
Because the VTS software is a web application, the software does not have a `main` method. When you load the visualization module in the browser, the visualization module opens a connection to the simulator module (using a websocket). The opening of the connection triggers the execution of the `MyWebServerSession.onOpen` method in the simulator module. When you click `Start` in the GUI of the visualization module, the module starts sending messages/commands to the simulator module. The messages/commands exchanged by the two modules are [JSON objects](https://www.w3schools.com/js/js_json_objects.asp). You can see the messages/commands created by the visualization module inside `<dir>/src/main/webapp/web_graphics/sketch.js`. The simulator module processes messages received by the visualization model inside the `MyWebServerSession.onMessage` method. The simulator module sends messages to the visualization module using the `MyWebServerSession.sendJson` method. Finally, once you start the simulation you can restart it only by reloading the visualization module in the browser (i.e., reloading the web page of the visualization module).

## Tasks and Deliverables
In this project iteration, you will need to further understand, extend, and test the VTS software. The tasks of this project iteration can be grouped into three types of activities: updating the software documentation, making software changes, and testing. The following table provides a summary of the tasks you need to perform in this project iteration. For each task, the table reports the task ID, the activity associated with the task, a summary description of the task, the deliverable associated with the task, and the major deliverable that includes the task deliverable.

| ID | Activity | Task Summary Description | Task Deliverable | Deliverable |
|---------|----------|--------------------------|------------------|----------------------|
| Task 1 | Software documentation | Update the UML class diagram describing the domain classes to account for the changes/additions that are required in this project iteration | UML Class Diagram | HTML Javadoc |
| Task 2 | Software documentation | Update the UML class diagram describing the web server classes to account for the changes/additions that are required in this project iteration | UML Class Diagram | HTML Javadoc |
| Task 3 | Software documentation | Create a Javadoc documentation for the code in the simulator module | HTML Javadoc | HTML Javadoc|
| Task 4 | Software documentation | Make sure that the code conforms to the Google Java code style guidelines | Source Code | Source Code |
| Task 5 | Software changes | Feature 1 - Save simulation data | Source Code | Source Code |
| Task 6 | Software changes | Feature 2 - Decorate busses based on route direction  | Source Code | Source Code |
| Task 7 | Testing | Create tests for all the classes | Test Code | Test Code |
| Task 8 | Software changes | Refactoring 1 - Remove testing flags in source and test code | Source and Test Code | Source and Test Code |

### Suggested Timeline for the Tasks

We suggest you define a tasks-oriented timeline for your team so that you can make progress throughout this project iteration. The schedule for project iterations is very tight, and it is important that the team keeps up with the project. We suggest the following timeline. However, you are free to define your own timeline. All the major deliverables are due at the end of the project iteration.

| Date | Milestone Description | Tasks |
|-----------------|-----|-----------------|
| 11/30/2020 at 8:00 am | Software documentation and testing | [Task 1], [Task 2], [Task 7] |
| 12/07/2020 at 8:00 am | Software changes and software testing | [Task 5],[Task 6],[Task 7],[Task 8] |
| 12/14/2020 at 8:00 am | Revision of the software documentation and software testing | [Task 1],[Task 2],[Task 3],[Task 4],[Task 7] |

### Tasks Detailed Description
This section details the tasks that your team **needs to perform** in this project iteration.

#### Task 1: Update the UML class diagram describing the domain classes
In this task, you should update the UML class diagram (`<dir>/doc/diagrams/domain_diagram.png`) describing the domain classes (i.e., classes in the `<dir>/src/main/java/edu/umn/cs/csci3081w/project/model` directory) to account for the changes/additions required in this project iteration. If necessary, provide a description inside `<dir>/doc/overview.html` to clarify the subtleties that are essential to understand the diagram and the design decisions you made.

You should place the diagram as am image file in `<dir>/doc/diagrams`. The file should be called `domain_diagram` with the suitable extension for the type of image file your team created. You can generate the HTML Javadoc documentation, which includes this diagram, by running `./gradlew clean javadoc`.

#### Task 2: Update the UML class diagram describing the web server classes
In this task, you should update the UML class diagram (`<dir>/doc/diagrams/webserver_diagram.png`) describing the web server classes (i.e., classes in the `<dir>/src/main/java/edu/umn/cs/csci3081w/project/webserver` directory) to account for the changes/additions required in this project iteration. If necessary, provide a description inside `<dir>/doc/overview.html` to clarify the subtleties that are essential to understand the diagram and the design decisions you made.

You should place the diagram as am image file in `<dir>/doc/diagrams`. The file should be called `webserver_diagram` with the suitable extension for the type of image file your team created. You can generate the HTML Javadoc documentation, which includes this diagram, by running `./gradlew clean javadoc`.

#### Task 3: Create a Javadoc documentation for the code in the simulator module
You should create Javadoc comments according to the Google Java code style guidelines we provided. In other words, add comments where the Google Java code style guidelines require. You can generate the HTML Javadoc documentation by running `./gradlew clean javadoc`.

#### Task 4: Make sure that the code conforms to the Google Java code style guidelines
Consistency in code organization, naming conventions, file structure, and formatting makes code easier to read and integrate. In this project, your team will follow the Google Java code style guidelines. These guidelines are provided in the `<dir>/config/checkstyle/google_checks.xml` code style file. Your team needs to make sure that the code produced in this project iteration (both source and test code) complies with the rules in `<dir>/config/checkstyle/google_checks.xml`. You can check if the code conforms to the code style rules by running `./gradlew clean check`.

#### Task 5: Feature 1 - Save simulation data
In this project iteration, you need to extend the simulator to save simulation data. Specifically, at the end of each simulation step, the simulator should save to a comma-separated values (CSV) file information about the current state of busses and stops. Each value, should be saved as a string.

For a bus you should save the following comma-separated line to the file:

```
BUS, SIMULATION_TIME_ELAPSED, ID, POSITION_X, POSITION_Y, NUM_PASS, CAP
```

* `BUS` is the string `"BUS"`.
* `SIMULATION_TIME_ELAPSED` is the string corresponding to the number of time steps elapsed during the simulation.
* `ID` is the string corresponding to identifier of the bus.
* `POSITION_X` is the string corresponding to the longitude of the bus position.
* `POSITION_Y` is the string corresponding to the latitude of the bus position.
* `NUM_PASS` is the string corresponding to the number of passenger on the bus.
* `CAP` is the string corresponding to the capacity of the bus.

For a stop you should save the following comma-separated line to the file:

```
STOP, SIMULATION_TIME_ELAPSED, ID, POSITION_X, POSITION_Y, NUM_PASS
```

* `STOP` is the string `"STOP"`.
* `SIMULATION_TIME_ELAPSED` is the string corresponding to the number of time steps elapsed during the simulation.
* `ID` is the string corresponding to identifier of the stop.
* `POSITION_X` is the string corresponding to the longitude of the stop position.
* `POSITION_Y` is the string corresponding to the latitude of the stop position.
* `NUM_PASS` is the string corresponding to the number of passenger waiting at the stop.


This feature needs to leverage the singleton pattern. The singleton pattern needs to be used to create an instance of the class holding the object that writes to file. This [link](https://stackabuse.com/reading-and-writing-csvs-in-java) provides some details on how to write information to a CSV file.

All the classes associated with this feature need to be in the web server component (i.e., inside `<dir>/src/main/java/edu/umn/cs/csci3081w/project/webserver`). Your team needs to create a GitHub issue to indicate that your team is working on this feature (in this project iteration we are using issues to work on features). The title of the issues should be "Feature 1 - Save simulation data". This task needs to be performed in a branch called `Feature1`. Your team needs to create a GitHub pull request to merge the changes into master. When your team is happy with the solution to this task, one of the team members needs to merge the `Feature1` branch into the `main` branch.

#### Task 6: Feature 2 - Decorate busses based on route direction
In the current version of the simulator, the bus color stays the same (color red) throughout the simulation. A bus on an inbound route is the same color as a bus on the outbound route, which makes them indistinguishable.

In this task, you will need to make the color of the bus distinguishable depending on whether the bus is on an outbound route or an inbound route. You need to accomplish this task using the decorator pattern and implementing the appropriate logic to change the color of the bus dependent on the type of route the bus is currently traveling. For a bus traveling on an outbound route you should use the color maroon, and for a bus traveling on an inbound route you should use the color gold. Color should be expressed in the [RGBA format](https://www.w3schools.com/css/css_colors_rgb.asp). In this format colors have a red, green, blue, and alpha component. For the maroon color, you should the values red=128, green=0, blue=0, and alpha=255. For the gold color, you should use the values red=255, green=215, blue=0, and alpha=255. The visualization module expects this information as part of the `updateBusses` command. Specifically the `updateBusses` JSON object should contain a `color` JSON object. This JSON object needs to have four attributes `red`, `green`, `blue`, and `alpha`. This attributes will hold integer values based on the state of the simulation.

This feature needs to leverage the decorator pattern. All the classes associated with this feature need to be in the directory containing the domain classes (i.e., inside `<dir>/src/main/java/edu/umn/cs/csci3081w/project/model`). Your team needs to create a GitHub issue to indicate that your team is working on this feature (in this project iteration we are using issues to work on features). The title of the issues should be "Feature 2 - Decorate busses based on route direction". This task needs to be performed in a branch called `Feature2`. Your team needs to create a GitHub pull request to merge the changes into master. When your team is happy with the solution to this task, one of the team members needs to merge the `Feature2` branch into the `main` branch.

#### Task 7: Create tests for all the classes
In this project iteration, your team needs to create JUnit test cases for all the classes in the simulator module. This means that all the domain (except for `RandomConcreteBusCreator`) and web server classes should be tested. Your test cases need to achieve 90% branch coverage. Some of your test cases need to use test doubles. Specifically, you should create at least 10 test cases using test doubles. Your team does not need to create test cases for getter and setter methods. Your team has to document what each test is supposed to do by adding a Javadoc comment to the test. A sample set of test cases is provided in the `<dir>/src/test/java/edu/umn/cs/csci3081w/project/model` and `<dir>/src/test/java/edu/umn/cs/csci3081w/project/webserver` directories. In this task, your team can also reuse (and we encourage you to do so) the test cases that the team built during project iterations 1 and 2. We encourage your team to write the test cases before making any change to the codebase. When you add the new features, you should also add new test cases for the features (when applicable). All the test cases you create should pass. Your team can create test cases in any branch but the final set of test cases should be in the `main` branch. You can run tests with the command `./gradlew clean test`.

#### Task 8: Refactoring 1 - Remove testing flags in source code
In this project iteration, your team will use test doubles for testing the VTS software. For this reason, you will not need the following two flags `Bus.TESTING` and `Stop.TESTING`. You should remove these two flags from the source and test code. You should also remove the related attributes `testingOutput` and keep the part of the code that send the message to the visualization simulator. You also need to remove the getters `testingOutput`. As part of this task, your team should also refactor existing tests.

Your team needs to create a GitHub issue to indicate that your team is working on this refactoring (in this project iteration we are using issues to work on features). The title of the issues should be "Refactoring 1 - Remove testing flags in source code". This task needs to be performed in a branch called `Refactoring1`. Your team needs to create a GitHub pull request to merge the changes into master. When your team is happy with the solution to this task, one of the team members needs to merge the `Refactoring1` branch into the `main` branch.

#### Important Notes

To complete the tasks of this project iteration, your team can reuse any of the design documents, code, and tests that your team created in project iterations 1 and 2. However, beware of the following points:

* You might have added classes, methods, and attributes that are not present in this repo.
* You should preserve and extend the functionality provided by this repo.

## Submission
**All the team members should submit the commit ID of the solution to this project iteration on Gradescope**. The commit ID should be from the `main` branch of this repo. We use the commit ID to be sure that all team members agree on the final solution submitted.

### General Submission Reminders
* Use GitHub issues and pull requests as you develop your solution.
* Use the branches we listed to produce your team solution.
* Make sure the files of your solution are in the repo.
* Do no add the content of the `build` directory to the repo.
* Make sure your code compiles.
* Make sure the code conforms to the Google Java code style guidelines we provided.
* Make sure the HTML Javadoc documentation can be generated using `./gradlew clean javadoc`
* Make sure all test cases pass.

## Assessment
The following list provides a breakdown of how this project iteration will be graded.

* Software documentation: 16 points
* Software changes: 32 points
* Testing: 52 points

## Resources

* [A Guide to the Java API for WebSocket](https://www.baeldung.com/java-websockets)
* [JSON objects](https://www.w3schools.com/js/js_json_objects.asp)
* [Google Maps](http://maps.google.com)
* [Reading and Writing CSVs in Java](https://stackabuse.com/reading-and-writing-csvs-in-java)
* [RGBA format](https://www.w3schools.com/css/css_colors_rgb.asp)
