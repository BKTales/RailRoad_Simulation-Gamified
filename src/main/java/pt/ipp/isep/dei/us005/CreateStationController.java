package pt.ipp.isep.dei.us005;

import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.domain.InsuficientBudget;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.repository.*;

import java.io.Serializable;


/**
 * Controller class responsible for creating stations within the current scenario.
 * It manages the necessary repositories and validates the budget before creation.
 */
public class CreateStationController implements Serializable {
    private static final long serialVersionUID = 1L;
    private StationRepository stationRepo;
    private Simulation instance;
    private Budget budget;

    /**
     * Constructs a CreateStationController and initializes all required repositories.
     */
    public CreateStationController() {
        initializeRepositories();
    }

    /**
     * Returns a formatted list of available station types.
     *
     * @return a String listing all station types.
     */
    public String getListOfStationTypes() {
        return stationRepo.getListOfStationTypes();
    }

    /**
     * Returns the name of the nearest city to the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the nearest city name as a String
     */
    public String getNearestCityName(int x, int y){
        return instance.getCurrentScenario().nearestCityNames(x, y);
    }

    /**
     * Creates a station at the specified location with the given attributes.
     * Checks if the current budget can cover the price before creation.
     *
     * @param name the name of the station
     * @param x the x-coordinate of the station location
     * @param y the y-coordinate of the station location
     * @param stationType the type of the station
     * @param cardinalPosition the cardinal position description of the station
     * @param price the cost to create the station
     * @throws InsuficientBudget if the current budget does not cover the price
     */
    public void createStation(String name, int x, int y, String stationType, String cardinalPosition, double price) {
        if (budget != null && budget.hasEnoughFunds(price)) {
            Position p = new Position(x, y);
            Location tmp = instance.getCurrentMap().createLocation(p);
            Map currentMap = instance.getCurrentMap();
            int maxHeight = currentMap.getHeight();
            int maxLenght = currentMap.getLength();
            instance.getCurrentScenario().createStation(name, stationType, tmp, cardinalPosition, maxHeight, maxLenght);
            budget.subtractFunds(price);
        } else {
            throw new InsuficientBudget("Insuficient budget. " + price + " required!");
        }
    }


    /**
     * Initializes all repositories used by the controller.
     */
    private void initializeRepositories() {
        instance = Simulation.getInstance();
        this.stationRepo = instance.getStationRepository();
        this.budget = instance.getCurrentScenario().getBudget();
    }
}
