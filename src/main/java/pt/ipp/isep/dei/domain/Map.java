package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.repository.LocationRepository;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.io.Serializable;

/**
 * Represents a map with a specified name, length, and height.
 * The map defines a rectangular area with its dimensions.
 */
public class Map implements Serializable {
    private static final long serialVersionUID = 1L;
    private int length;
    private int height;
    private int scale;
    private String name;

    private LocationRepository locationRepository;

    // going to be created in the map

    // private void CreateInfluenceArea()

    // ------------ New Methods ------------ //

    /**
     * Creates and adds a new {@link Location} at the given {@link Position}, if the position is unoccupied.
     *
     * @param position the position where the new location should be created
     * @return the newly created {@link Location}
     * @throws IllegalArgumentException if the position is already occupied
     */
    public Location createLocation(Position position) throws IllegalArgumentException {
        if (!locationRepository.isEmptyLocation(position)) {
            throw new IllegalArgumentException("The given position already exists!");
        }
        Location l = new Location(position);
        locationRepository.addLocation(l);
        return l;
    }

    /**
     * Creates a new City with the specified parameters and adds it to the repository.
     *
     * @param name           the name of the city
     * @param location       the geographical location of the city
     * @param maxPassengers  the maximum number of passengers (currently unused)
     * @param maxMail        the maximum amount of mail (currently unused)
     * @param consumedProduct the product type consumed in the city (currently unused)
     * @return the newly created City instance
     */
    public City createCity(String name, Location location, int maxPassengers, int maxMail, ProductType consumedProduct) {
        City city = new City(name, location, maxPassengers, maxMail, consumedProduct);
        //Simulator.getInstance().getMapRepository().().getCityRepository().getCities().add(city);
        Simulation.getInstance().getCityRepository().addCity(city);
        return city;
    }

    // ------------ New Methods ------------ //

    /**
     * Constructs a Map instance with the given name, length, and height.
     *
     * @param name   the name of the map
     * @param length the length of the map (horizontal dimension)
     * @param height the height of the map (vertical dimension)
     */
    public Map(String name, int length, int height, int scale) {
        this.length = length;
        this.height = height;
        this.name = name;
        this.scale = scale;
        locationRepository = new LocationRepository();
    }

    /**
     * Returns the length of the map.
     *
     * @return the length of the map
     */
    public int getLength() { return length; }

    /**
     * Returns the height of the map.
     *
     * @return the height of the map
     */
    public int getHeight() { return height; }

    /**
     * Returns the name of the map.
     *
     * @return the name of the map
     */
    public String getName() { return name; }

    /**
     * Returns the map scale
     * @return map scale
     */
    public int getScale() { return scale; }

    /**
     * Sets the name of the map.
     *
     * @param name the new name of the map
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the total area of the map (length multiplied by height).
     *
     * @return the area of the map
     */
    public int getMapArea() {
        return length * height;
    }

    /**
     * Returns a string representation of the map including its name and dimensions.
     *
     * @return string representation of the map
     */
    public String toString() {
        return name + " (" + length + ", " + height + ")";
    }

    public LocationRepository getlocationRepository() {
        return locationRepository;
    }

}
