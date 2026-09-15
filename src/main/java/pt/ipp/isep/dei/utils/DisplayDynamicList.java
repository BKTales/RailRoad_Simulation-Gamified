package pt.ipp.isep.dei.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import pt.ipp.isep.dei.application.Authentication.App;

import java.io.IOException;
import java.util.List;

/**
 * Utility class to display a list of strings in a JavaFX overlay with blurred background.
 */
public class DisplayDynamicList {

    /**
     * Builds a scene layering the current scene's root with the list overlay,
     * applying a blur effect to the background.
     *
     * @param menuOverlay the overlay Parent containing the list UI
     * @return the composed Scene with blur effect and overlay
     */
    private static Scene buildScene(Parent menuOverlay){
        Scene baseScene = App.getCurrentScene();
        Parent baseRoot = baseScene.getRoot();
        baseRoot.setEffect(new GaussianBlur(8));

        StackPane layered = new StackPane(baseRoot, menuOverlay);
        return new Scene(layered, baseScene.getWidth(), baseScene.getHeight());
    }

    /**
     * Displays the list of strings on top of the current scene, using the specified callback controller.
     *
     * @param listItems list of strings to display in the ListView
     * @param callbackController a controller implementing DisplayListControllerInterface for callbacks
     */
    public static void displayList(List<String> listItems, DisplayListControllerInterface callbackController,String message) {

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(DisplayDynamicList.class.getResource("/fxml/DisplayDynamicList.fxml"));

                Parent root = loader.load();

                DisplayDynamicListController controller = loader.getController();

                controller.setCallback(callbackController);

                controller.initialize(listItems);

                controller.initializeWithMessage(message);

                //controller.initializeWithMessage("Chosse an option:");

                Scene newScene = buildScene(root);
                App.refreshScene(newScene);
                App.getPrimaryStage().setScene(newScene);
                App.getPrimaryStage().show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
