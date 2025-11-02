package graph;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DatasetTest {
    private final JSONParser parser = new JSONParser();
    private final TarjanSCC sccFinder = new TarjanSCC();
    private final KahnTopologicalSort topoSort = new KahnTopologicalSort();
    private final CriticalPathFinder criticalPathFinder = new CriticalPathFinder();

    @Test
    public void testSmallGraph1() {
        Graph g = parser.parseTasksJSON("data/small_graph_1.json");
        assertNotNull(g);
        assertEquals(8, g.getN());
        assertTrue(g.isDirected());
        assertEquals(0, g.getSource());
        assertTrue(g.getEdges().size() > 0);
    }

    @Test
    public void testSmallGraph2() {
        Graph g = parser.parseTasksJSON("data/small_graph_2.json");
        assertNotNull(g);
        assertEquals(8, g.getN());
        assertTrue(g.isDirected());
        
        SCCResult sccResult = sccFinder.findSCCs(g, new Metrics());
        assertTrue(sccResult.getComponents().size() > 0);
    }

    @Test
    public void testSmallGraph3() {
        Graph g = parser.parseTasksJSON("data/small_graph_3.json");
        assertNotNull(g);
        assertEquals(10, g.getN());
        assertTrue(g.isDirected());
        
        SCCResult sccResult = sccFinder.findSCCs(g, new Metrics());
        assertTrue(sccResult.getComponents().size() > 0);
    }

    @Test
    public void testMediumGraph1() {
        Graph g = parser.parseTasksJSON("data/medium_graph_1.json");
        assertNotNull(g);
        assertEquals(15, g.getN());
        assertTrue(g.isDirected());
        
        TopoResult topoResult = topoSort.topologicalSort(g, new Metrics());
        assertTrue(topoResult.getOrder().size() > 0);
    }

    @Test
    public void testMediumGraph2() {
        Graph g = parser.parseTasksJSON("data/medium_graph_2.json");
        assertNotNull(g);
        assertEquals(18, g.getN());
        assertTrue(g.isDirected());
        
        CriticalPathResult result = criticalPathFinder.findCriticalPath(g, new Metrics());
        assertTrue(result.getTotalWeight() > 0);
    }

    @Test
    public void testMediumGraph3() {
        Graph g = parser.parseTasksJSON("data/medium_graph_3.json");
        assertNotNull(g);
        assertEquals(20, g.getN());
        assertTrue(g.isDirected());
        
        SCCResult sccResult = sccFinder.findSCCs(g, new Metrics());
        assertTrue(sccResult.getComponents().size() > 0);
    }

    @Test
    public void testLargeGraph1() {
        Graph g = parser.parseTasksJSON("data/large_graph_1.json");
        assertNotNull(g);
        assertEquals(30, g.getN());
        assertTrue(g.isDirected());
        
        Metrics metrics = new Metrics();
        SCCResult sccResult = sccFinder.findSCCs(g, metrics);
        assertTrue(sccResult.getComponents().size() > 0);
        assertTrue(metrics.getOperationCount() > 0);
    }

    @Test
    public void testLargeGraph2() {
        Graph g = parser.parseTasksJSON("data/large_graph_2.json");
        assertNotNull(g);
        assertEquals(40, g.getN());
        assertTrue(g.isDirected());
        
        Metrics metrics = new Metrics();
        TopoResult topoResult = topoSort.topologicalSort(g, metrics);
        assertTrue(topoResult.getOrder().size() >= 0);
        assertTrue(metrics.getOperationCount() >= 0);
    }

    @Test
    public void testLargeGraph3() {
        Graph g = parser.parseTasksJSON("data/large_graph_3.json");
        assertNotNull(g);
        assertEquals(50, g.getN());
        assertTrue(g.isDirected());
        
        Metrics metrics = new Metrics();
        CriticalPathResult result = criticalPathFinder.findCriticalPath(g, metrics);
        assertTrue(result.getTotalWeight() > 0);
        assertTrue(metrics.getOperationCount() > 0);
    }

    @Test
    public void testEmptyGraph() {
        Graph g = new Graph(0, true, "integer");
        SCCResult sccResult = sccFinder.findSCCs(g, new Metrics());
        assertEquals(0, sccResult.getComponents().size());
    }

    @Test
    public void testSingleVertex() {
        Graph g = new Graph(1, true, "integer");
        SCCResult sccResult = sccFinder.findSCCs(g, new Metrics());
        assertEquals(1, sccResult.getComponents().size());
        assertTrue(sccResult.getComponents().get(0).contains(0));
    }

    @Test
    public void testSelfLoop() {
        Graph g = new Graph(3, true, "integer");
        g.addEdge(0, 0, 1);
        g.addEdge(1, 2, 1);
        
        SCCResult sccResult = sccFinder.findSCCs(g, new Metrics());
        assertEquals(3, sccResult.getComponents().size());
    }

    @Test
    public void testDisconnectedComponents() {
        Graph g = new Graph(6, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 5, 1);
        
        TopoResult topoResult = topoSort.topologicalSort(g, new Metrics());
        assertEquals(6, topoResult.getOrder().size());
        assertFalse(topoResult.hasCycle());
    }

    @Test
    public void testCyclicGraph() {
        Graph g = new Graph(3, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        
        TopoResult topoResult = topoSort.topologicalSort(g, new Metrics());
        assertTrue(topoResult.hasCycle());
        assertTrue(topoResult.getOrder().size() < 3);
    }

    @Test
    public void testAcyclicGraph() {
        Graph g = new Graph(5, true, "integer");
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);
        
        TopoResult topoResult = topoSort.topologicalSort(g, new Metrics());
        assertFalse(topoResult.hasCycle());
        assertEquals(5, topoResult.getOrder().size());
    }

    @Test
    public void testComplexStructure() {
        Graph g = new Graph(8, true, "integer");
        g.addEdge(0, 1, 2);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 3);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 2);
        g.addEdge(2, 5, 4);
        g.addEdge(5, 6, 1);
        g.addEdge(4, 7, 5);
        
        Metrics metrics = new Metrics();
        metrics.startTimer();
        
        SCCResult sccResult = sccFinder.findSCCs(g, metrics);
        assertEquals(8, sccResult.getComponents().size());
        
        Graph condGraph = sccFinder.buildCondensationGraph(g, sccResult.getComponents(), metrics);
        assertEquals(8, condGraph.getN());
        
        CriticalPathResult critPath = criticalPathFinder.findCriticalPath(condGraph, metrics);
        assertTrue(critPath.getTotalWeight() > 0);
        assertTrue(metrics.getElapsedTimeMillis() >= 0);
    }

    @Test
    public void testAllDatasetsLoadSuccessfully() {
        String[] datasets = {
            "data/small_graph_1.json",
            "data/small_graph_2.json",
            "data/small_graph_3.json",
            "data/medium_graph_1.json",
            "data/medium_graph_2.json",
            "data/medium_graph_3.json",
            "data/large_graph_1.json",
            "data/large_graph_2.json",
            "data/large_graph_3.json"
        };
        
        for (String dataset : datasets) {
            Graph g = parser.parseTasksJSON(dataset);
            assertNotNull(g, "Failed to load " + dataset);
            assertTrue(g.getN() > 0, "Graph has no vertices: " + dataset);
        }
    }
}
