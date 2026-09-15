package pt.ipp.isep.dei.application.UserMenus;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import pt.ipp.isep.dei.Serializer.Load;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.utils.ReadListInput;

import java.io.IOException;
import java.util.List;

public class PlayerSimulationMenuController {
    private final String startSimulation = "1 - Start Simulation";
    private final String loadSimulation = "2 - Load Simulation";
    private final String loadScenario = "3 - Load Scenario";
    private final String exit = "4 - exit";

    private final int startSimulationVal = 1;
    private final int loadSimulationVal = 2;
    private final int loadScenarioVal = 3;
    private final int exitVal = 4;

    private boolean scenarioChosen = false;
    private String activeScenarioName = null;
    private boolean alreadyLoaded = false;
    private Simulation simulation;
    private Simulator repo;


    @FXML private Button btn1;
    @FXML private Button btn2;
    @FXML private Button btn3;
    @FXML private Button btn4;

    @FXML
    public void initialize() {
        repo = Simulator.getInstance();
        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/Eastwood.ttf"), 30);

        setButtonsStrings();
        btn1.setOnAction(e -> handleMenu(startSimulationVal));
        btn2.setOnAction(e -> handleMenu(loadSimulationVal));
        btn3.setOnAction(e -> handleMenu(loadScenarioVal));
        btn4.setOnAction(e -> handleMenu(exitVal));
    }

    public void setButtonsStrings() {
        btn1.setText(startSimulation);
        btn2.setText(loadSimulation);
        btn3.setText(loadScenario);
        btn4.setText(exit);
    }

    private void handleMenu(int which) {
        try {
           if (which == startSimulationVal) {
                List<String> scenarios = repo.getScenarioRepository().getScenariosNames();
                if (scenarios.isEmpty()) {
                    showInfo("No scenarios available.");
                    return;
                }
                readScenarioName(scenarios);
           } else if (which == loadSimulationVal) {
                if (!alreadyLoaded) {
                    try {
                        simulation = Load.loadSimulation();

                        if (simulation != null && simulation.getCurrentScenario() != null) {
                            PlayerMenu ui = new PlayerMenu();
                            ui.start(App.getPrimaryStage());

                        } else {
                            showInfo("Loaded simulation is invalid or missing scenario.");
                        }
                        alreadyLoaded = true;

                    } catch (IOException | ClassNotFoundException e) {
                        showInfo("Error loading simulation: " + e.getMessage());
                    }
                } else {
                    showInfo("You already loaded a scenario or saved in this session.");
                }
           } else if(which == loadScenarioVal) {
               try {
                   Load.loadScenarioEditor();
               } catch (Exception e){
                   e.printStackTrace();
               }
           } else if (which == exitVal) {
                App app = new App();
                app.start(App.getPrimaryStage());
           }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void readScenarioName(List<String> availableScenarios) {
        // Define a mensagem e a lista na AuthenticationApp para serem acessadas pela UI
        App.setMessage("Choose a scenario from the list:");
        StringBuilder sb = new StringBuilder();
        for (String scenario : availableScenarios) {
            sb.append(scenario).append("\n");
        }
        App.setList(sb.toString());
        // Chama a UI de seleção com callback
        ReadListInput.requestUserInputList(userInput -> {
            if (userInput == null || userInput.trim().isEmpty()) {
                displayWarningInput("Invalid selection.");
                return;
            }
            String selectedScenario = userInput.trim();
            try {
                repo.getScenarioRepository().setActiveScenario(selectedScenario);
                showInfo("Scenario selected: " + selectedScenario);
                Simulation simulation = Simulation.getInstance(repo.getScenarioRepository().getScenario(selectedScenario));
                PlayerMenu ui = new PlayerMenu();
                ui.start(App.getPrimaryStage());
                simulation.getTimeCounter().startSimulation(simulation.getIndustryRepository(), simulation.getRouteRepository(), simulation.getCityRepository());
            } catch (Exception e) {
                displayWarningInput("Failed to set scenario: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void displayWarningInput(String message) {
        System.out.printf(message);
    }

    /**
     * Display the return from execution message
     * @param message String with the return
     */
    private void displayReturn(String message) {
        System.out.println(message);
    }

}