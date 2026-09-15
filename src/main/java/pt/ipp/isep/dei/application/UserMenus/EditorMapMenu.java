package pt.ipp.isep.dei.application.UserMenus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.ipp.isep.dei.application.Authentication.App;

import java.io.IOException;

public class EditorMapMenu extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditorMapMenu.class.getResource("/fxml/EditorMapMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        App.refreshScene(scene);
        stage.setTitle("Editor Menu");
        stage.setScene(scene);
        stage.show();
    }
}
