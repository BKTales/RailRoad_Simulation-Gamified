# FleuryAlgor Documentation



## Overview

The Fleury Algorithm system provides a complete solution for:
- Finding Eulerian paths/circuits in railway networks
- Visualizing graph traversal in real-time
- Validating network connectivity and Eulerian conditions

---

## Class Overview

### 1. FleuryAlgorithm
**Purpose**: Implements Fleury's algorithm for finding *Eulerian path or circuit** within a graph-based railway network. It is an implementation of **Fleury’s Algorithm**, a graph traversal technique used in graph theory to ensure each edge is visited exactly once without prematurely disconnecting the graph.
This class operates over a graph structure (using the **GraphStream** library for visualization), as well as an **adjacency matrix**, to track and control the traversal process. The algorithm ensures compliance with the core rules of Eulerian graphs: it avoids bridges until necessary and chooses valid starting vertices based on vertex degree parity.

**Key Features**:
- Iterative traversal of edges using a loop to dynamically build the Eulerian path
- Bridge detection through DFS-based connectivity checks to avoid removing critical edges
- Real-time updates to the adjacency matrix and edge collections reflecting edge removals
- Visual updates to the graph by highlighting the current node and removing edges dynamically
- Management of vertex degrees to maintain Eulerian path or circuit conditions
- Supports graphs with Eulerian paths or circuits by analyzing vertex degrees and valid start nodes

### 2. GraphBuilder
**Purpose**: Constructs and manages visual graph representation  
**Key Features**:
- Node/edge creation from railway data
- Visual styling (electrified vs non-electrified lines)
- Interactive highlighting
- Graph visualization using GraphStream

### 3. GraphUtils
**Purpose**: Provides graph analysis utilities  
**Key Features**:
- Connectivity checking
- Adjacency matrix operations
- Degree calculations
- Graph validation

---

## Key Responsibilities

- Traverse the graph using **iterative depth-first search (DFS)** to explore connectivity and support bridge detection
- Find Eulerian paths or circuits by visiting all edges exactly once
- Detect and avoid traversing bridges unless they are the only remaining edges, ensuring correctness of Eulerian path
- Dynamically update the graph representation (adjacency matrix, degrees) and the GraphStream visualization during traversal
- Highlight current nodes visually to aid understanding of traversal progress
- Identify valid starting stations based on vertex degrees according to Eulerian path or circuit criteria
- Support multigraphs by allowing multiple edges between the same nodes
- Use adjacency matrices and station-index mappings to efficiently represent and process graph data

---

## Core Concepts


## Overview
The `GraphBuilder` class is responsible for constructing and visualizing the railway network graph used in Fleury's algorithm implementation. It handles:
- Graph creation from station and line data
- Visual styling of nodes and edges
- Station highlighting for user interaction

## Graph Representation

### Structural Components
- **Nodes**: Represent stations in the railway network
  - Each node is labeled with its station ID
- **Edges**: Represent connections between stations (railway lines)
  - Styled differently for electrified vs non-electrified lines
  - Can optionally include distance attributes

### Data Structures
```java
// Core graph structure
SingleGraph graph = new SingleGraph("Rota");

// Node representation
graph.addNode(stationId);

// Edge representation
Edge edge = graph.addEdge(edgeId, sourceId, targetId);
```
- Stations are represented as nodes in a graph.
- Connections between stations are represented as edges.
- The graph is modeled using an **adjacency matrix**, where:
    - `matrix[i][j] = 1` means there is a direct connection from station `i` to station `j`.

### Key Methods
---### `execute()`
**Purpose**: Main recursive function that implements Fleury's algorithm  
**Parameters**:
- `graph`: Visual representation (GraphStream)
- `path`: Current Eulerian path being constructed
- `adjacencyMatrix`: Current state of connections
- `indexMap`: Station to matrix index mapping
- `lines`: Original edge data
- `degrees`: Current vertex degrees

**Key Operations**:
1. Gets current node from path
2. Highlights current node (`"atual"` CSS class)
3. Finds next edge via `findAndProcessNextLine()`
4. Removes traversed edges (logically)
5. Sleeps 1s for visualization
6. Recurses until all edges traversed

### `findAndProcessNextLine()`
**Purpose**: Selects optimal next edge following Fleury's rules  
**Edge Selection Logic**:
1. Iterates through available edges
2. Prefers non-bridge edges (avoids disconnecting graph)
3. Allows bridge edges only when necessary (last edge)
4. Updates:
  - Adjacency matrix (sets edge to 0)
  - Vertex degrees (decrements both ends)
  - Path (adds new station)

**Bridge Detection**: Simplified check (`degree == 1`)

### `countTotalEdges()`
**Purpose**: Counts remaining edges in upper triangle of adjacency matrix  
**Optimization**: Only checks upper triangle (matrix is symmetric)

## Visualization Features
- Current node highlighted with `"atual"` class
- Final node specially marked
- Optional edge removal animation (commented)
- 1-second delay between steps for visual tracking

## Algorithm Characteristics
- **Type**: Recursive depth-first traversal
- **Edge Priority**: Non-bridge edges first
- **Termination**: When `countTotalEdges()` returns 0
- **Output**: Complete Eulerian path in `path` ArrayList

## Example Flow
```java
// Initialize
String[] stations = {"A","B","C"};
String[][] lines = {{"A","B"},{"B","C"}};
int[][] adjMatrix = {{0,1,0},{1,0,1},{0,1,0}};

// Execute
ArrayList<String> path = new ArrayList<>(List.of("A"));
execute(graph, path, adjMatrix, indexMap, lines, degrees);
// Result: ["A", "B", "C"]
```

## Utility Methods

## `isConnected(String[] stations, String[][] lines)`
**Purpose**: Checks if a railway network graph is fully connected  
**Implementation**:
- Uses DFS traversal from a random start station
- Returns `false` if:
  - Input is null/empty
  - Any station is unreachable

## `dfs(String current, String[][] lines, Map<String,Integer> stationIndex, boolean[] visited)`
**Purpose**: Recursive depth-first search helper  
**Key Logic**:
1. Marks current station as visited
2. Explores all adjacent stations via railway lines
3. Skips invalid/null lines

---

## Mathematical Concepts Explained with Java Code

This section dives into the mathematical logic behind the key functions , especially focusing on **Adjency matrices** and **graph theory**.


### 1. Adjacency Matrix (0/1 Representation)

#### Concept:
An **adjacency matrix** represents the connections between nodes (in the project context stations). 
So to all the nodes( x1 ) we get all the ones that they are connected with ( x2 ), and we take the position of the array 
which they intersect and change their value to 1

### Code:
```java

public static int[][] createAdjacencyMatrix(String[][] railwayLines,
                                            Map<String, Integer> indexMap) {
  int size = indexMap.size();
  int[][] adjacencyMatrix = new int[size][size];

  for (String[] railwayLine : railwayLines) {
    if (railwayLine.length < 2) continue;

    String station1 = railwayLine[0];
    String station2 = railwayLine[1];

    Integer s1 = indexMap.get(station1);
    Integer s2 = indexMap.get(station2);

    if (s1 != null && s2 != null) {
      adjacencyMatrix[s1][s2] = 1;
      adjacencyMatrix[s2][s1] = 1; // undirected connection
    }
  }

  return adjacencyMatrix;
}
```

#### Java Example:
```java
int[][] adjacencyMatrix = new int[n][n];
adjacencyMatrix[0][1] = 1; // Station 0 connects to 1
adjacencyMatrix[1][2] = 1; // Station 1 connects to 2
```
### 2. Vertex Degree Calculation and Odd-Degree Detection

####  Concept:
In graph theory, the **degree of a vertex** is the number of edges connected to it. This information is fundamental for analyzing the structure and properties of a graph — particularly when determining the existence of **Eulerian paths or circuits**.

- An **Eulerian circuit** exists if **all vertices have even degrees**.
   This type of graph is called a **Eulerian Graph**.  
   You can start and end at the same vertex, visiting **every edge exactly once**.
- An **Eulerian path** (but not circuit) exists if **exactly two vertices have odd degrees**. (Graph Semi Eulerian)
  This type of graph is called a **Semi-Eulerian Graph**.  
  You start at one of the odd-degree vertices and finish at the other.
- If the number of odd-degree vertices is **more than two**, the graph **is not Eulerian**, and neither an Eulerian path nor circuit is possible.

Thus, computing degrees and identifying odd-degree vertices is essential when applying Fleury’s Algorithm.

---

#### Method 1: `calculateVertexDegrees(...)`

This method calculates the degree (number of directly connected edges) for each station in the network.

```java
public static Map<String, Integer> calculateVertexDegrees(String[][] railwayLines, String[] stations) {
    Map<String, Integer> degrees = new HashMap<>();
    for (String station : stations) {
        int degree = 0;
        for (String[] line : railwayLines) {
            if (line[0].equals(station) || line[1].equals(station)) {
                degree++;
            }
        }
        degrees.put(station, degree);
    }
    return degrees;
}
```

#### Method 2: `countOddDegreeVertices(...)`

This method filters a map of station degrees and returns only those with odd values.

```java
public static Map<String, Integer> countOddDegreeVertices(Map<String, Integer> degrees) {
    Map<String, Integer> oddDegrees = new HashMap<>();
    for (String station : degrees.keySet()) {
        if (degrees.get(station) % 2 != 0) {
            oddDegrees.put(station, degrees.get(station));
        }
    }
    return oddDegrees;
}
```

## Notes on Design


---
