package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.authenticationrepository.AuthenticationRepository;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.scenario.*;

import java.io.Serializable;

public class Simulator implements Serializable {
    private static Simulator instance;

    private ScenarioRepository scenarioRepository;
    private MapRepository mapRepository;
    private AuthenticationRepository authenticationRepository;

    private Simulator(){
        scenarioRepository = new ScenarioRepository();
        mapRepository = new MapRepository();
        authenticationRepository = new AuthenticationRepository();
    }

    // --------- new methods --------- //

    /**
     * Creates a new {@link Map} with  name, height, and length,
     * adds it to the repository, and returns the created map.
     *
     * @param name the name of the map
     * @param height the height of the map
     * @param length the length of the map
     * @return the newly created {@code Map} object
     */
    public Map createMap(String name, int height, int length, int scale) throws IllegalArgumentException{
        if(!isNameUnique(name)){
            throw new IllegalArgumentException("Name already exists");
        }
        Map newMap = new Map(name, height, length, scale);
        mapRepository.addMap(newMap);
        return newMap;
    }

    public Map createMap(Map map){
        mapRepository.getAllMaps().add(map);
        return (map);
    }

    /**
     * Check if there is another map already created with the given name
     * @param mapName String with the map name
     * @return (True - MapName unique) (False - Already One map with name)
     */
    private boolean isNameUnique(String mapName) {
        if (mapRepository.getMap(mapName) != null)
            return false;
        return true;
    }

    /**
     * Creates a new {@link Scenario} with the specified parameters and adds it to the repository.
     *
     * @param name the name of the scenario
     * @param attachedMap the map attached to the scenario
     * @param timeRestrictions the time restrictions for the scenario
     * @param historicalRestrictions the historical restrictions for the scenario
     * @param technologyRestriction the technology restriction for the scenario
     * @param portBehaviour the port behaviour associated with the scenario
     * @param budget the budget associated with the scenario
     */
    public Scenario createScenario(String name, Map attachedMap, TimeRestrictions timeRestrictions,
                                   ProductRestrictions historicalRestrictions, TechnologyRestriction technologyRestriction,
                                   PortBehaviour portBehaviour, Budget budget) {
        Scenario scenario = new Scenario(name, attachedMap, timeRestrictions,
                historicalRestrictions, technologyRestriction, portBehaviour, budget);
        scenarioRepository.getScenarios().add(scenario);
        return scenario;
    }

    public void createScenario(Scenario scenario) {
        scenarioRepository.getScenarios().add(scenario);
    }


    // --------- new methods --------- //

    public static Simulator getInstance(){
        if (instance == null){
            instance = new Simulator();
        }
        return instance;
    }

    public static void setInstance(Simulator sim) {
        instance = sim;
    }

    public ScenarioRepository getScenarioRepository() {
        return scenarioRepository;
    }

    public MapRepository getMapRepository() {
        return mapRepository;
    }

    public AuthenticationRepository getAuthenticationRepository() {
        return authenticationRepository;
    }
}
