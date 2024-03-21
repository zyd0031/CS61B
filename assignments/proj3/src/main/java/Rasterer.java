import java.util.HashMap;
import java.util.Map;


/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private static final double[] DEPTH_LonDPP = new double[8];
    static{
        double lonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        for (int i = 0; i < DEPTH_LonDPP.length; i++) {
            DEPTH_LonDPP[i] = lonDPP;
            lonDPP /= 2;
        }
    }

    private static final double LON_INTERVAL = MapServer.ROOT_LRLON - MapServer.ROOT_ULLON;
    private static final double LAT_INTERVAL = MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT;

    public Rasterer(){
        
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        // // Map<String, Object> results = new HashMap<>();
        // // System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
        // //                    + "your browser.");
        // return results;
        Map<String, Object> results = new HashMap<>();
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double w = params.get("w");
        double h = params.get("h");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");

        if (!isInsideBounds(ullon, ullat, lrlon, lrlat)){
            results.put("render_grid", null);
            results.put("raster_ul_lon", 0);
            results.put("raster_ul_lat", 0);
            results.put("raster_lr_lon", 0);
            results.put("raster_lr_lat", 0);
            results.put("depth", -1);
            results.put("query_success", false);
        }

        int depth = calDepth(lrlon, ullon, w);
        results.put("depth", depth);
        results.put("query_success", true);

        int startX = getXIndex(depth, ullon);
        int endX = getXIndex(depth, lrlon);
        int startY = getYIndex(depth, ullat);
        int endY = getYIndex(depth, lrlat);

        String[][] render_grid = new String[endY - startY + 1][endX - startX + 1];
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                render_grid[y - startY][x - startX] = "d" + depth + "_x" + x + "_y" + y + ".png";
            }
        }
        results.put("render_grid", render_grid);

        double raster_ul_lon = getRasterULLON(depth, startX);
        double raster_ul_lat = getRasterULLAT(depth, startY);
        double raster_lr_lon = getRasterLRLON(depth, endX);
        double raster_lr_lat = getRasterLRLAT(depth, endY);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);

        return results;
    }

    private double getRasterULLON(int depth, int index){
        double lonInterval = LON_INTERVAL / Math.pow(2, depth);
        return MapServer.ROOT_ULLON + lonInterval * index;
    }

    private double getRasterULLAT(int depth, int index){    ////
        double latInterval = LAT_INTERVAL / Math.pow(2, depth);
        return MapServer.ROOT_ULLAT - latInterval * index;
    }

    private double getRasterLRLON(int depth, int index){
        double lonInterval = LON_INTERVAL / Math.pow(2, depth);
        return MapServer.ROOT_ULLON + lonInterval * (index + 1);
    }

    private double getRasterLRLAT(int depth, int index){   ////
        double latInterval = LAT_INTERVAL / Math.pow(2, depth);
        return MapServer.ROOT_ULLAT - latInterval * (index + 1);
    }



    private int getXIndex(int depth, double num){
        double lonInterval = LON_INTERVAL / Math.pow(2, depth);
        return (int)((num - MapServer.ROOT_ULLON) / lonInterval);
    }

    private int getYIndex(int depth, double num){
        int numInterval = (int)Math.pow(2, depth);
        double latInterval = LAT_INTERVAL / numInterval;
        int index = (int)((num - MapServer.ROOT_LRLAT) / latInterval);
        return numInterval - 1 - index;
    }


    private double calLonDPP(double lrlon, double ullon, double w){
        return (lrlon - ullon) / w;
    }



    private int calDepth(double lrlon, double ullon, double w){
        double userLonDPP = calLonDPP(lrlon, ullon, w);
        int depth = 0;
        while(userLonDPP < DEPTH_LonDPP[depth] && depth < DEPTH_LonDPP.length - 1){
            depth++;
        }
        return depth;
    }

    private boolean isInsideBounds(double ullon, double ullat, double lrlon, double lrlat){
        return isBtwnLat(lrlat) && isBtwnLat(ullat) && isBtwnLon(lrlon) && isBtwnLon(ullon);
    }

    private boolean isBtwnLon(double lon){
        return lon >= MapServer.ROOT_ULLON && lon <= MapServer.ROOT_LRLON;
    }

    private boolean isBtwnLat(double lat){
        return lat >= MapServer.ROOT_LRLAT && lat <= MapServer.ROOT_ULLAT;
    }

}
