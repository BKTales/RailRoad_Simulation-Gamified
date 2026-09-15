package pt.ipp.isep.dei.domain.industries;

import pt.ipp.isep.dei.domain.Generatable;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.Location;

import java.io.Serializable;

/**
 * Abstract class representing an industry that produces a product.
 * Tracks production time, quantity produced, location, sector, and type.
 * Supports starting production, updating production over time, and checking produced quantity.
 */
public abstract class Industry implements Comparable<Industry>, Serializable, Generatable {
    private static final long serialVersionUID = 1L;
    private final int CREATION = 0;

    protected int timeToProduce;
    protected int maxProduction;
    protected int producedQuantity;
    protected long productionStartTime;
    protected boolean isProducing;
    protected Product product;
    protected Location location;
    protected IndustryType type;
    protected IndustrySector sector;


    /**
     * Constructs an Industry with the specified product, type, location,
     * production time, and maximum production capacity.
     *
     * @param product       the product this industry produces
     * @param type          the type of the industry
     * @param location      the geographic location of the industry
     * @param timeToProduce the time required to produce one batch (in seconds)
     * @param maxProduction the maximum quantity the industry can produce at once
     */
    public Industry(Product product, IndustryType type, Location location, int timeToProduce, int maxProduction) {
        this.type = type;
        this.product = product;
        this.location = location;
        this.timeToProduce = timeToProduce;
        this.maxProduction = maxProduction;
        this.producedQuantity = CREATION;
        this.isProducing = false;
    }

    // =================== Generate =================== //

    /**
     * Starts the production process by recording the start time.
     * Does nothing if production is already in progress.
     */
    public void startProduction(long timeNow) {
        productionStartTime = timeNow;
    }

    @Override
    public boolean removeFromProduction(int quantity) {
        if (quantity <= producedQuantity) {
            producedQuantity -= quantity;
            return true;
        }
        return false;
    }


    public boolean increaseProductQuantity(int qnt){
        if (qnt <= 0) {
            return false;
        }
        producedQuantity += qnt;
        return true;
    }


    /**
     * Updates the produced quantity based on elapsed time since production started.
     * Resets the timer after incrementing production.
     */
    public abstract String updateProduction(long currentTime);

    // =================== Time =================== //


    /**
     * Adjusts the production start time by adding a pause duration,
     * useful for handling simulation pauses.
     *
     * @param pauseDurationMs the duration of the pause in milliseconds
     */
    public void adjustForPause(long pauseDurationMs) {
        productionStartTime += pauseDurationMs;
    }

    /**
     * Checks if the industry has at least the specified quantity produced.
     *
     * @param quantity the quantity to check for
     * @return true if produced quantity is greater than or equal to the requested quantity, false otherwise
     */
    public boolean hasQuantity(int quantity) {
        return this.producedQuantity >= quantity;
    }

    /**
     * Placeholder method intended for setting time to production end.
     * Can be overridden by subclasses.
     */
    public void setTimeToEnd() {
        // Intentionally left blank
    }

    /**
     * Will update the start time of action with the difference of time
     * between start and end of pause
     * @param timeDiff
     */
    public void updateStartTimePause(long timeDiff){
        productionStartTime += timeDiff;
    }


    // =================== Getters =================== //

    /**
     * Returns the industry sector.
     *
     * @return the IndustrySector of this industry
     */
    public IndustrySector getSector() { return sector; }

    /**
     * Returns the location of the industry.
     *
     * @return the Location of this industry
     */
    public Location getLocation() { return location; }

    /**
     * Returns the product produced by the industry.
     *
     * @return the Product produced
     */
    public Product getProduct() { return product; }

    /**
     * Returns the product type of the produced product.
     *
     * @return the ProductType of the product
     */
    public ProductType getProductType() { return product.getProductType(); }

    /**
     * Returns the quantity produced so far.
     *
     * @return the produced quantity
     */
    public int getProducedQuantity() { return producedQuantity; }

    /**
     * Returns the industry type.
     *
     * @return the IndustryType
     */
    public IndustryType getType() {
        return type;
    }

    // =================== Getters =================== //

    /**
     * Compares this Industry to another object for equality.
     * Two industries are equal if their locations are equal.
     *
     * @param obj the object to compare to
     * @return true if the other object is an Industry with the same location, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Industry) {
            return location.equals(((Industry) obj).location);
        }
        return false;
    }

    /**
     * Returns a string with details about the production state of this industry.
     *
     * @return formatted string describing the industry production details
     */
    public String getProductionDetails() {
        return String.format(
                "Type: %s | Producing: %s (Qty: %d/%d) | Time: %ds | Location: %s",
                type.name(),
                product.getProductType().productName,
                producedQuantity,
                maxProduction,
                timeToProduce,
                location.getPosition().toString()
        );
    }

    /**
     * Returns a string representation of the industry.
     *
     * @return string describing the industry type, location, product, production time, and max capacity
     */
    @Override
    public String toString() {
        return this.type.toString() + " in: " + this.location.getPosition().toString() + " is " +  "\n"
                + "Produces " + this.product.toString() + " in " + this.timeToProduce + "s\n"
                + "Max capacity: " + this.maxProduction + "\n";
    }

    /**
     * Compares this industry to another based on location.
     * Returns 0 if locations are equal, 1 otherwise.
     *
     * @param industry the industry to compare to
     * @return 0 if locations are equal, 1 otherwise
     */
    @Override
    public int compareTo(Industry industry) {
        if (industry.getLocation().getPosition().equals(this.location.getPosition()))
            return 0;
        return 1;
    }
}
