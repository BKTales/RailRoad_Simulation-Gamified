package pt.ipp.isep.dei.utils;

import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.MultiGraph;

public class GraphStreamGraphAdapter implements IGraph {
    private final MultiGraph graph;

    public GraphStreamGraphAdapter(MultiGraph graph) {
        this.graph = graph;
    }

    public MultiGraph getGraphStreamGraph() {
        return graph;
    }

    @Override
    public void display() {
        graph.display();
    }

    @Override
    public void setStyleSheet(String stylePath) {
        graph.setAttribute("ui.stylesheet", stylePath);
    }

    @Override
    public void addNode(String id, String label, String uiClass, float x, float y) {
        if (graph.getNode(id) == null) {
            var node = graph.addNode(id);
            node.setAttribute("ui.label", label);
            node.setAttribute("ui.class", uiClass);
            node.setAttribute("xyz", x, y, 0);
        }
    }

    @Override
    public void addEdge(String id, String fromId, String toId, String edgeClass) {
        if (!hasEdge(id) && !hasEdge(toId + "-" + fromId)) {
            Edge edge = graph.addEdge(id, fromId, toId);
            edge.setAttribute("ui.class", edgeClass);
        }
    }

    @Override
    public boolean hasNode(String id) {
        return graph.getNode(id) != null;
    }

    @Override
    public boolean hasEdge(String id) {
        return graph.getEdge(id) != null;
    }
}