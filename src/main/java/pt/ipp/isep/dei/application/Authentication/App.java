package pt.ipp.isep.dei.application.Authentication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.graphstream.graph.implementations.MultiGraph;
import pt.ipp.isep.dei.application.UserMenus.PlayerMenuController;

import java.util.Objects;

public class App extends Application {
    private static Stage primaryStage;
    private static Scene currentScene;

    // ------- vars to try to fix css
    private static double fontSize;
    private static int windowSizeHeight;
    private static int windowSizeLength;

    // ------- vars to use inside the display read functions
    private static String message;
    private static String listItems;
    private static String answer;
    private static PlayerMenuController playerMenuController;

    @Override
    public void start(Stage pStage) throws Exception {
        primaryStage = pStage;
        windowSizeLength = (int) Screen.getPrimary().getVisualBounds().getWidth();
        windowSizeHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();
        fontSize = windowSizeLength * 0.015;

        primaryStage.setTitle("Tycoon");
        Parent start = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/OpeningScreen.fxml")));
        Scene scene = new Scene(start);
        currentScene = scene;
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Change the variable that held the current scene to the given scene
     * @param newScene scene to be displayed now
     */
    public static void refreshScene(Scene newScene) {
        currentScene = newScene;
    }

    /**
     * Get the scene that is being shown
     * @return currentScene
     */
    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void launchUi(String[] args) {
        launch(args);
    }

    // ============ Gets and Sets ============//

    /**
     * Set the message to be used by the readStringInput and readListInput
     * @param message string with the wnated to be displayed message
     */
    public static void setMessage(String message) {
        App.message = message;
    }

    /**
     * Set the list to be used by the readListInput to show the option to the user
     * @param list string of list items separated by a '\n'
     */
    public static void setList(String list) {
        App.listItems = list;
    }

    /**
     * Get the message set
     * @return message
     */
    public static String getMessage() {
        return message;
    }

    /**
     * Get the list set
     * @return list
     */
    public static String getList() {
        return listItems;
    }

    /**
     * Get the stage(window) that the program is running in
     * @return primaryStage(stage)
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static double getFontSize() {
        return fontSize;
    }

    public static int getWindowSizeHeight() {
        return windowSizeHeight;
    }

    public static int getWindowSizeLength() {
        return windowSizeLength;
    }

    public static void setPlayerMenuController(PlayerMenuController playerMenuController) {
        App.playerMenuController = playerMenuController;
    }

    public static PlayerMenuController getPlayerMenuController() {
        return playerMenuController ;
    }
}

