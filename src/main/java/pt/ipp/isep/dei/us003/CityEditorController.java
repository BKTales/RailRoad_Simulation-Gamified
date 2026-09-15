package pt.ipp.isep.dei.us003;

import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.HouseBlock;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.repository.CityRepository;
import pt.ipp.isep.dei.repository.LocationRepository;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.io.Serializable;
import java.util.List;

public class CityEditorController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static List<HouseBlock> houseBlocksList;
    private static Simulation simulation = Simulation.getInstance(Simulator.getInstance().getScenarioRepository().getActiveScenario());
    private static LocationRepository locationRepo;

    /**
     * Constructs a CityEditorController for a city with the given name and position.
     *
     */
    public CityEditorController() {
        locationRepo = simulation.getLocationRepository();
    }

    /**
     * Creates a new HouseBlock at the specified position and adds it to the houseBlocksList.
     *
     * @param city the city to which the house block belongs
     * @param positionX the x-coordinate of the house block's position
     * @param positionY the y-coordinate of the house block's position
     */
    public void createCityBlock(City city, int positionX, int positionY) {
        city.createHouseBlock(positionX, positionY);
    }


    /**
     * Creates a city with the given number of passengers, mail, and consumed product.
     * Uses the stored name, position, and houseBlocksList.
     *
     * @param cityName     the name of the city
     * @param position the position of the city
     * @param numberOfPassengers the number of passengers in the city
     * @param numberOfMail       the amount of mail in the city
     * @param consumedProduct    the product consumed by the city
     */
    public City createCity(String cityName, Position position, int numberOfPassengers, int numberOfMail, ProductType consumedProduct) {
        Location cityLocation = simulation.getCurrentMap().createLocation(position);
        return simulation.getCurrentMap().createCity(cityName, cityLocation, numberOfPassengers, numberOfMail, consumedProduct);
    }

    /**
     * Deletes house blocks corresponding to the given list of positions by deleting
     * their locations from the location repository.
     *
     * @param houseBlocksListFailedPosition the list of positions to delete
     */
    public void deleteFailedHouseBlocks(List<Position> houseBlocksListFailedPosition){
        for (int i = 0; i < houseBlocksListFailedPosition.size(); i++) {
            locationRepo.deleteLocation(houseBlocksListFailedPosition.get(i));
        }
    }

    /**
     * Initializes the houseBlocksList as a new empty ArrayList.
     */
    public void createHouseBlockList(City city){
        city.createHouseBlockList();
    }

    /**
     * Prints the list of house blocks to the standard output.
     */
    public void printHouseBlockList(){
        for (int i = 0; i < houseBlocksList.size(); i++) {
            //System.out.println(houseBlocksList.get(i));
        }
    }

    /**
     * Returns the ProductType enum constant matching the provided name (case-insensitive).
     * Returns null if no matching product is found.
     *
     * @param name the name of the product
     * @return the matching ProductType or null if not found
     */
    public ProductType getProductByName(String name) {
        if (ProductType.valueOf(name.toUpperCase()) != null) {
            return ProductType.valueOf(name.toUpperCase());
        } else {
            return null;
        }
    }

    public String getAvailableProducts() {

        StringBuilder productList = new StringBuilder();
        for (ProductType type : ProductType.values()) {
            productList.append(type.name()).append("\n");
        }
        return productList.toString();
    }
}

/*
    1- Create HouseBlock:
        - array: HouseBlock
        - saida: HouseBlock
    2- Create City:
        - entrada: nome Location, HOuseBlock
 */
