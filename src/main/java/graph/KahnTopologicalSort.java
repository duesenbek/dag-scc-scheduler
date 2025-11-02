package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class KahnTopologicalSort {
    public TopoResult topologicalSort(Graph dag, Metrics metrics) {
        Objects.requireNonNull(dag, "Graph cannot be null");
        
        int n = dag.getN();
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
            if (metrics != null) metrics.incrementCounter("kahn_pop");
            
            for (int v : adj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) queue.add(v);
            }
        }

        boolean hasCycle = order.size() != n;
        return new TopoResult(order, hasCycle);
    }

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
