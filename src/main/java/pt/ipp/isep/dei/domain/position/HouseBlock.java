package pt.ipp.isep.dei.domain.position;

import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.io.Serializable;

/**
 * Represents a block of houses within a location on the map.
 * <p>
 * Each HouseBlock has a position (x,y) within a Location.
 * It enforces constraints on the number of blocks and the validity of positions based on the current map dimensions.
 */
public class HouseBlock implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Maximum allowed number of HouseBlocks.
     */
    private static final int MAX_BLOCKS = 1000;
    private Position position;
    private Location location;
    private Simulation simulation = Simulation.getInstance();
    private static Simulator simulator = Simulator.getInstance();

    /**
     * Constructs a HouseBlock associated with a specific Location.
     *
     * @param location the Location object where this HouseBlock belongs
     */
    public HouseBlock(Location location) {
        this.location = location;
    }

    /**
     * Checks if the given count of HouseBlocks is valid.
     * Valid counts are positive, less than or equal to MAX_BLOCKS,
     * and less than the total area of the current map.
     *
     * @param count the number of HouseBlocks to validate
     * @return true if the count is valid, false otherwise
     */
    public static boolean isValid(int count) {
        return count > 0 && count <= MAX_BLOCKS && simulator.getScenarioRepository().getActiveScenario().getAttachedMap().getMapArea() > count;
    }

    /**
     * Sets the position of this HouseBlock within the current map.
     * The position is only set if it is within the boundaries of the current map.
     *
     * @param posX x-coordinate within the map
     * @param posY y-coordinate within the map
     * @throws IndexOutOfBoundsException if the position is outside the current map boundaries
     */
    public void addPosition(int posX, int posY) {
        if (simulation.getCurrentMap().getHeight() > posY && simulation.getCurrentMap().getLength() > posX) {
            this.position = new Position(posX, posY);
        } else {
            throw new IndexOutOfBoundsException("Invalid position!");
        }
    }

    /**
     * Returns the position of this HouseBlock.
     *
     * @return the Position object representing the coordinates of this HouseBlock
     */
    public Position getPosition() {
        return location.getPosition();
    }

    /**
     * Returns a string representation of this HouseBlock,
     * showing the location's position.
     *
     * @return string describing the HouseBlock's location position
     */
    @Override
    public String toString() {
        return "HouseBlock";
    }

    public Location getLocation() {
        return this.location;
    }
}