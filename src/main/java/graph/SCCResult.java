package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SCCResult {
    private final List<List<Integer>> components;
    private final int[] componentOf;

    public SCCResult(List<List<Integer>> components, int[] componentOf) {
        this.components = new ArrayList<>();
        for (List<Integer> comp : components) this.components.add(new ArrayList<>(comp));
        this.componentOf = componentOf.clone();
    }

    public List<List<Integer>> getComponents() {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> comp : components) copy.add(Collections.unmodifiableList(comp));
        return Collections.unmodifiableList(copy);
    }

    public int[] getComponentOf() {
        return componentOf.clone();
    }
}
