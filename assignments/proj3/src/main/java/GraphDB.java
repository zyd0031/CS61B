import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {


    private final List<Way> ways = new ArrayList<>();
    private final Map<Long, Vertice> id2Vertice= new HashMap<>();
    private final Map<Long, Location> id2Location= new HashMap<>();
    private final KDTree kdTree= new KDTree();
    private final Trie trie = new Trie();


    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
        for (Vertice vertice : id2Vertice.values()) {
            kdTree.insert(vertice);
        }

        for (Location location : id2Location.values()) {
            trie.insert(location.cleanedName, location.nodeName);
        }
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Set<Long> uniqueVerticsID = getID();
        id2Vertice.keySet().removeIf(id -> !uniqueVerticsID.contains(id));
        id2Location.keySet().removeIf(id -> !uniqueVerticsID.contains(id));
    }

    /**
     * Iterate ways to get the connected node id
     */
    private Set<Long> getID(){
        Set<Long> uniqueVerticsID = new HashSet<>();
        for (Way way : ways) {
            uniqueVerticsID.addAll(way.way);
        }
        return uniqueVerticsID;
    }



    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return id2Vertice.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return id2Vertice.get(v).adjacencies;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        return kdTree.findClosest(lon, lat);
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return id2Vertice.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return id2Vertice.get(v).lat;
    }

    public void addNode(long id, Vertice vertice){
        id2Vertice.put(id, vertice);
    }

    public Vertice getNode(long id){
        return id2Vertice.get(id);
    }

    public void addWay(Way way){
        ways.add(way);
    }

    public void addLocation(long id, Location location){
        id2Location.put(id, location);
    }

    public void addAdj(long id1, long id2){
        id2Vertice.get(id1).adjacencies.add(id2);
        id2Vertice.get(id2).adjacencies.add(id1);
    }

    public Set<String> getWayNames(long id){
        Set<String> idWays = new HashSet<>();
        for (Way way : ways) {
            if (way.way.contains(id)){
                idWays.add(way.wayName);
            }
        }
        return idWays;
    }

    public Set<String> getLocationByPrefix(String prefix){
        String cleanedPrefix = cleanString(prefix);
        return trie.startsWith(cleanedPrefix);
    }

    public List<Map<String, List<Location>>> getLocations(String locationName){
        String cleanedName = cleanString(locationName);
        Map<String, List<Location>> tempMap = new HashMap<>();

        for (Location loc : id2Location.values()) {
            if (loc.cleanedName.equals(cleanedName)){
                tempMap.computeIfAbsent(loc.nodeName, k -> new LinkedList<>()).add(loc);
            }
        }

        return Collections.singletonList(tempMap);
    }


    static class Vertice{
        long id;
        double lon;
        double lat;
        Set<Long> adjacencies= new HashSet<>();

        Vertice(long id, double lon, double lat){
            this.id = id;
            this.lon = lon;
            this.lat = lat;
        }
    }

    static class Location extends Vertice{
        String nodeName;
        String cleanedName;

        Location(long id, double lon, double lat, String nodeName){
            super(id, lon, lat);
            this.nodeName = nodeName;
            this.cleanedName = cleanString(nodeName);
        }
    }

    static class Way{
        List<Long> way;
        String maxSpeed;
        String highwayType;
        String wayName;

        Way(List<Long> way, String maxSpeed, String highwayType, String wayName){
            this.way = way;
            this.maxSpeed = maxSpeed;
            this.highwayType = highwayType;
            this.wayName = wayName;
        }
    }

    static class KDNode{
        Vertice vertice;
        KDNode left, right;

        KDNode(Vertice vertice){
            this.vertice = vertice;
            this.left = null;
            this.right = null;
        }
    }

    static class KDTree{
        KDNode root;

        KDTree(){
            this.root = null;
        }

        void insert (Vertice vertice){
            root = insertRec(root, vertice, 0);
        }

        KDNode insertRec(KDNode root, Vertice vertice, int depth){
            if (root == null){
                return new KDNode(vertice);
            }

            int cd = depth % 2;
            if ((cd == 0 && vertice.lon < root.vertice.lon) || (cd!= 0 && vertice.lat < root.vertice.lat)){
                root.left = insertRec(root.left, vertice, depth + 1);
            }else{
                root.right = insertRec(root.right, vertice, depth + 1);
            }

            return root;
        }

        long findClosest(double lon, double lat){
            return findClosestRec(root, lon, lat, 0).vertice.id;
        }

        KDNode findClosestRec(KDNode root, double lon, double lat, int depth){
            if (root == null){
                return null;
            }

            KDNode nextBranch, oppositeBranch;
            int cd = depth % 2;
            boolean goLeft = (cd == 0 && lon < root.vertice.lon) || (cd != 0 && lat < root.vertice.lat);

            if (goLeft){
                nextBranch = root.left;
                oppositeBranch = root.right;
            } else {
                nextBranch = root.right;
                oppositeBranch = root.left;
            }

            KDNode best = closerDistance(lon, lat, findClosestRec(nextBranch, lon, lat, depth + 1), root);

            double radius = distance(lon, lat, best.vertice.lon, best.vertice.lat);
            double distToBoundary = cd == 0 ? Math.abs(lon - root.vertice.lon) : Math.abs(lat - root.vertice.lat);
            if (radius >= distToBoundary){
                best = closerDistance(lon, lat, findClosestRec(oppositeBranch, lon, lat, depth + 1), best);
            }

            return best;
        }

        private KDNode closerDistance(double lon, double lat, KDNode p1, KDNode p2){
            if (p1 == null){
                return p2;
            }
            if (p2 == null){
                return p1;
            }

            double d1 = distance(lon, lat, p1.vertice.lon, p1.vertice.lat);
            double d2 = distance(lon, lat, p2.vertice.lon, p2.vertice.lat);

            if (d1 < d2){
                return p1;
            }else{
                return p2;
            }
        }
    }

    static class TrieNode{
        Map<Character, TrieNode> children;
        boolean isNameEnd;
        Set<String> fullNames;

        TrieNode(){
            children = new HashMap<>();
            isNameEnd = false;
            fullNames = new HashSet<>();
        }
    }

    static class Trie{
        TrieNode root;

        Trie(){
            root = new TrieNode();
        }

        void insert(String cleanedName, String fullName){
            TrieNode node = root;
            for (char c : cleanedName.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
            }
            node.isNameEnd = true;
            node.fullNames.add(fullName);
        }

        Set<String> startsWith(String prefix){
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                if (!node.children.containsKey(c)){
                    return Collections.emptySet();
                }
                node = node.children.get(c);
            }
            return getAllNames(node);
        }

        private Set<String> getAllNames(TrieNode node){
            Set<String> names = new HashSet<>();
            if (node.isNameEnd){
                names.addAll(node.fullNames);
            }
            for (Character c : node.children.keySet()) {
                names.addAll(getAllNames(node.children.get(c)));
            }
            return names;
        }
    }
}


