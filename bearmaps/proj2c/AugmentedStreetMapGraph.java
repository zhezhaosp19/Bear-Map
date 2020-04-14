package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private Map<Point, Node> pointToNode = new HashMap<>();;
    private List<Point> P = new ArrayList<>();
//    private WeirdPointSet points;
    private MyTrieSet nameTrie = new MyTrieSet();
    private Map<String, List<Node>> nameToNode = new HashMap<>();

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        for (Node n: nodes) {
            if (!neighbors(n.id()).isEmpty()) {
                Point point = new Point(n.lon(), n.lat());
                pointToNode.put(point, n);
                P.add(point);
            }
        }

        for (Node n: nodes) {
            if (n.name() != null) {
                String cleanName = cleanString(n.name());
                nameTrie.add(cleanName);
                if (!nameToNode.containsKey(cleanName)) {
                    List<Node> N = new LinkedList<>();
                    nameToNode.put(cleanName, N);
                }
                nameToNode.get(cleanName).add(n);
            }
        }
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        WeirdPointSet points = new WeirdPointSet(P);
        Point nearestPoint = points.nearest(lon, lat);
        return pointToNode.get(nearestPoint).id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> result = new LinkedList<>();

        List<String> cleanName = nameTrie.keysWithPrefix(cleanString(prefix));
        for (String c : cleanName) {
            List<Node> nameNode = nameToNode.get(c);
            for (Node n : nameNode) {
                result.add(n.name());
            }
        }
        return result;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> result = new LinkedList<>();

        List<Node> node = nameToNode.get(cleanString(locationName));

        if (!nameToNode.containsKey(cleanString(locationName))) {
            return new LinkedList<>();
        }

        for (Node n: node) {
            Map<String, Object> location = new HashMap<>();
            location.put("lat", n.lat());
            location.put("lon", n.lon());
            location.put("name", n.name());
            location.put("id", n.id());
            result.add(location);
        }
        return result;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
