# DAG SCC Scheduler: Smart City Task Planning

This project provides a Java-based solution for scheduling and analyzing complex task dependencies, simulating a "Smart City / Smart Campus" scenario. It combines two core computer science topics: **Strongly Connected Components (SCC)** for cycle detection and **Shortest/Longest Path algorithms for DAGs** for optimal planning.

## Core Features

- **SCC Detection**: Uses an iterative version of **Tarjan's algorithm** to find and compress cyclic dependencies in a graph, preventing stack overflow on large datasets.
- **Topological Sorting**: Employs **Kahn's algorithm** to create a linear ordering of tasks from the condensed (acyclic) graph.
- **Critical Path Analysis**: Implements a dynamic programming approach on the topologically sorted graph to find the **longest path (critical path)**, which determines the minimum time to complete all tasks.
- **Shortest Path Analysis**: Finds the **shortest paths** from a given source vertex in the DAG.
- **Instrumentation**: A `Metrics` module tracks performance counters (e.g., DFS visits, edge relaxations) and execution time.
- **Maven Build**: Fully configured with Maven, including dependencies, testing, and packaging.

## Project Structure

```
dag-scc-scheduler/
├── src/
│   ├── main/java/graph/    # Core source code
│   └── test/java/graph/    # JUnit tests
├── data/                     # 9 JSON graph datasets
├── pom.xml                   # Maven configuration
└── README.md                 # Project documentation
```

---

## Build and Run Instructions

### Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher

### 1. Clone and Build

First, build the project from a clean clone to create an executable JAR.

```bash
mvn clean package
```

This command compiles the code, runs all tests, and creates `target/dag-scc-scheduler.jar`.

### 2. Run the Application

Run the application from the command line, passing the path to a dataset file as an argument.

```bash
java -jar target/dag-scc-scheduler.jar [path_to_dataset]
```

**Example:**

```bash
java -jar target/dag-scc-scheduler.jar data/small_graph_1.json
```

### 3. Run Tests

To run the full suite of 44 JUnit tests, use:

```bash
mvn test
```

---

## Dataset Summary

The project includes 9 datasets with varying sizes and structures, stored in the `/data` directory. The weight model is based on **node durations**, where the weight of an edge represents the time cost of the task at its source vertex.

| Dataset             | Vertices | Edges | Structure         | Description                               |
|---------------------|----------|-------|-------------------|-------------------------------------------|
| `small_graph_1.json`  | 8        | 8     | Pure DAG          | Simple acyclic graph.                     |
| `small_graph_2.json`  | 8        | 8     | 1 SCC             | Contains one 3-vertex cycle.              |
| `small_graph_3.json`  | 10       | 11    | 2 SCCs            | Disconnected components with cycles.      |
| `medium_graph_1.json` | 15       | 15    | Pure DAG          | Multiple parallel paths.                  |
| `medium_graph_2.json` | 18       | 24    | Pure DAG          | Complex, dense acyclic graph.             |
| `medium_graph_3.json` | 20       | 22    | 3 SCCs            | Multiple cycles of different sizes.       |
| `large_graph_1.json`  | 30       | 34    | Pure DAG          | Long, linear graph with shortcuts.        |
| `large_graph_2.json`  | 40       | 42    | 3 SCCs            | Large graph with several small cycles.    |
| `large_graph_3.json`  | 50       | 56    | 3 SCCs            | Very large graph with mixed structures.   |

---

## Results and Performance Analysis

The following table summarizes the results of running the pipeline on all 9 datasets.

| Dataset             | Vertices | Edges | SCCs Found | Condensation Edges | Critical Path | Time (ms) | Total Ops |
|---------------------|----------|-------|------------|--------------------|---------------|-----------|-----------|
| `small_graph_1.json`  | 8        | 8     | 8          | 8                  | 4             | 3         | 48        |
| `small_graph_2.json`  | 8        | 8     | 6          | 5                  | 5             | 2         | 37        |
| `small_graph_3.json`  | 10       | 11    | 7          | 6                  | 4             | 2         | 44        |
| `medium_graph_1.json` | 15       | 15    | 15         | 15                 | 10            | 3         | 90        |
| `medium_graph_2.json` | 18       | 24    | 18         | 24                 | 10            | 3         | 114       |
| `medium_graph_3.json` | 20       | 22    | 8          | 7                  | 7             | 3         | 59        |
| `large_graph_1.json`  | 30       | 34    | 30         | 34                 | 29            | 5         | 184       |
| `large_graph_2.json`  | 40       | 42    | 34         | 33                 | 33            | 4         | 209       |
| `large_graph_3.json`  | 50       | 56    | 38         | 41                 | 37            | 5         | 243       |

### Analysis of Results
<img width="1231" height="834" alt="Снимок экрана 2025-11-02 220537" src="https://github.com/user-attachments/assets/54f0fa63-8a9c-4413-aff5-c0f157bdb740" />
<img width="778" height="613" alt="Снимок экрана 2025-11-02 220421" src="https://github.com/user-attachments/assets/805cdbf6-c849-48b8-ab47-009d251a904f" />

1.  **Effect of SCCs on Complexity**: The number of **Total Operations** is not solely dependent on the number of vertices and edges. For instance, `medium_graph_3.json` (20 vertices) required fewer operations (59) than `medium_graph_2.json` (18 vertices, 114 ops). This is because compressing its 3 large SCCs significantly reduced the size of the condensation graph, making topological sort and pathfinding much faster.

2.  **Bottlenecks**: The primary bottleneck for all graphs is the **pathfinding stage** (`cp_kahn` and `TOPO_POPS` counters), which runs on the condensation graph. The cost of finding SCCs (`DFS_VISITS`) is linear (O(V+E)) and consistently fast. However, a dense condensation graph leads to more edge relaxations during pathfinding.

3.  **Performance on Large Graphs**: The iterative implementation of Tarjan's algorithm successfully processed the 50-node graph without stack overflow issues. The execution time remained low (under 5ms), demonstrating the efficiency of the O(V+E) approach.

---
<img width="784" height="616" alt="Снимок экрана 2025-11-02 220453" src="https://github.com/user-attachments/assets/5deeeca0-bb9e-4bf0-953f-8e1818358dec" />

## Conclusions and Recommendations
<img width="799" height="615" alt="Снимок экрана 2025-11-02 220500" src="https://github.com/user-attachments/assets/4349f09f-8ddd-44cf-9fbb-2a4c36cbcaeb" />

- **When to Use**: This pipeline is highly effective for any task-scheduling problem where dependencies can be cyclic. Compressing cycles into single nodes simplifies the problem to a standard DAG analysis, which is computationally efficient.

- **Practical Recommendations**:
    - For **pure DAGs**, the SCC step adds a small, linear overhead but is still very fast. It provides a safety net in case cyclic dependencies are introduced accidentally.
    - For **graphs with many cycles**, the SCC compression is critical. It transforms an otherwise intractable problem into a solvable one by eliminating cycles.
    - The **critical path** is the most important output for scheduling, as it identifies the sequence of tasks that dictates the project's minimum completion time. Any delay on this path will delay the entire project.

This project successfully demonstrates how to combine graph algorithms to solve a practical, real-world problem in scheduling and dependency analysis.
