package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CriticalPathFinder {
    public CriticalPathResult findCriticalPath(Graph dag, Metrics metrics) {
        int n = dag.getN();
        List<List<int[]>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[] indeg = new int[n];
        for (Edge e : dag.getEdges()) {
            adj.get(e.getSource()).add(new int[]{e.getDestination(), e.getWeight()});
            indeg[e.getDestination()]++;
        }
        int[] indegCopy = indeg.clone();
        int[] order = new int[n];
        int head = 0, tail = 0, k = 0;
        int[] q = new int[n];
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q[tail++] = i;
        while (head < tail) {
            int u = q[head++];
            order[k++] = u;
            if (metrics != null) metrics.incrementCounter("cp_kahn");
            for (int[] p : adj.get(u)) {
                int v = p[0];
                if (--indeg[v] == 0) q[tail++] = v;
            }
        }
        int[] dist = new int[n];
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) { dist[i] = Integer.MIN_VALUE / 4; prev[i] = -1; }
        for (int i = 0; i < n; i++) if (indegCopy[i] == 0) dist[i] = 0;
        int maxW = Integer.MIN_VALUE; int maxV = -1;
        for (int i = 0; i < k; i++) {
            int u = order[i];
            if (dist[u] == Integer.MIN_VALUE / 4) continue;
            for (int[] p : adj.get(u)) {
                int v = p[0], w = p[1];
                if (dist[v] < dist[u] + w) { dist[v] = dist[u] + w; prev[v] = u; }
            }
        }
        for (int i = 0; i < n; i++) if (dist[i] > maxW) { maxW = dist[i]; maxV = i; }
        List<Integer> path = new ArrayList<>();
        if (maxV != -1) {
            int cur = maxV;
            while (cur != -1) { path.add(0, cur); cur = prev[cur]; }
        }
        return new CriticalPathResult(path, Math.max(0, maxW));
    }
}
