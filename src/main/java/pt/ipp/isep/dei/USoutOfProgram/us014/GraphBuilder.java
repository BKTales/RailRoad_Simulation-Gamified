package pt.ipp.isep.dei.USoutOfProgram.us014;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;

public class GraphBuilder {

    private static final String ELECTRIFIED = "1";

    private GraphBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Builds a graph representing stations and railway lines.
     * Adds nodes for each station and edges for each railway line,
     * marking edges as electrified or non-electrified with appropriate attributes.
     *
     * @param stations Array of station identifiers
     * @param railwayLines 2D array representing railway lines;
     *                     each entry includes source station, target station,
     *                     line type (electrified or not), and optionally distance
     * @return a MultiGraph instance representing the railway network
     */
    public static MultiGraph buildGraph(String[] stations, String[][] railwayLines) {
        MultiGraph graph = new MultiGraph("Rota");
        System.setProperty("org.graphstream.ui", "swing");
        graph.setAttribute("ui.stylesheet", "url(src/main/resources/css/graph.css)");

        for (String station : stations) {
            if (graph.getNode(station) == null) {
                graph.addNode(station);
            }
        }

        // Label nodes with their IDs
        graph.forEach(node -> node.setAttribute("ui.label", node.getId()));
        int j = 0;
        // Add edges representing railway lines between stations
        for (String[] line : railwayLines) {
            String source = line[0];
            String target = line[1];
            String type = line[2];
            String edgeId = source + target;


            // Avoid adding duplicate edges in both directions
            if (graph.getNode(source) == null || graph.getNode(target) == null) {
                System.out.println("Erro: estação inexistente (" + source + ", " + target + ")");
                continue;
            }
            Edge edge;
            if (graph.getEdge(edgeId) == null && graph.getEdge(target + source) == null) {
                edge = graph.addEdge(edgeId, source, target);
            } else {
                edge = graph.addEdge(edgeId + j++, source, target);
            }

            applyAttributes(edge, type);
        }

        return graph;
    }


    private static void applyAttributes(Edge edge, String type) {
        if (ELECTRIFIED.equals(type)) {
            edge.setAttribute("isEletrified", true);
            edge.setAttribute("ui.class", "eletrified");
        } else {
            edge.setAttribute("isNonEletrified", true);
            edge.setAttribute("ui.class", "nonEletrified");
        }
    }

    /**
     * Highlights the given stations in the graph by setting their UI class to "start".
     *
     * @param graph the graph instance containing nodes
     * @param stations list of station IDs to highlight
     */
    public static void highlightStations(MultiGraph graph, ArrayList<String> stations) {
        for (String station : stations) {
            Node node = graph.getNode(station);
            if (node != null) {
                node.setAttribute("ui.class", "start");
            }
        }
    }

    /**
     * Resets the style of all stations except the one at the chosen index by
     * setting their UI class back to the default "node".
     *
     * @param graph the graph instance containing nodes
     * @param stations list of station IDs
     * @param chosenIndex the 1-based index of the station to exclude from unhighlighting
     */
    public static void unhighlightOtherStations(MultiGraph graph,
                                                ArrayList<String> stations,
                                                int chosenIndex) {
        for (int i = 0; i < stations.size(); i++) {
            if (i != chosenIndex - 1) {
                Node node = graph.getNode(stations.get(i));
                if (node != null) {
                    node.setAttribute("ui.class", "node");
                }
            }
        }
    }
}
