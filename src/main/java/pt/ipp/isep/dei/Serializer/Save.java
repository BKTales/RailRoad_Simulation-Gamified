package pt.ipp.isep.dei.Serializer;

import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.simulation.TimeCounter;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Save {

    private static boolean alreadySavedSimulation = false;
    private static boolean alreadySavedMap = false;
    private static boolean alreadySavedScenario = false;


    public void saveSimulation() throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream("simulation_savefile.bin");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {

//            TimeCounter timeCounter = Simulation.getInstance().getTimeCounter();
//            Scenario scenario = Simulator.getInstance().getScenarioRepository().getActiveScenario();
//
//            objectOutputStream.writeObject(timeCounter);
//            objectOutputStream.writeObject(scenario);

            Simulation simulation = Simulation.getInstance();

            objectOutputStream.writeObject(simulation);

            objectOutputStream.close();

            alreadySavedSimulation = true;

        }
    }

    public static String saveMap(String answer, String mapName) throws IOException {
        if (answer.equals("Y")) {
            try {
                Map map = Simulator.getInstance().getMapRepository().getMap(mapName);
                FileOutputStream outputStream = new FileOutputStream("map_savefile.bin");
                ObjectOutputStream objectOut = new ObjectOutputStream(outputStream);

                objectOut.writeObject(map);
                objectOut.close();

                alreadySavedMap = true;

                return ("Map saved successfully!");
            } catch (Exception e) {
                return ("Error saving map: " + e.getMessage() + "\n");
            }
        }
        else
            return  ("Map not saved.");
    }

    public void saveScenario(Scenario scenario) throws IOException {
        FileOutputStream outputStream = new FileOutputStream("scenario_savefile.bin");
        ObjectOutputStream objectOut = new ObjectOutputStream(outputStream);

        objectOut.writeObject(scenario);

        objectOut.close();

        alreadySavedScenario = true;
    }

    public static boolean isAlreadySavedSimulation() {
        return alreadySavedSimulation;
    }

    public static boolean isAlreadySavedMap() {
        return alreadySavedMap;
    }

    public static boolean isAlreadySavedScenario() {
        return alreadySavedScenario;
    }
}
