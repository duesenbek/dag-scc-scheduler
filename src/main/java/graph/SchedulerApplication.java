package graph;

import java.util.List;

public class SchedulerApplication {
    private final JSONParser parser;
    private final TarjanSCC sccFinder;
    private final KahnTopologicalSort topoSort;
    private final CriticalPathFinder criticalPathFinder;
    private final DAGShortestPath shortestPathFinder;

    public SchedulerApplication() {
        this.parser = new JSONParser();
        this.sccFinder = new TarjanSCC();
        this.topoSort = new KahnTopologicalSort();
        this.criticalPathFinder = new CriticalPathFinder();
        this.shortestPathFinder = new DAGShortestPath();
    }

    public void runPipeline(String jsonFilePath) {
        System.out.println("=== DAG SCC Scheduler Application ===\n");

        Metrics metrics = new Metrics();
        metrics.startTimer();

        Graph originalGraph = parser.parseTasksJSON(jsonFilePath);
        System.out.println("1. Loaded Graph:");
        System.out.println("   " + originalGraph);
        System.out.println("   Source Vertex: " + originalGraph.getSource());
        System.out.println();

        SCCResult sccResult = sccFinder.findSCCs(originalGraph, metrics);
        List<List<Integer>> components = sccResult.getComponents();
        System.out.println("2. Strongly Connected Components:");
        System.out.println("   Found " + components.size() + " component(s)");
        for (int i = 0; i < components.size(); i++) {
            System.out.println("   Component " + i + ": " + components.get(i));
        }
        metrics.addCounter(Metrics.SCC_COMPONENTS, components.size());
        System.out.println();

        Graph condensationGraph = sccFinder.buildCondensationGraph(originalGraph, components, metrics);
        System.out.println("3. Condensation Graph (DAG):");
        System.out.println("   " + condensationGraph);
        System.out.println("   Edges: " + condensationGraph.getEdges().size());
        System.out.println();

        TopoResult topoResult = topoSort.topologicalSort(condensationGraph, metrics);
        List<Integer> topoOrder = topoResult.getOrder();
        System.out.println("4. Topological Order:");
        System.out.println("   " + topoOrder);
        System.out.println("   Has Cycle: " + topoResult.hasCycle());
        System.out.println();

        CriticalPathResult criticalPath = criticalPathFinder.findCriticalPath(condensationGraph, metrics);
        List<Integer> path = criticalPath.getPath();
        System.out.println("5. Critical Path (Longest Path):");
        System.out.println("   Path: " + path);
        System.out.println("   Total Weight: " + criticalPath.getTotalWeight());
        metrics.addCounter(Metrics.CRITICAL_PATH_LENGTH, criticalPath.getTotalWeight());
        System.out.println();

        int sourceVertex = condensationGraph.getSource();
        int[] shortestPaths = shortestPathFinder.findShortestPaths(condensationGraph, sourceVertex, metrics);
        System.out.println("6. Shortest Paths from Source (" + sourceVertex + "):");
        for (int i = 0; i < shortestPaths.length; i++) {
            if (shortestPaths[i] == Integer.MAX_VALUE / 2) {
                System.out.println("   To vertex " + i + ": INF (unreachable)");
            } else {
                System.out.println("   To vertex " + i + ": " + shortestPaths[i]);
            }
        }
        System.out.println();

        int[] longestPaths = shortestPathFinder.findLongestPaths(condensationGraph, sourceVertex, metrics);
        System.out.println("7. Longest Paths from Source (" + sourceVertex + "):");
        for (int i = 0; i < longestPaths.length; i++) {
            if (longestPaths[i] == Integer.MIN_VALUE / 2) {
                System.out.println("   To vertex " + i + ": -INF (unreachable)");
            } else {
                System.out.println("   To vertex " + i + ": " + longestPaths[i]);
            }
        }
        System.out.println();

        System.out.println("=== Summary ===");
        System.out.println("Original Graph Vertices: " + originalGraph.getN());
        System.out.println("Original Graph Edges: " + originalGraph.getEdges().size());
        System.out.println("SCC Components: " + components.size());
        System.out.println("Condensation Graph Vertices: " + condensationGraph.getN());
        System.out.println("Condensation Graph Edges: " + condensationGraph.getEdges().size());
        System.out.println("Critical Path Length: " + criticalPath.getTotalWeight());
        System.out.println();

        metrics.printMetrics();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java SchedulerApplication <json_file_path>");
            System.exit(1);
        }

        SchedulerApplication app = new SchedulerApplication();
        try {
            app.runPipeline(args[0]);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
