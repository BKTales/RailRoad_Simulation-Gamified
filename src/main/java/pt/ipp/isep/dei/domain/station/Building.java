package pt.ipp.isep.dei.domain.station;

import java.io.Serializable;

/**
 * Represents a building with a specific type, name, and money multiplier.
 * <p>
 * Each building has a BuildingType which defines its characteristics,
 * including a name and a money multiplier affecting economic calculations.
 * </p>
 */
public class Building implements Serializable {
    private static final long serialVersionUID = 1L;
    private BuildingType buildingType;
    private double moneyMultiplier;
    private String name;

    /**
     * Constructs a Building instance based on the given BuildingType.
     *
     * @param buildingType the type of the building which determines its properties
     */
    public Building(BuildingType buildingType) {
        this.buildingType = buildingType;
        this.name = buildingType.name;
        this.moneyMultiplier = buildingType.moneyMultiplier;
    }

    /**
     * Returns the name of the building.
     *
     * @return the building name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the money multiplier associated with the building.
     *
     * @return the money multiplier value
     */
    public double getMoneyMultiplier() {
        return moneyMultiplier;
    }

    /**
     * Returns the BuildingType of this building.
     *
     * @return the building type
     */
    public BuildingType getBuildingType() {
        return buildingType;
    }

    /**
     * Returns a string representation of the building, including its name and price.
     *
     * <p>The format used is: {@code [name] (Price: [price])}, where the price is shown with two decimal places.</p>
     *
     * @return a formatted string representing the building and its price.
     */
    @Override
    public String toString() {
        return String.format("%s (Price: %.2f)", name, buildingType.price);
    }
}
