package pt.ipp.isep.dei.application.UserMenus;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import pt.ipp.isep.dei.LoadScenario.LoadScenarioUI;
import pt.ipp.isep.dei.Serializer.Load;
import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.us001.CreateMapUI;
import pt.ipp.isep.dei.us004.ScenarioCreationUI;

import java.io.IOException;

public class EditorMenuController {
    private final String createMap = "Create Map";
    private final String createScenario = "Create scenario";
    private final String editScenario = "Edit scenario";
    private final String loadMapMemory = "Load Map";
    private final String loadScenarioMemory = "Load Scenario";
    private final String exit = "Exit";

    //public ImageView backgroundImage;
    public StackPane stackPane;
    public Button btn6;

    @FXML
    private ListView<String> playerMenuOptions;

    @FXML private Button btn1;
    @FXML private Button btn2;
    @FXML private Button btn3;
    @FXML private Button btn4;
    @FXML private Button btn5;

    @FXML
    public void initialize() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/Eastwood.ttf"), 30);

        setButtonsStrings();
        btn1.setOnAction(e -> createMap());
        btn2.setOnAction(e -> createScenario());
        btn3.setOnAction(e -> editScenario());
        btn4.setOnAction(e -> loadMapMemory());
        btn5.setOnAction(e -> loadScenarioMemory());
        btn6.setOnAction(e -> exitMenu());
    }

    public void setButtonsStrings() {
        btn1.setText(createMap);
        btn2.setText(createScenario);
        btn3.setText(editScenario);
        btn4.setText(loadMapMemory);
        btn5.setText(loadScenarioMemory);
        btn6.setText(exit);
    }

    private void createMap() {
        try {
            CreateMapUI c = new CreateMapUI();
            c.run();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createScenario(){
        try {
            ScenarioCreationUI c = new ScenarioCreationUI();
            c.run();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void editScenario(){
        LoadScenarioUI l = new LoadScenarioUI();
        EditorMapMenu c = new EditorMapMenu();

        l.run(() -> {
            Platform.runLater(() -> {
                try {
                    c.start(App.getPrimaryStage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }


    private void loadScenarioMemory(){
        try {
            Load.loadScenarioEditor();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadMapMemory(){
        try {
            Load.loadMapEditor();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void exitMenu(){
        try {
            App app = new App();
            app.start(App.getPrimaryStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}