package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a product with its type, value, production time, and name.
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProductType productType;
    private double productValue;
    private final String productName;
    private int timeToProduce; // in seconds!

    /**
     * Constructs a Product based on a given ProductType.
     *
     * @param productType the type of the product
     */
    public Product(ProductType productType) {
        this.productType = productType;
        this.productValue = productType.productValue;
        this.productName = productType.productName;
        this.timeToProduce = productType.timeToProduce;
    }

    /**
     * Multiplies the current product value by a given multiplier.
     *
     * @param multiplier the factor to multiply the product value by
     */
    public void changeProductValue(double multiplier) {
        productValue *= multiplier;
    }

    /**
     * Returns a string representation of the product including its name and value.
     *
     * @return string describing the product
     */
    @Override
    public String toString() {
        return (productName + " worth " + productValue + "\n");
    }


    /**
     * Returns the product type of this product.
     *
     * @return the product type
     */
    public ProductType getProductType() {
        return productType;
    }

    /**
     * Returns a list of all available product types.
     *
     * @return list of all product types
     */
    public static List<String> getAllProducts() {
        List<String> productNames = new ArrayList<>();
        for (ProductType type : ProductType.values()) {
            productNames.add(type.name());
        }
        return productNames;
    }


    /**
     * Multiplies the product value by a multiplier if the product name matches.
     *
     * @param productName the name of the product to match
     * @param multiplier the factor to multiply the product value by
     */
    public void valueMultiplier(String productName, double multiplier){
        if (this.getProductName().equals(productName)) {
            this.productValue *= multiplier;
        }
    }

    /**
     * Returns the current value of the product.
     *
     * @return product value
     */
    public double getProductValue() {
        return productValue;
    }

    /**
     * Returns the name of the product.
     *
     * @return product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Returns the production time in seconds.
     *
     * @return time to produce the product in seconds
     */
    public int getTimeToProduce() {
        return timeToProduce;
    }

    /**
     * Sets the production time for the product in seconds.
     *
     * @param timeToProduce the production time in seconds
     */
    public void setTimeToProduce(int timeToProduce) {
        this.timeToProduce = timeToProduce;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return productName.equals(other.productName);
    }

    @Override
    public int hashCode() {
        return productName.hashCode();
    }
}
