package pt.ipp.isep.dei.domain.simulation;

import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.*;

import java.io.Serializable;

/**
 * Singleton {@code Repository} class that holds and manages references to all other repositories,
 * the current scenario, map, simulation, and provides global access to them.
 */
public class Simulation implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Simulation instance;
    private Scenario currentScenario; //
    private Map currentMap; //
    private TimeCounter timeCounter;

    /**
     * Constructs a new {@code Repository} instance initializing all sub-repositories.
     */
    private Simulation(Scenario selectedScenario) {
        currentScenario = selectedScenario;
        currentMap = selectedScenario.getAttachedMap();
        timeCounter = new TimeCounter(currentScenario.getTimeRestrictions().getStartYear(), currentScenario.getTimeRestrictions().getEndYear());
    }

    /**
     * Constructs a new {@code Repository} instance initializing all sub-repositories.
     */
    private Simulation(Simulation otherSimulation) {
        currentScenario = otherSimulation.getCurrentScenario();
        currentMap = otherSimulation.getCurrentMap();
        timeCounter = otherSimulation.getTimeCounter();
    }

    private void clean(){
        refreshOldScenario();
    }

    private void refreshOldScenario(){
        Simulator s = Simulator.getInstance();
        ScenarioRepository scenarioRepo = s.getScenarioRepository();

        Scenario oldScenario = scenarioRepo.getScenario(currentScenario.getName());
        // refresh the values in scenario

        InfluenceAreaRepository influenceAreaRepositoryOLD = oldScenario.getInfluenceAreaRepository();
        oldScenario.setInfluenceAreaRepository(influenceAreaRepositoryOLD);

        IndustryRepository industryRepositoryOLD = oldScenario.getIndustryRepository();
        oldScenario.setIndustryRepository(industryRepositoryOLD);

        RailwaylineRepository railwaylineRepositoryOLD = oldScenario.getRailwaylineRepository();
        oldScenario.setRailwaylineRepository(railwaylineRepositoryOLD);

        TrainRepository trainRepositoryOLD = oldScenario.getTrainRepository();
        oldScenario.setTrainRepository(trainRepositoryOLD);

        StationRepository stationRepositoryOLD = oldScenario.getStationRepository();
        oldScenario.setStationRepository(stationRepositoryOLD);

        RouteRepository routeRepositoryOLD = oldScenario.getRouteRepository();
        oldScenario.setRouteRepository(routeRepositoryOLD);

        TripRepository tripRepositoryoLD = oldScenario.getTripRepository();
        oldScenario.setTripRepository(tripRepositoryoLD);
    }

    /**
     * Returns the singleton instance of {@code Repository}.
     * If no instance exists, it creates one.
     *
     * @return the singleton {@code Repository} instance
     */
    public static Simulation getInstance(Scenario selectedScenario) {
        instance = new Simulation(selectedScenario);
        return instance;
    }

    public static Simulation getInstance(Simulation otherSimulation) {
        instance = new Simulation(otherSimulation);
        return instance;
    }

    /**
     * Returns the singleton instance of {@code Repository}.
     * If no instance exists, it creates one.
     *
     * @return the singleton {@code Repository} instance
     */
    public static Simulation getInstance() {
        if (instance == null){
            instance = new Simulation(Simulator.getInstance().getScenarioRepository().getActiveScenario());
        }
        return instance;
    }

    // ---------------------- all the gets! ----------------------

    /**
     * Sets the current simulation in the repository.
     *
     * @param timeCounter the {@link TimeCounter} to set
     */
    public void setSimulation(TimeCounter timeCounter) {
        this.timeCounter = timeCounter;
    }

    //spublic static void setInstance(Repository repo) {
    //s    instance = repo;
    //s}

    /**
     * Returns the current map.
     *
     * @return the current {@link Map}
     */

    public Map getCurrentMap() {
        return currentMap;
    }

    /**
     * Returns the current scenario.
     *
     * @return the current {@link Scenario}
     */
    public Scenario getCurrentScenario() {
        return currentScenario;
    }

    /**
     * Returns the {@link LocationRepository}.
     *
     * @return the location repository
     */
    public LocationRepository getLocationRepository() {
        return currentMap.getlocationRepository();
    }

    /**
     * Returns the {@link RailwaylineRepository}.
     *
     * @return the railway line repository
     */
    public RailwaylineRepository getRailwaylineRepository() {
        return currentScenario.getRailwaylineRepository();
    }

    /**
     * Returns the {@link IndustryRepository}.
     *
     * @return the industry repository
     */
    public IndustryRepository getIndustryRepository() {
        return currentScenario.getIndustryRepository();
    }

    /**
     * Returns the {@link CityRepository}.
     *
     * @return the city repository
     */
    public CityRepository getCityRepository() {
        return currentScenario.getCityRepository();
    }

    /**
     * Returns the {@link TrainRepository}.
     *
     * @return the train repository
     */
    public TrainRepository getTrainRepository() {
        return currentScenario.getTrainRepository();
    }


    /**
     * Returns the {@link StationRepository}.
     *
     * @return the station repository
     */
    public StationRepository getStationRepository() {
        return currentScenario.getStationRepository();
    }

    /**
     * Returns the {@link RailwaylineRepository}.
     * (alias for getRailwaylineRepository)
     *
     * @return the railway line repository
     */
    public RailwaylineRepository getRailRepository() {
        return currentScenario.getRailwaylineRepository();
    }

    /**
     * Returns the current {@link TimeCounter}.
     *
     * @return the simulation
     */
    public TimeCounter getTimeCounter() {
        return timeCounter;
    }

    /**
     * Returns the {@link RouteRepository}.
     *
     * @return the route repository
     */
    public RouteRepository getRouteRepository() {
        return currentScenario.getRouteRepository();
    }

    /**
     * Returns the {@link TripRepository}.
     *
     * @return the trip repository
     */
    public TripRepository getTripRepository() {
        return currentScenario.getTripRepository();
    }
}
