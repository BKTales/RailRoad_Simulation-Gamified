package pt.ipp.isep.dei.us002;

import pt.ipp.isep.dei.domain.scenario.PortBehaviour;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.industries.Industry;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.utils.Utils;

import java.io.IOException;
import java.io.Serializable;

public class CreateIndustryController implements Serializable {
    private static final long serialVersionUID = 1L;
    private Simulation simulator;

    /**
     * Constructs a new CreateIndustryController and initializes the simulator.
     */
    public CreateIndustryController() {
        simulator = Simulation.getInstance();
    }

    // -------------------- Public Methods --------------------

    /**
     * Creates a new Port industry at the given coordinates for the specified scenario.
     *
     * @param x the x-coordinate of the location
     * @param y the y-coordinate of the location
     * @return an IndustryCreationResult indicating success or failure and the created Port industry if successful
     */
    public Industry createPort(int x, int y) throws IllegalArgumentException, IOException {
        Industry port = null;

        try {
            port = simulator.getCurrentScenario().createPort(simulator.getCurrentMap().createLocation(new Position(x, y)));
        } catch (NullPointerException e) {
            try {
                simulator.getCurrentMap().getlocationRepository().deleteLocation(new Position(x, y));
                Utils.displayReturnMapEditor("No port behaviour was previously defined!");  
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            try {
                Utils.displayReturnMapEditor(e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return (port);
    }

    /**
     * Creates a new Industry of the specified product type at the given coordinates.
     *
     * @param productIndex the product type of the industry to create
     * @param x the x-coordinate of the location
     * @param y the y-coordinate of the location
     * @return an IndustryCreationResult indicating success or failure and the created industry if successful
     */
    public Industry createIndustry(int productIndex, int x, int y) throws IllegalArgumentException {
        Industry industry = null;

        ProductType product = ProductType.values()[productIndex];
        industry = simulator.getCurrentScenario().createIndustry(product, simulator.getCurrentMap().createLocation(new Position(x, y)));
        return (industry);
    }

    /**
     * Retrieves the list of products available for industries.
     *
     * @return a string listing all available products
     */
    public String getProductList() {
        return simulator.getCurrentScenario().getAvailableProducts();
    }

    /**
     * Get the index of the selected product
     * @return product index in the list
     */
    public int getWhichProduct(String selectedProduct) {
        int index = 0;
        String listOfProducts[] = getProductList().split("\n");
        for (String product : listOfProducts) {
            if (product.equals(selectedProduct)) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
