package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;
    private Route routeHolder;
    private String nameHolder;
    private String routeNumHolder;
    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when JSON data does not have expected format
     * @throws RouteDataMissingException when
     * <ul>
     *  <li> JSON data is not an array </li>
     *  <li> JSON data is missing Name, StopNo, Routes or location elements for any stop</li>
     * </ul>
     */

    public void parseRoutes(String jsonResponse)
            throws JSONException, RouteDataMissingException {

        JSONArray routes = new JSONArray(jsonResponse);

        for (int index = 0; index < routes.length(); index++){
            JSONObject route = routes.getJSONObject(index);
            if(!route.has("Name")
                    || !route.has("RouteNo")
                    || !route.has("Patterns")){
                throw new RouteDataMissingException();
            }
            parseRoute(route);
        }

        /*JSONObject data = new JSONObject(jsonResponse);
        String nameHolder = data.getString("Name");
        String numHolder = data.getString("RouteNo");

        JSONArray patterns = data.getJSONArray("Destination");

        for(int index = 0;index < patterns.length(); index ++){
         JSONObject pattern = patterns.getJSONObject(index);
            parsePatterns(pattern);


         storeRoute(numHolder,nameHolder);
        }*/
    }

    private void parseRoute(JSONObject route) throws JSONException, RouteDataMissingException {
        nameHolder = route.getString("Name");
        routeNumHolder = route.getString("RouteNo");
        routeHolder = RouteManager.getInstance().getRouteWithNumber(routeNumHolder,nameHolder);

        JSONArray patterns = route.getJSONArray("Patterns");

        for (int index = 0; index <patterns.length(); index++){

            JSONObject pattern = patterns.getJSONObject(index);
            if(!pattern.has("PatternNo")
                    ||!pattern.has("Destination")
                    ||!pattern.has("Direction"))
                throw new RouteDataMissingException();
            parsePatterns(pattern);
        }


    }

    private void parsePatterns(JSONObject pattern) throws JSONException {
        String destinationHolder = pattern.getString("Destination");
        String directionHolder = pattern.getString("Direction");
        String patternNoHolder = pattern.getString("PatternNo");

        storeRoute(routeNumHolder,nameHolder,new RoutePattern(patternNoHolder,destinationHolder,directionHolder,routeHolder));


    }

    private void storeRoute(String number,String name, RoutePattern routePattern){
        Route r = RouteManager.getInstance().getRouteWithNumber(number,name);
        r.addPattern(routePattern);

    }

}
