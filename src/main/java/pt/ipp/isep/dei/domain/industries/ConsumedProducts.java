package pt.ipp.isep.dei.domain.industries;

import pt.ipp.isep.dei.domain.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a collection of products consumed by an industry.
 * Maintains a list of products and their quantities.
 */
public class    ConsumedProducts implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Product> products;

    /**
     * Constructs a ConsumedProducts object initialized with a single product.
     *
     * @param product the initial product to be consumed
     */
    ConsumedProducts(Product product) {
        this.products = new ArrayList<>();
        products.add(product);
    }

    /**
     * Adds a product to the consumed products list.
     *
     * @param product the product to add
     */
    public void addProduct(Product product){
        this.products.add(product);
    }

    /**
     * Returns the list of consumed products.
     *
     * @return an ArrayList of Products currently consumed
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    public boolean equals(ArrayList<Product> toCompare) {
        int manyShouldHave;
        int manyHave = 0;

        manyShouldHave = this.products.size();
        for (Product temp : toCompare) {
            if (!this.products.contains(temp)) {
                manyHave++;
            }
        }
        if (manyShouldHave == manyHave)
            return true;
        return false;
    }

    /**
     * Lists all consumed products as a single string,
     * with each product's string representation on a new line.
     *
     * @return a string listing all consumed products
     */
    public String listProducts(){
        String output = "";
        for(Product product : products){
            output += product.toString() + "\n";
        }
        return output;
    }

    public ArrayList<Product> gtCs() {
        return products;
    }
}
