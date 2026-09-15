package pt.ipp.isep.dei.domain.station;

import pt.ipp.isep.dei.domain.position.InfluenceArea;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;

import java.io.Serializable;

/**
 * Represents a Terminal station type.
 * The Terminal has a fixed influence area size.
 */
public class Terminal extends StationType implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int SIZE = 5;

    /**
     * Constructs a {@code Terminal} station with the specified name, location, and map boundaries.
     * This constructor initializes the terminal with a name and location, and sets up
     * its area of influence based on the given map dimensions.
     *
     * @param name      The name of the terminal.
     * @param location  The geographic location of the terminal on the map.
     * @param maxHeight The maximum height (y-axis boundary) for the influence area.
     * @param maxWidth  The maximum width (x-axis boundary) for the influence area.
     */
    public Terminal(String name, Location location, int maxHeight, int maxWidth) {
        super(name, location);
        initArea(maxHeight, maxWidth);
    }

    /**
     * Creates the influence area for this terminal.
     * The influence area is a square of fixed size centered on the terminal's position,
     * excluding the terminal's own position.
     *
     * @param maxHeight maximum height boundary for the influence area
     * @param maxWidth  maximum width boundary for the influence area
     * @return the influence area around this terminal
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
     * Returns a string representation of the terminal.
     *
     * @return string including "Terminal" label and base class info
     */
    @Override
    public String toString() {
        return "Terminal " + super.toString();
    }

    /**
     * Compares this Terminal to another StationType.
     * Terminals are equal if they share the same location.
     * Otherwise, Terminal is considered greater than other StationTypes.
     *
     * @param stationType the station to compare with
     * @return 0 if locations are equal, 1 if other is Terminal but different location, -1 otherwise
     */
    @Override
    public int compareTo(StationType stationType) {
        if (stationType instanceof Terminal) {
            if (this.getLocation().equals(stationType.getLocation())) {
                return 0;
            }
            return 1;
        }
        return -1;
    }
}
