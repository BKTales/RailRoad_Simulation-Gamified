package pt.ipp.isep.dei.application.UserMenus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import pt.ipp.isep.dei.Serializer.Save;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.us002.CreateIndustryUi;
import pt.ipp.isep.dei.us003.CityEditorUI;
import pt.ipp.isep.dei.utils.GraphHolder;
import pt.ipp.isep.dei.utils.GraphViewerPane;

public class EditorMapMenuController {
    private final String createIndustry = "1 - Create Industry";
    private final int createIndustryVal = 1;
    private final String createCity = "2 - Create City";
    private final int createCityVal = 2;
    private final String save = "3 - Save";
    private final int saveVal = 3;
    private final String exit = "4 - Exit";
    private final int exitVal = 4;

    @FXML
    private ListView<String> playerMenuOptions;

    @FXML private Button btn1;
    @FXML private Button btn2;
    @FXML private Button btn3;
    @FXML private Button btn4;
    @FXML private StackPane graphContainer;

    @FXML
    public void initialize() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/Eastwood.ttf"), 30);

        setButtonsStrings();
        btn1.setOnAction(e -> handleMenu(createIndustryVal));
        btn2.setOnAction(e -> handleMenu(createCityVal));
        btn3.setOnAction(e -> handleMenu(saveVal));
        btn4.setOnAction(e -> handleMenu(exitVal));
        GraphViewerPane graphViewerPane = new GraphViewerPane(GraphHolder.getGraph(Simulator.getInstance().getScenarioRepository().getActiveScenario()));
        graphContainer.getChildren().add(graphViewerPane);
    }

    public void setButtonsStrings() {
        btn1.setText(createIndustry);
        btn2.setText(createCity);
        btn3.setText(save);
        btn4.setText(exit);
    }

    private void handleMenu(int which) {
        System.out.println("Selected Menu: " + which);
        try {
            if (which == createIndustryVal) {
                CreateIndustryUi c = new CreateIndustryUi();
                c.run();
            }
            else if (which == createCityVal) {
                CityEditorUI c = new CityEditorUI();
                c.run();
            }
            else if (which == saveVal) {
                Save s = new Save();
                s.saveScenario(Simulator.getInstance().getScenarioRepository().getActiveScenario());
            }
            else if (which == exitVal) {
                EditorMenu editorMenu = new EditorMenu();
                editorMenu.start(App.getPrimaryStage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
