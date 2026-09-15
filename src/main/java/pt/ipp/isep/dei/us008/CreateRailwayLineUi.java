package pt.ipp.isep.dei.us008;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.InsuficientBudget;
import pt.ipp.isep.dei.utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pt.ipp.isep.dei.utils.ReadListInput.requestUserInputList;
import static pt.ipp.isep.dei.utils.ReadStringInput.requestUserInputString;

public class CreateRailwayLineUi implements Serializable {
    private static final long serialVersionUID = 1L;
    private String railwayLineType;
    private int station2;
    private int station1;
    private String station1Name;
    private String station2Name;
    private String railwayTrackType;
    private Double price;
    private final CreateRailwayLineController controller = new CreateRailwayLineController();


    /**
     * Main method to run the UI flow.
     * It validates the existence of at least two stations, then proceeds with user input
     * to configure and attempt creation of a railway line.
     */
    public void run() {
        String stationList = controller.getListOfStations();
        String[] stations = stationList.split("\n");

        if (stations.length < 2) {
            Utils.displayWarningInput("You must have at least two stations to create a railway line.");
            return;
        }

        chooseStations(stationList, stations);
    }

    /**
     * Prompts user to choose two different stations from the available list.
     * @param stationList String representation of all available stations.
     * @param stations Array of individual station lines.
     */
    private void chooseStations(String stationList, String[] stations) {
        App.setMessage("Select the first station:");
        App.setList(stationList);
        requestUserInputList(first -> {
            station1 = extractIndex(first);
            station1Name = extractName(first);
            if (station1 < 0 || station1 >= stations.length) {
                Utils.displayWarningInput("Invalid selection. Aborting.");
                return;
            }

            App.setMessage("Select the second station (different from the first):");
            App.setList(stationList);
            requestUserInputList(second -> {
                station2 = extractIndex(second);
                station2Name = extractName(second);
                if (station2 < 0 || station2 >= stations.length || station2 == station1) {
                    Utils.displayWarningInput("Invalid second station. Must be different from the first.");
                    return;
                }

                selectRailwayLineType();
            });
        });
    }

    /**
     * Prompts user to select a railway line type and stores the name and price.
     * Validates the selected index and parses the string using regex.
     */
    private void selectRailwayLineType() {
        String railTypesString = controller.getListOfRailLineTypes();
        App.setMessage("Select the railway line type:");
        App.setList(railTypesString);
        requestUserInputList(selectedInput -> {
            try {
                int index = extractIndex(selectedInput);
                String[] railTypesArray = railTypesString.split("\n");
                if (index < 0 || index >= railTypesArray.length) {
                    Utils.displayWarningInput("Invalid index. Please choose a valid railway line type.");
                    selectRailwayLineType();
                    return;
                }

                String selectedLine = railTypesArray[index];
                Pattern linePattern = Pattern.compile("RailType: (.*), price: ([0-9]+(\\.[0-9]+)?)");
                Matcher lineMatcher = linePattern.matcher(selectedLine);
                if (lineMatcher.find()) {
                    String name = lineMatcher.group(1).trim();
                    double price = Double.parseDouble(lineMatcher.group(2));

                    this.railwayLineType = name;
                    this.price = price;
                    selectTrackType();
                } else {
                    Utils.displayWarningInput("Invalid format for selected railway line.");
                    selectRailwayLineType();
                }
            } catch (NumberFormatException e) {
                Utils.displayWarningInput("Invalid input. Please enter a valid number.");
                selectRailwayLineType();
            }
        });
    }

    /**
     * Prompts user to select a track type and stores the name.
     * Updates the price by multiplying with the track type's cost multiplier.
     */
    private void selectTrackType() {
        String trackList = controller.getListOfTrackTypes();
        App.setMessage("Select the track type:");
        App.setList(trackList);

        requestUserInputList(selectedInput -> {
            try {
                int index = extractIndex(selectedInput);
                String[] trackArray = trackList.split("\n");

                if (index < 0 || index >= trackArray.length) {
                    Utils.displayWarningInput("Invalid track type selected.");
                    selectTrackType();
                    return;
                }

                String selectedTrack = trackArray[index];
                Pattern trackPattern = Pattern.compile("TrackType: (.*), Price multiplier: ([0-9]+(\\.[0-9]+)?)");
                Matcher trackMatcher = trackPattern.matcher(selectedTrack);
                if (trackMatcher.find()) {
                    String name = trackMatcher.group(1).trim();
                    double multiplier = Double.parseDouble(trackMatcher.group(2).replace(',', '.'));

                    this.railwayTrackType = name;
                    this.price *= multiplier;
                    confirmCreation();
                } else {
                    Utils.displayWarningInput("Invalid format for selected railway track.");
                    selectTrackType();
                }
            } catch (NumberFormatException e) {
                Utils.displayWarningInput("Invalid input. Please enter a valid number.");
                selectTrackType();
            }
        });
    }

    /**
     * Asks the user for final confirmation to create the railway line.
     * Handles creation logic or cancellation feedback.
     */
    private void confirmCreation() {
        App.setMessage("Do you want to create the railway line? (Y/N)");

        requestUserInputString(userInput -> {
            String input = userInput.trim().toUpperCase();

            if (input.equals("Y")) {
                try {
                    createRailwayLine();
                    Utils.displayReturnPlayer("RailwayLine created!\n" + "From " + station1Name + " to " + station2Name +
                            "\nTrackType: " + railwayTrackType + "\nRailType:" + railwayLineType + "\nPrice: " + price);
                } catch (InsuficientBudget | IllegalArgumentException | IOException e) {
                    Utils.displayWarningInput("Error: " + e.getMessage());
                    try {
                        Utils.displayReturnPlayer("RailwayLine creation cancelled");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (input.equals("N")) {
                try {
                    Utils.displayReturnPlayer("RailwayLine creation cancelled");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Utils.displayWarningInput("Please enter 'Y' for yes or 'N' for no.");
                confirmCreation();
            }
        });
    }

    /**
     * Extracts the index from a string in the format "[index]".
     * @param input User input string containing the index.
     * @return The integer index found, or -1 if not valid.
     */
    private int extractIndex(String input) {
        Matcher matcher = Pattern.compile("\\[(\\d+)]").matcher(input.trim());
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    /**
     * Extracts the name of a station from a formatted input string.
     * <p>
     * The input is expected to follow a pattern such as:
     * "[index] Terminal Name at (x,y)", "[index] Depot Name at (x,y)", or "[index] Station Name at (x,y)".
     * This method is case-insensitive and captures the station name between the type (Terminal, Station, or Depot)
     * and the "at" keyword followed by coordinates.
     *
     * @param input the formatted string containing the station description
     * @return the extracted station name, or {@code null} if the pattern does not match
     */
    private String extractName(String input) {
        Matcher matcher = Pattern.compile("(?i)(?:terminal|station|depot)\\s+(.+?)\\s+at\\s+\\(\\d+,\\d+\\)").matcher(input.trim());
        return matcher.find() ? matcher.group(1).trim() : null;
    }


    /**
     * Calls the controller to create the railway line with the configured attributes.
     */
    private void createRailwayLine() {
        controller.createRailwayline(station1, station2, railwayTrackType, railwayLineType, price);
    }
}
