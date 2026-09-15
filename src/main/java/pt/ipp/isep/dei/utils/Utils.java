package pt.ipp.isep.dei.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.application.UserMenus.EditorMapMenu;
import pt.ipp.isep.dei.application.UserMenus.EditorMenu;
import pt.ipp.isep.dei.application.UserMenus.PlayerMenu;
import pt.ipp.isep.dei.application.UserMenus.PlayerSimulationMenu;

import java.io.IOException;
import java.util.Scanner;

public class Utils {
    /**
     * Get the current time in seconds from system clock
     * @return currentTime in seconds
     */
    public static long getCurrentTime(){
        return (System.currentTimeMillis() / 1000);
    }


    /**
     * Displays a header with the given title.
     *
     * @param title the header title
     */
    public static void displayHeader(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    /**
     * Displays a success message.
     *
     * @param message the message to display
     */
    public static void displaySuccessMessage(String message) {
        System.out.println("\nSUCCESS: " + message);
    }

    /**
     * Displays an error message.
     *
     * @param message the message to display
     */
    public static void displayErrorMessage(String message) {
        System.out.println("\nERROR: " + message);
    }

    // ====== for backend to request the input


    /**
     * Display the return from execution message
     * @param message String with the return
     */
    public static void displayReturnEditor(String message) throws IOException {

        App.setMessage(message);
        DisplayMessage.displayMessage(userInput -> {
            try {
                EditorMenu editorMenu = new EditorMenu();
                editorMenu.start(App.getPrimaryStage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Display the return from execution message
     * @param message String with the return
     */
    public static void displayReturnMapEditor(String message) throws IOException {

        App.setMessage(message);
        DisplayMessage.displayMessage(userInput -> {
            try {
                EditorMapMenu editorMapMenu = new EditorMapMenu();
                editorMapMenu.start(App.getPrimaryStage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Display the return from execution message
     * @param message String with the return
     */
    public static void displayReturnPlayer(String message) throws IOException {

        App.setMessage(message);
        DisplayMessage.displayMessage(userInput -> {
            try {
               PlayerMenu playerMenu = new PlayerMenu();
               playerMenu.start(App.getPrimaryStage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Display the return from execution message
     * @param message String with the return
     */
    public static void displayReturnPlayerSimulation(String message) throws IOException {

        App.setMessage(message);
        DisplayMessage.displayMessage(userInput -> {
            try {
                PlayerSimulationMenu playerMenu = new PlayerSimulationMenu();
                playerMenu.start(App.getPrimaryStage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void displayWarningInput(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
