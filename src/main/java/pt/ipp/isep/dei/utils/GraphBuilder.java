package pt.ipp.isep.dei.utils;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.industries.Industry;
import pt.ipp.isep.dei.domain.position.HouseBlock;
import pt.ipp.isep.dei.domain.rails.RailType;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.rails.TrackType;
import pt.ipp.isep.dei.domain.station.StationType;

import java.util.List;
/**
 * Class responsible for building a visual graph using the GraphStream library.
 * The graph represents cities, stations, industries, and railway lines on a map.
 */
public class GraphBuilder {

    /**
     * Builds the complete graph with cities, stations, industries, and railway lines.
     *
     * @param cities List of cities to be added to the graph.
     * @param stations List of stations to be added.
     * @param industries List of industries to be included.
     * @param railwayLines List of railway lines connecting stations.
     * @return A {@code MultiGraph} object representing the constructed graph.
     */
    public MultiGraph buildGraph(List<City> cities, List<StationType> stations,
                                 List<Industry> industries, List<RailwayLine> railwayLines) {
        MultiGraph graph = new MultiGraph("Rota");
        //System.setProperty("org.graphstream.ui", "swing");
        graph.setAttribute("ui.stylesheet", "url(src/main/resources/css/graphBuilder.css)");

        if (!stations.isEmpty()) {
            addStations(graph, stations);
        }
        if (!industries.isEmpty()) {
            addIndustries(graph, industries);
        }
        if (!railwayLines.isEmpty()) {
            addRailwayLines(graph, railwayLines);
        }
        if (!cities.isEmpty()) {
            addCities(graph, cities);
        }
        return graph;
    }

    /**
     * Adds a single station node to the graph.
     *
     * @param graph The graph to which the station will be added.
     * @param station The station to be added.
     */
    public void addStation(MultiGraph graph, StationType station) {
        if (graph.getNode(station.getName()) == null) {
            Node node = graph.addNode(station.getName() + station.getLocation().getPosition().toString());
            node.setAttribute("ui.label", station.getName() + station.getLocation().getPosition().toString());
            node.setAttribute("ui.class", "station");

            float x = station.getLocation().getPosition().getX();
            float y = station.getLocation().getPosition().getY();
            node.setAttribute("xyz", x, y, 0);
        }
    }

    /**
     * Adds a list of station nodes to the graph.
     *
     * @param graph The graph to which the stations will be added.
     * @param stations List of stations.
     */
    public void addStations(MultiGraph graph, List<StationType> stations) {
        for (StationType station : stations) {
            addStation(graph, station);
        }
    }

    /**
     * Adds an industry node to the graph.
     *
     * @param graph The graph to which the industry will be added.
     * @param industry The industry to be added.
     */
    public void addIndustry(MultiGraph graph, Industry industry) {
        String nodeId = industry.getType().name() + industry.getLocation().getPosition().toString();
        System.out.println(nodeId);
        if (graph.getNode(nodeId) == null) {
            Node node = graph.addNode(nodeId);
            node.setAttribute("ui.label", nodeId);
            node.setAttribute("ui.class", "industry");

            float x = industry.getLocation().getPosition().getX();
            float y = industry.getLocation().getPosition().getY();
            node.setAttribute("xyz", x, y, 0);
        }
    }

    /**
     * Adds a list of industry nodes to the graph.
     *
     * @param graph The graph to which the industries will be added.
     * @param industries List of industries.
     */
    public void addIndustries(MultiGraph graph, List<Industry> industries) {
        for (Industry industry : industries) {
            addIndustry(graph, industry);

        }
    }

    /**
     * Adds a city node and its house blocks to the graph.
     *
     * @param graph The graph to which the city will be added.
     * @param city The city to be added.
     */
    public void addCity(MultiGraph graph, City city) {
        if (graph.getNode(city.getName()) == null) {
            Node cityNode = graph.addNode(city.getName() + city.getLocation().getPosition().toString());
            cityNode.setAttribute("ui.label", city.getName() + city.getLocation().getPosition().toString());
            cityNode.setAttribute("ui.class", "city");

            float x = city.getPosition().getX();
            float y = city.getPosition().getY();
            cityNode.setAttribute("xyz", x, y, 0);

            if (!city.getHouseBlocks().isEmpty()) {
                System.out.println(city.getHouseBlocks());
                for (HouseBlock houseBlock : city.getHouseBlocks()) {
                    String blockId = houseBlock.toString();
                    if (graph.getNode(blockId) == null) {
                        Node blockNode = graph.addNode(blockId);
                        blockNode.setAttribute("ui.label", blockId);
                        blockNode.setAttribute("ui.class", "house");

                        System.out.println(houseBlock.getPosition().getX());
                        System.out.println(houseBlock.getPosition().getY());

                        float bx = houseBlock.getPosition().getX();
                        float by = houseBlock.getPosition().getY();
                        blockNode.setAttribute("xyz", bx, by, 0);
                    }
                }
            }
        }
    }

    /**
     * Adds a list of cities and their house blocks to the graph.
     *
     * @param graph The graph to which the cities will be added.
     * @param cities List of cities.
     */
    public void addCities(MultiGraph graph, List<City> cities) {
        for (City city : cities) {
            addCity(graph, city);
        }
    }

    /**
     * Adds a railway line (edge) between two stations in the graph.
     *
     * @param graph The graph where the railway line will be added.
     * @param line The railway line to be added.
     */
    public void addRailwayLine(MultiGraph graph, RailwayLine line) {
        String station1 = line.getStation1().getName() + line.getStation1().getLocation().getPosition().toString();
        String station2 = line.getStation2().getName() + line.getStation2().getLocation().getPosition().toString();
        String edgeId = station1 + "-" + station2;

        if (graph.getEdge(edgeId) == null && graph.getEdge(station2 + "-" + station1) == null) {
            Edge edge = graph.addEdge(edgeId, station1, station2);

            String edgeClass = "";

            if (line.getRailType() == RailType.ELECTRIFIED) {
                edgeClass += "electrified";
            } else {
                edgeClass += "nonElectrified";
            }

            if (line.getTrackType() == TrackType.SINGLE_RAIL) {
                edgeClass += "SingleTrack";
            } else {
                edgeClass += "DoubleTrack";
            }
            System.out.println(edgeClass);
            edge.setAttribute("ui.class", edgeClass.trim());
        }
    }

    /**
     * Adds a list of railway lines to the graph.
     *
     * @param graph The graph where the railway lines will be added.
     * @param railwayLines List of railway lines.
     */
    public void addRailwayLines(MultiGraph graph, List<RailwayLine> railwayLines) {
        for (RailwayLine line : railwayLines) {
            addRailwayLine(graph, line);
        }
    }
}
