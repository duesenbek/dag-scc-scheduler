package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopoResult {
    private final List<Integer> order;
    private final boolean hasCycle;

    public TopoResult(List<Integer> order, boolean hasCycle) {
        this.order = new ArrayList<>(order);
        this.hasCycle = hasCycle;
    }

    public List<Integer> getOrder() {
        return Collections.unmodifiableList(order);
    }

    public boolean hasCycle() {
        return hasCycle;
    }
}
