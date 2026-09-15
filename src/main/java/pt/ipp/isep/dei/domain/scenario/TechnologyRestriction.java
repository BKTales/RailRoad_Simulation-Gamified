package pt.ipp.isep.dei.domain.scenario;

import pt.ipp.isep.dei.domain.ProductType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restriction on available technologies for a scenario.
 * Holds a list of technology types that are restricted or unavailable.
 */
public class TechnologyRestriction implements Serializable {
    private static final long serialVersionUID = 1L;
    List<TechnologyType> techList;

    private final List<String> unavailableProducts = new ArrayList<>();


    /**
     * Constructs a TechnologyRestriction with a given list of restricted technology types.
     *
     * @param techList the list of TechnologyType instances that are restricted
     */
    public TechnologyRestriction(List<TechnologyType> techList){
        this.techList = techList;
    }

    public TechnologyRestriction(){
        List<TechnologyType> techList = new ArrayList<>();
    }

    /**
     * Returns the list of restricted technology types.
     *
     * @return list of TechnologyType objects that are restricted
     */
    public List<TechnologyType> getTechList() {
        return techList;
    }


    public void markAsUnavailable(String productName) {
        if (!unavailableProducts.contains(productName)) {
            unavailableProducts.add(productName);
        }
    }

    public boolean isProductAvailable(String productName) {
        return !unavailableProducts.contains(productName);
    }

    /**
     * function check if a given productType is inside the unavailable list
     * @param p product type wanted to be checked
     * @return (true - is available) (false - is unavailable)
     */
    public boolean isProductAvailable(ProductType p){
        if (unavailableProducts.contains(p.productName))
            return false;
        return true;
    }

    public List<String> getUnavailableProducts() {
        return new ArrayList<>(unavailableProducts);
    }
}
