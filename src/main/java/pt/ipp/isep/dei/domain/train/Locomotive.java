package pt.ipp.isep.dei.domain.train;

import java.io.Serializable;

/**
 * Represents a locomotive, created based on a {@link LocomotiveModel}.
 * Each locomotive has a type, top speed, acquisition price, maintenance cost, and fuel cost.
 */
public class Locomotive implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocomotiveModel model;
    private LocomotiveType type;
    private int topSpeed;
    private int sellPrice;
    private int maintenance;
    private int fuelCoast;
    private double acquisitionPrice;
    private int startYear;

    /**
     * Constructs a {@code Locomotive} based on a given {@code LocomotiveModel}.
     * The locomotive inherits attributes such as top speed, type, and price from the model.
     *
     * @param model the locomotive model used to create this locomotive
     */
    public Locomotive(LocomotiveModel model) {
        this.model = model;
        this.sellPrice = (int) (model.getAcquisitionPrice() * 0.7);
        this.topSpeed = (int) model.getTopSpeed();
        this.type = model.getType();
        this.maintenance = model.getMaintenance();
        this.fuelCoast = model.getFuelCoast();
        this.acquisitionPrice = model.getAcquisitionPrice();
        this.startYear = model.getStartYear();
    }

    /**
     * Factory method to create a new {@code Locomotive} instance.
     *
     * @param model the locomotive model
     * @return a new {@code Locomotive} instance
     */
    public static Locomotive createLocomotive(LocomotiveModel model) {
        return new Locomotive(model);
    }

    /**
     * Gets the type of this locomotive.
     *
     * @return the locomotive type
     */
    public LocomotiveType getLocomotiveType() {
        return type;
    }

    /**
     * Gets the year this locomotive model was introduced.
     *
     * @return the start year of the model
     */
    public int getStartYear() {
        return model.getStartYear();
    }

    /**
     * Gets the top speed of this locomotive.
     *
     * @return the top speed in mph
     */
    public double getTopSpeed() {
        return topSpeed;
    }

    /**
     * Gets the acquisition price of this locomotive.
     *
     * @return the acquisition price in euros
     */
    public double getAcquisitionPrice() {
        return model.getAcquisitionPrice();
    }

    /**
     * Gets the model associated with this locomotive.
     *
     * @return the locomotive model
     */
    public LocomotiveModel getModel() {
        return model;
    }

    /**
     * Gets the name of the locomotive (from the model).
     *
     * @return the name of the locomotive
     */
    public String getName() {
        return model.getName();
    }

    /**
     * Gets the maintenance cost of the locomotive.
     *
     * @return maintenance cost in euros
     */

    public int getMaintenance(){return model.getMaintenance();}

    /**
     * Gets the fuel cost of the locomotive.
     *
     * @return fuel cost in euros
     */

    public int getFuelCoast(){ return model.getFuelCoast();}

    /**
     * Returns a string representation of this locomotive, including name,
     * type, top speed, and acquisition price.
     *
     * @return a string describing the locomotive
     */
    @Override
    public String toString() {
        return getName() + " (" + getLocomotiveType() + ") - " +
                "Top speed: " + getTopSpeed() + " mph, " +
                "AcquisitionPrice: " + getAcquisitionPrice() + "€" +
                "Maintenance: " + getMaintenance() + "€" +
                "fuelCoast: " + getFuelCoast() + "€";
    }

}

