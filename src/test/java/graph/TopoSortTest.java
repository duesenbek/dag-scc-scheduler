package graph;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TopoSortTest {
    private Graph createSimpleDAG() {
        Graph g = new Graph(5, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);
        return g;
    }

    private Graph createComplexDAG() {
        Graph g = new Graph(8, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(2, 4, 1);
        g.addEdge(3, 5, 1);
        g.addEdge(4, 5, 1);
        g.addEdge(5, 6, 1);
        g.addEdge(5, 7, 1);
        return g;
    }

    private Graph createCyclicGraph() {
        Graph g = new Graph(3, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        return g;
    }

    private boolean isTopologicalOrderValid(Graph dag, List<Integer> order) {
        if (order.size() != dag.getN()) return false;
        int[] pos = new int[dag.getN()];
        for (int i = 0; i < order.size(); i++) pos[order.get(i)] = i;
        for (Edge e : dag.getEdges()) {
            if (pos[e.getSource()] >= pos[e.getDestination()]) return false;
        }
        return true;
    }

    @Test
    public void testTopologicalOrderValid() {
        Graph dag = createSimpleDAG();
        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult result = sorter.topologicalSort(dag, new Metrics());
        assertTrue(isTopologicalOrderValid(dag, result.getOrder()));
    }

    @Test
    public void testTopologicalOrderSize() {
        Graph dag = createSimpleDAG();
        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult result = sorter.topologicalSort(dag, new Metrics());
        assertEquals(5, result.getOrder().size());
        assertFalse(result.hasCycle());
    }

    @Test
    public void testTopologicalOrderComplex() {
        Graph dag = createComplexDAG();
        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult result = sorter.topologicalSort(dag, new Metrics());
        assertTrue(isTopologicalOrderValid(dag, result.getOrder()));
        assertEquals(8, result.getOrder().size());
    }

    @Test
    public void testTopologicalOrderCycleDetection() {
        Graph cyclic = createCyclicGraph();
        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult result = sorter.topologicalSort(cyclic, new Metrics());
        assertTrue(result.hasCycle());
        assertNotEquals(3, result.getOrder().size());
    }

    @Test
    public void testTopologicalOrderEmptyGraph() {
        Graph g = new Graph(0, true, "integer");
        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult result = sorter.topologicalSort(g, new Metrics());
        assertEquals(0, result.getOrder().size());
        assertFalse(result.hasCycle());
    }

    @Test
    public void testTopologicalOrderSingleNode() {
        Graph g = new Graph(1, true, "integer");
        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult result = sorter.topologicalSort(g, new Metrics());
        assertEquals(1, result.getOrder().size());
        assertEquals(0, result.getOrder().get(0).intValue());
        assertFalse(result.hasCycle());
    }

    @Test
    public void testTopologicalOrderWithMetrics() {
        Graph dag = createSimpleDAG();
        Metrics m = new Metrics();
        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult result = sorter.topologicalSort(dag, m);
        assertTrue(isTopologicalOrderValid(dag, result.getOrder()));
        assertTrue(m.getOperationCount() > 0);
    }

    @Test
    public void testTopologicalOrderDisconnected() {
        Graph g = new Graph(6, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 5, 1);
        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult result = sorter.topologicalSort(g, new Metrics());
        assertTrue(isTopologicalOrderValid(g, result.getOrder()));
        assertEquals(6, result.getOrder().size());
    }

    @Test
    public void testTopologicalOrderAllNodes() {
        Graph dag = createComplexDAG();
        KahnTopologicalSort sorter = new KahnTopologicalSort();
        TopoResult result = sorter.topologicalSort(dag, new Metrics());
        assertEquals(dag.getN(), result.getOrder().size());
        for (int i = 0; i < dag.getN(); i++) {
            assertTrue(result.getOrder().contains(i));
        }
    }
}
