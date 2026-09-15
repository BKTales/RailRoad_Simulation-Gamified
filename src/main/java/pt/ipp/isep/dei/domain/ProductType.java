package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Enum representing different types of products in the system.
 * Each product type has a default value, name, and production time (in seconds).
 */
public enum ProductType implements Serializable {
    GRAINS(15, "GRAINS", 3),
    VEGETABLES(15, "VEGETABLES", 1),
    COFFEE(15, "COFFEE", 2),
    RUBBER(15, "RUBBER", 5),
    WOOL(15, "WOOL", 3),

    IRON(15, "IRON", 6),
    COAL(15, "COAL", 6),

    STEEL(15, "STEEL", 0),
    BREAD(15, "BREAD", 0),
    CAR(15, "CAR", 0),
    CLOTHING(15, "CLOTHING", 0),

    PEOPLE(15, "PEOPLE", 0),
    MAIL(15, "MAIL", 0);

    /**
     * The default value of the product.
     */
    public final double productValue;

    /**
     * The name identifier of the product.
     */
    public final String productName;

    /**
     * The time required to produce the product, in seconds.
     */
    public final int timeToProduce; // in seconds!

    /**
     * Constructs a ProductType enum constant with the specified value, name, and production time.
     *
     * @param productValue  default product value
     * @param productName   product name string
     * @param timeToProduce production time in seconds
     */
    ProductType(int productValue, String productName, int timeToProduce) {
        this.productValue = productValue;
        this.productName = productName;
        this.timeToProduce = timeToProduce;
    }


}
