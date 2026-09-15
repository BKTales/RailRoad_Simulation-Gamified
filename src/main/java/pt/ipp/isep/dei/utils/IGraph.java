package pt.ipp.isep.dei.utils;

public interface IGraph {
    void display();

    void setStyleSheet(String stylePath);

    void addNode(String id, String label, String uiClass, float x, float y);

    void addEdge(String id, String fromId, String toId, String edgeClass);

    boolean hasNode(String id);

    boolean hasEdge(String id);
}
