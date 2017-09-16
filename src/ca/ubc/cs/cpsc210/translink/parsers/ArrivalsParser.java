package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {
    private static Stop stopHolder;
    private static String routeNoHolder;
    private static String routeName;
    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop         stop to which parsed arrivals are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException                when JSON response does not have expected format
     * @throws ArrivalsDataMissingException when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        stopHolder = stop;
        JSONArray arrivals = new JSONArray(jsonResponse);

        for(int index = 0; index < arrivals.length(); index++){
            JSONObject arrival = arrivals.getJSONObject(index);

            parseArrival(arrival);
        }

    }

    private static void parseArrival(JSONObject arrival) throws JSONException, ArrivalsDataMissingException {
        try {
            routeNoHolder = arrival.getString("RouteNo");
            routeName = arrival.getString("RouteName");
        }catch(Exception JSONException){
            throw new ArrivalsDataMissingException();}
        JSONArray schedules = arrival.getJSONArray("Schedules");

        int itemParsed = 0;
        for(int index = 0;index < schedules.length();index++){
            JSONObject schedule = schedules.getJSONObject(index);
            if(!schedule.has("ExpectedCountdown")
                    ||!schedule.has("ScheduleStatus")
                    ||!schedule.has("Destination"))
                continue;

            itemParsed ++;
            parseSchedule(schedule);


        }

        if (itemParsed == 0){
            throw new ArrivalsDataMissingException();
        }
    }

    private static void parseSchedule(JSONObject schedule) throws JSONException {
        int timetoStopHolder = schedule.getInt("ExpectedCountdown");
        String destinationHolder = schedule.getString("Destination");
        String statusholder = schedule.getString("ScheduleStatus");

        Route routeHolder = RouteManager.getInstance().getRouteWithNumber(routeNoHolder);

        storeArrivals(timetoStopHolder,destinationHolder,routeHolder,statusholder);


    }


    private static void storeArrivals(int timeToStop, String destination, Route route, String status) {
        Arrival a = new Arrival(timeToStop,destination,route);
        a.setStatus(status);

        stopHolder.addArrival(a);
        stopHolder.addRoute(route);
    }


}