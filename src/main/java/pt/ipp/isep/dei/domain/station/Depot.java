package pt.ipp.isep.dei.domain.station;

import pt.ipp.isep.dei.domain.position.InfluenceArea;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;

import java.io.Serializable;

/**
 * Represents a Depot station type with a defined influence area on the map.
 * The depot has a fixed size area centered around its location.
 */
public class Depot extends StationType implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int SIZE = 3;

    /**
     * Constructs a {@code Depot} with the specified name, location, and map boundaries.
     * This constructor initializes the depot with a name and location, and sets up
     * its area of influence based on the given map dimensions.
     *
     * @param name       The name of the depot.
     * @param location   The location of the depot on the map.
     * @param maxHeight  The maximum height of the map (y-axis boundary) used to validate the influence area.
     * @param maxWidth   The maximum width of the map (x-axis boundary) used to validate the influence area.
     */
    public Depot(String name, Location location, int maxHeight, int maxWidth) {
        super(name, location);
        initArea(maxHeight, maxWidth);
    }

    /**
     * Creates the influence area of the depot, a square area centered at the depot's position.
     * The area includes all positions within a radius of SIZE/2, excluding the depot's own position.
     * Positions outside the map boundaries are excluded.
     *
     * @param maxHeight Maximum height of the map (y-axis boundary).
     * @param maxWidth  Maximum width of the map (x-axis boundary).
     * @return The InfluenceArea around the depot.
     */
    @Override
    protected InfluenceArea createArea(int maxHeight, int maxWidth) {
        InfluenceArea area = new InfluenceArea();
        int radius = SIZE / 2;
        int centerX = super.getLocation().getPosition().getX();
        int centerY = super.getLocation().getPosition().getY();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int x = centerX + dx;
                int y = centerY + dy;
                if (x == centerX && y == centerY) {
                    continue;
                }
                if (x <= maxWidth && y <= maxHeight && x >= 0 && y >= 0) {
                        area.addPosition(new Position(x, y));
                }
            }
        }
        return area;
    }

    /**
     * Returns a string representation of the depot.
     *
     * @return String identifying the depot.
     */
    @Override
    public String toString() {
        return "Depot " + super.toString();
    }

    /**
     * Compares this Depot with another StationType for ordering.
     * Depots are considered equal if their locations are equal.
     * Other StationTypes are ordered before this Depot.
     *
     * @param stationType The station to compare against.
     * @return 0 if equal, 1 if this Depot is greater, -1 if the other station is different type.
     */
    @Override
    public int compareTo(StationType stationType) {
        if (stationType instanceof Depot) {
            if (this.getLocation().equals(stationType.getLocation())) {
                return 0;
            }
            return 1;
        }
        return -1;
    }
}
