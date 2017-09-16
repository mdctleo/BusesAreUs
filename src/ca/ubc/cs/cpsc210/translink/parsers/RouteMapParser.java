package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;

import java.util.LinkedList;
import java.util.List;

/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String fileName;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    parseOnePattern(line);
                    posn = endposn + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     * @param str
     */
    private void parseOnePattern(String str) {
        String routeNumberHolder = "";
        String patternNameHolder = "";
        Double latHolder;
        latHolder = 0.0;
        Double lonHolder;
        lonHolder = 0.0;
        List<LatLon> latLonHolder;
        latLonHolder = new LinkedList<LatLon>();
        int i = 0;
        int indexHolder;
        String[] strsplit = str.split(";");
        for (String next : strsplit){

            if(next.substring(0,1).equals("N")){

                indexHolder = next.indexOf("-",1);
                routeNumberHolder = next.substring(1,indexHolder);
                patternNameHolder = next.substring(indexHolder + 1);
                i = 0;
                latLonHolder.clear();
            }

            if(strsplit.length == 1){
                storeRouteMap(routeNumberHolder,patternNameHolder,latLonHolder);
                break;
            }

            i++;
            if(i == 1)
                continue;
            if(!(i % 2 == 0) && !(next.substring(0,1).equals("N")))
            {lonHolder = Double.parseDouble(next);
                latLonHolder.add(new LatLon(latHolder,lonHolder));}
            if((i % 2 == 0 )&& !(next.substring(0,1).equals("N")))
            {latHolder = Double.parseDouble(next);}



        }
        if(latLonHolder.size() > 0)
            storeRouteMap(routeNumberHolder,patternNameHolder,latLonHolder);





    }

    /**
     * Store the parsed pattern into the named route
     * Your parser should call this method to insert each route pattern into the corresponding route object
     * There should be no need to change this method
     *
     * @param routeNumber       the number of the route
     * @param patternName       the name of the pattern
     * @param elements          the coordinate list of the pattern
     */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
        r.addPattern(rp);
    }
}
