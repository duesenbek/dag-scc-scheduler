package graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Metrics {
    private long startTime;
    private int operationCount;
    private final Map<String, Integer> counters = new HashMap<>();

    public static final String DFS_VISITS = "DFS_VISITS";
    public static final String EDGE_RELAXATIONS = "EDGE_RELAXATIONS";
    public static final String SCC_COMPONENTS = "SCC_COMPONENTS";
    public static final String TOPO_POPS = "TOPO_POPS";
    public static final String COND_EDGES = "COND_EDGES";
    public static final String CRITICAL_PATH_LENGTH = "CRITICAL_PATH_LENGTH";

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void incrementCounter(String operation) {
        Objects.requireNonNull(operation, "Operation name cannot be null");
        operationCount++;
        counters.put(operation, counters.getOrDefault(operation, 0) + 1);
    }

    public void addCounter(String operation, int value) {
        Objects.requireNonNull(operation, "Operation name cannot be null");
        counters.put(operation, counters.getOrDefault(operation, 0) + value);
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTimeNanos() {
        return System.nanoTime() - startTime;
    }

    public long getElapsedTimeMillis() {
        return getElapsedTimeNanos() / 1_000_000;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public Map<String, Integer> getCounters() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }

    public int getCounter(String operation) {
        return counters.getOrDefault(operation, 0);
    }

    public void reset() {
        startTime = 0;
        operationCount = 0;
        counters.clear();
    }

    public void printMetrics() {
        System.out.println("=== Performance Metrics ===");
        System.out.println("Total Operations: " + operationCount);
        System.out.println("Elapsed Time: " + getElapsedTimeMillis() + " ms");
        System.out.println("--- Counter Details ---");
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    @Override
    public String toString() {
        return String.format("Metrics{ops=%d, elapsed=%dms, counters=%s}",
                operationCount, getElapsedTimeMillis(), counters);
    }
}
