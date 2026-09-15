package pt.ipp.isep.dei.domain.rails;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a collection of RailwayLine objects.
 * Provides methods to add, remove, and access railway lines.
 */
public class RailwayLines implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<RailwayLine> railwayLines;

    /**
     * Constructs an empty collection of RailwayLines.
     */
    public RailwayLines() {
        railwayLines = new ArrayList<>();
    }

    /**
     * Returns the number of railway lines in the collection.
     *
     * @return the count of railway lines
     */
    public int getManyRailwayLines() {
        return railwayLines.size();
    }

    /**
     * Adds a railway line to the collection.
     *
     * @param rL the RailwayLine to add
     */
    public void addRailwayline(RailwayLine rL) {
        railwayLines.add(rL);
    }

    /**
     * Removes a railway line from the collection.
     *
     * @param rL the RailwayLine to remove
     */
    public void removeRailwayline(RailwayLine rL) {
        railwayLines.remove(rL);
    }

    /**
     * Returns the list of all railway lines.
     *
     * @return the ArrayList of RailwayLine objects
     */
    public ArrayList<RailwayLine> getRailwayLines() {
        return railwayLines;
    }

    /**
     * Returns the railway line at the specified index.
     *
     * @param i the index of the railway line to return
     * @return the RailwayLine at index i
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public RailwayLine getRailwayLine(int i) {
        return railwayLines.get(i);
    }
}
