package pt.ipp.isep.dei.us004;

import pt.ipp.isep.dei.us004.ScenarioCreationController;
import pt.ipp.isep.dei.domain.scenario.TechnologyType;
import pt.ipp.isep.dei.domain.scenario.Scenario;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioCreationAssembler {
        private final ScenarioCreationController controller;

        public ScenarioCreationAssembler(ScenarioCreationController controller) {
            this.controller = controller;
        }

        /**
         * Creates a scenario from DTO
         * @param dto the data transfer object containing scenario parameters
         * @throws IllegalArgumentException if required parameters are missing
         * @throws IllegalStateException if scenario creation fails
         */
        public void createScenarioFromDTO(ScenarioCreationDTO dto) {
            validateDTO(dto);

            controller.createTimeRestriction(dto.getStartYear(), dto.getEndYear());

            if (dto.getRestrictedTechnologies() != null && !dto.getRestrictedTechnologies().isEmpty()) {
                controller.createTechnologicalRestriction(dto.getRestrictedTechnologies());
            }

            if (dto.getProductRestrictions() != null) {
                for (ScenarioCreationDTO.ProductRestrictionDTO restriction : dto.getProductRestrictions()) {
                    if (restriction.isUnavailable()) {
                        controller.markProductAsUnavailable(restriction.getProductName());
                    } else {
                        controller.addHistoricalRestriction(
                                restriction.getProductName(),
                                restriction.getTimeToProduce(),
                                restriction.getMultiplier()
                        );
                    }
                }
            }

            if (dto.getImportedProductsIndex() != null && dto.getExportedProductIndex() != null) {
                controller.createPortBehaviour(dto.getImportedProductsIndex(), dto.getExportedProductIndex());
            }

            controller.setInitialBudget(dto.getInitialBudget());

            controller.createScenario(dto.getScenarioName(), dto.getAttachedMapName());

            if (!controller.successScenarioCreationFlag()) {
                throw new IllegalStateException("Scenario creation failed");
            }
        }

        /**
         * Converts domain parameters to DTO
         * @param scenarioName Name of the scenario
         * @param mapName Name of the attached map
         * @param startYear Start year of the scenario
         * @param endYear End year of the scenario
         * @param techRestrictions List of restricted technologies
         * @param initialBudget Initial budget amount
         * @return Configured ScenarioCreationDTO
         */
        public ScenarioCreationDTO convertToDTO(String scenarioName, String mapName,
                                                int startYear, int endYear,
                                                List<TechnologyType> techRestrictions,
                                                double initialBudget) {
            ScenarioCreationDTO dto = new ScenarioCreationDTO();
            dto.setScenarioName(scenarioName);
            dto.setAttachedMapName(mapName);
            dto.setStartYear(startYear);
            dto.setEndYear(endYear);
            dto.setRestrictedTechnologies(techRestrictions);
            dto.setInitialBudget(initialBudget);
            return dto;
        }

        /**
         * Saves scenario to file using controller
         * @param scenario Scenario to save
         * @throws IOException if file operations fail
         */
        public void saveScenario(Scenario scenario) throws IOException {
            controller.saveScenario(scenario);
        }

        /**
         * Gets available product names from controller
         * @return List of available product names
         */
        public List<String> getAvailableProductNames() {
            return controller.getAvailableProductNames();
        }

        /**
         * Validates that required DTO fields are present
         * @param dto DTO to validate
         * @throws IllegalArgumentException if validation fails
         */
        public void validateDTO(ScenarioCreationDTO dto) {
            if (dto.getScenarioName() == null || dto.getScenarioName().trim().isEmpty()) {
                throw new IllegalArgumentException("Scenario name is required");
            }
            if (dto.getAttachedMapName() == null || dto.getAttachedMapName().trim().isEmpty()) {
                throw new IllegalArgumentException("Attached map name is required");
            }
            if (dto.getStartYear() == null || dto.getEndYear() == null) {
                throw new IllegalArgumentException("Start year and end year are required");
            }
            if (dto.getInitialBudget() == null || dto.getInitialBudget() < 0) {
                throw new IllegalArgumentException("Valid initial budget is required");
            }
        }

        /**
         * Creates a ProductRestrictionDTO from parameters
         * @param productName Name of the product
         * @param timeToProduce Time needed to produce
         * @param multiplier Production multiplier
         * @param unavailable Whether product is unavailable
         * @return Configured ProductRestrictionDTO
         */
        public ScenarioCreationDTO.ProductRestrictionDTO createProductRestrictionDTO(
                String productName, int timeToProduce, double multiplier, boolean unavailable) {
            return new ScenarioCreationDTO.ProductRestrictionDTO(
                    productName, timeToProduce, multiplier, unavailable);
        }
    }
