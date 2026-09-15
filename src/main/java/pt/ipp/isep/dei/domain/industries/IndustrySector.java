package pt.ipp.isep.dei.domain.industries;

import java.io.Serializable;

/**
 * Enumeration representing the different sectors within the industry.
 * <p>
 * The sectors classify industries into primary, transforming, and mixed categories.
 * </p>
 */
public enum IndustrySector implements Serializable {

    /**
     * Represents the primary sector, typically involving raw material extraction.
     */
    PRIMARY {
        @Override
        public String toString() {
            return "PRIMARY";
        }
    },

    /**
     * Represents the transforming sector, which involves processing and manufacturing.
     */
    TRANSFORMING {
        @Override
        public String toString() {
            return "TRANSFORMING";
        }
    },

    /**
     * Represents a mixed sector, combining characteristics of both primary and transforming sectors.
     */
    MIXED {
        @Override
        public String toString() {
            return "MIXED";
        }
    }
}
