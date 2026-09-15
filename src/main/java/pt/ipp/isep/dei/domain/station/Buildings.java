package pt.ipp.isep.dei.domain.station;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Represents a collection of buildings that have been acquired.
 * Provides methods to add, remove, check existence, and retrieve buildings.
 */
public class Buildings implements Serializable {
    private final ArrayList<Building> acquiredBuildings = new ArrayList<>();
    private static final long serialVersionUID = 1L;
    /**
     * Constructs a new Buildings object with an empty list of acquired buildings.
     */
    public Buildings() {}

    /**
     * Adds a building to the list of acquired buildings, applying upgrade rules and preventing duplicates.
     *
     * <p>This method performs the following logic:</p>
     * <ul>
     *   <li>Finds the corresponding {@link BuildingType} enum from the provided building name.</li>
     *   <li>If the building is a {@code TELEPHONE} and a {@code TELEGRAPH} already exists, the telegraph is removed automatically.</li>
     *   <li>If a large version of a building is being added (e.g., {@code CAFFEE_LARGE}, {@code HOTEL_LARGE}) and the corresponding small version exists,
     *       the small version is automatically removed.</li>
     *   <li>If the building type does not already exist in the list, it is added.</li>
     * </ul>
     *
     * @param building the name of the building to add (should match a {@link BuildingType} name).
     */
    public void addBuilding(String building) {
        BuildingType type = null;
        for (BuildingType build : BuildingType.values()) {
            if (build.name.equals(building)) {
                type = build;
            }
        }

        if (type != null) {
            // Automatic replacement: TELEGRAPH -> TELEPHONE
            if (type == BuildingType.TELEPHONE && existsBuildingType(BuildingType.TELEGRAPH)) {
                removeBuildingByType(BuildingType.TELEGRAPH);
            }

            // Automatic replacement: SMALL → LARGE versions
            if (type == BuildingType.CAFFEE_LARGE && existsBuildingType(BuildingType.CAFFEE_SMALL)) {
                removeBuildingByType(BuildingType.CAFFEE_SMALL);
            }
            if (type == BuildingType.HOTEL_LARGE && existsBuildingType(BuildingType.HOTEL_SMALL)) {
                removeBuildingByType(BuildingType.HOTEL_SMALL);
            }

            // Prevents adding duplicates
            if (!existsBuildingType(type)) {
                acquiredBuildings.add(new Building(type));
            }
        }
    }

    /**
     * Removes a building from the acquired buildings list.
     *
     * @param building the Building to remove
     */
    public void removeBuilding(Building building) {
        acquiredBuildings.remove(building);
    }

    /**
     * Removes buildings by their type from the acquired buildings list.
     *
     * @param type the BuildingType to remove
     */
    private void removeBuildingByType(BuildingType type) {
        acquiredBuildings.removeIf(b -> b.getBuildingType() == type);
    }

    /**
     * Checks if a building of the given type exists in the acquired buildings list.
     *
     * @param type the BuildingType to check for
     * @return true if such building exists, false otherwise
     */
    public boolean existsBuildingType(BuildingType type) {
        for (Building b : acquiredBuildings) {
            if (b.getBuildingType() == type) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a TELEGRAPH building exists among the acquired buildings.
     *
     * @return true if TELEGRAPH exists, false otherwise
     */
    public boolean existsTelegraph() {
        return existsBuildingType(BuildingType.TELEGRAPH);
    }

    /**
     * Checks if a small version of the given building exists.
     * Currently supports checking small versions for HOTEL_LARGE and CAFFEE_LARGE.
     *
     * @param requiredBuilding the Building to check small version for
     * @return true if the corresponding small version exists, false otherwise
     */
    public boolean existsSmallVersion(Building requiredBuilding) {
        BuildingType type = requiredBuilding.getBuildingType();
        for (Building building : acquiredBuildings) {
            if (type == BuildingType.HOTEL_LARGE) {
                if (building.getBuildingType() == BuildingType.HOTEL_SMALL) { return true; }
            } else if (type == BuildingType.CAFFEE_LARGE) {
                if (building.getBuildingType() == BuildingType.CAFFEE_SMALL) { return true; }
            }
        }
        return false;
    }

    /**
     * Checks if a small version of a building exists, given a required large version.
     *
     * <p>This method is used to verify whether the corresponding small version of a building
     * is already acquired, before attempting to add an upgrade (e.g., large version).</p>
     *
     * <p>Currently supports the following building pairs:
     * <ul>
     *     <li>{@code HOTEL_LARGE} → {@code HOTEL_SMALL}</li>
     *     <li>{@code CAFFEE_LARGE} → {@code CAFFEE_SMALL}</li>
     * </ul>
     * </p>
     *
     * @param requiredBuilding the name of the building (typically a large version) to check.
     * @return {@code true} if the corresponding small version of the building exists, {@code false} otherwise.
     */
    public boolean existsSmallVersion(String requiredBuilding) {
        BuildingType type = null;
        for (BuildingType build : BuildingType.values()) {
            if (build.name.equals(requiredBuilding)) {
                type = build;
            }
        }
        if (type != null) {
            for (Building building : acquiredBuildings) {
                if (type == BuildingType.HOTEL_LARGE) {
                    if (building.getBuildingType() == BuildingType.HOTEL_SMALL) { return true; }
                } else if (type == BuildingType.CAFFEE_LARGE) {
                    if (building.getBuildingType() == BuildingType.CAFFEE_SMALL) { return true; }
                }
            }
        }
        return false;
    }

    /**
     * Returns a copy of the list of acquired buildings.
     * Modifications to the returned list do not affect the internal list.
     *
     * @return a new ArrayList containing acquired Building objects
     */
    public ArrayList<Building> getAcquiredBuildings() {
        return new ArrayList<>(acquiredBuildings);
    }

    /**
     * Returns a list of buildings available for acquisition based on the current year and exclusion rules.
     *
     * <p>This method evaluates all building types and filters them according to the following criteria:
     * <ol>
     *     <li>The building's start year must be less than or equal to the current year.</li>
     *     <li>The building must not already be acquired.</li>
     *     <li>If a large version of a building is already acquired, its small version is excluded.</li>
     *     <li>If a telephone is already acquired (or available in the current year), the telegraph is excluded.</li>
     * </ol>
     * </p>
     *
     * @param currentYear the current simulation year used to determine availability.
     * @return a formatted string listing the available buildings with their index and details.
     */
    public String   getAvailableBuildings(int currentYear) {
        ArrayList<Building> available = new ArrayList<>();

        for (BuildingType type : BuildingType.values()) {
            // 1. Check year availability
            if (type.getStartYear() > currentYear) continue;

            // 2. Already acquired?
            if (existsBuildingType(type)) continue;

            // 3. Exclusion rules for small versions
            if ((type == BuildingType.CAFFEE_SMALL && existsBuildingType(BuildingType.CAFFEE_LARGE)) ||
                    (type == BuildingType.HOTEL_SMALL && existsBuildingType(BuildingType.HOTEL_LARGE))) {
                continue;
            }

            // 4. If TELEPHONE is owned, ignore TELEGRAPH
            if (type == BuildingType.TELEGRAPH && existsBuildingType(BuildingType.TELEPHONE)) continue;

            if (type == BuildingType.TELEGRAPH &&
                    (existsBuildingType(BuildingType.TELEPHONE) || currentYear >= BuildingType.TELEPHONE.getStartYear())) {
                continue;
            }

            // 5. Add new available building
            available.add(new Building(type));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < available.size(); i++) {
            Building b = available.get(i);
            sb.append("[").append(i).append("] - ").append(b.toString()).append("\n");
        }

        return sb.toString();
    }
}

