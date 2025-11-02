package graph;

import java.util.Objects;

public class Edge {
    private final int u;
    private final int v;
    private final int w;

    public Edge(int u, int v, int w) {
        if (u < 0 || v < 0) throw new IllegalArgumentException("Vertices must be non-negative");
        this.u = u;
        this.v = v;
        this.w = w;
    }

    public int getSource() {
        return u;
    }

    public int getDestination() {
        return v;
    }

    public int getWeight() {
        return w;
    }

    @Override
    public String toString() {
        return String.format("%d -> %d (%d)", u, v, w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        return u == edge.u && v == edge.v && w == edge.w;
    }

    @Override
    public int hashCode() {
        return Objects.hash(u, v, w);
    }
}
