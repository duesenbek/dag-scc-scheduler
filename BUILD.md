# Build and Run Guide

## Prerequisites
- Java 11 or higher
- Maven 3.6.0 or higher

## Building the Project

### Clean Build
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Package JAR
```bash
mvn clean package
```

This creates two JAR files in `target/`:
- `dag-scc-scheduler.jar` - Executable JAR with all dependencies
- `dag-scc-scheduler-sources.jar` - Source JAR

## Running the Application

### Using Maven
```bash
mvn exec:java -Dexec.mainClass="graph.SchedulerApplication" -Dexec.args="data/small_graph_1.json"
```

### Using JAR
```bash
java -jar target/dag-scc-scheduler.jar data/small_graph_1.json
```

## Example Datasets

The project includes 9 pre-built test datasets in the `data/` directory:

### Small Graphs (6-10 nodes)
- `small_graph_1.json` - Pure DAG
- `small_graph_2.json` - DAG with cycles
- `small_graph_3.json` - Mixed structure

### Medium Graphs (10-20 nodes)
- `medium_graph_1.json` - DAG with multiple paths
- `medium_graph_2.json` - Complex DAG
- `medium_graph_3.json` - Mixed structure with cycles

### Large Graphs (20-50 nodes)
- `large_graph_1.json` - Linear DAG with shortcuts
- `large_graph_2.json` - DAG with cycles
- `large_graph_3.json` - Large mixed structure

## Test Results

All 43 tests pass successfully:
- **SCCTest**: 8 tests for SCC detection
- **TopoSortTest**: 9 tests for topological sorting
- **CriticalPathTest**: 9 tests for critical path finding
- **DatasetTest**: 17 tests for dataset loading and processing

## Maven Plugins

The POM includes:
- **maven-compiler-plugin**: Java 11 compilation
- **maven-surefire-plugin**: JUnit 5 test execution
- **maven-jar-plugin**: JAR packaging with manifest
- **maven-assembly-plugin**: Fat JAR creation
- **maven-javadoc-plugin**: Javadoc generation

## Generating Javadoc

```bash
mvn javadoc:javadoc
```

Javadoc will be generated in `target/site/apidocs/`

## Project Structure

```
dag-scc-scheduler/
├── src/
│   ├── main/java/graph/
│   │   ├── Graph.java
│   │   ├── Edge.java
│   │   ├── Metrics.java
│   │   ├── TarjanSCC.java
│   │   ├── KahnTopologicalSort.java
│   │   ├── CriticalPathFinder.java
│   │   ├── DAGShortestPath.java
│   │   ├── SchedulerApplication.java
│   │   └── ... (other classes)
│   └── test/java/graph/
│       ├── SCCTest.java
│       ├── TopoSortTest.java
│       ├── CriticalPathTest.java
│       └── DatasetTest.java
├── data/
│   ├── small_graph_*.json
│   ├── medium_graph_*.json
│   └── large_graph_*.json
├── pom.xml
└── README.md
```

## Performance Notes

- **TarjanSCC**: Uses iterative approach to handle large graphs (50+ nodes)
- **KahnTopologicalSort**: O(V+E) complexity
- **CriticalPathFinder**: O(V+E) using topological sort + DP
- **DAGShortestPath**: O(V+E) for both shortest and longest paths

## Troubleshooting

### Maven not found
Ensure Maven is installed and in your PATH:
```bash
mvn --version
```

### Java version mismatch
The project requires Java 11+. Check your version:
```bash
java -version
```

### Tests fail
Run tests with verbose output:
```bash
mvn test -X
```

### Build fails
Clean and rebuild:
```bash
mvn clean install
```
