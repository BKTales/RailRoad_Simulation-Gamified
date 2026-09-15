package pt.ipp.isep.dei.domain.station;

import java.io.Serializable;

/**
 * Enum representing different station upgrade types and their respective prices.
 */
public enum Upgrade implements Serializable {
    STATIONTOTERMINAL(100000,"STATIONTOTERMINAL"),
    DEPOTTOTERMINAL(150000,"DEPOTTOTERMINAL"),
    DEPOTTOSTATION(50000,"DEPOTTOSTATION"),;

    public final double price;
    public final String name;

    Upgrade(double price, String name) {
        this.price = price;
        this.name = name;
    }


    @Override
    public String toString() {
        return String.format("%s, price:%.2f", name,price);
    }
}
