package pt.ipp.isep.dei.us010;

import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.rails.RailType;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.route.CargoMode;
import pt.ipp.isep.dei.domain.route.Trip;
import pt.ipp.isep.dei.domain.route.TripStatus;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.domain.train.Cargo;
import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.repository.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class responsible for managing the process of assigning a train to a route.
 * It handles validation of cargo, compatibility with railway lines, and creation of trips and routes.
 *
 * This class interacts with several repositories to fetch and persist data related to trains,
 * stations, railway lines, and trips, within the context of a simulation scenario.
 */
public class TrainToRouteController implements Serializable {
    private static final long serialVersionUID = 1L;
    private Simulation instance;
    private TrainRepository trainRepository;
    private StationRepository stationRepository;
    private RouteRepository routeRepository;
    private RailwaylineRepository railwaylineRepository;
    private TripRepository tripRepository;
    private Budget budget;

    public TrainToRouteController() {
        initializeRepositories();
    }
    private void initializeRepositories() {
        instance = Simulation.getInstance();
        this.budget = instance.getCurrentScenario().getBudget();
        this.trainRepository = instance.getCurrentScenario().getTrainRepository();
        this.routeRepository = instance.getCurrentScenario().getRouteRepository();
        this.railwaylineRepository = instance.getCurrentScenario().getRailwaylineRepository();
        this.stationRepository = instance.getCurrentScenario().getStationRepository();
        this.tripRepository = instance.getCurrentScenario().getTripRepository();
    }


    /**
     * Gets all available stations from the repository.
     *
     * @return a list of station types
     */
    public List<StationType> getAllStations() {
        return stationRepository.getStations();
    }

    /**
     * Gets all purchased trains from the repository.
     *
     * @return a list of trains
     */
    public List<Train> getAllTrains() {
        return trainRepository.getTrains();
    }

    /**
     * Validates if the cargo load is within max capacity.
     *
     * @param cargoList   list of cargos
     * @param maxCapacity max allowed capacity
     * @return true if total cargo quantity is within capacity, false otherwise
     */
    private boolean isValidCarriageLoad(List<Cargo> cargoList, int maxCapacity) {
        int total = cargoList.stream().mapToInt(Cargo::getQuantity).sum();
        return total <= maxCapacity;
    }

    /**
     * Calculates the total available quantity of the requested cargos in the given station.
     *
     * @param station   station to check availability
     * @param cargoList list of cargos requested
     * @return total available quantity for the requested cargos
     */
    private int calculateAvailableCargo(StationType station, List<Cargo> cargoList) {
        int totalAvailable = 0;

        for (Cargo cargo : cargoList) {
            Product product = cargo.getProduct();
            int requestedQuantity = cargo.getQuantity();

            int availableQuantity = station.getAvailableQuantityForProductFromInfluenceArea(product);

            totalAvailable += Math.min(requestedQuantity, availableQuantity);
        }

        return totalAvailable;
    }

    /**
     * Validates if the cargo load satisfies the requirements of the cargo mode and station availability.
     *
     * @param station    station where cargo is loaded
     * @param cargoList  list of cargos requested
     * @param mode       cargo mode (FULL, HALF, AVAILABLE)
     * @param maxCapacity max allowed cargo capacity
     */
    private void validateCargoLoad(StationType station, List<Cargo> cargoList, CargoMode mode, int maxCapacity) {
        int totalQuantity = cargoList.stream().mapToInt(Cargo::getQuantity).sum();

        if (!isValidCarriageLoad(cargoList, maxCapacity)) {
            throw new IllegalArgumentException("Total cargo quantity exceeds max capacity of " + maxCapacity);
        }

        int availableQuantity = calculateAvailableCargo(station, cargoList);

        switch (mode) {
            case FULL:
                if (availableQuantity < totalQuantity) {
                    System.out.println("Warning: Not enough cargo at '" + station.getName() + "' for FULL mode. Train will wait until enough cargo is available.");
                }
                break;
            case HALF:
                int minHalf = (int) Math.ceil(totalQuantity / 2.0);
                if (availableQuantity < minHalf) {
                    throw new IllegalArgumentException("Not enough cargo available at station '" + station.getName() + "' for HALF mode (needs at least " + minHalf + ").");
                }
                break;
            case AVAILABLE:
                if (availableQuantity == 0) {
                    throw new IllegalArgumentException("No cargo available at station '" + station.getName() + "' for AVAILABLE mode.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid cargo mode");
        }
    }

    /**
     * Creates a circular route for a train, made up of trips between selected stations.
     * Also validates compatibility with railway lines and train capacity.
     *
     * @param train         train to assign the route to.
     * @param pointsOfRoute selected stations and their respective cargos/modes.
     * @return true if the route was successfully created and saved; false otherwise.
     */

    public boolean createRoute(Train train, List<PointOfRoute> pointsOfRoute) {
        try {
            List<Trip> trips = new ArrayList<>();
            final int MAX_CAPACITY = 10;
            double totalRouteDistance = 0.0;

            for (int i = 0; i < pointsOfRoute.size(); i++) {
                PointOfRoute originPoint = pointsOfRoute.get(i);
                PointOfRoute destinationPoint = pointsOfRoute.get((i + 1) % pointsOfRoute.size());

                StationType origin = originPoint.getStation();
                StationType destination = destinationPoint.getStation();

                RailwayLine line = railwaylineRepository.getRailwayLineBetween(origin,destination);
                if (!isTrainCompatibleWithLine(train, line)) {
                    throw new IllegalArgumentException("Train type '" + train.getLocomotive().getLocomotiveType() +
                            "' is not compatible with rail type '" + line.getRailType() + "' between " +
                            origin.getName() + " and " + destination.getName());
                }

                if (line == null) {
                    throw new IllegalArgumentException("No railway line between " + origin.getName() + " and " + destination.getName());
                }

                List<Cargo> cargoList = originPoint.getCargoList();
                CargoMode cargoMode = originPoint.getCargoMode();

                // Verifica se carga total ultrapassa capacidade máxima
                int totalQuantity = cargoList.stream().mapToInt(Cargo::getQuantity).sum();
                if (totalQuantity > MAX_CAPACITY) {
                    throw new IllegalArgumentException("Total cargo exceeds max capacity of " + MAX_CAPACITY);
                }

                TripStatus status = (i == 0) ? TripStatus.WAITING_FOR_CARGO : TripStatus.PENDING;

                // Cria a Trip com distância já conhecida (via RailwayLine)

                Trip trip = tripRepository.createAndSaveTrip(train, origin, destination, line, cargoList, cargoMode, status);
                //Trip trip = getTripRepository().createAndSaveTrip(train, origin, destination, line, cargoList, cargoMode, status);

                trips.add(trip);

                double travelTime = trip.getTravelTime();
                System.out.println("Tempo estimado da trip: " + travelTime + " hours");

                // Acumula a distância da trip na distância total da rota
                totalRouteDistance += line.getDistance();

                // DEBUG
                System.out.println("DEBUG: Criando trip " + origin.getName() + " -> " + destination.getName() +
                        " | Status: " + status +
                        " | Total requerido: " + totalQuantity +
                        " | Disponível: " + calculateAvailableCargo(origin, cargoList) +
                        " | Distância da trip: " + line.getDistance()+ "Km");
            }

            // DEBUG: listar todos os trips criados
            System.out.println("DEBUG: Trips criados:");
            for (Trip t : trips) {
                System.out.println("  Trip: " + t.getOrigin().getName() + " -> " + t.getDestination().getName() +
                        " | Status: " + t.getStatus() +
                        " | Carga: " + t.getCargoList().stream()
                        .map(c -> c.getProduct().getProductName() + " x" + c.getQuantity())
                        .collect(Collectors.joining(", ")) +
                        " | Distância: " + t.getDistance());
            }

            // Mostra a distância total da rota
            System.out.println("DEBUG: Distância total da rota: " + totalRouteDistance);

            // Define a localização do trem para a estação de origem da primeira trip
            if (!trips.isEmpty()) {
                StationType startingStation = trips.get(0).getOrigin();
                train.setCurrentLocation(startingStation);
            }

            train.setActive(true);

            // Salva a rota no repositório
            routeRepository.createAndSaveRoute(pointsOfRoute, train, Simulation.getInstance());
            //getRouteRepository().createAndSaveRoute(pointsOfRoute, train, Simulation.getInstance());


            return true;

        } catch (Exception e) {
            System.out.println("Error while creating route: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the train is compatible with the given railway line based on locomotive type.
     *
     * @param train the train to verify.
     * @param line  the railway line to check.
     * @return true if compatible, false otherwise.
     */

    private boolean isTrainCompatibleWithLine(Train train, RailwayLine line) {
        switch (train.getLocomotive().getLocomotiveType()) {
            case ELECTRICITY_LOCOMOTIVE:
                return line.getRailType() == RailType.ELECTRIFIED;
            case DIESEL_LOCOMOTIVE:
            case STEAM_LOCOMOTIVE:
                return line.getRailType() == RailType.NON_ELECTRIFIED;
            default:
                return false;
        }
    }

    /**
     * Validates whether a train can be assigned a route composed of the given points.
     * Checks railway line connectivity, compatibility, and cargo load constraints.
     *
     * @param train          the train to be assigned the route.
     * @param pointsOfRoute  list of route points to validate.
     * @return list of validation error messages. Empty if all checks pass.
     */
    public List<String> validateTrainRouteCompatibility(Train train, List<PointOfRoute> pointsOfRoute) {
        List<String> messages = new ArrayList<>();

        final int MAX_CAPACITY = 10;

        for (int i = 0; i < pointsOfRoute.size(); i++) {
            PointOfRoute originPoint = pointsOfRoute.get(i);
            PointOfRoute destinationPoint = pointsOfRoute.get((i + 1) % pointsOfRoute.size());

            StationType origin = originPoint.getStation();
            StationType destination = destinationPoint.getStation();

            RailwayLine line = railwaylineRepository.getRailwayLineBetween(origin, destination);

            //RailwayLine line = getRailwalineRepository().getRailwayLineBetween(origin, destination);

            if (line == null) {
                messages.add("There is no railway line between " + origin.getName() + " and "  + destination.getName());
                continue;
            }

            if (!isTrainCompatibleWithLine(train, line)) {
                messages.add("The type train " + train.getLocomotive().getLocomotiveType() + "\n" +
                        "does not support line type  " + line.getRailType() +"\n" + "between " +
                        origin.getName() + " and " + destination.getName());
            }

            List<Cargo> cargoList = originPoint.getCargoList();
            int totalQuantity = cargoList.stream().mapToInt(Cargo::getQuantity).sum();

            if (totalQuantity > MAX_CAPACITY) {
                messages.add("The total load (" + totalQuantity + ") exceeds maximum capacity (" + MAX_CAPACITY +
                        ") at the station " + origin.getName());
            }

        }

        return messages;
    }
}
