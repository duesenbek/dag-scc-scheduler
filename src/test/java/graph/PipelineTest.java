package graph;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PipelineTest {

    private final JSONParser parser = new JSONParser();
    private final TarjanSCC sccFinder = new TarjanSCC();
    private final KahnTopologicalSort topoSort = new KahnTopologicalSort();
    private final CriticalPathFinder criticalPathFinder = new CriticalPathFinder();

    @Test
    public void testFullPipelineCorrectness() {
        // 1. Create a deterministic graph
        Graph g = new Graph(8, true, "integer", 0);
        g.addEdge(0, 1, 2); // SCC 1
        g.addEdge(1, 2, 3);
        g.addEdge(2, 0, 1);
        g.addEdge(1, 3, 5); // Edge from SCC 1 to SCC 2
        g.addEdge(3, 4, 2); // SCC 2
        g.addEdge(4, 5, 1);
        g.addEdge(5, 3, 1);
        g.addEdge(4, 6, 4); // Edge from SCC 2 to SCC 3
        g.addEdge(6, 7, 3); // SCC 3 (single node)

        Metrics metrics = new Metrics();

        // 2. Find SCCs
        SCCResult sccResult = sccFinder.findSCCs(g, metrics);
        assertEquals(4, sccResult.getComponents().size(), "Should find 4 components");

        // 3. Build Condensation Graph
        Graph condensationGraph = sccFinder.buildCondensationGraph(g, sccResult.getComponents(), metrics);
        assertEquals(4, condensationGraph.getN(), "Condensation graph should have 4 vertices");
        assertEquals(3, condensationGraph.getEdges().size(), "Condensation graph should have 3 edges");

        // 4. Topological Sort
        TopoResult topoResult = topoSort.topologicalSort(condensationGraph, metrics);
        assertFalse(topoResult.hasCycle(), "Condensation graph must be a DAG");
        assertEquals(4, topoResult.getOrder().size(), "Topological order should include all components");

        // 5. Critical Path
        CriticalPathResult criticalPath = criticalPathFinder.findCriticalPath(condensationGraph, metrics);
        assertTrue(criticalPath.getTotalWeight() > 0, "Critical path weight should be positive");
        assertFalse(criticalPath.getPath().isEmpty(), "Critical path should not be empty");
    }
}
