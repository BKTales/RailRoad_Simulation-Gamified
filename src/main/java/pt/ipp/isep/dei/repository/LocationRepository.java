package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.position.Location;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The {@code LocationRepository} class manages a collection of {@link Location} objects,
 * allowing creation, retrieval, and deletion of locations based on their {@link Position}.
 */
public class LocationRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<Location> m_locations = new ArrayList<>();

    //========== Manipulation functions ==========//


    /**
     * Deletes the {@link Location} associated with the specified {@link Position}.
     * <p><b>Note:</b> Only the first match is removed.</p>
     *
     * @param pos the position of the location to delete
     */
    public void deleteLocation(Position pos) {
        for (Location l : m_locations) {
            if (l.getPosition().equals(pos)) {
                m_locations.remove(l);
                break;
            }
        }
    }
    public ArrayList<Location> getLocations(){
        return m_locations;
    }

    //========== get functions ==========//

    /**
     * Retrieves a {@link Location} based on its (x, y) coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the {@link Location} at the given coordinates, or {@code null} if none exists
     */
    public Location getLocation(int x, int y) {
        Position pos = new Position(x, y);
        for (Location l : m_locations) {
            if (l.getPosition().equals(pos))
                return l;
        }
        return null;
    }

    /**
     * Retrieves a {@link Location} that matches the specified {@link Position}.
     *
     * @param p the position to search for
     * @return the {@link Location} at the specified position, or {@code null} if not found
     */
    public Location getLocation(Position p) {
        for (Location l : m_locations) {
            if (l.getPosition().equals(p))
                return l;
        }
        return null;
    }

    //========== Utils functions ==========//

    /**
     * Checks whether a given {@link Position} is not already occupied by a {@link Location}.
     *
     * @param position the position to check
     * @return {@code true} if the position is unoccupied; {@code false} otherwise
     */
    public boolean isEmptyLocation(Position position) {
        for (Location l : m_locations) {
            if (l.getPosition().equals(position))
                return false;
        }
        return true;
    }

    public void addLocation(Location location) {
        m_locations.add(location);
    }

    public void clean() {
        m_locations.clear();
    }
    
}
