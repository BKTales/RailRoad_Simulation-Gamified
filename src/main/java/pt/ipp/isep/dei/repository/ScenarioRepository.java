package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.scenario.Scenario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ScenarioRepository} class manages a collection of {@link Scenario} objects,
 * providing methods to create, retrieve, and print scenarios.
 */
public class ScenarioRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Scenario> scenarios;
    private Scenario activeScenario;

    /**
     * Constructs an empty {@code ScenarioRepository}.
     */
    public ScenarioRepository(){
        scenarios = new ArrayList<Scenario>();
    }


    /**
     * Returns the number of scenarios stored in the repository.
     *
     * @return the size of the scenarios list
     */
    public int getScenariosSize() {
        return scenarios.size();
    }

    /**
     * Retrieves a scenario by its name.
     *
     * @param name the name of the scenario to retrieve
     * @return the {@link Scenario} if found, or {@code null} otherwise
     */
    public Scenario getScenario(String name){
        for(Scenario tmp : scenarios){
            if (tmp.getName().equals(name))
                return tmp;
        }
        return null;
    }

    public List<Scenario> getScenarios(){
        return (this.scenarios);
    }

    /**
     * Creates and returns a new {@link Budget} with the specified initial funds.
     *
     * @param funds the initial amount of funds
     * @return a new {@code Budget} instance
     */
    public Budget setInitialBudget(double funds){
        Budget budget = new Budget(funds);
        return budget;
    }

    /**
     * Prints the names of all scenarios stored in the repository to standard output.
     */
    public void printScenarios(){
        System.out.println("=== Available scenarios ===");
        for (Scenario scenario: scenarios){
            System.out.println("Scenario's name:" + scenario.getName() + "\n");
        }
    }

    /**
     * Prints the names of scenarios associated with a specific map.
     *
     * @param map the map to filter scenarios by
     */
    public void printAvailableScenariosForMap(Map map){
        System.out.println("=== Available scenarios for map: " + map.getName() + " ===");
        for (Scenario scenario: scenarios){
            if (scenario.getAttachedMap().equals(map)){
                System.out.println("Scenario's name:" + scenario.getName() + "\n");
            }
        }
    }

    /**
     * Returns the scenario with the given name if it exists, otherwise prints a message and returns null.
     *
     * @param name the name of the scenario to set active
     * @return the {@link Scenario} if found, or {@code null} if not found
     */
    public Scenario setActiveScenario(String name){
        if(getScenario(name) != null){
            activeScenario = getScenario(name);
            return activeScenario;
        }else{
            System.out.println("Scenario not available");
            printScenarios();

            return null;
        }
    }

    public Scenario getActiveScenario() {
        return activeScenario;
    }

    /**
     * Auxiliary function that will see if the scenario was already modified
     * @param scenario scenario to analyse
     * @return (true - if it havent being modified) (false - if it has)
     */
    private boolean isScenarioNew(Scenario scenario){
        if (!scenario.getStationRepository().getStations().isEmpty())
            return false;
    if (!scenario.getCityRepository().getCities().isEmpty())
            return false;
    if (!scenario.getIndustryRepository().getIndustries().isEmpty())
            return false;
    if (!scenario.getTrainRepository().getTrains().isEmpty())
            return false;
    return true;
    }
    public List<String> getEditableScenariosNames() {
        List<String> scenarioNames = new ArrayList<>();
        for (Scenario temp : scenarios) {
            if (isScenarioNew(temp))
                scenarioNames.add(temp.getName());
        }
        return scenarioNames;
    }

    public List<String> getScenariosNames() {
        List<String> scenarioNames = new ArrayList<>();
        for (Scenario temp : scenarios) {
            scenarioNames.add(temp.getName());
        }
        return scenarioNames;
    }
}
