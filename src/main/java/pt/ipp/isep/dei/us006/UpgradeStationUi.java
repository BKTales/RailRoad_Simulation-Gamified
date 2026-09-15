package pt.ipp.isep.dei.us006;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.InsuficientBudget;
import pt.ipp.isep.dei.domain.station.BuildingType;
import pt.ipp.isep.dei.domain.station.Upgrade;
import pt.ipp.isep.dei.utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pt.ipp.isep.dei.utils.ReadListInput.requestUserInputList;
import static pt.ipp.isep.dei.utils.ReadStringInput.requestUserInputString;

public class UpgradeStationUi implements Serializable {
    private static final long serialVersionUID = 1L;
    private int station;
    private String selectedUpgrade;
    private String selectedBuilding;
    private String upgradeType;
    private String cardinalPosition;
    private double price;

    private final UpgradeStationController controller = new UpgradeStationController();

    public void run() {
        String stationList = controller.getListOfStations();
        if (stationList.isEmpty()) {
            Utils.displayWarningInput("No stations available. Returning to main menu.");
            return;
        }

        App.setMessage("Select a station to upgrade:");
        App.setList(stationList);
        requestUserInputList(input -> {
            station = extractIndex(input);
            if (station < 0) {
                Utils.displayWarningInput("Invalid station selection.");
                return;
            }

            selectUpgradeType();
        });
    }

    private void selectUpgradeType() {
        String message = "Choose an upgrade option:\n" +
                "[0] - Upgrade with building\n" +
                "[1] - Upgrade station type";

        App.setMessage(message);

        requestUserInputString(input -> {
            switch (input.trim()) {
                case "0":
                    upgradeWithBuilding();
                    break;
                case "1":
                    upgradeStationType();
                    break;
                default:
                    Utils.displayWarningInput("Invalid option. Please choose 0 or 1.");
                    selectUpgradeType();
            }
        });
    }

    private void upgradeWithBuilding() {
        String buildingsList = controller.getAvailableBuildings(station);
        if (buildingsList.isEmpty()) {
            Utils.displayWarningInput("No available building upgrades.");
            try {
                Utils.displayReturnPlayer("No available building upgrades.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            App.setMessage("Select a building to add:");
            App.setList(buildingsList);

            requestUserInputList(input -> {
                int index = extractIndex(input);
                String[] options = buildingsList.split("\n");
                if (index < 0 || index >= options.length) {
                    Utils.displayWarningInput("Invalid index.");
                    return;
                }

                String selectedLine = options[index];
                Pattern pattern = Pattern.compile("^\\[\\d+] - (.+?) \\(Price: ([\\d,.]+)\\)");
                Matcher matcher = pattern.matcher(selectedLine);

                if (matcher.find()) {
                    selectedBuilding = matcher.group(1);
                    price = Double.parseDouble(matcher.group(2).replace(",", "."));
                    upgradeType = "WITH_BUILDING";

                    confirmUpgrade();
                } else {
                    Utils.displayWarningInput("Error parsing selected upgrade.");
                }
            });
        }
    }

    private void upgradeStationType() {
        String upgradeList = controller.getAvailableStationUpgrades(station);
        if (upgradeList.isEmpty()) {
            Utils.displayWarningInput("No available station type upgrades.");
            try {
                Utils.displayReturnPlayer("No available station type upgrades.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            App.setMessage("Select a station type upgrade:");
            App.setList(upgradeList);

            requestUserInputList(input -> {
                int index = extractIndex(input);
                String[] upgrades = upgradeList.split("\n");
                if (index < 0 || index >= upgrades.length) {
                    Utils.displayWarningInput("Invalid index.");
                    return;
                }

                String selectedLine = upgrades[index];
                Pattern pattern = Pattern.compile("^\\s*\\[\\d+]\\s*-\\s*(.+?),\\s*price:\\s*([\\d.,]+)$");
                Matcher matcher = pattern.matcher(selectedLine.trim());

                if (matcher.find()) {
                    selectedUpgrade = matcher.group(1).trim();
                    price = Double.parseDouble(matcher.group(2).replace(",", "."));
                    upgradeType = "STATION_TYPE_UPGRADE";
                    if (selectedUpgrade.equals(Upgrade.DEPOTTOSTATION.name)) {
                        askCardinalPosition();
                    } else {
                        confirmUpgrade();
                    }
                } else {
                    Utils.displayWarningInput("Error parsing selected upgrade.");
                }
            });
        }

    }

    private void askCardinalPosition() {
        App.setMessage("Enter the cardinal position (NE, SE, NW, SW):");
        App.setList("");

        requestUserInputString(input -> {
            String normalized = input.trim().toUpperCase();
            if (normalized.matches("NE|SE|NW|SW")) {
                cardinalPosition = normalized;
                confirmUpgrade();
            } else {
                Utils.displayWarningInput("Invalid cardinal position.");
                askCardinalPosition();
            }
        });
    }

    private void confirmUpgrade() {
        App.setMessage("Do you want to apply this upgrade? (Y/N)");
        App.setList("");

        requestUserInputString(input -> {
            String upper = input.trim().toUpperCase();
            if (upper.equals("Y")) {
                try {
                    applyUpgrade();
                } catch (InsuficientBudget | IllegalArgumentException e) {
                    Utils.displayWarningInput("Error: " + e.getMessage());
                }
            } else if (upper.equals("N")) {
                try {
                    Utils.displayReturnPlayer("Station creation canceled.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.displayWarningInput("Invalid input. Please enter 'Y' or 'N'.");
                confirmUpgrade();
            }
        });
    }

    private void applyUpgrade() {
        if ("WITH_BUILDING".equals(upgradeType)) {
            if ((selectedBuilding.equals(BuildingType.CAFFEE_LARGE.name) ||
                    selectedBuilding.equals(BuildingType.HOTEL_LARGE.name)) &&
                    controller.existSmallVersion(selectedBuilding, station)) {
                controller.applyBuilding(station, selectedBuilding, price);
                try {
                    Utils.displayReturnPlayer("The small version was  upgraded!\n" + "Building:" + selectedBuilding + "\nPrice:" + price);
                } catch (IOException e) {

                }
            } else if (selectedBuilding.equals(BuildingType.TELEPHONE.name) &&
                    controller.existTelegraph(station)) {
                controller.applyBuilding(station, selectedBuilding, price);
                try {
                    Utils.displayReturnPlayer("The telegraph was substituted!\n" + "Building:" + selectedBuilding + "\nPrice:" + price);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                controller.applyBuilding(station, selectedBuilding, price);
                try {
                    Utils.displayReturnPlayer("The station was successfully upgraded!\n" + "Building:" + selectedBuilding + "\nPrice:" + price);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if ("STATION_TYPE_UPGRADE".equals(upgradeType)) {
            controller.applyUpgrade(station, selectedUpgrade, cardinalPosition, price);
            try {
                Utils.displayReturnPlayer("The station was successfully upgraded!\n" + "Upgraded to " + selectedUpgrade +
                "\nPrice:" + price );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int extractIndex(String input) {
        Matcher matcher = Pattern.compile("\\[(\\d+)]").matcher(input.trim());
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }
}
