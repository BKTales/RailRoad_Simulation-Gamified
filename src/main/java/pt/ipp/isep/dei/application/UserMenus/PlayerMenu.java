package pt.ipp.isep.dei.application.UserMenus;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.utils.Utils;

import java.io.IOException;

public class PlayerMenu extends Application {
    private static javafx.animation.Timeline refreshTimeline;
    private Simulation simulation;

    private boolean isIdle = false;


    @Override
    public void start(Stage stage) throws IOException {
        PlayerMenuController oldController = App.getPlayerMenuController();
        if (oldController != null) {
            oldController.close();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerMenu.class.getResource("/fxml/PlayerMenu.fxml"));
        Parent root = fxmlLoader.load();
        PlayerMenuController newController = fxmlLoader.getController();

        App.setPlayerMenuController(newController);
        root.setUserData("PlayerMenu");
        Scene scene = new Scene(root);

        simulation = Simulation.getInstance();
        stage.setTitle("Player Menu!");
        App.refreshScene(scene);
        stage.setScene(scene);
        stage.show();
        newController.clearLog();
        startRefreshing();
    }

    private void startRefreshing() {
        refreshTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.seconds(2), e -> {
                    String output = simulation.getTimeCounter().refresh(simulation);
                    if (output != null) {
                        writeOnLog(output);
                    }
                    PlayerMenuController controller = App.getPlayerMenuController();
                    if (controller != null) {
                        controller.updateTrainPositions();
                    }
                })
        );
        refreshTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        refreshTimeline.play();
    }

    public void stopRefreshing() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
    }

    /**
     * Function will get the controller from the scene and add the new happenings to
     * the log
     * @param message message with what happened
     */
    public static void writeOnLog(String message){
        try {
            PlayerMenuController controller = App.getPlayerMenuController();

            Scene currentScene = App.getCurrentScene();
            if (controller != null && "PlayerMenu".equals(currentScene.getRoot().getUserData())) {
                controller.addLog(message);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}