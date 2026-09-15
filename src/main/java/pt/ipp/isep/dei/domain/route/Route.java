package pt.ipp.isep.dei.domain.route;

import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.us010.PointOfRoute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a route, which is composed of a list of trips.
 * <p>
 //* Each {@link Trip} in the route corresponds to a segment of travel
 * between two stations using a specific train and railway line.
 * </p>
 *
 * <p>This class is immutable after construction.</p>
 */

public class Route implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Trip> trips;
    private long timeStart;
    private List<PointOfRoute> pointsOfRoute;
    private Simulation instance;

    /**
     * Constructs a new Route based on the given points of route, the train to be used, and the simulation instance.
     * Initializes trips between consecutive points of the route.
     *
     * @param pointsOfRoute the ordered list of points representing stations and cargo details along the route
     * @param train the train that will be used for all trips in this route
     * @param instance the simulation instance used to access scenario data such as railway lines
     */

    public Route(List<PointOfRoute> pointsOfRoute, Train train,Simulation instance) {
        this.instance = instance;
        this.pointsOfRoute = pointsOfRoute;
        this.trips = new ArrayList<>();
        initializeTrips(train);
    }

    /**
     * Returns the list of trips composing this route.
     *
     * @return an unmodifiable list of trips
     */

    public List<Trip> getTrips() {
        return trips;
    }

    /**
     * Adds a pause duration to the route's start time.
     * This can be used to adjust the start time due to delays or pauses.
     *
     * @param timeNow the duration to add to the start time, in milliseconds
     */

    public void updateStartTimePause(long timeNow){
        timeStart += timeNow;
    }

    /**
     * Returns a string representation of the route including all trips.
     *
     * @return a human-readable string describing the route and its trips
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Route:\n");
        for (Trip trip : trips) {
            sb.append("  - ").append(trip).append("\n");
        }
        return sb.toString();
    }
    /**
     * Sets the start time of production (route execution).
     *
     * @param timeNow the timestamp marking the start of production in milliseconds
     */

    public void startProduction(Long timeNow) {
        timeStart = timeNow;
    }

    /**
     * Updates the status of trips in the route based on the current time.
     * It advances trips from PENDING to WAITING_FOR_CARGO when the previous trip is DELIVERED or COMPLETED.
     * Also restarts the route cycle when all trips are delivered.
     *
     * @param currentTime the current timestamp in milliseconds
     * @return a log string describing the status changes that occurred during the update
     */

    public String updateStatus(long currentTime) {
        StringBuilder log = new StringBuilder();

        // Liberta a próxima trip que está PENDING se a anterior foi entregue
        for (int i = 0; i < trips.size() - 1; i++) {
            Trip current = trips.get(i);
            Trip next = trips.get(i + 1);

            if ((current.getStatus() == TripStatus.DELIVERED || current.getStatus() == TripStatus.COMPLETED)
                    && next.getStatus() == TripStatus.PENDING) {
                next.setStatus(TripStatus.WAITING_FOR_CARGO);
                log.append("[ROUTE] Trip from ").append(next.getOrigin().getName())
                        .append(" to ").append(next.getDestination().getName())
                        .append(" is now WAITING_FOR_CARGO.\n");
            }
        }

        // Só reinicia o loop se TODAS já forem executadas e entregues
        boolean allDelivered = trips.stream().allMatch(trip ->
                trip.getStatus() == TripStatus.DELIVERED || trip.getStatus() == TripStatus.COMPLETED
        );

        boolean nonePending = trips.stream().noneMatch(trip ->
                trip.getStatus() == TripStatus.PENDING || trip.getStatus() == TripStatus.WAITING_FOR_CARGO
                        || trip.getStatus() == TripStatus.IN_TRANSIT
        );

        if (allDelivered && nonePending) {
            for (Trip trip : trips) {
                trip.setStatus(TripStatus.WAITING_FOR_CARGO);
            }
            timeStart = currentTime;
            log.append("[ROUTE LOOP] All trips delivered. Restarting circular route.\n");
        }

        return log.toString();
    }

    /**
     * Initializes the list of trips based on the points of route and the train provided.
     * Creates trips between consecutive stations and attempts to close the route cycle by adding a return trip from the last to the first station if possible.
     *
     * @param train the train to be assigned to all trips in this route
     * @throws IllegalArgumentException if no railway line exists between two consecutive stations
     */

    public void initializeTrips(Train train) {
        trips.clear();

        // Cria as trips normais entre pontos consecutivos
        for (int i = 0; i < pointsOfRoute.size() - 1; i++) {
            PointOfRoute origin = pointsOfRoute.get(i);
            PointOfRoute destination = pointsOfRoute.get(i + 1);

            RailwayLine rl = instance.getCurrentScenario()
                    .getRailwaylineRepository()
                    .getRailwayLineBetween(origin.getStation(), destination.getStation());

            if (rl == null) {
                throw new IllegalArgumentException("No railway line between " +
                        origin.getStation().getName() + " and " + destination.getStation().getName());
            }

            TripStatus status = (i == 0) ? TripStatus.WAITING_FOR_CARGO : TripStatus.PENDING;

            Trip trip = new Trip(train,
                    origin.getStation(),
                    destination.getStation(),
                    rl,
                    origin.getCargoList(),
                    origin.getCargoMode(),
                    status);

            trips.add(trip);
        }

        // Fecha o ciclo: última estação → primeira estação
        if (pointsOfRoute.size() > 1) {
            PointOfRoute last = pointsOfRoute.get(pointsOfRoute.size() - 1);
            PointOfRoute first = pointsOfRoute.get(0);

            RailwayLine returnLine = instance.getCurrentScenario()
                    .getRailwaylineRepository()
                    .getRailwayLineBetween(last.getStation(), first.getStation());

            if (returnLine != null) {
                Trip returnTrip = new Trip(train,
                        last.getStation(),
                        first.getStation(),
                        returnLine,
                        last.getCargoList(),
                        last.getCargoMode(),
                        TripStatus.PENDING); // Começa como PENDING para ser "libertada" no fim do ciclo

                trips.add(returnTrip);
            } else {
                System.out.println("WARNING: No railway line between " +
                        last.getStation().getName() + " and " + first.getStation().getName() +
                        ". Circular route not closed.");
            }
        }
    }


}
