package pt.ipp.isep.dei.us010;

import pt.ipp.isep.dei.domain.route.CargoMode;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.domain.train.Cargo;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a point in a train route, including the station,
 * the list of cargo to be loaded at that station, and the mode
 * of cargo loading.
 *
 * This class is used when constructing a complete route made up
 * of multiple points (stations), each with its own cargo behavior.
 */

public class PointOfRoute implements Serializable {
    private static final long serialVersionUID = 1L;
    private final StationType station;
    private final List<Cargo> cargoList;
    private final CargoMode cargoMode;

    /**
     * Constructs a new PointOfRoute.
     *
     * @param station   the station at this point in the route.
     * @param cargoList the list of cargo to load at this station.
     * @param cargoMode the mode of cargo loading (FULL, HALF, AVAILABLE).
     */
    public PointOfRoute(StationType station, List<Cargo> cargoList, CargoMode cargoMode) {
        this.station = station;
        this.cargoList = cargoList;
        this.cargoMode = cargoMode;
    }

    /**
     * Gets the station associated with this route point.
     *
     * @return the station.
     */
    public StationType getStation() { return station; }

    /**
     * Gets the list of cargo items to be loaded at this station.
     *
     * @return the list of cargo.
     */
    public List<Cargo> getCargoList() { return cargoList; }
    /**
     * Gets the cargo loading mode for this station.
     *
     * @return the cargo mode.
     */
    public CargoMode getCargoMode() { return cargoMode; }
}