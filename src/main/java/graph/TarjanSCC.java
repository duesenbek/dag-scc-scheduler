package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Tarjan's algorithm for finding strongly connected components in a directed graph.
 * Uses an iterative approach to avoid stack overflow on large graphs.
 */
public class TarjanSCC implements SCCFinder {
    private int time;
    private int[] index;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private List<List<Integer>> components;

    /**
     * Finds all strongly connected components in the given graph.
     *
     * @param graph   the directed graph to analyze
     * @param metrics optional metrics collector for performance tracking
     * @return SCCResult containing all components and vertex-to-component mapping
     * @throws IllegalArgumentException if graph is null
     */
    @Override
    public SCCResult findSCCs(Graph graph, Metrics metrics) {
        Objects.requireNonNull(graph, "Graph cannot be null");
        
        int n = graph.getN();
        List<List<Integer>> adj = buildAdjacencyList(graph, n);

        time = 0;
        index = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        components = new ArrayList<>();
        for (int i = 0; i < n; i++) index[i] = -1;

        if (metrics != null) metrics.startTimer();
        
        for (int v = 0; v < n; v++) {
            if (index[v] == -1) {
                strongConnectIterative(v, adj, metrics);
            }
        }

        int[] compOf = new int[n];
        for (int c = 0; c < components.size(); c++) {
            for (int v : components.get(c)) compOf[v] = c;
        }
        return new SCCResult(components, compOf);
    }

    private List<List<Integer>> buildAdjacencyList(Graph graph, int n) {
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : graph.getEdges()) {
            adj.get(e.getSource()).add(e.getDestination());
        }
        return adj;
    }

    private void strongConnectIterative(int startVertex, List<List<Integer>> adj, Metrics metrics) {
        Deque<Integer> callStack = new ArrayDeque<>();
        callStack.push(startVertex);

        while (!callStack.isEmpty()) {
            int v = callStack.peek();

            if (index[v] == -1) {
                index[v] = time;
                low[v] = time;
                time++;
                stack.push(v);
                onStack[v] = true;
                if (metrics != null) metrics.incrementCounter(Metrics.DFS_VISITS);
            }

            boolean hasUnvisitedChild = false;
            for (int w : adj.get(v)) {
                if (index[w] == -1) {
                    callStack.push(w);
                    hasUnvisitedChild = true;
                    break;
                } else if (onStack[w]) {
                    low[v] = Math.min(low[v], index[w]);
                }
            }

            if (!hasUnvisitedChild) {
                callStack.pop();
                
                for (int w : adj.get(v)) {
                    if (index[w] > index[v] && onStack[w]) {
                        low[v] = Math.min(low[v], low[w]);
                    }
                }

                if (low[v] == index[v]) {
                    List<Integer> comp = new ArrayList<>();
                    while (true) {
                        int w = stack.pop();
                        onStack[w] = false;
                        comp.add(w);
                        if (w == v) break;
                    }
                    components.add(comp);
                }
            }
        }
    }

    /**
     * Builds a condensation graph (DAG) from the original graph and its SCCs.
     * Each SCC becomes a single vertex in the condensation graph.
     *
     * @param original the original directed graph
     * @param sccs     list of strongly connected components
     * @param metrics  optional metrics collector
     * @return a new DAG where each vertex represents an SCC
     * @throws IllegalArgumentException if original or sccs is null
     */
    public Graph buildCondensationGraph(Graph original, List<List<Integer>> sccs, Metrics metrics) {
        Objects.requireNonNull(original, "Original graph cannot be null");
        Objects.requireNonNull(sccs, "SCC list cannot be null");
        
        int n = original.getN();
        int compCount = sccs.size();
        int[] compOf = new int[n];
        
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) {
                if (v < 0 || v >= n) {
                    throw new IllegalArgumentException("Invalid vertex in SCC: " + v);
                }
                compOf[v] = i;
            }
        }

        Graph dag = new Graph(compCount, true, original.getWeightModel());
        Set<Long> seen = new HashSet<>();
        
        for (Edge e : original.getEdges()) {
            int cu = compOf[e.getSource()];
            int cv = compOf[e.getDestination()];
            if (cu != cv) {
                long key = (((long) cu) << 32) ^ (cv & 0xffffffffL);
                if (seen.add(key)) {
                    dag.addEdge(cu, cv, 1);
                    if (metrics != null) metrics.incrementCounter(Metrics.COND_EDGES);
                }
            }
        }
        return dag;
    }
}
