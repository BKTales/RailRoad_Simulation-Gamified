package pt.ipp.isep.dei.USoutOfProgram.us014;

import pt.ipp.isep.dei.domain.industries.Industry;
import pt.ipp.isep.dei.domain.rails.RailType;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.station.StationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GraphUtils {

    private GraphUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Checks if the graph formed by the given stations and railway lines is connected.
     * Uses depth-first search (DFS) starting from an arbitrary station to visit all reachable stations.
     *
     * @param stations array of station identifiers
     * @param lines 2D array of railway lines connecting stations
     * @return true if all stations are reachable (connected graph), false otherwise
     */
    public static boolean isConnected(String[] stations, String[][] lines) {
        if (stations == null || stations.length == 0 || lines == null || lines.length == 0) {
            return false;
        }

        // Map station to index
        Map<String, Integer> stationIndex = getStationIndexMap(lines);
        boolean[] visited = new boolean[stationIndex.size()];
        String[] keys = stationIndex.keySet().toArray(new String[0]);
        String startStation = keys[0];

        dfsIterative(startStation, lines, stationIndex, visited);

        // Check if all stations were visited
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    /**
     * Recursive helper method to perform depth-first search (DFS) on the graph.
     *
     * @param start current station being visited
     * @param lines railway lines representing edges
     * @param stationIndex mapping of station names to their indices
     * @param visited boolean array tracking visited stations
     */
    private static void dfsIterative(String start, String[][] lines, Map<String, Integer> stationIndex, boolean[] visited) {
        int n = stationIndex.size();
        String[] stack = new String[n];
        int top = 0;

        stack[top++] = start;
        visited[stationIndex.get(start)] = true;

        while (top > 0) {
            String current = stack[--top];
            //int currentIndex = stationIndex.get(current);

            for (String[] line : lines) {
                if (line == null || line.length < 2) continue;

                String s1 = line[0];
                String s2 = line[1];
                if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) continue;

                String neighbor = null;
                if (s1.equals(current)) neighbor = s2;
                else if (s2.equals(current)) neighbor = s1;

                if (neighbor != null) {
                    int neighborIndex = stationIndex.get(neighbor);
                    if (!visited[neighborIndex]) {
                        visited[neighborIndex] = true;
                        stack[top++] = neighbor;
                    }
                }
            }
        }
    }


    /**
     * Creates a map from station names to unique indices, used for adjacency matrix construction.
     *
     * @param lines 2D array of railway lines connecting stations
     * @return map from station names to their assigned index
     */
    private static Map<String, Integer> getStationIndexMap(String[][] lines) {
        Map<String, Integer> map = new HashMap<>();
        int index = 0;
        for (String[] line : lines) {
            if (line.length < 2) continue;

            String s1 = line[0];
            String s2 = line[1];

            if (s1 != null && !s1.isEmpty() && !map.containsKey(s1)) {
                map.put(s1, index++);
            }
            if (s2 != null && !s2.isEmpty() && !map.containsKey(s2)) {
                map.put(s2, index++);
            }
        }
        return map;
    }

    /**
     * Builds an adjacency matrix representing connections between stations.
     * Matrix entries are 1 if there is a connection between stations, 0 otherwise.
     *
     * @param railwayLines 2D array of railway lines (edges)
     * @param indexMap mapping of station names to their indices in the matrix
     * @return adjacency matrix as a 2D integer array
     */
    public static int[][] createAdjacencyMatrix(String[][] railwayLines,
                                                Map<String, Integer> indexMap) {
        int size = indexMap.size();
        int[][] adjacencyMatrix = new int[size][size];

        for (String[] railwayLine : railwayLines) {
            if (railwayLine.length < 2) {
                continue;
            }

            String station1 = railwayLine[0];
            String station2 = railwayLine[1];

            Integer s1 = indexMap.get(station1);
            Integer s2 = indexMap.get(station2);

            if (s1 != null && s2 != null) {
                // Permitir múltiplas ligações (multigrafo)
                adjacencyMatrix[s1][s2]++;
                adjacencyMatrix[s2][s1]++;
            }
        }

        return adjacencyMatrix;
    }

}
