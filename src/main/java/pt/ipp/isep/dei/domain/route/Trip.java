
package pt.ipp.isep.dei.domain.route;

import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.domain.train.Cargo;
import pt.ipp.isep.dei.domain.train.Train;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single trip between two stations using a specific train and railway line.
 * Also includes cargo list and cargo loading mode from the origin station.
 */
public class Trip implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Train train;
    private final StationType origin;
    private final StationType destination;
    private final RailwayLine railwayLine;
    private final List<Cargo> cargoList;
    private final CargoMode cargoMode;
    private TripStatus status;
    private Simulation instance;
    private double time;
    private long departureTime = -1;
    private long departureGameTime = -1;
    private double SECONDS_PER_YEAR_IN_GAME = 60.0;
    private double SECONDS_PER_HOUR = 3600.0;

    /**
     * Constructs a new Trip instance with the specified train, origin and destination stations,
     * railway line, cargo list, cargo loading mode, and initial trip status.
     *
     * @param train       the train assigned to the trip
     * @param origin      the starting station of the trip
     * @param destination the ending station of the trip
     * @param railwayLine the railway line used for this trip
     * @param cargoList   the list of cargo to be transported
     * @param cargoMode   the mode of cargo loading (FULL, HALF, AVAILABLE)
     * @param status      the initial status of the trip
     */

    public Trip(Train train, StationType origin, StationType destination, RailwayLine railwayLine,
                List<Cargo> cargoList, CargoMode cargoMode, TripStatus status) {
        this.train = train;
        this.origin = origin;
        this.destination = destination;
        this.railwayLine = railwayLine;
        this.cargoList = cargoList;
        this.cargoMode = cargoMode;
        this.status = status;
        this.time = calculateTravelTime();
    }


    private Simulation getInstance() {
        if (instance == null) {
            instance = Simulation.getInstance();
        }
        return instance;
    }

    /**
     * Sets the status of the trip.
     *
     * @param status the new status to set
     */
    public void setStatus(TripStatus status) {
        this.status = status;
    }

    /**
     * Updates the status of the trip based on the current simulation time.
     * Handles loading cargo at the origin station according to the cargo mode,
     * transitions the status to READY when cargo is loaded,
     * and delivers cargo to the destination, updating the status to DELIVERED.
     * Returns a log of all actions performed during the update.
     * <p>
     * //* @param timeNow the current simulation time
     *
     * @return a log string describing status updates and cargo loading/unloading
     */
    public String updateStatus(long timeNow, double moneyEarned) {
        StringBuilder log = new StringBuilder();
        List<Cargo> loadedCargoList = new ArrayList<>();

        if (this.status == TripStatus.PENDING) {
            log.append("[TRIP SKIPPED] Trip from ").append(origin.getName())
                    .append(" to ").append(destination.getName())
                    .append(" is still PENDING. Waiting for previous trip to complete.\n");
            log.insert(0, moneyEarned + "\t");
            return log.toString();
        }

        if (this.status == TripStatus.WAITING_FOR_CARGO) {
            train.setCurrentLocation(origin);

            int totalRequired = cargoList.stream().mapToInt(Cargo::getQuantity).sum();
            boolean readyToGo = false;
            int availableSum = 0;

            switch (cargoMode) {
                case FULL:
                    readyToGo = cargoList.stream().allMatch(cargo ->
                            origin.getAvailableQuantityForProductFromInfluenceArea(cargo.getProduct()) >= cargo.getQuantity());
                    break;

                case HALF:
                    for (Cargo cargo : cargoList) {
                        int stationQty = origin.getAvailableQuantityForProductFromInfluenceArea(cargo.getProduct());
                        availableSum += Math.min(cargo.getQuantity(), stationQty);
                    }
                    int requiredHalf = (int) Math.ceil(totalRequired / 2.0);
                    readyToGo = availableSum >= requiredHalf;
                    break;

                case AVAILABLE:
                    readyToGo = cargoList.stream().anyMatch(cargo ->
                            origin.getAvailableQuantityForProductFromInfluenceArea(cargo.getProduct()) > 0);
                    break;
            }

            if (readyToGo) {
                log.append("[CARGO LOADED] Train at station ").append(origin.getName()).append(" loaded with:\n");
                boolean somethingLoaded = false;

                for (Cargo cargo : cargoList) {
                    int availableQty = origin.getAvailableQuantityForProductFromInfluenceArea(cargo.getProduct());
                    int quantityToLoad = Math.min(cargo.getQuantity(), availableQty);

                    if (quantityToLoad > 0) {
                        loadedCargoList.add(new Cargo(cargo.getProduct(), quantityToLoad));
                        log.append("  • ").append(cargo.getProduct().getProductName())
                                .append(" x ").append(quantityToLoad).append("\n");
                        somethingLoaded = true;
                    }
                }

                if (somethingLoaded) {
                    cargoList.clear();
                    cargoList.addAll(loadedCargoList);
                    this.status = TripStatus.IN_TRANSIT;
                    this.departureTime = timeNow;
                    this.departureGameTime = getInstance().getTimeCounter().getGameTime();
                    train.setCurrentLocation(null);
                    log.append("[TRIP DEPARTED] Trip from ").append(origin.getName())
                            .append(" to ").append(destination.getName()).append(" has departed.\n");
                } else {
                    log.append("[WAITING] No cargo could be loaded.\n");
                }

            } else {
                log.append("[WAITING] Not enough cargo to depart. Trip still waiting at station ")
                        .append(origin.getName()).append("\n");
            }

            log.insert(0, moneyEarned + "\t");
            return log.toString();
        }

        if (this.status == TripStatus.IN_TRANSIT) {
            double inTravelTime = calculateInTravelTime();
            long gameNow = getInstance().getTimeCounter().getGameTime();
            if (departureGameTime > 0 && (gameNow - departureGameTime) >= inTravelTime) {
                for (Cargo cargo : cargoList) {
                    Product product = cargo.getProduct();
                    int quantity = cargo.getQuantity();
                    destination.addProductsToStorage(product, quantity);
                    log.append("[CARGO UNLOADED] Delivered ").append(quantity)
                            .append(" of ").append(product.getProductName())
                            .append(" to station ").append(destination.getName()).append("\n");
                }

                destination.addProductsToIndustryStorage();

                double revenue = 0.0;
                for (Cargo cargo : cargoList) {
                    Product product = cargo.getProduct();
                    revenue += product.getProductValue() * destination.demandBonus(product) * cargo.getQuantity();
                }

                moneyEarned += revenue;
                getInstance().getCurrentScenario().getBudget().addFunds(revenue);
                log.append("[BUDGET UPDATED] Income of ").append(revenue).append(" added to budget.\n");

                this.status = TripStatus.DELIVERED;
                train.setCurrentLocation(destination);

                log.append("[TRIP COMPLETED] Trip from ").append(origin.getName())
                        .append(" to ").append(destination.getName()).append(" is now DELIVERED.\n");
            } else {
                log.append("[TRIP IN TRANSIT] Trip from ").append(origin.getName())
                        .append(" to ").append(destination.getName()).append(" is still in transit.\n");
            }
        }

        log.insert(0, moneyEarned + "\t");
        return log.toString();
    }



    public double calculateTravelTime() {
        double speedReduction = 0.05;
        double distance = this.getDistance(); // em KM
        double speedMph = this.getTrain().getLocomotive().getTopSpeed(); // em mph
        double speedKmph = speedMph * 1.60934; // converte para km/h
        double effectiveSpeed = Math.max(0.1, speedKmph * (1 - cargoList.size() * speedReduction));
        return distance / effectiveSpeed; // tempo em horas
    }

    public double calculateInTravelTime() {
        double travelTimeInSeconds = time * SECONDS_PER_HOUR;
        return travelTimeInSeconds / SECONDS_PER_YEAR_IN_GAME;
    }


    // =============== Getters =============== //

    /**
     * Returns the distance of the railway line for this trip.
     *
     * @return the distance in the unit defined by RailwayLine
     */
    public double getDistance() {
        return railwayLine.getDistance();
    }

    public double getTravelTime() {
        return time;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public double getInTravelTime() {
        return calculateInTravelTime();
    }

    public long getDepartureGameTime() {
        return departureGameTime;
    }

    /**
     * Returns the train assigned to this trip.
     *
     * @return the train object
     */
    public Train getTrain() {
        return train;
    }

    /**
     * Returns the origin station of the trip.
     *
     * @return the origin station
     */
    public StationType getOrigin() {
        return origin;
    }

    /**
     * Returns the destination station of the trip.
     *
     * @return the destination station
     */
    public StationType getDestination() {
        return destination;
    }

    /**
     * Returns the railway line used for this trip.
     *
     * @return the railway line
     */
    public RailwayLine getRailwayLine() {
        return railwayLine;
    }

    /**
     * Returns the list of cargo for this trip.
     *
     * @return list of cargo objects
     */
    public List<Cargo> getCargoList() {
        return cargoList;
    }

    /**
     * Returns the cargo loading mode for this trip.
     *
     * @return the cargo mode (FULL, HALF, AVAILABLE)
     */
    public CargoMode getCargoMode() {
        return cargoMode;
    }

    /**
     * Returns the current status of the trip.
     *
     * @return the trip status
     */
    public TripStatus getStatus() {
        return status;
    }

    /**
     * Returns a string representation of the trip including origin, destination,
     * train used, cargo mode, number of cargo items, and current status.
     *
     * @return a descriptive string of the trip
     */
    @Override
    public String toString() {
        return "Trip from " + origin.getName() +
                " to " + destination.getName() +
                " using train " + train.getLocomotive().getName() +
                " [Mode: " + cargoMode +
                ", Cargo count: " + cargoList.size() +
                ", Status: " + status + "]";
    }
}


