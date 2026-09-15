package pt.ipp.isep.dei.domain.station;

import java.io.Serializable;

/**
 * Enumeration representing different types of stations,
 * each with a name and an associated price.
 */
public enum StationTypes implements Serializable {
    DEPOT("DEPOT", 50000),
    STATION("STATION", 100000),
    TERMINAL("TERMINAL", 200000);

    public final String name;
    public final double price;

    /**
     * Constructor for the StationTypes enum.
     *
     * @param name  the name of the station type
     * @param price the price associated with the station type
     */
    StationTypes(String name, double price) {
        this.name = name;
        this.price = price;
    }


    /**
     * Returns the string representation of the station type,
     * including the name and price formatted to two decimal places.
     *
     * @return formatted string with name and price
     */
    @Override
    public String toString() {
        return String.format("%s, price:%.2f", name, price);
    }
}
