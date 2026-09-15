package pt.ipp.isep.dei.us005;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.InsuficientBudget;
import pt.ipp.isep.dei.domain.station.StationTypes;
import pt.ipp.isep.dei.utils.ReadStringInput;
import pt.ipp.isep.dei.utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pt.ipp.isep.dei.utils.ReadListInput.requestUserInputList;
import static pt.ipp.isep.dei.utils.Utils.displayWarningInput;

public class CreateStationUi implements Serializable {
    private static final long serialVersionUID = 1L;
    private String stationType;
    private double price;
    private String cardinalPosition = null;
    private int x;
    private int y;
    private String name;
    private final CreateStationController controller = new CreateStationController();


    // -------------------- public methods --------------------

    /**
     * Runs the user interface to create a station.
     * It guides the user through selecting station type, position, name, and optional cardinal position,
     * then confirms and attempts station creation.
     */
    public void run() {
        if (listOfStationTypes()) {
            requestStationTypeIndex();
        }
    }


    // -------------------- private methods --------------------

    /**
     * Asks the user for confirmation to create the station.
     * Attempts to create the station if confirmed.
     */
    private void confirmStation() {
        try {
            createStation();
            if (stationType.equals("STATION")){
                Utils.displayReturnPlayer("Sucessfully Created\n" + stationType + ": " + name + "\n" +
                        "Position (" + x + ", " + y + ") and geometric center:" + cardinalPosition + "\nPrice:" + price);
            }else{
                Utils.displayReturnPlayer("Sucessfully Created\n" + stationType + ": " + name + "\n" +
                        "Position (" + x + ", " + y + ")\n" + "Price:" + price);
            }
        } catch (InsuficientBudget | IllegalArgumentException e) {
            try {
                Utils.displayReturnPlayer("Error creating " + stationType + ": " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to confirm station creation.
     * Repeats prompt until a valid answer is received ('Y' or 'N').
     */
    private void askConfirmationBeforeCreatingStation() {
        App.setMessage("Do you want to create the " + stationType + "? (Y/N)");

        ReadStringInput.requestUserInputString(userInput -> {
            String input = userInput.trim().toUpperCase();

            if (input.equals("Y")) {
                confirmStation();
            } else if (input.equals("N")) {
                try {
                    Utils.displayReturnPlayer("Station creation canceled.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                displayWarningInput("Please enter 'Y' for yes or 'N' for no.");
                askConfirmationBeforeCreatingStation();
            }
        });
    }

    /**
     * Retrieves and displays the list of available station types.
     * @return true if station types exist and are shown, false otherwise.
     */
    private boolean listOfStationTypes() {
        String stations = controller.getListOfStationTypes();

        if (stations == null || stations.trim().isEmpty()) {
            Utils.displayWarningInput("No station types found!");
            return false;
        }

        App.setMessage("Choose the station type index:");
        App.setList(stations);
        return true;
    }

    /**
     * Requests the user to select the station type by index.
     * Parses and stores the selected type and its price.
     */
    private void requestStationTypeIndex() {
        requestUserInputList(selectedInput -> {
            try {
                int index = extractIndex(selectedInput);
                String[] stationArray = App.getList().split("\n");

                if (index < 0 || index >= stationArray.length) {
                    Utils.displayWarningInput("Invalid station type selected.");
                    requestStationTypeIndex(); // tenta de novo
                    return;
                }

                String selectedStation = stationArray[index];
                Pattern pattern = Pattern.compile("^\\s*\\[\\d+]\\s*-\\s*(.+?),\\s*price:\\s*([\\d.,]+)$");
                Matcher matcher = pattern.matcher(selectedStation);

                if (matcher.find()) {
                    String name = matcher.group(1).trim();
                    double price = Double.parseDouble(matcher.group(2).replace(',', '.'));

                    this.stationType = name;
                    this.price = price;
                    readPosition();
                } else {
                    Utils.displayWarningInput("Invalid format for selected station type.");
                    requestStationTypeIndex();
                }
            } catch (NumberFormatException e) {
                Utils.displayWarningInput("Invalid input. Please enter a valid number.");
                requestStationTypeIndex();
            }
        });
    }

    /**
     * Reads the coordinates for the station position (x, y).
     * Repeats until a valid input is provided.
     */
    private void readPosition() {
        App.setMessage("Enter the x and y coordinates (e.g., 5 8):");
        ReadStringInput.requestUserInputString(userInput -> {
            String[] parts = userInput.trim().split("\\s+");
            if (parts.length != 2) {
                displayWarningInput("Please enter exactly two integers.");
                readPosition();
                return;
            }

            try {
                x = Integer.parseInt(parts[0]);
                y = Integer.parseInt(parts[1]);

                if (x < 0 || y < 0) {
                    displayWarningInput("Coordinates cannot be negative.");
                    readPosition();
                } else {
                    name = getProposedName();
                    askForNameWithSuggestion(name);
                }
            } catch (NumberFormatException e) {
                displayWarningInput("Invalid input. Try again.");
                readPosition();
            }
        });
    }


    /**
     * Reads the cardinal position (NE, SE, SW, NW) if the station type is "STATION".
     * Validates user input to ensure it matches one of the allowed options.
     */
    private void readCardinalPosition() {
        App.setMessage("Enter the cardinal position (NE, SE, SW, NW):");

        ReadStringInput.requestUserInputString(userInput -> {
            String input = userInput.trim().toUpperCase();

            if (input.matches("NE|SE|NW|SW")) {
                cardinalPosition = input.toUpperCase();
                //displayMessage("Cardinal position set to: " + cardinalPosition);
                askConfirmationBeforeCreatingStation();
            } else {
                displayWarningInput("Choose a valid option (NE, SE, SW, NW):");
                readCardinalPosition(); // Repete até ser válido
            }
        });
    }


    /**
     * Asks the user repeatedly for a valid custom name until a valid input is entered.
     * @param proposedName A suggested station name based on coordinates or city name.
     */
    private void askForNameWithSuggestion(String proposedName) {
        String suggestedText = (proposedName == null || proposedName.isEmpty())
                ? ""
                : proposedName + " " + stationType.toLowerCase();

        App.setMessage("Edit the station name if you wish:");

        ReadStringInput.requestUserInputString(suggestedText, userInput -> {
            String input = userInput.trim();
            if (!isValidCharacters(input)) {
                displayWarningInput("Invalid name. Only letters are allowed (max 255 characters).");
                askForNameWithSuggestion(proposedName);
            } else {
                name = input;
                continueAfterName();
            }
        });
    }


    /**
     * Requests the nearest city name from the controller based on coordinates.
     * @return the proposed city name or null if not available.
     */
    private String getProposedName() {
        String proposedName = null;
        try{
            proposedName = controller.getNearestCityName(x, y);
        }catch(IllegalArgumentException e){
            displayMessage(e.getMessage());
        }
        return proposedName;
    }

    /**
     * Calls the controller to create a station with the gathered information.
     */
    private void createStation() {
        controller.createStation(name, x, y, stationType,cardinalPosition,price);
    }


    /**
     * Validates that the station name contains only letters, and is non-empty and at most 255 characters.
     * @param name the name to validate.
     * @return true if valid, false otherwise.
     */
    private boolean isValidCharacters(String name) {
        if (name == null || name.isEmpty() || name.length() > 255) {
            //displayMessage("Name cannot be empty, null, or longer than 255 characters.\n");
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetter(c) && c != ' ' && c != '\'') {
                //displayMessage("The station name contains invalid characters. Only letters, spaces, and apostrophes are allowed!\n");
                return false;
            }
        }

        return true;
    }

    /**
     * Proceeds with station creation if the station type is not "STATION";
     * otherwise, prompts for the cardinal position.
     */
    private void continueAfterName() {
        if (StationTypes.STATION.name.equalsIgnoreCase(stationType)) {
            readCardinalPosition();
        } else {
            askConfirmationBeforeCreatingStation();
        }
    }

    /**
     * Displays a message to the user.
     * @param message the message to be displayed.
     */
    private void displayMessage(String message) {
        System.out.printf(message);
    }

    /**
     * Extracts the index from a formatted input string like "[2] City Station".
     * @param input the input string containing the index.
     * @return the integer index if found, -1 otherwise.
     */
    private int extractIndex(String input) {
        Matcher matcher = Pattern.compile("\\[(\\d+)]").matcher(input.trim());
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

}
