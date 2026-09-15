package pt.ipp.isep.dei.application.UserMenus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.ipp.isep.dei.application.Authentication.App;

import java.io.IOException;

public class PlayerSimulationMenu extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerMenu.class.getResource("/fxml/PlayerSimulationMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Player Simulation Menu!");
        App.refreshScene(scene);
        stage.setScene(scene);
        stage.show();
    }
}
