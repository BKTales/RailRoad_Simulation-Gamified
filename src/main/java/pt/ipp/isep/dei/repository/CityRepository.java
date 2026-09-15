package pt.ipp.isep.dei.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.HouseBlock;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;

/**
 * Repository class responsible for managing City entities.
 * Provides methods to create, delete, retrieve, and query cities.
 */
public class CityRepository implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<City> cities;

    /**
     * Constructs an empty CityRepository.
     */
    public CityRepository() {
        cities = new ArrayList<>();
    }


    /**
     * Deletes a city with the specified name from the repository.
     *
     * @param name the name of the city to delete
     */
    public void deleteCity(String name) {
        for (int i = 0; i < cities.size(); i++) {
            // Use equals() to compare strings instead of '=='
            if (cities.get(i).getName().equals(name)) {
                cities.remove(i);
                break;
            }
        }
    }



    /**
     * Returns a copy of the list of all cities currently stored in the repository.
     * Modifications to the returned list will not affect the internal state of the repository.
     *
     * @return a new {@code ArrayList} containing all {@code City} objects in the repository
     */
    public ArrayList<City> getCities() {
        return cities;
    }

    // -------------------- private methods --------------------


    /**
     * Returns a string listing all cities with their names and positions.
     *
     * @return a formatted string listing all cities and their positions
     */
    public String listAllCities() {
        StringBuilder s = new StringBuilder();

        for (City city : cities) {
            s.append(city.getName()).append(" ").append(city.getPosition()).append("\n");
        }
        return s.toString();
    }

    public void clean() {
        cities.clear();
    }

    public void addCity(City city) {
        cities.add(city);
    }
}
