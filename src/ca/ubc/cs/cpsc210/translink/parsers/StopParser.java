package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse stop data from the file and add all stops to stop manager.
     *
     */
    public void parse() throws IOException, StopDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }
    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when JSON data does not have expected format
     * @throws StopDataMissingException when
     * <ul>
     *  <li> JSON data is not an array </li>
     *  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     * </ul>
     */

    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {

        JSONArray stops = new JSONArray(jsonResponse);

        for (int index = 0; index < stops.length(); index++){
            JSONObject stop = stops.getJSONObject(index);
            if(!stop.has("StopNo")
                    || !stop.has("Name")
                    || !stop.has("Latitude")
                    || !stop.has("Longitude")
                    || !stop.has("Routes")){
                throw new StopDataMissingException();
            }
            parseStop(stop);
        }

    }

    private void parseStop(JSONObject stop) throws JSONException {
        int numberHolder = stop.getInt("StopNo");
        String nameHolder = stop.getString("Name");
        LatLon latLonHolder = new LatLon(stop.getDouble("Latitude"),stop.getDouble("Longitude"));
        String routeNo = stop.getString("Routes");


        storeStops(numberHolder,nameHolder,latLonHolder,routeNo);
    }

    private void storeStops(int number, String name, LatLon locn, String routeNo){
        String[] routeNoSplit = routeNo.split(",");
        Stop s = StopManager.getInstance().getStopWithId(number,name,locn);
        for(String next : routeNoSplit){
            String trimNext = next.trim();
            Route r = RouteManager.getInstance().getRouteWithNumber(trimNext);
            r.addStop(s);
            s.addRoute(r);

        }


    }
}
