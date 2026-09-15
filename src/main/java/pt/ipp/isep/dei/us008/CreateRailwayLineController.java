package pt.ipp.isep.dei.us008;

import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.domain.InsuficientBudget;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.repository.*;

import java.io.Serializable;

/**
 * Controller class for creating railway lines between stations.
 * Handles the business logic for railway line creation including budget validation,
 * distance calculation, and price computation.
 */
public class CreateRailwayLineController implements Serializable {
    private static final long serialVersionUID = 1L;
    private RailwaylineRepository railwaylineRepo;
    private StationRepository stationRepo;
    private Simulation instance;
    private int currentYear;
    private Budget budget;

    // -------------------- public methods --------------------

    /**
     * Constructs a new CreateRailwayLineController and initializes the required repositories.
     */
    public CreateRailwayLineController() {
        initializeRepositories();
    }

    /**
     * Retrieves a formatted list of all available stations.
     * @return String representation of all stations
     */
    public String getListOfStations() {
        return stationRepo.listAllStations();
    }

    /**
     * Creates a railway line between two stations with given track and rail types.
     * Price is calculated based on distance and given price per unit.
     * @param station1 index of first station
     * @param station2 index of second station
     * @param trackType type of track
     * @param railType type of rail
     * @param price price per distance unit
     * @throws IllegalArgumentException if stations don't exist or line already exists
     * @throws InsuficientBudget if budget is insufficient for the operation
     */
    public void createRailwayline(int station1, int station2, String trackType, String railType, double price) {
        StationType firstStation = stationRepo.getStation(station1);
        StationType secondStation = stationRepo.getStation(station2);

        if (firstStation == null || secondStation == null) {
            throw new IllegalArgumentException("One or both stations do not exist.");
        }

        double distance = RailwayLine.calculateDistBetweenStations(firstStation, secondStation, instance.getCurrentMap().getScale());
        double totalPrice = RailwayLine.calculatePrice(distance, price);

        if (budget != null && budget.hasEnoughFunds(totalPrice)) {
            if (railwaylineRepo.alreadyExists(firstStation, secondStation)) {
                throw new IllegalArgumentException("Railway line already exists between these stations.");
            } else {
                instance.getCurrentScenario().createRailwayLine(firstStation, secondStation, trackType, railType, distance);
                budget.subtractFunds(totalPrice);
            }
        } else {
            throw new InsuficientBudget("Insufficient budget. " + totalPrice + " required!");
        }
    }

    /**
     * Retrieves the list of available rail types for the current year.
     * @return ArrayList of available RailType objects
     */
    public String getListOfRailLineTypes() {
        return railwaylineRepo.getListOfAvailableRailwayLineTypes(currentYear);
    }

    /**
     * Retrieves a formatted list of all available track types.
     * @return String representation of all track types
     */
    public String getListOfTrackTypes() {
        return railwaylineRepo.getListOfRailwayTrackTypes();
    }

    // -------------------- private methods --------------------

    /**
     * Initializes the required repositories from the main Repository instance.
     */
    private void initializeRepositories() {
        instance = Simulation.getInstance();
        this.railwaylineRepo = instance.getRailwaylineRepository();
        this.stationRepo = instance.getStationRepository();
        this.currentYear = instance.getTimeCounter().getCurrentYear();
        this.budget = instance.getCurrentScenario().getBudget();
    }

}