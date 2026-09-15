package pt.ipp.isep.dei.us006;

import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.domain.InsuficientBudget;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.repository.*;

import java.io.Serializable;

public class UpgradeStationController implements Serializable {
    private static final long serialVersionUID = 1L;
    private StationRepository stationRepo;
    private Simulation instance;
    private int currentYear;
    private Budget budget;

    // -------------------- public methods --------------------

    /**
     * Constructs an UpgradeStationController and initializes repositories.
     */
    public UpgradeStationController() {
        initializeRepositories();
    }

    /**
     * Returns a string listing all stations.
     * @return list of all stations as a string.
     */
    public String getListOfStations() {
        return stationRepo.listAllStations();
    }

    /**
     * Gets the available upgrades for the station specified by index.
     *
     * @param stationIndex index of the station in the repository.
     * @return a string describing the available upgrades.
     */
    public String getAvailableStationUpgrades(int stationIndex){
        StationType station = stationRepo.getStation(stationIndex);
        return stationRepo.getAvailableUpgradeStation(station);
    }

    /**
     * Gets the available buildings for acquisition by the station
     * at the current year of the simulation.
     *
     * @param stationIndex index of the station.
     * @return a string listing the available buildings.
     */
    public String getAvailableBuildings(int stationIndex){
        StationType station = stationRepo.getStation(stationIndex);
        return station.getAcquiredBuildings().getAvailableBuildings(currentYear);
    }

    /**
     * Applies an upgrade to a station if the budget is sufficient.
     *
     * Updates station data, recalculates its area of influence, maps nearby objects,
     * and subtracts the upgrade cost from the budget.
     *
     * @param stationIndex      index of the station.
     * @param upgrade           name of the upgrade to apply.
     * @param cardinalPosition  direction (e.g., North, South) where the upgrade is applied.
     * @param price             cost of the upgrade.
     * @throws InsuficientBudget if there are not enough funds.
     */
    public void applyUpgrade(int stationIndex, String upgrade, String cardinalPosition, double price) {
        if (budget != null && budget.hasEnoughFunds(price)){
            StationType station = stationRepo.getStation(stationIndex);
            Map currentMap = instance.getCurrentMap();
            int maxHeight = currentMap.getHeight();
            int maxLength = currentMap.getLength();
            instance.getCurrentScenario().passDataFromStation(station, maxHeight, maxLength, upgrade, cardinalPosition);
            budget.subtractFunds(price);
        } else {
            throw new InsuficientBudget("Insuficient budget. " + price + " required!");
        }
    }

    /**
     * Adds a building to a station if there is enough budget.
     * The cost is subtracted from the budget upon success.
     *
     * @param stationIndex index of the station.
     * @param building     name of the building to add.
     * @param price        cost of the building.
     * @throws InsuficientBudget if there are not enough funds.
     */
    public void applyBuilding(int stationIndex, String building, double price) {
        if (budget != null && budget.hasEnoughFunds(price)){
            StationType station = stationRepo.getStation(stationIndex);
            station.getAcquiredBuildings().addBuilding(building);
            budget.subtractFunds(price);
        } else {
            throw new InsuficientBudget("Insuficient budget. " + price + " required!");
        }
    }

    /**
     * Checks whether a small version of a specific building already exists in the station.
     *
     * @param building     name of the building.
     * @param stationIndex index of the station.
     * @return true if a small version exists, false otherwise.
     */
    public boolean existSmallVersion(String building, int stationIndex) {
        return stationRepo.getStation(stationIndex).getAcquiredBuildings().existsSmallVersion(building);
    }

    /**
     * Checks whether the station already has a telegraph building.
     *
     * @param stationIndex index of the station.
     * @return true if a telegraph exists, false otherwise.
     */
    public boolean existTelegraph(int stationIndex) {
        return stationRepo.getStation(stationIndex).getAcquiredBuildings().existsTelegraph();
    }



    /**
     * Initializes all repository dependencies and retrieves the current year and budget.
     */
    private void initializeRepositories() {
        instance = Simulation.getInstance();
        this.stationRepo = instance.getStationRepository();
        this.currentYear = instance.getTimeCounter().getCurrentYear();
        this.budget = instance.getCurrentScenario().getBudget();
    }

}
