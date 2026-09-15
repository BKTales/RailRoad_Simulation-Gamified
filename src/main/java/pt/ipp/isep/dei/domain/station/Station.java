package pt.ipp.isep.dei.domain.station;

import pt.ipp.isep.dei.domain.position.InfluenceArea;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;

import java.io.Serializable;

/**
 * Represents a Station with a specific cardinal position and influence area.
 * The influence area is computed based on the station's cardinal position
 * (SE, SW, NE, NW) and map boundaries.
 */
public class Station extends StationType implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cardinalPosition;

    /**
     * Constructs a {@code Station} with the specified name, cardinal position, location,
     * and map boundaries.
     * This constructor initializes the station and validates the provided cardinal position.
     *
     * @param name             The station's name.
     * @param cardinalPosition The cardinal direction of the station ("SE", "SW", "NE", or "NW").
     * @param location         The geographic location of the station on the map.
     * @param maxHeight        The maximum height (y-axis boundary) of the map.
     * @param maxLength        The maximum width (x-axis boundary) of the map.
     * @throws IllegalArgumentException if {@code cardinalPosition} is null, empty, or not one of the allowed values.
     */
    public Station(String name, String cardinalPosition, Location location, int maxHeight, int maxLength) {
        super(name, location);
        if (cardinalPosition == null || cardinalPosition.isEmpty()) {
            throw new IllegalArgumentException("Cardinal position cannot be null or empty!");
        }
        this.cardinalPosition = cardinalPosition;
        initArea(maxHeight, maxLength);
    }

    /**
     * Computes the influence area positions relative to the station's center,
     * based on the cardinal position and map boundaries.
     *
     * @param centerX   The x-coordinate of the station's center.
     * @param centerY   The y-coordinate of the station's center.
     * @param maxHeight The maximum map height.
     * @param maxWidth  The maximum map width.
     * @return The InfluenceArea containing all valid positions.
     */
    private InfluenceArea addPositions(int centerX, int centerY, int maxHeight, int maxWidth) {
        InfluenceArea area = new InfluenceArea();
        int[][] deltas = getPositionsPerCardinalPosition();

        for (int i = 0; i < deltas.length; i++) {
            for (int j = 0; j < deltas[i].length; j += 2) {
                int x = centerX + deltas[i][j];
                int y = centerY + deltas[i][j + 1];
                if (x <= maxWidth && y <= maxHeight && x >= 0 && y >= 0) {
                    area.addPosition(new Position(x, y));
                }
            }
        }
        return area;
    }

    /**
     * Returns the relative positions to add around the station center,
     * according to the station's cardinal position.
     *
     * @return A 2D array of (x, y) offsets representing the influence area positions.
     * @throws IllegalArgumentException if the cardinal position is invalid.
     */
    private int[][] getPositionsPerCardinalPosition() {
        switch (cardinalPosition.toUpperCase()) {
            case "NW":
                return new int[][]{
                        {-2, 2}, {-1, 2}, {0, 2}, {1, 2},
                        {-2, 1}, {-1, 1}, {0, 1}, {1, 1},
                        {-2, 0}, {-1, 0},          {1, 0},
                        {-2, -1}, {-1, -1}, {0, -1}, {1, -1}
                };
            case "NE":
                return new int[][]{
                        {-2, 1}, {-1, 1}, {0, 1}, {1, 1},
                        {-2, 0}, {-1, 0},          {1, 0},
                        {-2, -1}, {-1, -1}, {0, -1}, {1, -1},
                        {-2, -2}, {-1, -2}, {0, -2}, {1, -2}
                };
            case "SW":
                return new int[][]{
                        {-1, 2}, {0, 2}, {1, 2}, {2, 2},
                        {-1, 1}, {0, 1}, {1, 1}, {2, 1},
                        {-1, 0},          {1, 0}, {2, 0},
                        {-1, -1}, {0, -1}, {1, -1}, {2, -1}
                };
            case "SE":
                return new int[][]{
                        {-1, 1}, {0, 1}, {1, 1}, {2, 1},
                        {-1, 0},          {1, 0}, {2, 0},
                        {-1, -1}, {0, -1}, {1, -1}, {2, -1},
                        {-1, -2}, {0, -2}, {1, -2}, {2, -2}
                };
            default:
                throw new IllegalArgumentException("Invalid quadrant. Use SE, SW, NE, or NW.");
        }
    }

    /**
     * Returns a string containing information about the depot.
     * <p>
     * This includes the base information from the superclass and the cardinal direction
     * of the depot's central position.
     *
     * @return A formatted string with detailed information about the depot.
     */
    @Override
    public String getInfo() {
        StringBuilder info = new StringBuilder(super.getInfo());
        info.append("\nCardinal Position of the Center: ").append(this.cardinalPosition).append("\n");

        return info.toString();
    }


    /**
     * Creates the influence area for this station based on its position and map boundaries.
     *
     * @param maxHeight Maximum height of the map.
     * @param maxWidth  Maximum width of the map.
     * @return The calculated InfluenceArea around the station.
     */
    @Override
    protected InfluenceArea createArea(int maxHeight, int maxWidth) {
        int centerX = super.getLocation().getPosition().getX();
        int centerY = super.getLocation().getPosition().getY();
        return addPositions(centerX, centerY, maxHeight, maxWidth);
    }

    /**
     * Returns a string representation of the station.
     *
     * @return A string identifying the station.
     */
    @Override
    public String toString() {
        return "Station " + super.toString();
    }

    /**
     * Compares this Station with another StationType.
     * Stations are equal if their locations are equal.
     *
     * @param stationType The StationType to compare against.
     * @return 0 if equal, 1 if this Station is greater, -1 if different StationType.
     */
    @Override
    public int compareTo(StationType stationType) {
        if (stationType instanceof Station) {
            if (this.getLocation().equals(stationType.getLocation()))
                return 0;
            return 1;
        }
        return -1;
    }
}
