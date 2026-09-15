package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.route.Trip;
import pt.ipp.isep.dei.domain.route.TripStatus;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.domain.train.Cargo;
import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.domain.route.CargoMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing {@link Trip} instances.
 * <p>
 * This repository allows creation and storage of trips involving trains,
 * stations, and railway lines. Internally, it maintains an in-memory list
 * of all created trips.
 * </p>
 */
public class TripRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Trip> trips = new ArrayList<>();

    /**
     * Creates a new {@link Trip} with the specified parameters and stores it in the repository.
     *
     * @param train the train that performs the trip
     * @param origin the starting station of the trip
     * @param destination the ending station of the trip
     * @param railwayLine the railway line used for this trip
     * @param cargoList list of cargo to transport
     * @param cargoMode the cargo mode (FULL, HALF, AVAILABLE)
     * @param status initial status of the trip (e.g., WAITING_FOR_CARGO, READY)
     * @return the created and stored {@link Trip} object
     */
    public Trip createAndSaveTrip(Train train, StationType origin, StationType destination,
                                  RailwayLine railwayLine, List<Cargo> cargoList,
                                  CargoMode cargoMode, TripStatus status) {
        Trip trip = new Trip(train, origin, destination, railwayLine, cargoList, cargoMode, status);
        trips.add(trip);
        return trip;
    }

    public List<Trip> getAllTrips() {
        return new ArrayList<>(trips);
    }

    public void clean() {
        trips.clear();
    }


}
