package pt.ipp.isep.dei.us007;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pt.ipp.isep.dei.utils.ReadListInput.requestUserInputList;
import static pt.ipp.isep.dei.utils.Utils.displayWarningInput;

public class DisplayStationsUi implements Serializable {
    private static final long serialVersionUID = 1L;
    private final DisplayStationsController controller = new DisplayStationsController();

    /**
     * Runs the UI flow to list all stations and display selected station info.
     */
    public void run() {
        if (listAllStations()) {
            requestStationIndex();
        }
    }


    // -------------------- private methods --------------------

    /**
     * Prints a message to the console.
     * @param message The message to print.
     */
    private void displayMessage(String message) {
        System.out.printf(message);
    }

    /**
     * Retrieves and displays the list of available stations, then prompts the user to select one by index.
     * <p>
     * The method validates that the input is an integer within the valid range of station indices.
     * If no stations are available, it returns {@code -1} immediately.
     *
     * @return the selected station index if valid input is provided; {@code -1} if no stations are available.
     */
    private boolean listAllStations() {
        String stations = controller.getListOfStations();

        if (stations == null || stations.trim().isEmpty()) {
            Utils.displayWarningInput("List of stations is empty");
            return false;
        }

        App.setMessage("Choose the station index:");
        App.setList(stations);
        return true;
    }

    private void requestStationIndex() {
        requestUserInputList(selectedInput -> {
            try {
                Pattern pattern = Pattern.compile("\\[(\\d+)]");
                Matcher matcher = pattern.matcher(selectedInput.trim());

                if (matcher.find()) {
                    int index = Integer.parseInt(matcher.group(1));
                    String[] stations = App.getList().split("\n");

                    if (index < 0 || index >= stations.length) {
                        displayWarningInput("Invalid index. Please choose a valid station from the list.");
                    } else {
                        displaysStationInfo(index);
                    }
                } else {
                    displayWarningInput("Invalid format. Please select an item from the list.");
                }
            } catch (NumberFormatException e) {
                displayWarningInput("Please enter a valid number.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * Displays detailed information for the station at the given index.
     * @param index The index of the station to display.
     */
    private void displaysStationInfo(int index) throws IOException {
        String info = controller.getStationInfo(index);
        Utils.displayReturnPlayer(info);
    }
}
