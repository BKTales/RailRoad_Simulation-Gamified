package pt.ipp.isep.dei.domain.position;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents an area of influence defined by a collection of unique positions.
 */
public class InfluenceArea implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Position> m_Positions = new ArrayList<Position>();

    /**
     * Returns the list of positions that define this influence area.
     *
     * @return an ArrayList containing all positions in this influence area
     */
    public ArrayList<Position> getArea() {
        return m_Positions;
    }

    /**
     * Adds a new position to the influence area if it is not already present.
     * Prevents duplicate positions.
     *
     * @param position the Position to be added
     * @return true if the position was successfully added, false if it was already contained
     */
    public boolean addPosition(Position position) {
        // checking if the position is already inside the array! Prevent double reference!
        if (m_Positions.contains(position))
            return false;
        return m_Positions.add(position);
    }
}
