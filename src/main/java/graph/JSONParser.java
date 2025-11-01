package graph;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONParser {
    public Graph parseTasksJSON(String filePath) {
        String json;
        try {
            json = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Pattern nPat = Pattern.compile("\"n\"\\s*:\\s*(\\d+)");
        Pattern dirPat = Pattern.compile("\"directed\"\\s*:\\s*(true|false)");
        Pattern wmPat = Pattern.compile("\"weightModel\"\\s*:\\s*\"([^\"]*)\"");
        Pattern edgesBlockPat = Pattern.compile("\"edges\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL);
        Pattern edgePat = Pattern.compile("\\{[^}]*?\"u\"\\s*:\\s*(\\d+)\\s*,[^}]*?\"v\"\\s*:\\s*(\\d+)\\s*,[^}]*?\"w\"\\s*:\\s*(\\d+)[^}]*?\\}", Pattern.DOTALL);

        int n = -1;
        boolean directed = true;
        String weightModel = "integer";

        Matcher m;
        m = nPat.matcher(json);
        if (m.find()) n = Integer.parseInt(m.group(1));

        m = dirPat.matcher(json);
        if (m.find()) directed = Boolean.parseBoolean(m.group(1));

        m = wmPat.matcher(json);
        if (m.find()) weightModel = m.group(1);

        int maxVertex = -1;
        Matcher edgesBlockMatcher = edgesBlockPat.matcher(json);
        String edgesBlock = null;
        if (edgesBlockMatcher.find()) {
            edgesBlock = edgesBlockMatcher.group(1);
            Matcher em = edgePat.matcher(edgesBlock);
            while (em.find()) {
                int u = Integer.parseInt(em.group(1));
                int v = Integer.parseInt(em.group(2));
                if (u > maxVertex) maxVertex = u;
                if (v > maxVertex) maxVertex = v;
            }
        }

        int finalN = (n > -1) ? n : (maxVertex >= 0 ? maxVertex + 1 : 0);
        Graph g = new Graph(finalN, directed, weightModel);

        if (edgesBlock != null) {
            Matcher em = edgePat.matcher(edgesBlock);
            while (em.find()) {
                int u = Integer.parseInt(em.group(1));
                int v = Integer.parseInt(em.group(2));
                int w = Integer.parseInt(em.group(3));
                g.addEdge(u, v, w);
            }
        }
        return g;
    }
}
