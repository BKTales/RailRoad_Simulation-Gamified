package pt.ipp.isep.dei.USoutOfProgram.us029;

/**
 * Represents a dataset with station names and connection lines.
 */
public class DataSet {
    private final String name;
    private final String[] stations;
    private final String[][] lines;

    public DataSet(String name, String[] stations, String[][] lines) {
        this.name = name;
        this.stations = stations;
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public String[] getStations() {
        return stations;
    }

    public String[][] getLines() {
        return lines;
    }
}
