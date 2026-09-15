package pt.ipp.isep.dei.USoutOfProgram.us014;

import org.graphstream.graph.implementations.MultiGraph;
import pt.ipp.isep.dei.USoutOfProgram.us013.ReadCsvs;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RouteMantainanceController {
    private Map<String, Integer> degrees;
    private Map<String, Integer> oddCount;
    private ArrayList<String> validStartStations;
    private MultiGraph graph;
    private File stationFile;
    private File lineFile;

    // -------------------- public methods --------------------

    /**
     * Filters railway lines according to the maintenance type.
     *
     * @param allLines all railway lines
     * @param maintenanceType type of maintenance (0 = ALL, 1 = only electrified)
     * @return filtered lines matching the maintenance type
     */
    public String[][] filterLines(String[][] allLines, int maintenanceType) {
        if (maintenanceType == 0) { // 0 represents "ALL"
            return allLines;
        }

        // Filter according to the type (1 for electrified, 0 for all, etc.)
        List<String[]> filteredList = new ArrayList<>();
        for (String[] line : allLines) {
            if (line.length >= 3) { // Safety check
                int lineType = Integer.parseInt(line[2]);
                    if (lineType == 1) { // Only electrified
                        filteredList.add(line);
                    }

                // Add other cases if needed
            }
        }

        // Convert back to String[][]
        return filteredList.toArray(new String[0][]);
    }

    /**
     * Checks if the graph formed by the given stations and lines is connected.
     *
     * @param stations array of station names
     * @param lines railway lines connecting stations
     * @return true if connected, false otherwise
     */
    public boolean isGraphConnected(String[] stations, String[][] lines) {
        if (!GraphUtils.isConnected(stations, lines)){
            return false;
        }
        return true;
    }

    /**
     * Calculates the degrees of stations based on filtered railway lines.
     *
     * @param filteredLines filtered railway lines
     * @param stations array of station names
     * @return map of station names to their degree counts
     */
    public Map<String, Integer> getStationDegrees(String[][] filteredLines,String[] stations) {
        this.degrees = FleuryAlgorithm.calculateVertexDegrees(filteredLines,stations);
        return this.degrees;
    }

    /**
     * Retrieves the vertices with odd degree counts.
     *
     * @param degrees map of stations to their degree counts
     * @return map of stations with odd degrees
     */
    public Map<String, Integer> getOddDegreeVertices(Map<String, Integer> degrees) {
        this.oddCount = FleuryAlgorithm.countOddDegreeVertices(degrees);
        return this.oddCount;
    }

    /**
     * Determines the valid start stations for the route.
     *
     * @param stations array of station names
     * @return list of valid start stations
     */
    public ArrayList<String> getValidStartStations(String[] stations) {
        this.validStartStations = FleuryAlgorithm.getValidStations(oddCount,degrees,stations);
        GraphBuilder.highlightStations(graph, validStartStations);
        return this.validStartStations;
    }

    /**
     * Builds the graph from stations and railway lines.
     *
     * @param stations array of station names
     * @param lines railway lines connecting stations
     * @return constructed SingleGraph instance
     */
    public MultiGraph buildGraph(String[] stations, String[][] lines) {
        this.graph = GraphBuilder.buildGraph(stations,lines);
        return graph;
    }

    /**
     * Executes Fleury's algorithm to find a Eulerian path starting from the chosen station.
     *
     * @param choice index of chosen start station in validStartStations
     * @param choosenStart the chosen start station name
     * @param filteredLines filtered railway lines
     * @param stations array of station names
     * @return list representing the Eulerian path
     */
    public ArrayList<String> executeFleuryAlgorithm(int choice, String choosenStart, String[][] filteredLines,String[] stations) {
        GraphBuilder.unhighlightOtherStations(graph, validStartStations, choice);
        Map<String, Integer> indexMap = getIndexMap(stations);
        int[][] adjacencyMatrix = createAdjacencyMatrix(indexMap,filteredLines);
        ArrayList<String> path = new ArrayList<>();
        path.add(choosenStart);
        String[][] lineClone = cloneArray(filteredLines);
        FleuryAlgorithm.execute(graph, path, adjacencyMatrix, indexMap,
                lineClone, new HashMap<>(degrees));
        return path;
    }

    /**
     * Returns the station CSV file for the given scenario.
     *
     * @param whichScenario scenario number
     * @return File instance pointing to stations CSV
     * @throws IOException if file not found or inaccessible
     */
    public File getStationFile(int whichScenario) throws IOException {
        stationFile = new File("src/csvs/scenario" + whichScenario + "_stations.csv");
        return (stationFile);
    }

    /**
     * Returns the lines CSV file for the given scenario.
     *
     * @param whichScenario scenario number
     * @return File instance pointing to lines CSV
     * @throws IOException if file not found or inaccessible
     */
    public File getLineFile(int whichScenario) throws IOException{
        lineFile = new File("src/csvs/scenario" + whichScenario + "_lines.csv");
        return (lineFile);
    }

    /**
     * Reads all stations from the station CSV file.
     *
     * @return array of station names
     * @throws IllegalArgumentException if CSV data is invalid
     * @throws IOException if file reading fails
     */
    public String[] getStations() throws IllegalArgumentException, IOException {
        return (ReadCsvs.getAllStations(stationFile));
    }

    /**
     * Reads all lines from the lines CSV file.
     *
     * @return 2D array of lines data
     * @throws IllegalArgumentException if CSV data is invalid
     * @throws IOException if file reading fails
     */
    public String[][] getLines() throws IllegalArgumentException, IOException {
        return ReadCsvs.readCSVTo2DArray(lineFile);
    }

    // -------------------- private methods --------------------

    /**
     * Creates a deep copy of a 2D String array.
     *
     * @param array original 2D String array
     * @return cloned 2D String array
     */
    private String[][] cloneArray(String[][] array) {
        String[][] copy = new String[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                copy[i][j] = array[i][j];
            }
        }
        return copy;
    }


    /**
     * Creates adjacency matrix from filtered lines and station index map.
     *
     * @param indexMap map of stations to indices
     * @param filteredLines filtered railway lines
     * @return adjacency matrix representing connections
     */
    private int[][] createAdjacencyMatrix(Map<String, Integer> indexMap, String[][] filteredLines) {
        return GraphUtils.createAdjacencyMatrix(filteredLines, indexMap);
    }

    /**
     * Creates an index map for the given stations.
     *
     * @param stations array of station names
     * @return map of station names to indices
     */
    private Map<String, Integer> getIndexMap(String[] stations) {
        return FleuryAlgorithm.createIndexMap(stations);
    }
}
