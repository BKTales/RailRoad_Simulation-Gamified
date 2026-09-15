package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.domain.position.HouseBlock;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a city with a name, location, and a collection of house blocks.
 * The city may also be responsible for generating passengers, mail, and products.
 */
public class City implements Serializable, Generatable {
    private static final long serialVersionUID = 1L;
    private Simulation simulation = Simulation.getInstance();
    private String name;
    private List<HouseBlock> houseBlocksList;
    private Location location;
    private final int maxPassengers;
    private final int maxMail;
    private int passengersQnt;
    private int mailQnt;
    private ProductType consumedProduct;

    // cities don't "produce" because they are always available, because their production is instantaneous
    //  so they are always full, and if needed they produce more in the moment needed


    /**
     * Constructs a new City instance.
     *
     * @param name the name of the city
     * @param location the geographical location of the city
     * @param maxPassengers maximum number of passengers (currently unused)
     * @param maxMail maximum mail capacity (currently unused)
     * @param consumedProduct type of product consumed by the city (currently unused)
     */
    public City(String name, Location location, int maxPassengers, int maxMail, ProductType consumedProduct){
        this.name = name;
        this.location = location;
        this.maxPassengers = maxPassengers;
        this.maxMail = maxMail;
        mailQnt = 0;
        passengersQnt = 0;
        this.consumedProduct = consumedProduct;
    }

    // =================== Generate =================== //

    /**
     * Update the production of passengers and mail based on the time now
     * @param currentTime current time
     */
    @Override
    public String updateProduction(long currentTime) {
        int mailGenerated = generateMail();
        int passengersGenerated = generatePassenger();
        String toReturn = "";

        System.out.println(passengersQnt + " " + mailQnt);
        if (passengersGenerated > 0 || mailGenerated > 0) {
            toReturn += "City " + this.name + " at "
                    + location.getPosition().toString() + " generated ";
            if (mailGenerated != 0)
                toReturn += mailGenerated + " mails ";
            if (passengersGenerated != 0)
                toReturn += passengersGenerated + " passengers  ";
            toReturn += "\n";
        }
        return toReturn;
    }

    @Override
    public boolean removeFromProduction(int quantity) {
        if (quantity < passengersQnt) {
            passengersQnt -= quantity;
            return true;
        }
        return false;
    }

    /**
     * Generates passengers for the city.
     * (Method implementation pending)
     * @return producedQuantity
     */
    private int generatePassenger(){
        int producedQuantity = 0;

        if (passengersQnt < maxPassengers){
            producedQuantity = maxPassengers - passengersQnt;
            passengersQnt = maxPassengers;
        }
        return producedQuantity;
    }

    /**
     * Generates mail for the city.
     * (Method implementation pending)
     * @return producedQuantity
     */
    private int generateMail(){
        int producedQuantity = 0;

        if (mailQnt < maxMail){
            producedQuantity = maxMail - mailQnt;
            mailQnt = maxMail;
        }
        return producedQuantity;
    }

    // =================== Create =================== //

    public void createHouseBlockList(){
        houseBlocksList = new ArrayList<>();
    }

    public void createHouseBlock(int positionX, int positionY) {
        houseBlocksList.add(new HouseBlock(simulation.getCurrentMap().createLocation(new Position(positionX, positionY))));
    }

    // =================== Getters =================== //

    /**
     * Returns the name of the city.
     *
     * @return the city's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of house blocks within the city.
     *
     * @return list of HouseBlock objects
     */
    public List<HouseBlock> getHouseBlocks() {
        return houseBlocksList;
    }

    /**
     * Returns the geographical position of the city.
     *
     * @return the city's position
     */
    public Position getPosition() {
        return location.getPosition();
    }

    public Location getLocation(){
        return location;
    }

    public int getMailQnt() {
        return mailQnt;
    }

    public int getPassengersQnt() {
        return passengersQnt;
    }

    public ProductType getConsumedProductType() {
        return consumedProduct;
    }


    // =================== Gettters =================== //

    /**
     * Reduces the number of passengers if the requested amount is available.
     * If the requested amount exceeds the current number of passengers, nothing happens.
     *
     * @param amount the number of passengers to remove
     */
    public void reducePassengerQuantity(int amount) {
        if (amount > passengersQnt) {
            return;
        }
        passengersQnt -= amount;
    }

    /**
     * Reduces the amount of mail if the requested amount is available.
     * If the requested amount exceeds the current mail amount, nothing happens.
     *
     * @param amount the amount of mail to remove
     */
    public void reduceMailQuantity(int amount) {
        if (amount > mailQnt) {
            return;
        }
        mailQnt -= amount;
    }

    /**
     * Increases the amount of mail, respecting the maximum mail capacity.
     * If the provided amount exceeds the available space, only the allowed amount is added.
     *
     * @param amount the amount of mail to add; values less than or equal to zero are ignored
     */
    public void increaseMailQuantity(int amount) {
        if (amount <= 0) {
            return;
        }

        int availableSpace = maxMail - mailQnt;

        if (availableSpace <= 0) {
            return;
        }

        mailQnt += Math.min(amount, availableSpace);
    }

    /**
     * Increases the number of passengers, respecting the maximum passenger capacity.
     * If the provided amount exceeds the available space, only the allowed amount is added.
     *
     * @param amount the number of passengers to add; values less than or equal to zero are ignored
     */
    public void increasePassangersQuantity(int amount) {
        if (amount <= 0) {
            return;
        }

        int availableSpace = maxPassengers - passengersQnt;

        if (availableSpace <= 0) {
            return;
        }

        passengersQnt += Math.min(amount, availableSpace);
    }
}
