package pt.ipp.isep.dei;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.simulation.Simulation;

public class Main {
    public static void main(String[] args) {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.run();

        App.launchUi(new String[]{});
    }

    public void startEditor(Scenario selectedScenario) {
        Simulation repo = Simulation.getInstance(selectedScenario);
    }

    public void startPlayer(Scenario selectedScenario) {
        Simulation repo = Simulation.getInstance(selectedScenario);
    }
}

