package graph;

import java.util.HashMap;
import java.util.Map;

public class Metrics {
    private long startTime;
    private int operationCount;
    private final Map<String, Integer> counters = new HashMap<>();

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void incrementCounter(String operation) {
        operationCount++;
        counters.put(operation, counters.getOrDefault(operation, 0) + 1);
    }

    public long getStartTime() {
        return startTime;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public Map<String, Integer> getCounters() {
        return new HashMap<>(counters);
    }
}
