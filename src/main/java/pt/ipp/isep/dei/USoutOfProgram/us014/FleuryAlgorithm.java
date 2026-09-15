package pt.ipp.isep.dei.USoutOfProgram.us014;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Implements Fleury's Algorithm to find an Eulerian path or circuit in a graph.
 */
public class FleuryAlgorithm {

    /**
     * Executes Fleury's Algorithm on the given graph to find an Eulerian path or circuit.
     * It recursively traverses edges, updates the path, and modifies the graph accordingly.
     *
     * @param graph            the GraphStream MultiGraph representing the railway network
     * @param path             the current Eulerian path (list of station identifiers)
     * @param adjacencyMatrix  the adjacency matrix representing edges between stations
     * @param indexMap         mapping of station identifiers to indices in the adjacency matrix
     * @param lines            array of railway line pairs (edges) connecting stations
     * @param degrees          map of stations to their current degree (number of connected edges)
     */
    public static void execute(MultiGraph graph, ArrayList<String> path, int[][] adjacencyMatrix,
                                        Map<String, Integer> indexMap, String[][] lines,
                                        Map<String, Integer> degrees) {
        while (countTotalEdges(adjacencyMatrix) > 0) {
            String current = path.get(path.size() - 1);
            int i = indexMap.get(current);

            Node node = graph.getNode(current);
            if (node != null) {
                node.setAttribute("ui.class", "atual"); // Destacar o nó atual
            }

            String[] toRemoveLater = findAndProcessNextLine(path, current, i, adjacencyMatrix, indexMap, lines, degrees);

            if (toRemoveLater == null) {
                break; // Nada mais a remover
            }

            String node1 = toRemoveLater[0];
            String node2 = toRemoveLater[1];

            // Remove a aresta visualmente
            for (Edge edge : graph.edges().collect(Collectors.toList())) {
                String src = edge.getSourceNode().getId();
                String tgt = edge.getTargetNode().getId();

                if ((src.equals(node1) && tgt.equals(node2)) || (src.equals(node2) && tgt.equals(node1))) {
                    graph.removeEdge(edge);
                    break;
                }
            }

//            try {
//                Thread.sleep(5);  // Visualização (opcional)
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }

            // Remove a linha logicamente (matriz de adjacência + array `lines`)
            for (int k = 0; k < lines.length; k++) {
                if (lines[k] == toRemoveLater) {
                    lines[k] = null;
                    break;
                }
            }
        }
    }


    /**
     * Finds and processes the next valid edge from the current station that maintains Eulerian conditions.
     * Prioritizes non-bridge edges unless only one edge remains.
     *
     * @param path             the current traversal path
     * @param current          the current station identifier
     * @param i                the index of the current station in the adjacency matrix
     * @param adjacencyMatrix  the adjacency matrix
     * @param indexMap         mapping of station identifiers to matrix indices
     * @param lines            array of station pairs representing edges
     * @param degrees          map of station degrees
     * @return the selected edge to process (as a String array), or null if none are valid
     */
    private static String[] findAndProcessNextLine(ArrayList<String> path, String current, int i,
                                                   int[][] adjacencyMatrix, Map<String, Integer> indexMap,
                                                   String[][] lines, Map<String, Integer> degrees) {

        int totalEdges = countTotalEdges(adjacencyMatrix);

        for (String[] line : lines) {
            if (line == null) continue;

            String station1 = line[0];
            String station2 = line[1];
            String destination = station1.equals(current) ? station2 :
                    station2.equals(current) ? station1 : null;
            if (destination == null) continue;

            int j = indexMap.get(destination);

            if (adjacencyMatrix[i][j] > 0) {
                if (totalEdges == 1) {
                    return processEdge(path, adjacencyMatrix, degrees, i, j, line);
                }

                if (isNotBridge(adjacencyMatrix, indexMap, current, destination)) {
                    return processEdge(path, adjacencyMatrix, degrees, i, j, line);
                }
            }
        }

        for (String[] line : lines) {
            if (line == null) continue;

            String station1 = line[0];
            String station2 = line[1];
            String destination = station1.equals(current) ? station2 :
                    station2.equals(current) ? station1 : null;
            if (destination == null) continue;

            int j = indexMap.get(destination);

            if (adjacencyMatrix[i][j] > 0) {
                return processEdge(path, adjacencyMatrix, degrees, i, j, line);
            }
        }

        return null;
    }

    /**
     * Determines if removing the edge (u, v) would leave the graph connected (i.e., it is not a bridge).
     *
     * @param originalMatrix  the original adjacency matrix
     * @param indexMap        mapping of station identifiers to matrix indices
     * @param u               current station identifier
     * @param v               destination station identifier
     * @return true if the edge is not a bridge, false otherwise
     */
    private static boolean isNotBridge(int[][] originalMatrix, Map<String, Integer> indexMap,
                                       String u, String v) {
        int[][] testMatrix = new int[originalMatrix.length][originalMatrix[0].length];
        for (int i = 0; i < originalMatrix.length; i++) {
            for (int j = 0; j < originalMatrix[0].length; j++) {
                testMatrix[i][j] = originalMatrix[i][j];
            }
        }

        int i = indexMap.get(u);
        int j = indexMap.get(v);

        testMatrix[i][j]--;
        testMatrix[j][i]--;

        boolean[] visited = new boolean[testMatrix.length];
        int countBefore = dfsCount(originalMatrix, i, new boolean[originalMatrix.length]);
        int countAfter = dfsCount(testMatrix, i, visited);

        return countAfter >= countBefore;
    }

    /**
     * Depth-First Search helper to count reachable vertices.
     *
     * @param adjacencyMatrix the adjacency matrix
     * @param startNode            the starting node index
     * @param visited         visited flags for each node
     * @return number of reachable nodes from the starting node
     */
    private static int dfsCount(int[][] adjacencyMatrix, int startNode, boolean[] visited) {
        int n = adjacencyMatrix.length;
        int[] toVisit = new int[n]; // array para simular a "pilha"
        int top = 0; // simula o topo da pilha

        toVisit[top++] = startNode;
        visited[startNode] = true;
        int count = 1;

        while (top > 0) {
            int node = toVisit[--top]; // "pop"

            for (int i = 0; i < n; i++) {
                if (adjacencyMatrix[node][i] > 0 && !visited[i]) {
                    visited[i] = true;
                    count++;
                    toVisit[top++] = i; // "push"
                }
            }
        }

        return count;
    }



    /**
     * Processes the selected edge: updates path, degrees, and adjacency matrix.
     *
     * @param path             the current Eulerian path
     * @param adjacencyMatrix  the adjacency matrix
     * @param degrees          map of station degrees
     * @param i                index of current station
     * @param j                index of destination station
     * @param line             the edge (station pair) being processed
     * @return the processed edge (station pair)
     */
    private static String[] processEdge(ArrayList<String> path,
                                        int[][] adjacencyMatrix, Map<String, Integer> degrees,
                                        int i, int j, String[] line) {
        adjacencyMatrix[i][j]--;
        adjacencyMatrix[j][i]--;

        String current = path.get(path.size() - 1);
        String destination = line[0].equals(current) ? line[1] : line[0];

        path.add(destination);
        degrees.put(current, degrees.get(current) - 1);
        degrees.put(destination, degrees.get(destination) - 1);

        return line;
    }

    /**
     * Counts the total number of remaining edges in the adjacency matrix.
     *
     * @param matrix the adjacency matrix
     * @return total number of remaining edges
     */
    private static int countTotalEdges(int[][] matrix) {
        int count = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix[i].length; j++) {
                count += matrix[i][j];
            }
        }
        return count;
    }

    /**
     * Returns a map of stations with odd degrees.
     *
     * @param degrees map of stations and their degrees
     * @return map containing only stations with odd degrees
     */
    public static Map<String, Integer> countOddDegreeVertices(Map<String, Integer> degrees) {
        Map<String, Integer> oddDegrees = new HashMap<>();
        for (String station : degrees.keySet()) {
            if (degrees.get(station) % 2 != 0) {
                oddDegrees.put(station, degrees.get(station));
            }
        }
        return oddDegrees;
    }

    /**
     * Calculates the degree (number of connections) for each station.
     *
     * @param railwayLines array of station pairs representing railway lines
     * @param stations     array of station identifiers
     * @return map of stations and their degrees
     */
    public static Map<String, Integer> calculateVertexDegrees(String[][] railwayLines, String[] stations) {
        Map<String, Integer> degrees = new HashMap<>();
        for (String station : stations) {
            int degree = 0;
            for (String[] line : railwayLines) {
                if (line[0].equals(station) || line[1].equals(station)) {
                    degree++;
                }
            }
            degrees.put(station, degree);
        }

        return degrees;
    }

    /**
     * Determines valid starting stations for the Eulerian path.
     * If there are exactly two odd-degree stations, those are valid starts.
     * Otherwise, returns all stations with a degree greater than zero.
     *
     * @param oddCount map of stations with odd degrees
     * @param degrees  full map of stations and their degrees
     * @param stations array of all station identifiers
     * @return list of valid station identifiers
     */
    public static ArrayList<String> getValidStations(Map<String, Integer> oddCount,
                                                     Map<String, Integer> degrees,
                                                     String[] stations) {
        ArrayList<String> validStations;
        if (oddCount.size() == 2) {
            validStations = new ArrayList<>(oddCount.keySet());
        } else {
            validStations = new ArrayList<>();
            for (String station : stations) {
                if (degrees.get(station) != 0) {
                    validStations.add(station);
                }
            }
        }
        return validStations;
    }

    /**
     * Creates a mapping from station identifiers to their index positions in the adjacency matrix.
     *
     * @param stations array of station identifiers
     * @return map of station identifiers to index values
     */
    public static Map<String, Integer> createIndexMap(String[] stations) {
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < stations.length; i++) {
            indexMap.put(stations[i], i);
        }
        return indexMap;
    }
}
