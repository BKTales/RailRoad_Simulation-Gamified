package pt.ipp.isep.dei.domain.train;

import pt.ipp.isep.dei.domain.Product;

import java.io.Serializable;

/**
 * Represents a cargo item consisting of a product and its quantity.
 * Also calculates the total value of the cargo based on product value and quantity.
 */
public class Cargo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Product product;
    private int quantity;
    private double cargoValue;

    /**
     * Constructs a Cargo object.
     *
     * @param product the product being transported
     * @param quantity the quantity of the product
     */
    public Cargo(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.cargoValue = product.getProductValue() * quantity;
    }

    /**
     * Returns the total value of the cargo.
     *
     * @return the cargo value
     */
    public double getCargoValue() {
        return cargoValue;
    }

    /**
     * Returns the product of the cargo.
     *
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns the quantity of the product in the cargo.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }


    /**
     * Returns a string representation of the cargo.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return product.getProductName() + " x" + quantity;
    }
}
