package ca.ubc.cs.cpsc210.translink.model;

import java.util.*;

/**
 * Manages all routes.
 *
 * Singleton pattern applied to ensure only a single instance of this class that
 * is globally accessible throughout application.
 */


public class RouteManager implements Iterable<Route> {
    private static RouteManager instance;
    // Use this field to hold all of the routes.
    // Do not change this field or its type, as the iterator method depends on it
    private Map<String, Route> routeMap;

    /**
     * Constructs Route manager with empty set of routes
     */
    private RouteManager() {
        routeMap = new HashMap<String, Route>();

    }

    /**
     * Gets one and only instance of this class
     *
     * @return  instance of class
     */
    public static RouteManager getInstance() {
        // Do not modify the implementation of this method!
        if(instance == null) {
            instance = new RouteManager();
        }
        return instance;
    }

    /**
     * Get route with given number, creating it if necessary.
     * If it is necessary to create the route, give it an empty string "" as its name
     *
     * @param number  the number of this route
     *
     * @return  route with given number
     */
    public Route getRouteWithNumber(String number) {

        if(!routeMap.containsKey(number))
        {routeMap.put(number,new Route(number));}

        return routeMap.get(number);

    }

    /**
     * Get route with given number, creating it if necessary, using the given name and number
     *
     * @param number  the number of this route
     *
     * @return  route with given number and name
     */
    public Route getRouteWithNumber(String number, String name) {

        if(!routeMap.containsKey(number))
        {Route r = new Route(number);
            r.setName(name);
            routeMap.put(number,r);}

        return routeMap.get(number);
    }

    /**
     * Get number of routes managed
     *
     * @return  number of routes added to manager
     */
    public int getNumRoutes() {
        return routeMap.size();
    }

    @Override
    public Iterator<Route> iterator() {
        // Do not modify the implementation of this method!
        return routeMap.values().iterator();
    }

    /**
     * Remove all routes from the route manager
     */
    public void clearRoutes() {
        routeMap.clear();

    }
}
