package pt.ipp.isep.dei.us004;

import pt.ipp.isep.dei.domain.scenario.TechnologyType;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object for scenario creation parameters
 */
public class ScenarioCreationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String scenarioName;
    private String attachedMapName;
    private Integer startYear;
    private Integer endYear;
    private List<TechnologyType> restrictedTechnologies;
    private List<ProductRestrictionDTO> productRestrictions;
    private List<Integer> importedProductsIndex;
    private Integer exportedProductIndex;
    private Double initialBudget;

    // Constructors
    public ScenarioCreationDTO() {
    }

    public ScenarioCreationDTO(String scenarioName, String attachedMapName, Integer startYear,
                               Integer endYear, List<TechnologyType> restrictedTechnologies,
                               List<ProductRestrictionDTO> productRestrictions,
                               List<Integer> importedProductsIndex, Integer exportedProductIndex,
                               Double initialBudget) {
        this.scenarioName = scenarioName;
        this.attachedMapName = attachedMapName;
        this.startYear = startYear;
        this.endYear = endYear;
        this.restrictedTechnologies = restrictedTechnologies;
        this.productRestrictions = productRestrictions;
        this.importedProductsIndex = importedProductsIndex;
        this.exportedProductIndex = exportedProductIndex;
        this.initialBudget = initialBudget;
    }

    // Getters and Setters
    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getAttachedMapName() {
        return attachedMapName;
    }

    public void setAttachedMapName(String attachedMapName) {
        this.attachedMapName = attachedMapName;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public List<TechnologyType> getRestrictedTechnologies() {
        return restrictedTechnologies;
    }

    public void setRestrictedTechnologies(List<TechnologyType> restrictedTechnologies) {
        this.restrictedTechnologies = restrictedTechnologies;
    }

    public List<ProductRestrictionDTO> getProductRestrictions() {
        return productRestrictions;
    }

    public void setProductRestrictions(List<ProductRestrictionDTO> productRestrictions) {
        this.productRestrictions = productRestrictions;
    }

    public List<Integer> getImportedProductsIndex() {
        return importedProductsIndex;
    }

    public void setImportedProductsIndex(List<Integer> importedProductsIndex) {
        this.importedProductsIndex = importedProductsIndex;
    }

    public Integer getExportedProductIndex() {
        return exportedProductIndex;
    }

    public void setExportedProductIndex(Integer exportedProductIndex) {
        this.exportedProductIndex = exportedProductIndex;
    }

    public Double getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(Double initialBudget) {
        this.initialBudget = initialBudget;
    }

    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScenarioCreationDTO that = (ScenarioCreationDTO) o;
        return Objects.equals(scenarioName, that.scenarioName) &&
                Objects.equals(attachedMapName, that.attachedMapName) &&
                Objects.equals(startYear, that.startYear) &&
                Objects.equals(endYear, that.endYear) &&
                Objects.equals(restrictedTechnologies, that.restrictedTechnologies) &&
                Objects.equals(productRestrictions, that.productRestrictions) &&
                Objects.equals(importedProductsIndex, that.importedProductsIndex) &&
                Objects.equals(exportedProductIndex, that.exportedProductIndex) &&
                Objects.equals(initialBudget, that.initialBudget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scenarioName, attachedMapName, startYear, endYear,
                restrictedTechnologies, productRestrictions,
                importedProductsIndex, exportedProductIndex, initialBudget);
    }

    @Override
    public String toString() {
        return "ScenarioCreationDTO{" +
                "scenarioName='" + scenarioName + '\'' +
                ", attachedMapName='" + attachedMapName + '\'' +
                ", startYear=" + startYear +
                ", endYear=" + endYear +
                ", restrictedTechnologies=" + restrictedTechnologies +
                ", productRestrictions=" + productRestrictions +
                ", importedProductsIndex=" + importedProductsIndex +
                ", exportedProductIndex=" + exportedProductIndex +
                ", initialBudget=" + initialBudget +
                '}';
    }

    /**
     * Nested DTO for product restrictions
     */
    public static class ProductRestrictionDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        private String productName;
        private int timeToProduce;
        private double multiplier;
        private boolean unavailable;

        public ProductRestrictionDTO() {
        }

        public ProductRestrictionDTO(String productName, int timeToProduce,
                                     double multiplier, boolean unavailable) {
            this.productName = productName;
            this.timeToProduce = timeToProduce;
            this.multiplier = multiplier;
            this.unavailable = unavailable;
        }

        // Getters and Setters
        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getTimeToProduce() {
            return timeToProduce;
        }

        public void setTimeToProduce(int timeToProduce) {
            this.timeToProduce = timeToProduce;
        }

        public double getMultiplier() {
            return multiplier;
        }

        public void setMultiplier(double multiplier) {
            this.multiplier = multiplier;
        }

        public boolean isUnavailable() {
            return unavailable;
        }

        public void setUnavailable(boolean unavailable) {
            this.unavailable = unavailable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductRestrictionDTO that = (ProductRestrictionDTO) o;
            return timeToProduce == that.timeToProduce &&
                    Double.compare(that.multiplier, multiplier) == 0 &&
                    unavailable == that.unavailable &&
                    Objects.equals(productName, that.productName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productName, timeToProduce, multiplier, unavailable);
        }

        @Override
        public String toString() {
            return "ProductRestrictionDTO{" +
                    "productName='" + productName + '\'' +
                    ", timeToProduce=" + timeToProduce +
                    ", multiplier=" + multiplier +
                    ", unavailable=" + unavailable +
                    '}';
        }
    }
}