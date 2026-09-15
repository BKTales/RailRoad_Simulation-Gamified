package pt.ipp.isep.dei.LoadScenario;

import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.repository.ScenarioRepository;
import pt.ipp.isep.dei.utils.*;
import java.io.IOException;
import java.util.List;

public class LoadScenarioUI {
    private final LoadScenarioController controller = new LoadScenarioController();

    public void run(Runnable onFinished) {
        if (controller.mapRepositoryIsEmpty()) {
            Utils.displayErrorMessage("You must create a map before loading a scenario");
            try {
                Utils.displayReturnEditor("Returning to editor menu");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (onFinished != null) {
                onFinished.run();
            }
            return;
        }
        askToSetActiveScenario(onFinished);
    }


    private void askToSetActiveScenario(Runnable onFinished) {
        App.setMessage("Set a scenario as active?");
        App.setList("Yes\nNo");

        ReadListInput.requestUserInputList(choice -> {
            if ("Yes".equals(choice)) {
                showAvailableScenarios(onFinished);
            } else {
                try {
                    Utils.displayReturnEditor("No scenario set as active");
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (onFinished != null) onFinished.run();
            }
        });
    }


    private void showAvailableScenarios(Runnable onFinished) {
        ScenarioRepository scenarioRepo = controller.getScenarioRepository();

        if (scenarioRepo == null || scenarioRepo.getScenarios().isEmpty()) {
            Utils.displayErrorMessage("No scenarios available! Create a scenario first.");
            safeReturnEditor("Returning to editor menu", onFinished);
            return;
        }

        List<String> scenarioNames = scenarioRepo.getEditableScenariosNames();
        if (scenarioNames.isEmpty()) {
            try{
                Utils.displayReturnEditor("No scenarios available to edit! Create a scenario first.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        App.setMessage("Select scenario to activate:");
        App.setList(String.join("\n", scenarioNames));

        ReadListInput.requestUserInputList(selected -> {
            controller.setCurrentScenario(selected);
            controller.startScenarioRepo(Simulator.getInstance()
                    .getScenarioRepository()
                    .getScenario(selected));

            safeReturnEditor("Scenario '" + selected + "' activated successfully!", onFinished);
        });
    }

    private void safeReturnEditor(String message, Runnable onFinished) {
        try {
            Utils.displayReturnEditor(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (onFinished != null) {
            onFinished.run();
        }
    }


}