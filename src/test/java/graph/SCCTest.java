package graph;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {
    private Graph createThreeNodeCycle() {
        Graph g = new Graph(3, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        return g;
    }

    private Graph createSimpleDAG() {
        Graph g = new Graph(5, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);
        return g;
    }

    private Graph createMultipleSCCs() {
        Graph g = new Graph(8, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 5, 1);
        g.addEdge(5, 3, 1);
        g.addEdge(5, 6, 1);
        g.addEdge(6, 7, 1);
        return g;
    }

    @Test
    public void testSCCOnCyclicGraph() {
        Graph cyclicGraph = createThreeNodeCycle();
        SCCFinder finder = new TarjanSCC();
        SCCResult result = finder.findSCCs(cyclicGraph, new Metrics());
        assertEquals(1, result.getComponents().size());
    }

    @Test
    public void testSCCOnDAG() {
        Graph dag = createSimpleDAG();
        SCCFinder finder = new TarjanSCC();
        SCCResult result = finder.findSCCs(dag, new Metrics());
        assertEquals(5, result.getComponents().size());
    }

    @Test
    public void testSCCMultipleComponents() {
        Graph g = createMultipleSCCs();
        SCCFinder finder = new TarjanSCC();
        SCCResult result = finder.findSCCs(g, new Metrics());
        assertEquals(4, result.getComponents().size());
    }

    @Test
    public void testSCCComponentMapping() {
        Graph cyclicGraph = createThreeNodeCycle();
        SCCFinder finder = new TarjanSCC();
        SCCResult result = finder.findSCCs(cyclicGraph, new Metrics());
        int[] compOf = result.getComponentOf();
        assertEquals(compOf[0], compOf[1]);
        assertEquals(compOf[1], compOf[2]);
    }

    @Test
    public void testSCCWithMetrics() {
        Graph g = createMultipleSCCs();
        Metrics m = new Metrics();
        SCCFinder finder = new TarjanSCC();
        SCCResult result = finder.findSCCs(g, m);
        assertNotNull(result);
        assertTrue(m.getOperationCount() > 0);
    }

    @Test
    public void testSCCEmptyGraph() {
        Graph g = new Graph(0, true, "integer");
        SCCFinder finder = new TarjanSCC();
        SCCResult result = finder.findSCCs(g, new Metrics());
        assertEquals(0, result.getComponents().size());
    }

    @Test
    public void testSCCSingleNode() {
        Graph g = new Graph(1, true, "integer");
        SCCFinder finder = new TarjanSCC();
        SCCResult result = finder.findSCCs(g, new Metrics());
        assertEquals(1, result.getComponents().size());
        assertTrue(result.getComponents().get(0).contains(0));
    }

    @Test
    public void testSCCWithSelfLoop() {
        Graph g = new Graph(3, true, "integer");
        g.addEdge(0, 0, 1);
        g.addEdge(1, 2, 1);
        SCCFinder finder = new TarjanSCC();
        SCCResult result = finder.findSCCs(g, new Metrics());
        assertEquals(3, result.getComponents().size());
    }
}
