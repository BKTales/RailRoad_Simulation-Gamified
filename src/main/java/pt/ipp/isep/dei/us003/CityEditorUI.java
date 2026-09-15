package pt.ipp.isep.dei.us003;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.position.PositionRandomizer;
import pt.ipp.isep.dei.utils.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityEditorUI implements Serializable {
    private static final long serialVersionUID = 1L;
    private CityEditorController controller = new CityEditorController();
    private String cityName;
    private int houseBlocksQuantity;
    private int cityX, cityY;
    private int houseBlockX, houseBlockY;
    private int maxQuantityPassenger;
    private int maxQuantityMail;
    private String productName;
    private List<Position> houseBlocksPositionsTemp = new ArrayList<>();
    private int positioningMethod;
    private int currentHouseBlockIndex = 0;
    private City city;


    public void run() {
        readCityName();
    }

    private void readCityName() {
        App.setMessage("Enter city name (alphabetic characters only, max 255):");
        ReadStringInput.requestUserInputString(input -> {
            if (isValidCityName(input)) {
                cityName = input;
                readCityX();
            } else {
                Utils.displayErrorMessage("Invalid city name! Use only letters, max 255 characters.");
                readCityName();
            }
        });
    }

    private void readCityX() {
        App.setMessage("Enter X coordinate for city:");
        ReadStringInput.requestUserInputString(input -> {
            try {
                cityX = Integer.parseInt(input);
                readCityY();
            } catch (NumberFormatException e) {
                Utils.displayErrorMessage("Invalid integer! Please enter a valid number.");
                readCityX();
            }
        });
    }

    private void readCityY() {
        App.setMessage("Enter Y coordinate for city:");
        ReadStringInput.requestUserInputString(input -> {
            try {
                cityY = Integer.parseInt(input);
                readMaxPassengers();
            } catch (NumberFormatException e) {
                Utils.displayErrorMessage("Invalid integer! Please enter a valid number.");
                readCityY();
            }
        });
    }

    private void readHouseBlocksQuantity() {
        App.setMessage("Enter number of house blocks:");
        ReadStringInput.requestUserInputString(input -> {
            try {
                houseBlocksQuantity = Integer.parseInt(input);
                if (houseBlocksQuantity > 0) {
                    readPositioningMethod();
                } else {
                    Utils.displayErrorMessage("Must be a positive integer!");
                    readHouseBlocksQuantity();
                }
            } catch (NumberFormatException e) {
                Utils.displayErrorMessage("Invalid integer! Please enter a valid number.");
                readHouseBlocksQuantity();
            }
        });
    }

    private void readPositioningMethod() {
        App.setMessage("Position house blocks:\n1 - Manually\n2 - Automatically");
        ReadStringInput.requestUserInputString(input -> {
            try {
                positioningMethod = Integer.parseInt(input);
                if (positioningMethod == 1 || positioningMethod == 2) {
                    if (positioningMethod == 1) {
                        createManualBlocks();
                    } else {
                        generateHouseBlocksAutomatically();
                    }
                } else {
                    Utils.displayErrorMessage("Invalid option! Choose 1 or 2.");
                    readPositioningMethod();
                }
            } catch (NumberFormatException e) {
                Utils.displayErrorMessage("Invalid integer! Please enter 1 or 2.");
                readPositioningMethod();
            }
        });
    }

    public void createManualBlocks()
    {
        controller.createHouseBlockList(city);
        readHouseBlockX();
    }

    private void readHouseBlockX() {
        App.setMessage(String.format("Enter X for house block %d/%d:",
                currentHouseBlockIndex + 1, houseBlocksQuantity));
        ReadStringInput.requestUserInputString(input -> {
            try {
                houseBlockX = Integer.parseInt(input);
                readHouseBlockY();
            } catch (IllegalArgumentException e) {
                Utils.displayErrorMessage("Invalid coordinate! " + e.getMessage());
                readHouseBlockX(); // Retry X
            }
        });
    }

    private void readHouseBlockY() {
        App.setMessage(String.format("Enter Y for house block %d/%d:",
                currentHouseBlockIndex + 1, houseBlocksQuantity));
        ReadStringInput.requestUserInputString(input -> {
            try {
                houseBlockY = Integer.parseInt(input);
                controller.createCityBlock(city, houseBlockX, houseBlockY);

                currentHouseBlockIndex++;
                if (currentHouseBlockIndex < houseBlocksQuantity) {
                    readHouseBlockX();
                } else {
                    finalizeCreateCity();
                }
            } catch (IllegalArgumentException e) {
                Utils.displayErrorMessage("Invalid coordinate! " + e.getMessage());
                readHouseBlockY(); // Retry Y
            }
        });
    }





    private void generateHouseBlocksAutomatically() {
        Position cityPosition = new Position(cityX, cityY);
        controller.createHouseBlockList(city);
        int i;


            for (i = 0; i < houseBlocksQuantity; i++) {
                try{
                    controller.createCityBlock(city, PositionRandomizer.getRandomPositionAround(cityPosition).getX(), PositionRandomizer.getRandomPositionAround(cityPosition).getY());
                }catch (IllegalArgumentException e) {
                    i--;
                }
            }

        finalizeCreateCity();

    }

    private void finalizeCreateCity(){
        try{
            Utils.displayReturnMapEditor("City created successfully!");

        }catch (IllegalArgumentException | IOException e) {
            try {
                if (positioningMethod == 1) {
                    controller.deleteFailedHouseBlocks(houseBlocksPositionsTemp);
                }
                Utils.displayReturnMapEditor("City creation failed: " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void readMaxPassengers() {
        App.setMessage("Enter max passengers quantity:");
        ReadStringInput.requestUserInputString(input -> {
            try {
                maxQuantityPassenger = Integer.parseInt(input);
                if (maxQuantityPassenger > 0) {
                    readMaxMail();
                } else {
                    Utils.displayErrorMessage("Must be positive!");
                    readMaxPassengers();
                }
            } catch (NumberFormatException e) {
                Utils.displayErrorMessage("Invalid integer!");
                readMaxPassengers();
            }
        });
    }

    private void readMaxMail() {
        App.setMessage("Enter max mail quantity:");
        ReadStringInput.requestUserInputString(input -> {
            try {
                maxQuantityMail = Integer.parseInt(input);
                if (maxQuantityMail > 0) {
                    readProduct();
                } else {
                    Utils.displayErrorMessage("Must be positive!");
                    readMaxMail();
                }
            } catch (NumberFormatException e) {
                Utils.displayErrorMessage("Invalid integer!");
                readMaxMail();
            }
        });
    }

    private void readProduct() {

        App.setMessage("Select product to consume:");
        App.setList(controller.getAvailableProducts());

        ReadListInput.requestUserInputList(selected -> {
            productName = selected;
            createCity();
        });
    }

    private void createCity() {
        try{
            city = controller.createCity(
                    cityName,
                    new Position(cityX, cityY),
                    maxQuantityPassenger,
                    maxQuantityMail,
                    controller.getProductByName(productName)
            );

            readHouseBlocksQuantity();
        }catch(IllegalArgumentException e) {
            Utils.displayErrorMessage("City creation failed: " + e.getMessage());
            try {
                Utils.displayReturnMapEditor("Something went wrong");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isValidCityName(String name) {
        return name != null &&
                !name.isEmpty() &&
                name.length() <= 255 &&
                name.matches("[a-zA-Z]+");
    }
}