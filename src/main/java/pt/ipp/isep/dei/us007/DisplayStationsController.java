package pt.ipp.isep.dei.us007;

import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.repository.StationRepository;

import java.io.Serializable;

public class DisplayStationsController implements Serializable {
    private static final long serialVersionUID = 1L;
    private StationRepository stationRepo;

    public DisplayStationsController() {
        initialize();
    }
    /**
     * Retrieves a string listing all stations.
     * @return A formatted string listing all stations.
     */
    public String getListOfStations() {
        return stationRepo.listAllStations();
    }

    /**
     * Retrieves detailed information about a specific station.
     * @param index The index of the station.
     * @return A string with the station's detailed information.
     */
    public String getStationInfo(int index) {
        return stationRepo.getStationInfo(index);
    }

    // -------------------- private methods --------------------

    /**
     * Initializes the StationRepository instance.
     */
    private void initialize() {
        if (stationRepo == null) {
            Simulation repo = Simulation.getInstance();
            stationRepo = repo.getStationRepository();
        }
    }
}
