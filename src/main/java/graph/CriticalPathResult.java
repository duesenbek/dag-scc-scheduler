package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CriticalPathResult {
    private final List<Integer> path;
    private final int totalWeight;

    public CriticalPathResult(List<Integer> path, int totalWeight) {
        this.path = new ArrayList<>(path);
        this.totalWeight = totalWeight;
    }

    public List<Integer> getPath() {
        return Collections.unmodifiableList(path);
    }

    public int getTotalWeight() {
        return totalWeight;
    }
}
