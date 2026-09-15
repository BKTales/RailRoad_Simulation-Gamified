package pt.ipp.isep.dei.domain.scenario;

import java.io.Serializable;

/**
 * Enum representing different types of technology available in the system.
 */
public enum TechnologyType implements Serializable {
    DIESEL,
    ELECTRIC,
    STEAM;

    /**
     * Prints all TechnologyType values with their corresponding index starting at 1.
     * Useful for displaying technology options to users.
     */
    public static void printTec() {
        int index = 1;
        for (TechnologyType type : TechnologyType.values()) {
            System.out.println(index + " - " + type);
            index++;
        }
    }
}
