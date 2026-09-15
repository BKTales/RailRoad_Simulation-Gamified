package pt.ipp.isep.dei.domain.position;

import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.Map;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a position in a 2D coordinate system within a map.
 * Ensures that the coordinates are within the bounds of the current map.
 * Implements Comparable to allow natural ordering of positions.
 */
public class Position implements Comparable<Position>, Serializable {
    private static final long serialVersionUID = 1L;
    private final int x;
    private final int y;
    private Simulator simulator = Simulator.getInstance();

    /**
     * Validates whether the given coordinates are within the bounds of the current map.
     *
     * @param x the x-coordinate to validate
     * @param y the y-coordinate to validate
     * @return true if the coordinates are valid, false otherwise
     */
    private boolean isValidPosition(int x, int y) {
        Map cMap = simulator.getScenarioRepository().getActiveScenario().getAttachedMap(); //must be protected
        if (x < 0 || y < 0 || x > cMap.getLength() || y > cMap.getLength())
            return false;
        return true;
    }

    /**
     * Constructs a Position with given x and y coordinates.
     * Throws IllegalArgumentException if coordinates are out of map bounds.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Position(int x, int y) {
        if (!isValidPosition(x, y))
            throw new IllegalArgumentException("Coordinates out of map bounds!\n");
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of this Position.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this Position.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Checks equality between this Position and another object.
     * Two positions are equal if they have the same coordinates.
     *
     * @param obj the object to compare with
     * @return true if obj is a Position with the same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            return this.compareTo(((Position) obj)) == 0;
        }
        return false;
    }

    /**
     * Compares this Position with another Position.
     * Ordering is determined first by x-coordinate, then by y-coordinate.
     *
     * @param pos the other Position to compare with
     * @return 0 if positions are equal, -1 if this is less, 1 if this is greater
     */
    @Override
    public int compareTo(Position pos) {
        if (this.x == pos.getX() && this.y == pos.getY())
            return 0;
        if (this.x < pos.getX() || this.y < pos.getY())
            return -1;
        return 1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a string representation of this Position.
     *
     * @return the string in format "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}
