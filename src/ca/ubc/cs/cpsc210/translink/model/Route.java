package ca.ubc.cs.cpsc210.translink.model;

import java.util.*;
import java.util.List;

/**
 * Represents a bus route with a route number, name, list of stops, and list of RoutePatterns.
 * <p/>
 * Invariants:
 * - no duplicates in list of stops
 * - iterator iterates over stops in the order in which they were added to the route
 */


public class Route implements Iterable<Stop> {
    private String number;
    private String name;
    private List<RoutePattern> patterns;
    private List<Stop> stops;

    /**
     * Constructs a route with given number.
     * List of stops is empty.
     *
     * @param number the route number
     */
    public Route(String number) {
        this.number = number;
        stops = new LinkedList<Stop>();
        patterns = new LinkedList<RoutePattern>();
    }

    /**
     * Return the number of the route
     *
     * @return the route number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Set the name of the route
     * @param name  The name of the route
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add the pattern to the route if it is not already there
     *
     * @param pattern
     */
    public void addPattern(RoutePattern pattern) {

        if(!patterns.contains(pattern)){
            patterns.add(pattern);

        }


    }

    /**
     * Add stop to route.  Stops must not be duplicated in a route.
     *
     * @param stop the stop to add to this route
     */
    public void addStop(Stop stop) {
        if(!stops.contains(stop)){
            stops.add(stop);
        }

        if(!stop.onRoute(this)){
            stop.addRoute(this);
        }


    }

    /**
     * Remove stop from route
     *
     * @param stop the stop to remove from this route
     */
    public void removeStop(Stop stop) {
        if(stops.contains(stop)){
            stops.remove(stop);
            stop.removeRoute(this);
        }
    }

    /**
     * Return all the stops in this route, in the order in which they were added
     *
     * @return      A list of all the stops
     */
    public List<Stop> getStops() {
        return Collections.unmodifiableList(stops);
    }

    /**
     * Determines if this route has a stop at a given stop
     *
     * @param stop the stop
     * @return true if route has a stop at given stop
     */
    public boolean hasStop(Stop stop) {

        if(stops.contains(stop))
            return true;
        else
            return false;
    }

    /**
     * Two routes are equal if their numbers are equal
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        return (number.equals(route.getNumber()));
    }

    /**
     * Two routes are equal if their numbers are equal.
     * Therefore hashCode only pays attention to number.
     */
    @Override
    public int hashCode() {

        int result = number.hashCode();
        return 31 * result;
    }

    @Override
    public Iterator<Stop> iterator() {
        // Do not modify the implementation of this method!
        return stops.iterator();
    }

    /**
     * Return the name of this route
     *
     * @return      the name of the route
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Route " + getNumber();
    }

    /**
     * Return the pattern with the given name. If it does not exist, then create it and add it to the patterns.
     * In either case, update the destination and direction of the pattern.
     *
     * @param patternName       the name of the pattern
     * @param destination       the destination of the pattern
     * @param direction         the direction of the pattern
     * @return                  the pattern with the given name
     */


    public RoutePattern getPattern(String patternName, String destination, String direction) {
        for(RoutePattern next: patterns) {
            if (next.getName().equals(patternName)) {
                next.setDestination(destination);
                next.setDirection(direction);
                return next;
            }}
        patterns.add(new RoutePattern(patternName, destination, direction,new Route(patternName)));


        return getPattern(patternName, destination, direction);
    }

    /**
     * Return the pattern with the given name. If it does not exist, then create it and add it to the patterns
     * with empty strings as the destination and direction for the pattern.
     *
     * @param patternName       the name of the pattern
     * @return                  the pattern with the given name
     */
    public RoutePattern getPattern(String patternName) {

        for(RoutePattern next: patterns){
            if(next.getName().equals(patternName)){
                return next;
            }}
        patterns.add(new RoutePattern(patternName,"","",this));

        return getPattern(patternName);



    }

    /**
     * Return all the patterns for this route as a list
     *
     * @return      a list of the patterns for this route
     */
    public List<RoutePattern> getPatterns() {
        return Collections.unmodifiableList(patterns);
    }
}
