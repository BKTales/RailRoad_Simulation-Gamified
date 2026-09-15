package pt.ipp.isep.dei.us010;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.route.CargoMode;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.domain.train.Cargo;
import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ipp.isep.dei.utils.DisplayDynamicList;
import pt.ipp.isep.dei.utils.DisplayListControllerInterface;
import pt.ipp.isep.dei.utils.ReadStringInput;
import pt.ipp.isep.dei.utils.Utils;

import java.util.*;

/**
 * UI class responsible for interacting with the user to create and assign a route to a selected train.
 *
 * The process includes:
 * - Selecting a train from the available ones.
 * - Selecting stations to form a route.
 * - Selecting products (as cargo) at each station.
 * - Choosing the cargo mode for each station.
 * - Validating the route against the train's capacity and constraints.
 * - Finalizing and saving the route if valid.
 *
 * This UI relies on callbacks and dynamic user prompts to navigate through multiple choices
 * such as train selection, station selection, product loading, and cargo mode selection.
 */
public class TrainToRouteUIFX {

    private final TrainToRouteController controller = new TrainToRouteController();
    private Train selectedTrain;
    private final List<PointOfRoute> pointsOfRoute = new ArrayList<>();

    /**
     * Starts the UI workflow by prompting the user to select a train.
     */
    public void run() {
        selectTrain();
    }

    /**
     * Allows the user to select a train from the list of available trains.
     * If no trains are available, a message is shown and the process is halted.
     */
    private void selectTrain() {
        List<Train> trains = controller.getAllTrains();

        if (trains.isEmpty()) {

            try {
                Utils.displayReturnPlayer("No trains purchased.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        List<String> trainDescriptions = trains.stream()
                .map(Train::toString)
                .collect(Collectors.toList());

        App.setMessage("Choose a train:");
        DisplayDynamicList.displayList(trainDescriptions, new DisplayListControllerInterface() {
            @Override
            public void initialize(List<String> items) {}

            @Override
            public void onItemSelected(int trainIndex) {
                selectedTrain = trains.get(trainIndex);
                selectStations();
            }
        }, "Choose a train:");
    }

    /**
     * Prompts the user to select stations to build a route.
     * Requires at least two stations to proceed.
     */
    private void selectStations() {
        List<StationType> stations = controller.getAllStations();

        if (stations.size() < 2) {
            try {
                Utils.displayReturnPlayer("Not enough stations to create a route.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        List<String> stationNames = stations.stream()
                .map(StationType::getName)
                .collect(Collectors.toList());

        pointsOfRoute.clear();
        List<Integer> selectedIndices = new ArrayList<>();

        selectStationRecursive(stations, stationNames, selectedIndices, "Select stations for the route:");
    }

    /**
     * Recursively allows the user to select multiple stations to include in the route.
     * Each selected station can be associated with a list of cargo and a cargo mode.
     *
     * @param stations        List of available stations
     * @param stationNames    Names of the available stations
     * @param selectedIndices Indices of already selected stations to avoid duplication
     * @param message         Message displayed to the user during selection
     */
    private void selectStationRecursive(List<StationType> stations, List<String> stationNames,
                                        List<Integer> selectedIndices, String message) {
        List<String> options = new ArrayList<>(stationNames);
        options.add("Finish selection");

        DisplayDynamicList.displayList(options, new DisplayListControllerInterface() {
            @Override
            public void initialize(List<String> items) {}

            @Override
            public void onItemSelected(int index) {
                if (index == options.size() - 1) {
                    if (pointsOfRoute.size() >= 2) {
                        finalizeRoute();
                    } else {
                        selectStationRecursive(stations, stationNames, selectedIndices, "You must select at least two stations.");
                    }
                    return;
                }

                if (selectedIndices.contains(index)) {
                    selectStationRecursive(stations, stationNames, selectedIndices, "Station already selected.");
                    return;
                }

                selectedIndices.add(index);
                StationType selectedStation = stations.get(index);

                selectProductsForStation(selectedStation, cargos -> {
                    selectCargoMode(mode -> {
                        pointsOfRoute.add(new PointOfRoute(selectedStation, cargos, mode));
                        selectStationRecursive(stations, stationNames, selectedIndices, "Select stations for the route:");
                    });
                });
            }
        }, message);
    }


    /**
     * Allows the user to select available products from a station to load as cargo.
     *
     * @param station  The station from which products are selected
     * @param callback Callback that returns the selected list of cargos
     */
    private void selectProductsForStation(StationType station, ProductSelectionCallback callback) {
        List<Product> availableProducts = station.getAvailableProducts();

        if (availableProducts.isEmpty()) {
            App.setMessage("No available products in this station.");
            DisplayMessage.displayMessage(userInput -> {});
            callback.onProductsSelected(new ArrayList<>());
            return;
        }

        List<String> productNames = availableProducts.stream()
                .map(Product::getProductName)
                .collect(Collectors.toList());

        List<Cargo> selectedCargos = new ArrayList<>();
        selectProductRecursive(station, availableProducts, productNames, selectedCargos, callback);
    }

    /**
     * Recursively allows the user to select multiple products from the station.
     * User must enter the quantity for each selected product.
     *
     * @param station        The station in context
     * @param products       List of available products at the station
     * @param productNames   Names of the products to display
     * @param selectedCargos List of selected cargos (product + quantity)
     * @param callback       Callback to return selected cargos
     */
    private void selectProductRecursive(StationType station, List<Product> products, List<String> productNames,
                                        List<Cargo> selectedCargos, ProductSelectionCallback callback) {
        List<String> options = new ArrayList<>(productNames);
        options.add("Finish product selection");

        DisplayDynamicList.displayList(options, new DisplayListControllerInterface() {
            @Override
            public void initialize(List<String> items) {}

            @Override
            public void onItemSelected(int index) {
                if (index == options.size() - 1) {
                    callback.onProductsSelected(selectedCargos);
                    return;
                }

                Product selectedProduct = products.get(index);
                boolean alreadySelected = selectedCargos.stream()
                        .anyMatch(c -> c.getProduct().equals(selectedProduct));

                if (alreadySelected) {
                    App.setMessage("Product already selected.");
                    DisplayMessage.displayMessage(userInput -> {});
                    selectProductRecursive(station, products, productNames, selectedCargos, callback);
                    return;
                }

                App.setMessage("Enter quantity for product: " + selectedProduct.getProductName());
                ReadStringInput.requestUserInputString(quantityStr -> {
                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        if (quantity <= 0) {

                            try {
                                Utils.displayReturnPlayer("Quantity must be positive.");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        selectedCargos.add(new Cargo(selectedProduct, quantity));
                        selectProductRecursive(station, products, productNames, selectedCargos, callback);
                    } catch (NumberFormatException e) {
                        App.setMessage("Invalid quantity input.");
                        DisplayMessage.displayMessage(userInput -> {});
                        selectProductRecursive(station, products, productNames, selectedCargos, callback);
                    }
                });
            }
        }, "Select product for station " + station.getName() + ":");
    }

    /**
     * Allows the user to choose a cargo mode from the available options.
     *
     * @param callback Callback to return the selected cargo mode
     */
    private void selectCargoMode(CargoModeSelectionCallback callback) {
        CargoMode[] modes = CargoMode.values();
        List<String> modeNames = Arrays.stream(modes)
                .map(Enum::toString)
                .collect(Collectors.toList());

        DisplayDynamicList.displayList(modeNames, new DisplayListControllerInterface() {
            @Override
            public void initialize(List<String> items) {}

            @Override
            public void onItemSelected(int index) {
                if (index >= 0 && index < modes.length) {
                    callback.onCargoModeSelected(modes[index]);
                } else {
                    App.setMessage("Invalid cargo mode index.");
                    DisplayMessage.displayMessage(userInput -> {});
                    selectCargoMode(callback);
                }
            }
        }, "Select Cargo Mode:");
    }

    /**
     * Final step in the process: validates the selected route and train,
     * then attempts to create and save the route.
     * Displays appropriate success or failure messages.
     */
    private void finalizeRoute() {
        if (selectedTrain == null) {
            try {
                Utils.displayReturnPlayer("No train selected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (pointsOfRoute.size() < 2) {
            try {
                Utils.displayReturnPlayer("You must select at least two stations.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        List<String> validationMessages = controller.validateTrainRouteCompatibility(selectedTrain, pointsOfRoute);

        if (!validationMessages.isEmpty()) {
            StringBuilder errorBuilder = new StringBuilder("Cannot create route due to the following issues:\n");
            for (String msg : validationMessages) {
                errorBuilder.append("- ").append(msg).append("\n");
            }
            try {
                Utils.displayReturnPlayer(errorBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        boolean success = controller.createRoute(selectedTrain, pointsOfRoute);

        try {
            if (success) {
                Utils.displayReturnPlayer("Route created and saved successfully.");
            } else {
                Utils.displayReturnPlayer("Failed to create route. Check if all stations are connected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Callback interface used to return selected cargos for a station.
     */

    // Internal callbacks
    private interface ProductSelectionCallback {
        void onProductsSelected(List<Cargo> cargos);
    }

    /**
     * Callback interface used to return selected cargo mode for a station.
     */

    private interface CargoModeSelectionCallback {
        void onCargoModeSelected(CargoMode mode);
    }
}
