package graph;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONParser {
    private static final Pattern N_PATTERN = Pattern.compile("\"n\"\\s*:\\s*(\\d+)");
    private static final Pattern SOURCE_PATTERN = Pattern.compile("\"source\"\\s*:\\s*(\\d+)");
    private static final Pattern DIRECTED_PATTERN = Pattern.compile("\"directed\"\\s*:\\s*(true|false)");
    private static final Pattern WEIGHT_MODEL_PATTERN = Pattern.compile("\"weightModel\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern EDGES_BLOCK_PATTERN = Pattern.compile("\"edges\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL);
    private static final Pattern EDGE_PATTERN = Pattern.compile(
        "\\{[^}]*?\"u\"\\s*:\\s*(\\d+)\\s*,[^}]*?\"v\"\\s*:\\s*(\\d+)\\s*,[^}]*?\"w\"\\s*:\\s*(\\d+)[^}]*?\\}",
        Pattern.DOTALL
    );

    public Graph parseTasksJSON(String filePath) {
        Objects.requireNonNull(filePath, "File path cannot be null");
        
        String json = readFile(filePath);
        
        int n = extractVertexCount(json);
        boolean directed = extractDirected(json);
        String weightModel = extractWeightModel(json);
        int source = extractSource(json, n);
        
        Graph graph = new Graph(n, directed, weightModel, source);
        addEdgesToGraph(graph, json);
        
        return graph;
    }

    private String readFile(String filePath) {
        try {
            return Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    private int extractVertexCount(String json) {
        Matcher m = N_PATTERN.matcher(json);
        if (m.find()) return Integer.parseInt(m.group(1));
        return inferVertexCount(json);
    }

    private boolean extractDirected(String json) {
        Matcher m = DIRECTED_PATTERN.matcher(json);
        return !m.find() || Boolean.parseBoolean(m.group(1));
    }

    private String extractWeightModel(String json) {
        Matcher m = WEIGHT_MODEL_PATTERN.matcher(json);
        return m.find() ? m.group(1) : "integer";
    }

    private int inferVertexCount(String json) {
        int maxVertex = -1;
        Matcher edgesBlockMatcher = EDGES_BLOCK_PATTERN.matcher(json);
        if (edgesBlockMatcher.find()) {
            String edgesBlock = edgesBlockMatcher.group(1);
            Matcher em = EDGE_PATTERN.matcher(edgesBlock);
            while (em.find()) {
                int u = Integer.parseInt(em.group(1));
                int v = Integer.parseInt(em.group(2));
                if (u > maxVertex) maxVertex = u;
                if (v > maxVertex) maxVertex = v;
            }
        }
        return maxVertex >= 0 ? maxVertex + 1 : 0;
    }

    private int extractSource(String json, int n) {
        Matcher m = SOURCE_PATTERN.matcher(json);
        if (m.find()) {
            int source = Integer.parseInt(m.group(1));
            if (source < 0 || source >= n) {
                throw new IllegalArgumentException("Source vertex out of bounds: " + source);
            }
            return source;
        }
        return 0;
    }

    private void addEdgesToGraph(Graph graph, String json) {
        Matcher edgesBlockMatcher = EDGES_BLOCK_PATTERN.matcher(json);
        if (!edgesBlockMatcher.find()) return;
        
        String edgesBlock = edgesBlockMatcher.group(1);
        Matcher em = EDGE_PATTERN.matcher(edgesBlock);
        while (em.find()) {
            int u = Integer.parseInt(em.group(1));
            int v = Integer.parseInt(em.group(2));
            int w = Integer.parseInt(em.group(3));
            graph.addEdge(u, v, w);
        }
    }
}
