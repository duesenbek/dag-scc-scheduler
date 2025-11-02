package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Kahn's algorithm for topological sorting of directed acyclic graphs (DAGs).
 * Detects cycles and reports them in the result.
 */
public class KahnTopologicalSort {
    
    /**
     * Performs topological sorting on the given graph using Kahn's algorithm.
     * 
     * @param dag     the directed graph to sort
     * @param metrics optional metrics collector for performance tracking
     * @return TopoResult containing the topological order and cycle detection status
     * @throws IllegalArgumentException if graph is null
     */
    public TopoResult topologicalSort(Graph dag, Metrics metrics) {
        Objects.requireNonNull(dag, "Graph cannot be null");
        
        int n = dag.getN();
        if (n == 0) return new TopoResult(new ArrayList<>(), false);
        
        int[] inDegree = new int[n];
        List<List<Integer>> adj = buildAdjacencyList(dag, inDegree);

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) queue.add(i);
        }

        List<Integer> order = new ArrayList<>(n);
        if (metrics != null) metrics.startTimer();
        
        while (!queue.isEmpty()) {
            int u = queue.remove();
            order.add(u);
            if (metrics != null) metrics.incrementCounter(Metrics.TOPO_POPS);
            
            for (int v : adj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) queue.add(v);
            }
        }

        boolean hasCycle = order.size() != n;
        if (hasCycle && metrics != null) {
            metrics.addCounter("CYCLE_DETECTED", 1);
        }
        
        return new TopoResult(order, hasCycle);
    }

    /**
     * Builds an adjacency list representation from the graph edges.
     * Also computes in-degrees for all vertices.
     *
     * @param dag      the graph to convert
     * @param inDegree array to store in-degree values
     * @return adjacency list representation
     */
    private List<List<Integer>> buildAdjacencyList(Graph dag, int[] inDegree) {
        int n = dag.getN();
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        
        for (Edge e : dag.getEdges()) {
            adj.get(e.getSource()).add(e.getDestination());
            inDegree[e.getDestination()]++;
        }
        return adj;
    }
}
