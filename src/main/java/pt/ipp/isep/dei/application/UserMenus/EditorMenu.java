package pt.ipp.isep.dei.application.UserMenus;


import static javafx.application.Application.launch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.ipp.isep.dei.application.Authentication.App;

import java.io.IOException;


public class EditorMenu extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditorMenu.class.getResource("/fxml/EditorMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Editor Menu");
        App.refreshScene(scene);
        stage.setScene(scene);
        stage.show();
    }
}
