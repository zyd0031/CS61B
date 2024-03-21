import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class  DebugDirections{
    private static final String PATHS_FILE = "path_results.txt";
    private static final String RESULTS_FILE = "directions_results.txt";
    private static final int NUM_TESTS = 8;
    private static final String OSM_DB_PATH = "../library-sp18/data/berkeley-2018.osm.xml";
    private static GraphDB graph;

    public static void main(String[] args) {
        try {
            graph = new GraphDB(OSM_DB_PATH);

            List<List<Long>> paths = pathsFromFile();
            List<List<Router.NavigationDirection>> expectedResults = resultsFromFile();

            for (int i = 1; i < NUM_TESTS; i++) {
                System.out.println(String.format("Running test: %d", i));
                List<Long> path = paths.get(i);
                List<Router.NavigationDirection> actual = Router.routeDirections(graph, path);
                List<Router.NavigationDirection> expected = expectedResults.get(i);
                System.out.println("Actual");
                for (Router.NavigationDirection direction : actual) {
                    System.out.println(direction.toString());
                }
                System.out.println();
                System.out.println("Expected");
                for (Router.NavigationDirection direction : expected) {
                    System.out.println(direction.toString());
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<List<Long>> pathsFromFile() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(PATHS_FILE), Charset.defaultCharset());
        List<List<Long>> paths = new ArrayList<>();
        int lineIdx = 2; // ignore comment lines
        for (int i = 0; i < NUM_TESTS; i++) {
            int numVertices = Integer.parseInt(lines.get(lineIdx));
            lineIdx++;
            List<Long> path = new ArrayList<>();
            for (int j = 0; j < numVertices; j++) {
                path.add(Long.parseLong(lines.get(lineIdx)));
                lineIdx++;
            }
            paths.add(path);
        }
        return paths;
    }

    private static List<List<Router.NavigationDirection>> resultsFromFile() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(RESULTS_FILE), Charset.defaultCharset());
        List<List<Router.NavigationDirection>> expected = new ArrayList<>();
        int lineIdx = 2; // ignore comment lines
        for (int i = 0; i < NUM_TESTS; i++) {
            int numDirections = Integer.parseInt(lines.get(lineIdx));
            lineIdx++;
            List<Router.NavigationDirection> directions = new ArrayList<>();
            for (int j = 0; j < numDirections; j++) {
                directions.add(Router.NavigationDirection.fromString(lines.get(lineIdx)));
                lineIdx++;
            }
            expected.add(directions);
        }
        return expected;
    }
}

