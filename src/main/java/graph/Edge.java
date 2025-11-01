package graph;

public class Edge {
    private final int u;
    private final int v;
    private final int w;

    public Edge(int u, int v, int w) {
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
}
