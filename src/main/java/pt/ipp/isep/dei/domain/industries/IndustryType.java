package pt.ipp.isep.dei.domain.industries;

import java.io.Serializable;

/**
 * Enumeration representing the specific types of industries.
 * <p>
 * Each industry type is associated with an {@link IndustrySector} indicating its sector classification.
 * </p>
 */
public enum IndustryType implements Serializable {

    /**
     * Farm industry, part of the primary sector.
     */
    FARM(IndustrySector.PRIMARY),

    /**
     * Mine industry, part of the primary sector.
     */
    MINE(IndustrySector.PRIMARY),

    /**
     * Bakery industry, part of the transforming sector.
     */
    BAKERY(IndustrySector.TRANSFORMING),

    /**
     * Steel mill industry, part of the transforming sector.
     */
    STEEL_MILL(IndustrySector.TRANSFORMING),

    /**
     * Textile industry, part of the transforming sector.
     */
    TEXTILE(IndustrySector.TRANSFORMING),

    /**
     * Automobile industry, part of the transforming sector.
     */
    AUTOMOBILE(IndustrySector.TRANSFORMING),

    /**
     * Port industry, part of the mixed sector.
     */
    PORT(IndustrySector.MIXED);

    private IndustrySector sector;

    /**
     * Constructs an IndustryType with the specified sector.
     *
     * @param sector the sector to which this industry type belongs
     */
    IndustryType(IndustrySector sector) {
        this.sector = sector;
    }

    /**
     * Returns the sector classification of this industry type.
     *
     * @return the associated {@link IndustrySector}
     */
    public IndustrySector getSector() {
        return sector;
    }
}
