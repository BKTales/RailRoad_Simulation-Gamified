package pt.ipp.isep.dei.utils;

import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.industries.Industry;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.station.StationType;

import java.util.List;

public interface IGraphBuilder {
    IGraph buildGraph(List<City> cities,
                      List<StationType> stations,
                      List<Industry> industries,
                      List<RailwayLine> railwayLines);
}