package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DAGShortestPath {
    private static final int INF = Integer.MAX_VALUE / 2;

    public int[] findShortestPaths(Graph dag, int source, Metrics metrics) {
        Objects.requireNonNull(dag, "Graph cannot be null");
        
        int n = dag.getN();
        if (source < 0 || source >= n) {
            throw new IllegalArgumentException("Source vertex out of bounds");
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

    public int[] findLongestPaths(Graph dag, int source, Metrics metrics) {
        Objects.requireNonNull(dag, "Graph cannot be null");
        
        int n = dag.getN();
        if (source < 0 || source >= n) {
            throw new IllegalArgumentException("Source vertex out of bounds");
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

    private List<List<int[]>> buildAdjacency(Graph dag, int n) {
        List<List<int[]>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : dag.getEdges()) {
            adj.get(e.getSource()).add(new int[]{e.getDestination(), e.getWeight()});
        }
        return adj;
    }
}
