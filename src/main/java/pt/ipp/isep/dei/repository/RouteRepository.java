package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.route.Route;
import pt.ipp.isep.dei.domain.route.Trip;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.us010.PointOfRoute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing {@link Route} instances.
 * <p>
 * This repository allows creation, storage, and retrieval of routes,
 * which are composed of a list of trips. Internally, it maintains an in-memory list
 * of all created routes.
 * </p>
 */
public class RouteRepository implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Route> routes = new ArrayList<>();


    /*public void createAndSaveRoute(List<Trip> trips) {
        Route route = new Route(trips);
        routes.add(route);
    }*/
    public void createAndSaveRoute(List<PointOfRoute> pointsOfRoute, Train train, Simulation instance) {
        Route route = new Route(pointsOfRoute, train, instance);
        routes.add(route);
    }

    /**
     * Retrieves all stored routes.
     *
     * @return a list of all {@link Route} objects stored in the repository
     */
    public List<Route> getAllRoutes() {
        return routes;
    }

    /**
     * Retrieves a specific route from the repository based on its index.
     *
     * @param index the index of the route to be retrieved
     * @return the {@link Route} object at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Route getRouteByIndex(int index) {
        return routes.get(index);
    }

    /**
     * Checks whether the repository contains any routes.
     *
     * @return true if the repository is empty, false otherwise
     */
    public boolean isRouteListEmpty() {
        return routes.isEmpty();
    }

    public void clean() {
        routes.clear();
    }

    public String getRoutesInfo() {
        int manyRoutes = routes.size();
        String info = "";

        if (manyRoutes != 0){
            for (int i = 0; i < manyRoutes; i++) {
                info += routes.get(i).toString() + "\n";
            }
        }
        else
            info = "No Routes Were Created!\n";
        return info;
    }
}
