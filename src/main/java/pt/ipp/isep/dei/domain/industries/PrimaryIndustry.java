package pt.ipp.isep.dei.domain.industries;

import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.Location;

import java.io.Serializable;

/**
 * Represents a primary sector industry which extracts natural resources.
 * <p>
 * This class extends the generic Industry class with the sector set to PRIMARY.
 */
public class PrimaryIndustry extends Industry implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Constructs a PrimaryIndustry with the specified product, industry type, location,
     * production time, and maximum production capacity.
     *
     * @param product       The product produced by this industry.
     * @param type          The industry type.
     * @param location      The geographic location of the industry.
     * @param timeToProduce The time (in seconds) required to produce the product.
     * @param maxProduction The maximum production capacity of this industry.
     */
    public PrimaryIndustry(Product product, IndustryType type, Location location, int timeToProduce, int maxProduction) {
        super(product, type, location, timeToProduce, maxProduction);
        this.sector = IndustrySector.PRIMARY;
    }

    // =================== updates =================== //

    @Override
    public String updateProduction(long currentTime) {
        String toReturn = "";
        int manyProduced = 0;

        if (producedQuantity < maxProduction) {
            manyProduced = (int) (currentTime - productionStartTime) / timeToProduce;
            if (manyProduced == 1) {
                producedQuantity++;
                productionStartTime = currentTime;
                toReturn = type.toString() + " at " + location.getPosition().toString()
                        + " has " + producedQuantity + " of " + product.getProductType().productName + "\n";
            }
        }
        return toReturn;
    }

    /**
     * Returns a string representation of this primary industry.
     *
     * @return A string describing the primary industry.
     */
    @Override
    public String toString() {
        return "PrimaryIndustry " + super.toString();
    }
}
