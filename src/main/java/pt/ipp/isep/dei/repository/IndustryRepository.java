package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.scenario.PortBehaviour;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.industries.*;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.position.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code IndustryRepository} class manages a collection of {@link Industry} instances,
 * providing methods for creation, retrieval, deletion, and listing of industries.
 * It also includes utilities for determining industry types and listing products.
 */
public class IndustryRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Industry> industries = new ArrayList<>();

    //========== Manipulation functions ==========//


    /**
     * Returns a list of all industries.
     *
     * @return an {@link ArrayList} containing all industries
     */
    public ArrayList<Industry> getIndustries() {
        return (ArrayList<Industry>) industries;
    }

    /**
     * Deletes an industry located at the specified {@link Location}.
     *
     * @param l the location of the industry to delete
     */
    public void deleteIndustry(Position l) {
        for (Industry i : industries) {
            if (i.getLocation().getPosition().equals(l)) {
                industries.remove(i);
                break;
            }
        }
    }

    /**
     * Returns a status report of all active ports.
     *
     * @return a string representing the production details of all ports, separated by new lines
     */
    public String getActivePortsStatus() {
        return industries.stream()
                .filter(i -> i.getType() == IndustryType.PORT)
                .map(i -> (Port) i) // Safe cast since we filtered
                .map(Port::getProductionDetails)
                .collect(Collectors.joining("\n"));
    }


    //========== Get functions ==========//

    /**
     * Retrieves the {@link Industry} at the specified location.
     *
     * @param l the location to search
     * @return the {@link Industry} at the given location, or {@code null} if not found
     */
    public Industry getIndustry(Position l) {
        for (Industry i : industries) {
            if (i.getLocation().getPosition().equals(l))
                return i;
        }
        return null;
    }

    /**
     * Retrieves the industry at the given index.
     *
     * @param index the index of the industry
     * @return the {@link Industry} at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Industry getIndustry(int index) {
        return industries.get(index);
    }


    //========== Utility functions ==========//


    /**
     * Returns a formatted string listing all industries in the repository.
     *
     * @return a string representation of all industries
     */
    public String listIndustries() {
        StringBuilder s = new StringBuilder("---------------------\n");
        for (Industry i : industries) {
            s.append(i.toString());
            s.append("---------------------\n");
        }
        return s.toString();
    }

    /**
     * Determines the {@link IndustryType} for a given {@link ProductType}.
     *
     * @param product the product to evaluate
     * @return the corresponding {@link IndustryType}
     */
    public IndustryType getIndustryType(ProductType product) {
        if (product == ProductType.VEGETABLES)
            return IndustryType.FARM;
        else if (product == ProductType.GRAINS)
            return IndustryType.FARM;
        else if (product == ProductType.COFFEE)
            return IndustryType.FARM;
        else if (product == ProductType.RUBBER)
            return IndustryType.FARM;
        else if (product == ProductType.WOOL)
            return IndustryType.FARM;
        else if (product == ProductType.IRON)
            return IndustryType.MINE;
        else if (product == ProductType.COAL)
            return IndustryType.MINE;
        else if (product == ProductType.BREAD)
            return IndustryType.BAKERY;
        else if (product == ProductType.CAR)
            return IndustryType.AUTOMOBILE;
        else if (product == ProductType.CLOTHING)
            return IndustryType.TEXTILE;
        else
            return IndustryType.STEEL_MILL;
    }

//    /**
//     * Returns a new list containing all industries.
//     *
//     * @return a {@link List} of industries
//     */
//    public List<Industry> getAllIndustries() {
//        return new ArrayList<>(industries);
//    }
    public void addPort(Port port) {
        industries.add(port);
    }

    public void addIndustry(Industry industry) {
        industries.add(industry);
    }

    public void clean() {
        industries.clear();
    }
}


