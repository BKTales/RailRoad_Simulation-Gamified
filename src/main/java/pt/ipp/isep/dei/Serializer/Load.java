package pt.ipp.isep.dei.Serializer;

import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.scenario.Scenario;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Load {

    public static void loadMapEditor() throws IOException, ClassNotFoundException {

        FileInputStream inputStreamMap = new FileInputStream("map_savefile.bin");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStreamMap);

        Map map = (Map) objectInputStream.readObject();

        if (Simulator.getInstance().getMapRepository().getMap(map.getName()) == null)
            Simulator.getInstance().createMap(map);

        objectInputStream.close();

    }

    public static void loadScenarioEditor() throws IOException, ClassNotFoundException {
        FileInputStream inputStreamScenario = new FileInputStream("scenario_savefile.bin");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStreamScenario);

        Scenario scenario = (Scenario) objectInputStream.readObject();

        if (Simulator.getInstance().getScenarioRepository().getScenario(scenario.getName()) == null)
            Simulator.getInstance().createScenario(scenario);

        if (Simulator.getInstance().getMapRepository().getMap(scenario.getAttachedMap().getName()) == null)
            Simulator.getInstance().createMap(scenario.getAttachedMap());

        objectInputStream.close();
    }

    public static Simulation loadSimulation() throws IOException, ClassNotFoundException{
        try (FileInputStream inputStream = new FileInputStream("simulation_savefile.bin");
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

//            TimeCounter yearCounter = (TimeCounter) objectInputStream.readObject();
//            Scenario simulationScenario = (Scenario) objectInputStream.readObject();

            Simulation simulation = (Simulation) objectInputStream.readObject();

            Simulation.getInstance(simulation);

            if(Simulator.getInstance().getScenarioRepository().getScenario(simulation.getCurrentScenario().getName()) == null) {
                Simulator.getInstance().createScenario(simulation.getCurrentScenario());
            }

            if (Simulator.getInstance().getMapRepository().getMap(simulation.getCurrentMap().getName()) == null) {
                Simulator.getInstance().createMap(simulation.getCurrentMap());
            }

            return simulation;
        }
    }
}
