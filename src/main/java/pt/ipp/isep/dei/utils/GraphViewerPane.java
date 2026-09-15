package pt.ipp.isep.dei.utils;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.view.camera.Camera;

public class GraphViewerPane extends StackPane {
    private FxViewer viewer;
    private FxDefaultView view;
    private static final double NODE_SIZE = 20.0;
    private double textZoomMultiplier = 1.0;
    private double baseTextSize = 5.0;
    private MultiGraph graph;

    public GraphViewerPane(IGraph igraph) {
        if (!(igraph instanceof GraphStreamGraphAdapter)) {
            throw new IllegalArgumentException("GraphViewerPane currently only supports GraphStreamGraphAdapter");
        }

        this.graph = ((GraphStreamGraphAdapter) igraph).getGraphStreamGraph();

        initializeViewer(graph);
        applyRoundedClipping();
        initializeGraphDependentFeatures();
    }

    private void initializeViewer(Graph graph) {
        viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.disableAutoLayout();
        view = (FxDefaultView) viewer.addDefaultView(false);
        this.getChildren().add(view);
    }

    private void initializeGraphDependentFeatures() {
        setBackgroundImage();
        setupZoomAndScroll(graph);
        applyInitialNodeStyles(graph);
    }

    private void setBackgroundImage() {
        Image background = new Image(getClass().getResource("/images/grassBackground.png").toExternalForm());

        view.setBackLayerRenderer((g, gg, px2Gu, width, height, minX, minY, maxX, maxY) -> {
            if (g != null && background.getPixelReader() != null) {
                try {
                    g.drawImage(background, 0, 0, width, height);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void applyInitialNodeStyles(Graph graph) {
        for (Node node : graph) {
            node.setAttribute("ui.style", "size: " + NODE_SIZE + "px; text-size: " + baseTextSize + "px;");
        }
    }

    private void setupZoomAndScroll(Graph graph) {
        Camera cam = view.getCamera();
        double initialViewPercent = cam.getViewPercent();

        view.setOnScroll(event -> {
            double zoomFactor = event.getDeltaY() > 0 ? 0.9 : 1.1;
            double oldViewPercent = cam.getViewPercent();
            double newViewPercent = oldViewPercent * zoomFactor;
            newViewPercent = Math.max(0.15, Math.min(5, newViewPercent));
            double factor = newViewPercent / oldViewPercent;

            Point2 mouseGU = cam.transformPxToGu((float) event.getX(), (float) event.getY());
            Point2 center = cam.getViewCenter();
            double newCX = mouseGU.x - (mouseGU.x - center.x) * factor;
            double newCY = mouseGU.y - (mouseGU.y - center.y) * factor;

            cam.setViewPercent(newViewPercent);
            cam.setViewCenter(newCX, newCY, 0);

            textZoomMultiplier = Math.min(2.0, initialViewPercent / newViewPercent);
            double iconMultiplier = 1.0 + (textZoomMultiplier - 1.0) * 0.4;
            for (Node node : graph) {
                node.setAttribute("ui.style", "size: " + (NODE_SIZE * iconMultiplier) + "px; text-size: " + (baseTextSize * textZoomMultiplier) + "px;");
            }

            event.consume();
        });
    }

    private void applyRoundedClipping() {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(30);
        clip.setArcHeight(30);

        widthProperty().addListener((obs, oldVal, newVal) -> clip.setWidth(newVal.doubleValue()));
        heightProperty().addListener((obs, oldVal, newVal) -> clip.setHeight(newVal.doubleValue()));

        setClip(clip);
    }

    public void close() {
        if (viewer != null) {
            viewer.close();
            viewer = null;
        }

        if (view != null) {
            getChildren().remove(view);
            view = null;
        }
    }

    public MultiGraph getGraph() {
        return graph;
    }
}
