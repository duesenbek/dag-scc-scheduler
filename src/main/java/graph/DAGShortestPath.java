package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Finds shortest and longest paths in a directed acyclic graph (DAG).
 * Uses topological sorting combined with dynamic programming.
 */
public class DAGShortestPath {
    private static final int INF = Integer.MAX_VALUE / 2;

    /**
     * Finds shortest paths from a source vertex to all other vertices.
     *
     * @param dag     the directed acyclic graph
     * @param source  the source vertex
     * @param metrics optional metrics collector for performance tracking
     * @return array where dist[i] is the shortest path distance from source to i
     * @throws IllegalArgumentException if graph is null or source is out of bounds
     */
    public int[] findShortestPaths(Graph dag, int source, Metrics metrics) {
        Objects.requireNonNull(dag, "Graph cannot be null");
        
        int n = dag.getN();
        if (source < 0 || source >= n) {
            throw new IllegalArgumentException("Source vertex out of bounds: " + source);
        }

        List<List<int[]>> adj = buildAdjacency(dag, n);
        int[] dist = new int[n];
        for (int i = 0; i < n; i++) dist[i] = INF;
        dist[source] = 0;

        if (metrics != null) metrics.startTimer();

        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult topoResult = sorter.topologicalSort(dag, metrics);
        List<Integer> topoOrder = topoResult.getOrder();

        for (int u : topoOrder) {
            if (dist[u] == INF) continue;
            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    if (metrics != null) metrics.incrementCounter(Metrics.EDGE_RELAXATIONS);
                }
            }
        }

        return dist;
    }

    /**
     * Finds longest paths from a source vertex to all other vertices.
     *
     * @param dag     the directed acyclic graph
     * @param source  the source vertex
     * @param metrics optional metrics collector for performance tracking
     * @return array where dist[i] is the longest path distance from source to i
     * @throws IllegalArgumentException if graph is null or source is out of bounds
     */
    public int[] findLongestPaths(Graph dag, int source, Metrics metrics) {
        Objects.requireNonNull(dag, "Graph cannot be null");
        
        int n = dag.getN();
        if (source < 0 || source >= n) {
            throw new IllegalArgumentException("Source vertex out of bounds: " + source);
        }

        List<List<int[]>> adj = buildAdjacency(dag, n);
        int[] dist = new int[n];
        for (int i = 0; i < n; i++) dist[i] = -INF;
        dist[source] = 0;

        if (metrics != null) metrics.startTimer();

        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult topoResult = sorter.topologicalSort(dag, metrics);
        List<Integer> topoOrder = topoResult.getOrder();

        for (int u : topoOrder) {
            if (dist[u] == -INF) continue;
            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int w = edge[1];
                if (dist[u] + w > dist[v]) {
                    dist[v] = dist[u] + w;
                    if (metrics != null) metrics.incrementCounter(Metrics.EDGE_RELAXATIONS);
                }
            }
        }

        return dist;
    }

    /**
     * Builds adjacency list representation with edge weights.
     *
     * @param dag the graph
     * @param n   number of vertices
     * @return adjacency list where each entry contains [destination, weight] pairs
     */
    private List<List<int[]>> buildAdjacency(Graph dag, int n) {
        List<List<int[]>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : dag.getEdges()) {
            adj.get(e.getSource()).add(new int[]{e.getDestination(), e.getWeight()});
        }
        return adj;
    }
}
