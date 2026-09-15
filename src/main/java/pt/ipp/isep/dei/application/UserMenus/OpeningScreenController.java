package pt.ipp.isep.dei.application.UserMenus;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import pt.ipp.isep.dei.application.Authentication.App;

import java.util.Objects;

public class OpeningScreenController {
    public Button startButton;
    public ImageView backgroundImage;
    public StackPane stackPane;

    @FXML
    public void initialize() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/Eastwood.ttf"), 30);

        startButton.setOnAction(e -> openAuthentication());
    }

    private void openAuthentication() {
        Stage primaryStage = App.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Authentication.fxml")));
            Scene scene2 = new Scene(root);
            primaryStage.setTitle("Login Menu");
            primaryStage.setScene(scene2);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
