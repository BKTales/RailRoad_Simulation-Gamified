package pt.ipp.isep.dei.utils;

import org.graphstream.graph.implementations.MultiGraph;
import pt.ipp.isep.dei.domain.scenario.Scenario;

/**
 * Utility class that holds and manages a static instance of a {@link MultiGraph}.
 * Provides methods to set and generate a graph based on a given {@link Scenario}.
 */
public class GraphHolder {
    private static IGraph graph;
    private static final GraphStreamGraphBuilderAdapter graphBuilder = new GraphStreamGraphBuilderAdapter();

    /**
     * Sets the current graph instance.
     *
     * @param g the {@link MultiGraph} to be stored statically.
     */
    public static void setGraph(IGraph g) {
        graph = g;
    }

    /**
     * Builds and returns a new graph based on the provided {@link Scenario}.
     * The graph is constructed using cities, stations, industries, and railway lines
     * from the scenario's repositories.
     *
     * @param currentScenario the scenario containing all data needed to build the graph.
     * @return a {@link MultiGraph} representing the scenario.
     */
    public static IGraph getGraph(Scenario currentScenario) {
        graph = graphBuilder.buildGraph(
                currentScenario.getCityRepository().getCities(),
                currentScenario.getStationRepository().getStations(),
                currentScenario.getIndustryRepository().getIndustries(),
                currentScenario.getRailwaylineRepository().getRails()
        );
        return graph;
    }
}
