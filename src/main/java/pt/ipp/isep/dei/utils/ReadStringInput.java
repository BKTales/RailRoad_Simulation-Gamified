package pt.ipp.isep.dei.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import pt.ipp.isep.dei.application.Authentication.App;

import java.io.IOException;

public class ReadStringInput {

    /**
     * Function get the current scene and overlay it with the question menu
     * @param menuOverlay the list menu
     * @return build scene
     */
    private static Scene buildScene(Parent menuOverlay){
        Scene baseScene = App.getCurrentScene();

        Parent baseRoot = baseScene.getRoot();

        baseRoot.setEffect(new GaussianBlur(8));

        StackPane layered = new StackPane(baseRoot, menuOverlay);

        return (new Scene(layered, baseScene.getWidth(), baseScene.getHeight()));
    }

    public static void requestUserInputString(InputReceiver receiver) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(ReadStringInputController.class.getResource("/fxml/ReadStringInput.fxml"));

                // Inject controller with callback
                ReadStringInputController controller = new ReadStringInputController(receiver);
                loader.setController(controller);


                // Load FXML
                Parent menuOverlay = loader.load();

                // Initialize with the message and list
                controller.initialize(App.getMessage());

                Scene newScene = buildScene(menuOverlay);
                App.refreshScene(newScene);
                App.getPrimaryStage().setScene(newScene);
                App.getPrimaryStage().show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void requestUserInputString(String suggestedText, InputReceiver receiver) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(ReadStringInputController.class.getResource("/fxml/ReadStringInput.fxml"));

                ReadStringInputController controller = new ReadStringInputController(receiver);
                controller.setSuggestedText(suggestedText);  // Define o texto sugerido
                loader.setController(controller);

                Parent menuOverlay = loader.load();

                controller.initialize(App.getMessage());

                Scene newScene = buildScene(menuOverlay);
                App.refreshScene(newScene);
                App.getPrimaryStage().setScene(newScene);
                App.getPrimaryStage().show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
