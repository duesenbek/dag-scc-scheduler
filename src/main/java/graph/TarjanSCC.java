package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class TarjanSCC implements SCCFinder {
    private int time;
    private int[] index;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private List<List<Integer>> components;

    @Override
    public SCCResult findSCCs(Graph graph, Metrics metrics) {
        int n = graph.getN();
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : graph.getEdges()) adj.get(e.getSource()).add(e.getDestination());

        time = 0;
        index = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        components = new ArrayList<>();
        for (int i = 0; i < n; i++) index[i] = -1;

        if (metrics != null) metrics.startTimer();
        for (int v = 0; v < n; v++) if (index[v] == -1) strongConnect(v, adj, metrics);

        int[] compOf = new int[n];
        for (int c = 0; c < components.size(); c++) {
            for (int v : components.get(c)) compOf[v] = c;
        }
        return new SCCResult(components, compOf);
    }

    private void strongConnect(int v, List<List<Integer>> adj, Metrics metrics) {
        index[v] = time;
        low[v] = time;
        time++;
        stack.push(v);
        onStack[v] = true;
        if (metrics != null) metrics.incrementCounter("visit");

        for (int w : adj.get(v)) {
            if (index[w] == -1) {
                strongConnect(w, adj, metrics);
                low[v] = Math.min(low[v], low[w]);
            } else if (onStack[w]) {
                low[v] = Math.min(low[v], index[w]);
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

    public Graph buildCondensationGraph(Graph original, List<List<Integer>> sccs, Metrics metrics) {
        int n = original.getN();
        int compCount = sccs.size();
        int[] compOf = new int[n];
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) compOf[v] = i;
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
                    if (metrics != null) metrics.incrementCounter("cond_edge");
                }
            }
        }
        return dag;
    }
}
