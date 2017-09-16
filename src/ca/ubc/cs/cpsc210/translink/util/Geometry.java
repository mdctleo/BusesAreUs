package ca.ubc.cs.cpsc210.translink.util;

/**
 * Compute relationships between points, lines, and rectangles represented by LatLon objects
 */
public class Geometry {
    /**
     * Return true if the point is inside of, or on the boundary of, the rectangle formed by northWest and southeast
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param point             the point in question
     * @return                  true if the point is on the boundary or inside the rectangle
     */
    public static boolean rectangleContainsPoint(LatLon northWest, LatLon southEast, LatLon point) {



        Double xlwb = northWest.getLongitude();
        Double xupb = southEast.getLongitude();
        Double ylwb = southEast.getLatitude();
        Double yupb = northWest.getLatitude();

        return (between(xlwb,xupb,point.getLongitude()) && between(ylwb,yupb,point.getLatitude()));


    }

    /**
     * Return true if the rectangle intersects the line
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param src               one end of the line in question
     * @param dst               the other end of the line in question
     * @return                  true if any point on the line is on the boundary or inside the rectangle
     */
    public static boolean rectangleIntersectsLine(LatLon northWest, LatLon southEast, LatLon src, LatLon dst) {
        Double linex1 = src.getLongitude();
        Double liney1 = src.getLatitude();
        Double linex2 = dst.getLongitude();
        Double liney2 = dst.getLatitude();

        Double rightSideBottomx3 = southEast.getLongitude();
        Double rightSideBottomy3 = southEast.getLatitude();
        Double rightSideTopx4 = southEast.getLongitude();
        Double rightSideTopy4 = northWest.getLatitude();

        Double leftSideBottomx3 = northWest.getLongitude();
        Double leftSideBottomy3 = southEast.getLatitude();
        Double leftSideTopx4 = northWest.getLongitude();
        Double leftSideTopy4 = northWest.getLatitude();

        Double topSideLeftx3 = northWest.getLongitude();
        Double topSideRighty3 = northWest.getLatitude();
        Double topSideRightx4 = southEast.getLongitude();
        Double topSideRighty4 = northWest.getLatitude();

        Double bottomSideLeftx3 = northWest.getLongitude();
        Double bottomSideLefty3 = southEast.getLatitude();
        Double bottomSideRightx4 = southEast.getLongitude();
        Double bottomSideRighty4 = southEast.getLatitude();

        Double xupb = southEast.getLongitude();
        Double xlwb = northWest.getLongitude();
        Double yupb = northWest.getLatitude();
        Double ylwb = southEast.getLatitude();

        if(linesIntersect(linex1,liney1,linex2,liney2,rightSideBottomx3,rightSideBottomy3,rightSideTopx4,rightSideTopy4)
                || linesIntersect(linex1,liney1,linex2,liney2,leftSideBottomx3,leftSideBottomy3,leftSideTopx4,leftSideTopy4)
                || linesIntersect(linex1,liney1,linex2,liney2,topSideLeftx3,topSideRighty3,topSideRightx4,topSideRighty4)
                || linesIntersect(linex1,liney1,linex2,liney2,bottomSideLeftx3,bottomSideLefty3,bottomSideRightx4,bottomSideRighty4))
            return true;

        if(((between(ylwb,yupb,src.getLatitude()))
                && between(xlwb,xupb,src.getLongitude()))
                || (between(ylwb,yupb,dst.getLatitude())
                && between(xlwb,xupb,dst.getLongitude()))){
            return true;
        }



        return false;




    }

    // Referenced User MadProgrammer's comments on stackoverflow for help
    //http://stackoverflow.com/questions/15514906/how-to-check-intersection-between-a-line-and-a-rectangle
    //rip nvm you cant use that

    public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
        // Return false if either of the lines have zero length
        if (x1 == x2 && y1 == y2 ||
                x3 == x4 && y3 == y4){
            return false;
        }
        // Fastest method, based on Franklin Antonio's "Faster Line Segment Intersection" topic "in Graphics Gems III" book (http://www.graphicsgems.org/)
        double ax = x2-x1;
        double ay = y2-y1;
        double bx = x3-x4;
        double by = y3-y4;
        double cx = x1-x3;
        double cy = y1-y3;

        double alphaNumerator = by*cx - bx*cy;
        double commonDenominator = ay*bx - ax*by;
        if (commonDenominator > 0){
            if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
                return false;
            }
        }else if (commonDenominator < 0){
            if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
                return false;
            }
        }
        double betaNumerator = ax*cy - ay*cx;
        if (commonDenominator > 0){
            if (betaNumerator < 0 || betaNumerator > commonDenominator){
                return false;
            }
        }else if (commonDenominator < 0){
            if (betaNumerator > 0 || betaNumerator < commonDenominator){
                return false;
            }
        }
        if (commonDenominator == 0){
            // This code wasn't in Franklin Antonio's method. It was added by Keith Woodward.
            // The lines are parallel.
            // Check if they're collinear.
            double y3LessY1 = y3-y1;
            double collinearityTestForP3 = x1*(y2-y3) + x2*(y3LessY1) + x3*(y1-y2);   // see http://mathworld.wolfram.com/Collinear.html
            // If p3 is collinear with p1 and p2 then p4 will also be collinear, since p1-p2 is parallel with p3-p4
            if (collinearityTestForP3 == 0){
                // The lines are collinear. Now check if they overlap.
                if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 ||
                        x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4 ||
                        x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2){
                    if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 ||
                            y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4 ||
                            y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2){
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    //reference User Commander Keith, java gaming.org
    //http://www.java-gaming.org/index.php?topic=22590.0


    /**
     * A utility method that you might find helpful in implementing the two previous methods
     * Return true if x is >= lwb and <= upb
     * @param lwb      the lower boundary
     * @param upb      the upper boundary
     * @param x         the value in question
     * @return          true if x is >= lwb and <= upb
     */
    private static boolean between(double lwb, double upb, double x) {
        return lwb <= x && x <= upb;
    }
}
