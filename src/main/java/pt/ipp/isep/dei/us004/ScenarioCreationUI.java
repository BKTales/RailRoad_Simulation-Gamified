package pt.ipp.isep.dei.us004;


import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.scenario.*;
import pt.ipp.isep.dei.utils.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class ScenarioCreationUI implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ScenarioCreationController controller = new ScenarioCreationController();
    private final ScenarioCreationAssembler assembler = new ScenarioCreationAssembler(controller);
    private ScenarioCreationDTO scenarioDTO = new ScenarioCreationDTO();
    private boolean timeRestrictionSet = false;

    public void run() {
        readScenarioName();
    }

    private void readScenarioName() {
        App.setMessage("Enter scenario name:");
        ReadStringInput.requestUserInputString(input -> {
            scenarioDTO.setScenarioName(input);
            selectMap();
        });
    }

    private void selectMap() {
        List<String> mapNames = controller.getAllMapNames();
        if (mapNames.isEmpty()) {
            Utils.displayErrorMessage("No maps available! Create a map first.");
            returnToMenu();
            return;
        }

        App.setMessage("Select a map:");
        App.setList(String.join("\n", mapNames));
        ReadListInput.requestUserInputList(selected -> {
            scenarioDTO.setAttachedMapName(selected);
            readBudget();
        });
    }

    private void readBudget() {
        App.setMessage("Enter initial budget:");
        ReadStringInput.requestUserInputString(input -> {
            try {
                double budget = Double.parseDouble(input);
                if (budget < 0) {
                    throw new NumberFormatException("Budget cannot be negative");
                }
                scenarioDTO.setInitialBudget(budget);
                showRestrictionsMenu();
            } catch (NumberFormatException e) {
                Utils.displayErrorMessage("Invalid budget: " + e.getMessage());
                readBudget();
            }
        });
    }

    private void showRestrictionsMenu() {
        List<String> options = Arrays.asList(
                "Time Restriction (REQUIRED)",
                "Technology Restriction",
                "Product Restriction",
                "Port Behavior",
                "Historical Restrictions",
                "Finish & Create Scenario"
        );

        App.setMessage("Add restrictions:");
        App.setList(String.join("\n", options));
        ReadListInput.requestUserInputList(choice -> {
            switch (options.indexOf(choice)) {
                case 0: createTimeRestriction(); break;
                case 1: createTechnologyRestriction(); break;
                case 2: createProductRestriction(); break;
                case 3: createPortBehavior(); break;
                case 4: displayHistoricalRestrictionsMenu(); break;
                case 5: finishScenarioCreation(); break;
                default: showRestrictionsMenu();
            }
            System.out.println(choice);
        });
    }

    private void displayHistoricalRestrictionsMenu() {
        App.setMessage("Historical Restrictions Menu:");

        String presetNames = controller.getPresetRestrictions();

        App.setList(presetNames);

        ReadListInput.requestUserInputList(choice -> {

            controller.initiateHistoricalRestriction(choice);

            scenarioDTO.setStartYear(controller.getRestriction().getTimeRestrictions().getStartYear());
            scenarioDTO.setEndYear(controller.getRestriction().getTimeRestrictions().getEndYear());
            scenarioDTO.setProductRestrictions(controller.getRestriction().getProductRestrictions().getProductRestrictionDTOList());
            scenarioDTO.setRestrictedTechnologies(controller.getRestriction().getTechnologyRestriction().getTechList());
            // Imported product indices (list)
            List<Integer> importedIndexes = controller.getRestriction()
                    .getPortBehaviour()
                    .getImportedProducts()
                    .stream()
                    .map(ProductType::ordinal)
                    .collect(Collectors.toList());

            scenarioDTO.setImportedProductsIndex(importedIndexes);

            int exportedIndex = controller.getRestriction()
                    .getPortBehaviour()
                    .getExportedProduct()
                    .ordinal();

            scenarioDTO.setExportedProductIndex(exportedIndex);


            timeRestrictionSet = true;
            finishScenarioCreation();
        });
    }





    private void createTimeRestriction() {
        List<String> yearOptions = new ArrayList<>();
        for (int year = 1800; year <= 2010; year += 5) {
            yearOptions.add(String.valueOf(year));
        }

        App.setMessage("Select start year:");
        App.setList(String.join("\n", yearOptions));
        ReadListInput.requestUserInputList(selected -> {
            try {
                int startYear = Integer.parseInt(selected);
                if (startYear < 1800) {
                    throw new IllegalArgumentException("Start year must be ≥1800");
                }
                scenarioDTO.setStartYear(startYear);
                selectEndYear(startYear);
            } catch (IllegalArgumentException e) {
                Utils.displayErrorMessage("Invalid year: " + e.getMessage());
                createTimeRestriction();
            }
        });
    }

    private void selectEndYear(int startYear) {
        List<String> yearOptions = new ArrayList<>();
        for (int year = startYear; year <= 2010; year += 10) {
            yearOptions.add(String.valueOf(year));
        }

        App.setMessage("Select end year:");
        App.setList(String.join("\n", yearOptions));
        ReadListInput.requestUserInputList(selected -> {
            try {
                int endYear = Integer.parseInt(selected);
                if (endYear < startYear) {
                    throw new IllegalArgumentException("End year must be ≥ start year");
                }
                scenarioDTO.setEndYear(endYear);
                timeRestrictionSet = true;
                showRestrictionsMenu();
            } catch (IllegalArgumentException e) {
                Utils.displayErrorMessage("Invalid year: " + e.getMessage());
                selectEndYear(startYear);
            }
        });
    }

    private void createTechnologyRestriction() {

        List<String> options = Arrays.asList(
                "Technologies to restrict",
                "Mark product as unavailable",
                "Finish technological restrictions"
        );

        App.setMessage("Technology Restrictions Menu:");
        App.setList(String.join("\n", options));
        ReadListInput.requestUserInputList(choice -> {
            switch (options.indexOf(choice)) {
                case 0: restrictTechnology(); break;
                case 1: selectProductForUnavailable(); break;
                case 2: showRestrictionsMenu(); break;
                default: createTechnologyRestriction();
            }
        });
    }

    public void restrictTechnology() {
        showTechnologyRestrictionMenu();
    }

    private void showTechnologyRestrictionMenu() {
        // Get all technology types (effectively final)
        final List<TechnologyType> allTechs = Arrays.asList(TechnologyType.values());

        // Get or create restricted technologies list (effectively final)
        final List<TechnologyType> restrictedTechs = Optional.ofNullable(scenarioDTO.getRestrictedTechnologies())
                .orElseGet(ArrayList::new);

        // Prepare menu options (derived from effectively final variables)
        List<String> menuOptions = allTechs.stream()
                .map(tech -> restrictedTechs.contains(tech)
                        ? "[RESTRICTED] " + tech.name()
                        : tech.name())
                .collect(Collectors.toList());

        menuOptions.add("FINISH SELECTION");

        // Show menu with current status
        App.setMessage(String.format(
                "Select technologies to restrict (Current restrictions: %d/%d):",
                restrictedTechs.size(),
                allTechs.size()
        ));
        App.setList(String.join("\n", menuOptions));

        ReadListInput.requestUserInputList(selected -> {
            if ("FINISH SELECTION".equals(selected)) {
                createTechnologyRestriction();
                return;
            }

            try {
                // Remove [RESTRICTED] prefix if present
                String cleanSelection = selected.replace("[RESTRICTED] ", "");
                TechnologyType selectedTech = TechnologyType.valueOf(cleanSelection);

                // Create new list for modifications (to avoid modifying effectively final variable)
                List<TechnologyType> newRestrictedTechs = new ArrayList<>(restrictedTechs);

                // Toggle restriction status
                if (newRestrictedTechs.contains(selectedTech)) {
                    newRestrictedTechs.remove(selectedTech);
                    Utils.displaySuccessMessage(selectedTech + " restriction removed");
                } else {
                    newRestrictedTechs.add(selectedTech);
                    Utils.displaySuccessMessage(selectedTech + " marked as restricted");
                }

                // Update DTO with new list
                scenarioDTO.setRestrictedTechnologies(newRestrictedTechs);

                // Refresh menu with updated state
                showTechnologyRestrictionMenu();
            } catch (IllegalArgumentException e) {
                Utils.displayErrorMessage("Invalid selection! Please try again.");
                showTechnologyRestrictionMenu();
            }
        });
    }

    private void selectProductForUnavailable() {
        showProductUnavailableMenu();
    }

    private void showProductUnavailableMenu() {
        List<String> productOptions = new ArrayList<>(controller.getAvailableProductNames());

        if (scenarioDTO.getProductRestrictions() != null) {
            List<String> unavailableProducts = scenarioDTO.getProductRestrictions().stream()
                    .filter(ScenarioCreationDTO.ProductRestrictionDTO::isUnavailable)
                    .map(ScenarioCreationDTO.ProductRestrictionDTO::getProductName)
                    .collect(Collectors.toList());
            productOptions.removeAll(unavailableProducts);
        }

        productOptions.add("BACK");

        App.setMessage("Select product to mark as unavailable (current unavailable: " +
                (scenarioDTO.getProductRestrictions() != null ?
                        scenarioDTO.getProductRestrictions().size() : 0) + ")");
        App.setList(String.join("\n", productOptions));

        ReadListInput.requestUserInputList(selected -> {
            if ("BACK".equals(selected)) {
                createTechnologyRestriction();
            } else {
                ScenarioCreationDTO.ProductRestrictionDTO restriction =
                        assembler.createProductRestrictionDTO(selected, 0, 0, true);

                List<ScenarioCreationDTO.ProductRestrictionDTO> restrictions =
                        scenarioDTO.getProductRestrictions();
                if (restrictions == null) {
                    restrictions = new ArrayList<>();
                    scenarioDTO.setProductRestrictions(restrictions);
                }
                restrictions.add(restriction);

                Utils.displaySuccessMessage(selected + " marked as unavailable");

                showProductUnavailableMenu();
            }
        });
    }


    private void createProductRestriction() {
        List<String> options = Arrays.asList(
                "Add product restriction",
                "Finish product restrictions"
        );

        App.setMessage("Product Restrictions Menu:");
        App.setList(String.join("\n", options));
        ReadListInput.requestUserInputList(choice -> {
            switch (options.indexOf(choice)) {
                case 0: selectProductForRestriction(); break;
                case 1: showRestrictionsMenu(); break;
                default: createProductRestriction();
            }
        });
    }

    private void selectProductForRestriction() {
        List<String> productOptions = controller.getAvailableProductNames();
        productOptions.add("BACK");

        App.setMessage("Select product to configure:");
        App.setList(String.join("\n", productOptions));
        ReadListInput.requestUserInputList(selected -> {
            if ("BACK".equals(selected)) {
                createProductRestriction();
            } else {
                ScenarioCreationDTO.ProductRestrictionDTO restriction =
                        assembler.createProductRestrictionDTO(selected, 0, 0, false);
                configureProductRestriction(restriction);
            }
        });
    }


    private void configureProductRestriction(ScenarioCreationDTO.ProductRestrictionDTO restriction) {
        App.setMessage("Enter production time (years):");
        ReadStringInput.requestUserInputString(timeInput -> {
            try {
                int time = Integer.parseInt(timeInput);
                if (time <= 0) throw new IllegalArgumentException();
                restriction.setTimeToProduce(time);

                App.setMessage("Enter value multiplier:");
                ReadStringInput.requestUserInputString(multiplierInput -> {
                    try {
                        double multiplier = Double.parseDouble(multiplierInput);
                        if (multiplier <= 0) throw new IllegalArgumentException();
                        restriction.setMultiplier(multiplier);

                        List<ScenarioCreationDTO.ProductRestrictionDTO> restrictions =
                                scenarioDTO.getProductRestrictions();
                        if (restrictions == null) {
                            restrictions = new ArrayList<>();
                        }
                        restrictions.add(restriction);
                        scenarioDTO.setProductRestrictions(restrictions);

                        Utils.displaySuccessMessage("Restriction added for " + restriction.getProductName());
                        createProductRestriction();
                    } catch (Exception e) {
                        Utils.displayErrorMessage("Invalid multiplier!");
                        configureProductRestriction(restriction);
                    }
                });
            } catch (Exception e) {
                Utils.displayErrorMessage("Invalid production time!");
                configureProductRestriction(restriction);
            }
        });
    }


    private void createPortBehavior() {
        List<ProductType> products = List.of(ProductType.values());
        List<String> productOptions = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            productOptions.add(i + ": " + products.get(i).name());
        }
        productOptions.add("FINISH SELECTION");

        App.setMessage("Select imported products:");
        App.setList(String.join("\n", productOptions));
        ReadListInput.requestUserInputList(selected -> {
            if ("FINISH SELECTION".equals(selected)) {
                if (scenarioDTO.getImportedProductsIndex() == null ||
                        scenarioDTO.getImportedProductsIndex().isEmpty()) {
                    Utils.displayWarningInput("Please select at least one imported product");
                    createPortBehavior();
                } else {
                    selectExportedProduct();
                }
                return;
            }

            try {
                int index = Integer.parseInt(selected.split(":")[0].trim());
                if (index < 0 || index >= products.size()) {
                    throw new IndexOutOfBoundsException();
                }

                List<Integer> imports = scenarioDTO.getImportedProductsIndex();
                if (imports == null) {
                    imports = new ArrayList<>();
                }
                if (!imports.contains(index)) {
                    imports.add(index);
                    scenarioDTO.setImportedProductsIndex(imports);
                }
                createPortBehavior();
            } catch (Exception e) {
                Utils.displayErrorMessage("Invalid selection!");
                createPortBehavior();
            }
        });
    }

    private void selectExportedProduct() {
        List<ProductType> products = List.of(ProductType.values());
        List<String> productOptions = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            productOptions.add(i + ": " + products.get(i).name());
        }

        App.setMessage("Select exported product:");
        App.setList(String.join("\n", productOptions));
        ReadListInput.requestUserInputList(selected -> {
            try {
                int index = Integer.parseInt(selected.split(":")[0].trim());
                if (index < 0 || index >= products.size()) {
                    throw new IndexOutOfBoundsException();
                }
                scenarioDTO.setExportedProductIndex(index);
                showRestrictionsMenu();
            } catch (Exception e) {
                Utils.displayErrorMessage("Invalid selection!");
                selectExportedProduct();
            }
        });
    }

    private void finishScenarioCreation() {
        if (!timeRestrictionSet) {
            Utils.displayErrorMessage("Time restriction is required!");
            showRestrictionsMenu();
            return;
        }

        try {
            assembler.createScenarioFromDTO(scenarioDTO);
            if (controller.successScenarioCreationFlag()) {
                askToSaveScenario();
            } else {
                //throw new Exception("Scenario creation failed");
            }
        } catch (Exception e) {
            Utils.displayErrorMessage("Error: " + e.getMessage());
            showRestrictionsMenu();
        }
    }

    private void askToSaveScenario() {
        List<String> options = Arrays.asList("Yes", "No");

        App.setMessage("Save scenario?");
        App.setList(String.join("\n", options));
        ReadListInput.requestUserInputList(choice -> {
            if ("Yes".equals(choice)) {
                try {
                    controller.saveScenario(
                            Simulator.getInstance().getScenarioRepository().getScenario(scenarioDTO.getScenarioName())
                    );
                    Utils.displayReturnEditor("Scenario saved successfully!");
                } catch (Exception e) {
                    Utils.displayErrorMessage("Save failed: " + e.getMessage());
                }
            }
            returnToMenu();
        });
    }

    private void returnToMenu() {
        try {
            Utils.displayReturnEditor("Returning to editor menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}