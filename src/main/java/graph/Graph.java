package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Graph {
    private final int n;
    private final List<Edge> edges;
    private final boolean directed;
    private final String weightModel;

    public Graph(int n, boolean directed, String weightModel) {
        if (n < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        Objects.requireNonNull(weightModel, "Weight model cannot be null");
        this.n = n;
        this.directed = directed;
        this.weightModel = weightModel;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int weight) {
        if (u < 0 || u >= n || v < 0 || v >= n) {
            throw new IllegalArgumentException("Vertex out of bounds");
        }
        edges.add(new Edge(u, v, weight));
        if (!directed) {
            edges.add(new Edge(v, u, weight));
        }
    }

    public int getN() {
        return n;
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(new ArrayList<>(edges));
    }

    public List<Edge> getEdgesFrom(int u) {
        if (u < 0 || u >= n) throw new IllegalArgumentException("Vertex out of bounds");
        return edges.stream()
                .filter(edge -> edge.getSource() == u)
                .collect(Collectors.toList());
    }

    public List<Edge> getEdgesTo(int v) {
        if (v < 0 || v >= n) throw new IllegalArgumentException("Vertex out of bounds");
        return edges.stream()
                .filter(edge -> edge.getDestination() == v)
                .collect(Collectors.toList());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph)) return false;
        Graph graph = (Graph) o;
        return n == graph.n && directed == graph.directed && 
               Objects.equals(weightModel, graph.weightModel) && 
               Objects.equals(edges, graph.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, edges, directed, weightModel);
    }
}
