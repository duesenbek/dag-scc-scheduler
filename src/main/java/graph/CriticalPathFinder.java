package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CriticalPathFinder {
    private static final int INF = Integer.MIN_VALUE / 4;

    public CriticalPathResult findCriticalPath(Graph dag, Metrics metrics) {
        Objects.requireNonNull(dag, "Graph cannot be null");
        
        int n = dag.getN();
        if (n == 0) return new CriticalPathResult(new ArrayList<>(), 0);

        List<List<int[]>> adj = new ArrayList<>(n);
        int[] indeg = new int[n];
        buildAdjacency(dag, adj, indeg, n);

        int[] indegCopy = indeg.clone();
        int[] topoOrder = computeTopologicalOrder(adj, indeg, n, metrics);
        int orderSize = 0;
        for (int i = 0; i < n; i++) if (topoOrder[i] != -1) orderSize++;

        int[] dist = new int[n];
        int[] prev = new int[n];
        computeLongestPaths(adj, topoOrder, orderSize, indegCopy, dist, prev, n, metrics);

        int maxDist = Integer.MIN_VALUE;
        int maxNode = -1;
        for (int i = 0; i < n; i++) {
            if (dist[i] > maxDist) {
                maxDist = dist[i];
                maxNode = i;
            }
        }

        List<Integer> path = reconstructPath(maxNode, prev);
        return new CriticalPathResult(path, Math.max(0, maxDist));
    }

    private void buildAdjacency(Graph dag, List<List<int[]>> adj, int[] indeg, int n) {
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : dag.getEdges()) {
            adj.get(e.getSource()).add(new int[]{e.getDestination(), e.getWeight()});
            indeg[e.getDestination()]++;
        }
    }

    private int[] computeTopologicalOrder(List<List<int[]>> adj, int[] indeg, int n, Metrics metrics) {
        int[] order = new int[n];
        for (int i = 0; i < n; i++) order[i] = -1;
        
        int[] q = new int[n];
        int head = 0, tail = 0, k = 0;
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q[tail++] = i;
        
        while (head < tail) {
            int u = q[head++];
            order[k++] = u;
            if (metrics != null) metrics.incrementCounter("cp_kahn");
            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                if (--indeg[v] == 0) q[tail++] = v;
            }
        }
        return order;
    }

    private void computeLongestPaths(List<List<int[]>> adj, int[] topoOrder, int orderSize,
                                     int[] indegCopy, int[] dist, int[] prev, int n, Metrics metrics) {
        for (int i = 0; i < n; i++) {
            dist[i] = INF;
            prev[i] = -1;
        }
        for (int i = 0; i < n; i++) if (indegCopy[i] == 0) dist[i] = 0;

        for (int i = 0; i < orderSize; i++) {
            int u = topoOrder[i];
            if (dist[u] == INF) continue;
            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int w = edge[1];
                if (dist[v] < dist[u] + w) {
                    dist[v] = dist[u] + w;
                    prev[v] = u;
                }
            }
        }
    }

    private List<Integer> reconstructPath(int node, int[] prev) {
        List<Integer> path = new ArrayList<>();
        if (node == -1) return path;
        
        int cur = node;
        while (cur != -1) {
            path.add(0, cur);
            cur = prev[cur];
        }
        return path;
    }
}
