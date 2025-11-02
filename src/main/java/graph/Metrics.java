package graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Metrics {
    private long startTime;
    private int operationCount;
    private final Map<String, Integer> counters = new HashMap<>();

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void incrementCounter(String operation) {
        Objects.requireNonNull(operation, "Operation name cannot be null");
        operationCount++;
        counters.put(operation, counters.getOrDefault(operation, 0) + 1);
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTimeNanos() {
        return System.nanoTime() - startTime;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public Map<String, Integer> getCounters() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }

    public void reset() {
        startTime = 0;
        operationCount = 0;
        counters.clear();
    }

    @Override
    public String toString() {
        return String.format("Metrics{ops=%d, elapsed=%dms, counters=%s}",
                operationCount, getElapsedTimeNanos() / 1_000_000, counters);
    }
}
