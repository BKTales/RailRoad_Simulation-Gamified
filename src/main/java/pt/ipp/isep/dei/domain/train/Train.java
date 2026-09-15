package pt.ipp.isep.dei.domain.train;

import pt.ipp.isep.dei.domain.station.StationType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a train consisting of a locomotive and an optional list of cargo.
 * Tracks the train's current location and status.
 */

public class Train implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private Locomotive locomotive;
    private String whereItIs; // check to see if it will be added
    private ArrayList<Cargo> cargo;
    private StationType currentLocation;
    private boolean active = false;

    /**
     * Creates a new train with the specified locomotive model and unique ID.
     * The train initially has no cargo and is located in the inventory.
     *
     * @param model the locomotive model of the train
     * @param id    unique identifier for the train
     */

    public Train(LocomotiveModel model, int id) {
        this.id = id;
        this.locomotive = new Locomotive(model);
        this.whereItIs = "inventory";
        this.cargo = null;
    }
    /**
     * Returns the list of cargo currently loaded on the train.
     *
     * @return the cargo list, or null if none is loaded
     */

    public ArrayList<Cargo> getCargo() {return cargo;}

    /**
     * Returns the unique identifier of this train.
     *
     * @return the train ID
     */

    public int getId(){return id;}

    /**
     * Returns a string describing where the train currently is.
     * Note: this is currently a string and might be replaced by a more structured representation.
     *
     * @return a string describing the train's location
     */

    // need to change its behaviour, no way it's going to continue as string
    public String getWhereItIs() { return (this.whereItIs); }
    /**
     * Returns the locomotive associated with this train.
     *
     * @return the locomotive object
     */

    public Locomotive getLocomotive() { return (this.locomotive); }
    /**
     * Determines if a trip from the departure station to the arrival station is possible.
     * Returns -1 if the trip is not possible.
     * Returns a positive integer indicating the distance if the trip is possible.
     * <p>
     * Note: this method currently returns 1 as a placeholder and needs implementation.
     *
     * @param departure the departure station
     * @param arrival   the arrival station
     * @return -1 if trip not possible; positive integer distance otherwise
     */

    // need to build method
    ///  -1 means that is not possible, and the positive integers means that
    /// it is possible and that is how much it will take (in distance) to get to the other station
    public int isTripPossible(StationType departure, StationType arrival) {

        return (1);
    }

    /**
     * Returns a detailed string representation of the train, including locomotive details,
     * current location, and loaded cargo with quantities and values.
     *
     * @return a string description of the train
     */

    @Override
    public String toString() {
        StringBuilder cargoDescription = new StringBuilder();

        if (cargo == null || cargo.isEmpty()) {
            cargoDescription.append(", No cargo");
        } else {
            cargoDescription.append(", Cargo:");
            for (Cargo c : cargo) {
                cargoDescription.append(" ")
                        .append(c.getQuantity()).append(" x ")
                        .append(c.getProduct().getProductName())
                        .append(" (").append(c.getCargoValue()).append("€);");
            }
        }

        return "Train with locomotive:  " + locomotive.getName() + "\n" +
                "Type:  " + locomotive.getLocomotiveType() + "\n" +
                "Top Speed:  " + locomotive.getTopSpeed() + " mph, " + "\n" +
                "Acquisition Price:  " + locomotive.getAcquisitionPrice() + "€, " + "\n" +
                "Maintenance:  " + locomotive.getMaintenance() + "€ per Year" + "\n" +
                "Fuel Cost:  " + locomotive.getFuelCoast() + "€ per Year" + "\n" +
                "location:  " + whereItIs + "\n" +
                cargoDescription;
    }

    /**
     * Sets the current station location of the train.
     *
     * @param station the current location station
     */
    public void setCurrentLocation(StationType station) {
        this.currentLocation = station;
    }

    /**
     * Gets the current station location of the train.
     *
     * @return the current location station
     */

    public StationType getCurrentLocation() {
        return this.currentLocation;
    }

    /**
     * Checks whether the train is currently active.
     *
     * @return true if the train is active; false otherwise
     */

    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the train.
     *
     * @param active true to activate the train; false to deactivate it
     */

    public void setActive(boolean active) {
        this.active = active;
    }

}
