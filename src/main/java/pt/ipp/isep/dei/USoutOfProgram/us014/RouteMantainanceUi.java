package pt.ipp.isep.dei.USoutOfProgram.us014;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class RouteMantainanceUi {
    private final RouteMantainanceController controller = new RouteMantainanceController();

    private int maintenanceType;
    private String[] stationsValidName;
    private String[][] linesValidName;
    private final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        RouteMantainanceUi ui = new RouteMantainanceUi();
        ui.run();
    }

    /**
     * Runs the UI for route maintenance scenario selection and route creation.
     */
    public void run() {
        int whichScenario = getWhichScenario();

        try {
            controller.getLineFile(whichScenario);
            controller.getStationFile(whichScenario);

            String[] stations = controller.getStations();
            stationsValidName = new String[stations.length];
            for (int i = 0; i < stations.length; i++) {
                stationsValidName[i] = stations[i].replace("_", "").trim();
            }

            String[][] lines = controller.getLines();
            linesValidName = new String[lines.length][lines[0].length];
            for (int i = 0; i < lines.length; i++) {
                for (int j = 0; j < lines[0].length; j++) {
                    if (j == 0 || j == 1) {
                        linesValidName[i][j] = lines[i][j].replace("_", "").trim();
                    } else {
                        linesValidName[i][j] = lines[i][j];
                    }
                }
            }

            listMaintenanceType();
            createRoute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to input which scenario to use.
     *
     * @return scenario number selected by the user
     */
    private int getWhichScenario() {
        String input;
        System.out.print("Enter scenario that you want to be used: ");
        do {
            input = sc.nextLine();
        } while (input.isEmpty() || input.isBlank() || !isValidInput(input, 4, 1));
        return Integer.parseInt(input);
    }

    /**
     * Prints the specified message to the console.
     *
     * @param message message to display
     */
    private void displayMessage(String message) {
        System.out.print(message);
    }

    /**
     * Validates if the input string represents a valid integer within specified bounds.
     *
     * @param input the user input string
     * @param max maximum valid value (inclusive)
     * @param min minimum valid value (inclusive)
     * @return true if valid, false otherwise
     */
    private boolean isValidInput(String input, int max, int min) {
        if (input == null || input.isEmpty()) {
            System.out.print("Bad input, try again: ");
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                System.out.print("Bad input, try again: ");
                return false;
            }
        }
        int value = Integer.parseInt(input);
        if (value >= min && value <= max) {
            return true;
        }
        System.out.printf("Bad input, should be between %d and %d, try again: ", min, max);
        return false;
    }

    /**
     * Displays available maintenance types and reads user selection.
     */
    private void listMaintenanceType() {
        String input;
        System.out.print("[0] - All\n[1] - Electric\nChoose maintenance type: ");
        do {
            input = sc.nextLine();
        } while (!isValidInput(input, 1, 0));
        maintenanceType = Integer.parseInt(input);
    }

    /**
     * Creates a route based on filtered lines and user-selected options.
     * Validates connectivity, odd degree vertices, and displays the resulting route.
     */
    private void createRoute() {
        String[][] filteredLines = controller.filterLines(linesValidName, maintenanceType);
        if (filteredLines.length == 0) {
            displayMessage("No lines found for the maintenance type selected!\n");
            return;
        }

        if (!controller.isGraphConnected(stationsValidName, filteredLines)) {
            displayMessage("Graph is not connected!\n");
            return;
        }

        Map<String, Integer> stationDegrees = getOddDegreeVertices(getStationDegrees(filteredLines, stationsValidName));
        if (!stationDegrees.isEmpty() && stationDegrees.size() != 2) {
            displayMessage("Number of vertices with odd degrees is different from 0 or 2!\n");
            return;
        }

        MultiGraph graph = controller.buildGraph(stationsValidName, linesValidName);

        ArrayList<String> validStartStations = getValidStartStations(stationsValidName);
        if (validStartStations.isEmpty()) {
            displayMessage("No valid start stations available for the route.\n");
            return;
        }

        int choice = menu(validStartStations, sc);
        Viewer viewer = graph.display();
        String chosenStart = validStartStations.get(choice - 1);
        displayMessage(controller.executeFleuryAlgorithm(choice, chosenStart, filteredLines, stationsValidName).toString());
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }

    /**
     * Displays a menu for the user to select the start station from valid options.
     *
     * @param validStartStations list of valid stations to start the route
     * @param sc Scanner object for reading user input
     * @return index selected by the user (1-based)
     */
    private int menu(ArrayList<String> validStartStations, Scanner sc) {
        displayMessage("Choose the start station:\n");
        int count = 1;
        String index;
        for (String station : validStartStations) {
            System.out.println(count + " - " + station);
            count++;
        }
        do {
            index = sc.nextLine();
        } while (!isValidInput(index, validStartStations.size(), 1));
        return Integer.parseInt(index);
    }

    /**
     * Retrieves a map of station degrees for the given filtered lines and stations.
     *
     * @param filteredLines filtered railway lines
     * @param stations list of stations
     * @return map of station names to their degrees
     */
    private Map<String, Integer> getStationDegrees(String[][] filteredLines, String[] stations) {
        return controller.getStationDegrees(filteredLines, stations);
    }

    /**
     * Gets the vertices with odd degrees from a map of station degrees.
     *
     * @param degrees map of station names to their degrees
     * @return map of stations with odd degrees
     */
    private Map<String, Integer> getOddDegreeVertices(Map<String, Integer> degrees) {
        return controller.getOddDegreeVertices(degrees);
    }

    /**
     * Obtains the list of valid start stations based on odd degree vertices and station degrees.
     *
     * @param stations array of station names
     * @return list of valid start stations
     */
    private ArrayList<String> getValidStartStations(String[] stations) {
        return controller.getValidStartStations(stations);
    }
}
