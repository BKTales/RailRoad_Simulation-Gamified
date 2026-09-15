package pt.ipp.isep.dei.USoutOfProgram.us014;

import pt.ipp.isep.dei.domain.scenario.TechnologyRestriction;
import pt.ipp.isep.dei.domain.scenario.TechnologyType;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;

import java.util.ArrayList;

public enum MaintainceType {
    ONLY_ELECTRIFIED("ONLY_ELECTRIFIED"),
    ALL("ALL");

    private static final TechnologyType ELECTRIC = TechnologyType.ELECTRIC;
    private final String name;

    MaintainceType(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the maintenance type.
     *
     * @return name as String
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the list of available maintenance types based on technology restrictions
     * and time restrictions.
     *
     * @param restriction the technology restrictions to consider
     * @param timeRestrictions the time restrictions (currently unused in method logic)
     * @return list of available maintenance types
     */
    public static ArrayList<MaintainceType> getAvailableMaintainceTypes(TechnologyRestriction restriction, TimeRestrictions timeRestrictions) {
        ArrayList<MaintainceType> availableMaintainceTypes = new ArrayList<MaintainceType>();
        // Always add ALL type as available
        availableMaintainceTypes.add(ALL);
        // If the restriction does not contain ELECTRIC technology, add ONLY_ELECTRIFIED type
        if (!restriction.getTechList().contains(ELECTRIC)) {
            availableMaintainceTypes.add(ONLY_ELECTRIFIED);
        }
        return availableMaintainceTypes;
    }

}
