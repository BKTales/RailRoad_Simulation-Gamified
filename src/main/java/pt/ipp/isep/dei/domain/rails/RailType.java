package pt.ipp.isep.dei.domain.rails;

import java.io.Serializable;
import java.util.List;

/**
 * Enum representing different types of railway rails.
 * <p>
 * Each rail type includes information about its introduction year, name, and associated price.
 * </p>
 */
public enum RailType implements Serializable {
    /**
     * Electrified rail type, introduced in 1910.
     */
    ELECTRIFIED(1910, "ELECTRIFIED", 100,20000),

    /**
     * Non-electrified rail type, introduced in 1800.
     */
    NON_ELECTRIFIED(1800, "NON_ELECTRIFIED", 100,10000);

    public final int startYear;
    public final String name;
    public final double price;
    public final double maintainancePrice;

    /**
     * Constructs a RailType with the specified start year, name, and price.
     *
     * @param startYear the year the rail type was introduced
     * @param name the name of the rail type
     * @param price the price associated with this rail type
     */
    RailType(int startYear, String name, double price, double maintainancePrice) {
        this.startYear = startYear;
        this.name = name;
        this.price = price;
        this.maintainancePrice = maintainancePrice;
    }

    /**
     * Returns a string representation of the rail type.
     *
     * @return formatted string with rail type details
     */
    @Override
    public String toString() {
        return String.format("RailType: %s, price: %.2f, maintainancePrice: %.2f", name, price, maintainancePrice);
    }

    /**
     * (To be implemented) Returns a list of available rail types.
     *
     * @return list of available RailType instances
     */
    public List<RailType> getAvailableTypes() {
        // TODO: implement this method to return available rail types
        return null;
    }
}
