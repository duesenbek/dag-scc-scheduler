package graph;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CriticalPathTest {
    private Graph createSimpleDAG() {
        Graph g = new Graph(5, true, "integer");
        g.addEdge(0, 1, 2);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 3);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 2);
        return g;
    }

    private Graph createComplexDAG() {
        Graph g = new Graph(8, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 2);
        g.addEdge(1, 3, 3);
        g.addEdge(2, 3, 1);
        g.addEdge(2, 4, 4);
        g.addEdge(3, 5, 2);
        g.addEdge(4, 5, 1);
        g.addEdge(5, 6, 3);
        g.addEdge(5, 7, 2);
        return g;
    }

    private Graph createLinearDAG() {
        Graph g = new Graph(5, true, "integer");
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 2);
        g.addEdge(3, 4, 4);
        return g;
    }

    @Test
    public void testCriticalPathSimpleDAG() {
        Graph dag = createSimpleDAG();
        CriticalPathFinder finder = new CriticalPathFinder();
        CriticalPathResult result = finder.findCriticalPath(dag, new Metrics());
        assertNotNull(result);
        assertTrue(result.getTotalWeight() > 0);
        assertTrue(result.getPath().size() > 0);
    }

    @Test
    public void testCriticalPathEndsAtSink() {
        Graph dag = createSimpleDAG();
        CriticalPathFinder finder = new CriticalPathFinder();
        CriticalPathResult result = finder.findCriticalPath(dag, new Metrics());
        List<Integer> path = result.getPath();
        assertEquals(4, path.get(path.size() - 1).intValue());
    }

    @Test
    public void testCriticalPathLinearDAG() {
        Graph dag = createLinearDAG();
        CriticalPathFinder finder = new CriticalPathFinder();
        CriticalPathResult result = finder.findCriticalPath(dag, new Metrics());
        assertEquals(5 + 3 + 2 + 4, result.getTotalWeight());
        assertEquals(5, result.getPath().size());
    }

    @Test
    public void testCriticalPathComplexDAG() {
        Graph dag = createComplexDAG();
        CriticalPathFinder finder = new CriticalPathFinder();
        CriticalPathResult result = finder.findCriticalPath(dag, new Metrics());
        assertNotNull(result);
        assertTrue(result.getTotalWeight() > 0);
        assertTrue(result.getPath().size() > 0);
    }

    @Test
    public void testCriticalPathEmptyGraph() {
        Graph g = new Graph(0, true, "integer");
        CriticalPathFinder finder = new CriticalPathFinder();
        CriticalPathResult result = finder.findCriticalPath(g, new Metrics());
        assertEquals(0, result.getPath().size());
    }

    @Test
    public void testCriticalPathSingleNode() {
        Graph g = new Graph(1, true, "integer");
        CriticalPathFinder finder = new CriticalPathFinder();
        CriticalPathResult result = finder.findCriticalPath(g, new Metrics());
        assertEquals(1, result.getPath().size());
        assertEquals(0, result.getPath().get(0).intValue());
    }

    @Test
    public void testCriticalPathWithMetrics() {
        Graph dag = createSimpleDAG();
        Metrics m = new Metrics();
        CriticalPathFinder finder = new CriticalPathFinder();
        CriticalPathResult result = finder.findCriticalPath(dag, m);
        assertNotNull(result);
        assertTrue(m.getOperationCount() > 0);
    }

    @Test
    public void testCriticalPathValidPath() {
        Graph dag = createSimpleDAG();
        CriticalPathFinder finder = new CriticalPathFinder();
        CriticalPathResult result = finder.findCriticalPath(dag, new Metrics());
        List<Integer> path = result.getPath();
        for (int i = 0; i < path.size() - 1; i++) {
            int u = path.get(i);
            int v = path.get(i + 1);
            boolean found = false;
            for (Edge e : dag.getEdges()) {
                if (e.getSource() == u && e.getDestination() == v) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Edge " + u + " -> " + v + " not found in graph");
        }
    }

    @Test
    public void testCriticalPathStartsAtSource() {
        Graph dag = createSimpleDAG();
        CriticalPathFinder finder = new CriticalPathFinder();
        CriticalPathResult result = finder.findCriticalPath(dag, new Metrics());
        List<Integer> path = result.getPath();
        assertEquals(0, path.get(0).intValue());
    }
}
