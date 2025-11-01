package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final List<Edge> edges;
    private final boolean directed;
    private final String weightModel;

    public Graph(int n, boolean directed, String weightModel) {
        this.n = n;
        this.directed = directed;
        this.weightModel = weightModel;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int weight) {
        edges.add(new Edge(u, v, weight));
        if (!directed) {
            edges.add(new Edge(v, u, weight));
        }
    }

    public int getVertexCount() {
        return n;
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public boolean isDirected() {
        return directed;
    }

    public String getWeightModel() {
        return weightModel;
    }

    @Override
    public String toString() {
        return String.format("Graph{n=%d, edges=%d, directed=%s, weightModel='%s'}",
                n, edges.size(), directed, weightModel);
    }
}
