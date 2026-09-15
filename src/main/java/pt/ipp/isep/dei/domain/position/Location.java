package pt.ipp.isep.dei.domain.position;

import java.io.Serializable;

/**
 * Represents a location defined by a Position.
 * Implements Comparable interface to allow comparison based on the underlying position.
 */
public class Location implements Comparable<Location>, Serializable {
    private static final long serialVersionUID = 1L;
    private Position position;

    /**
     * Constructs a Location given a Position.
     *
     * @param position the position defining this location
     */
    public Location(Position position) {
        this.position = position;
    }

    /**
     * Returns the Position of this Location.
     *
     * @return the position associated with this location
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Compares this Location to another object for equality.
     * Two Locations are equal if their underlying Positions are equal.
     *
     * @param obj the object to compare to
     * @return true if the other object is a Location with the same Position, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location other = (Location) obj;
            return this.position.equals(other.getPosition());
        }
        return false;
    }

    /**
     * Compares this Location to another Location based on their Positions.
     *
     * @param o the other Location to compare to
     * @return a negative integer, zero, or a positive integer as this Location is less than,
     * equal to, or greater than the specified Location.
     */
    @Override
    public int compareTo(Location o) {
        return this.position.compareTo(o.getPosition());
    }
}
