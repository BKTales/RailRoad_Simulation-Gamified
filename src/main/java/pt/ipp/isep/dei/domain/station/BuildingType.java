package pt.ipp.isep.dei.domain.station;

import java.io.Serializable;

/**
 * Enumeration representing the different types of buildings.
 * Each building type includes properties such as price, name, year of introduction, and a money multiplier.
 */
public enum BuildingType implements Serializable {
    CAFFEE_SMALL(20000, "SMALL_CAFFEE", 1959, 1),
    CAFFEE_LARGE(30000, "LARGE_CAFFEE", 1965, 2),
    TELEGRAPH(40000, "TELEGRAPH", 1850, 1),
    TELEPHONE(80000, "TELEPHONE", 1876, 2),
    CUSTOMS(30000, "CUSTOMS", 1900, 2),
    POST_OFFICE(30000, "POST_OFFICE", 1880, 2),
    HOTEL_SMALL(20000, "SMALL_HOTEL", 1910, 2),
    HOTEL_LARGE(30000, "LARGE_HOTEL", 1930, 3),
    SILO(20000, "SILO", 1875, 2),
    LIQUID_STORAGE(20000, "LIQUID_STORAGE", 1885, 2);

    /** The purchase price of the building type. */
    public final double price;

    /** The name identifier of the building type. */
    public final String name;

    /** The year when this building type was introduced or became available. */
    public final int startYear;

    /** The multiplier applied to monetary calculations related to this building. */
    public final double moneyMultiplier;

    /**
     * Constructs a BuildingType enum constant.
     *
     * @param price           The purchase price of the building.
     * @param name            The name identifier of the building type.
     * @param startYear       The year the building type became available.
     * @param moneyMultiplier The multiplier for financial calculations.
     */
    BuildingType(double price, String name, int startYear, int moneyMultiplier) {
        this.price = price;
        this.name = name;
        this.startYear = startYear;
        this.moneyMultiplier = moneyMultiplier;
    }

    /**
     * Returns the year the building type was introduced.
     *
     * @return The start year.
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * Returns the price of the building type.
     *
     * @return The purchase price.
     */
    public double getPrice() {
        return price;
    }
}
