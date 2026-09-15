package pt.ipp.isep.dei.LoadScenario;

import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.repository.ScenarioRepository;

public class LoadScenarioController {
    private Simulator simulator;
    private ScenarioRepository scenarioRepository;

    public LoadScenarioController() {
        simulator = Simulator.getInstance();
        scenarioRepository = simulator.getScenarioRepository();
    }

    public void availableScenarios() {
        simulator.getScenarioRepository().printScenarios();
    }

    public ScenarioRepository getScenarioRepository() {
        return scenarioRepository;
    }

    public void startScenarioRepo(Scenario scenario) {
        scenario.startRepos();
        Simulation.getInstance(scenario);
    }

    /**
     * Sets the current scenario by name.
     *
     * @param chosenScenario the name of the scenario to set as current
     */
    public void setCurrentScenario(String chosenScenario) {
        simulator.getScenarioRepository().setActiveScenario(chosenScenario);
    }

    public boolean mapRepositoryIsEmpty(){
        return simulator.getMapRepository().isEmpty();
    }

}
