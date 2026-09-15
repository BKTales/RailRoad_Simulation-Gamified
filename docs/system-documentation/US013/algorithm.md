# VerifyTrip2 Documentation

## Overview

The `VerifyTrip2` class is responsible for validating connectivity and reachability between train stations represented as nodes in a graph. The connections between them (edges) are read from a CSV file. The class operates on Boolean adjacency matrices and uses **graph theory** and **matrix operations** to determine properties such as:

- Whether all stations of a given type are reachable from each other.
- Whether a specific path exists between two stations.
- Whether the entire network is fully connected (i.e., all trips are possible).
- Whether the connectivity respects train type constraints (e.g., electric trains only using electric lines).

---

## Core Concepts

### Graph Representation

- Stations are represented as nodes in a graph.
- Connections between stations are represented as edges.
- The graph is modeled using an **adjacency matrix**, where:
    - `matrix[i][j] = true` means there is a direct connection from station `i` to station `j`.

### Train Types

- `ELECTRIC = 2` restricts trips to electric-compatible routes (based on a flag in the input).
- `ignoreNonElectric = true` filters out connections that are not electric.

### Station Types

- `D` (Depot), `S` (Station), and `T` (Terminal) are mapped to integers `1`, `2`, and `3`, respectively.

---

## Key Methods and Their Roles

### `initializeAdjacencyMatrix()`

Initializes the boolean adjacency matrix based on CSV connections.

- Parses the input data and sets `matrix[i][j] = true` for each bidirectional connection.
- Filters out non-electric connections if needed.

### `multiplyNTimes()`

Computes the **transitive closure** of the adjacency matrix using Boolean matrix multiplication.

#### Mathematical Explanation:

The matrix \( A \) is the adjacency matrix of a graph. Transitive closure is computed as:

\[
T = A + A^2 + A^3 + \ldots + A^n
\]

Where:
- \( A^k \) denotes the matrix multiplied \( k \) times.
- Logical OR is used instead of addition.
- Logical AND is used instead of multiplication.

This allows checking whether there is any path (direct or indirect) between nodes.

---

## Connectivity Checks

### `isTransitive(boolean[][] matrix)`

Checks whether the matrix satisfies the transitive property:

If \( A[i][j] \) and \( A[j][k] \) are `true`, then \( A[i][k] \) must also be `true`.

### `areAllTripsPossible(boolean[][] matrix)`

Checks if the graph is **fully connected**:

- Every station can reach every other station.
- For all \( i j \), \( matrix[i][j] == true \).

This corresponds to checking if the **transitive closure** matrix is full (except for the diagonal).

---

## Station Type Connectivity

### `isOfType(char type, int[] stationTypes)`
\[
C[i][j] = \bigvee_k (A[i][k] \land B[k][j])
\]

Checks if a character represents a desired station type.

### `areAllOfTypeOn(boolean[][] matrix, String[] stations, int[] stationTypes)`

Checks if all stations of a specific type can reach each other by examining the matrix row-wise.

### `areAllOfTypeConnected(boolean[][] matrix, String[] stations, int[] stationTypes)`

Similar to `areAllOfTypeOn`, but works by collecting relevant station indices and ensuring all are mutually reachable.

---

## File-based Methods

### `verifyTripStationType(File, int[], String[], int)`

Validates if all stations of the given types are connected. Can handle filtering based on electric-only constraints.

### `verifyTripTwoStations(File, String, String, String[], int)`

Checks if a path exists between two given stations.

### `verifyPossibilityOfAllStations(File, String[])`

Checks if the entire network (graph) is connected — i.e., all trips are possible.

---

## Utility Methods

### `multiplyBooleanMatrices(a, b)`

Performs Boolean matrix multiplication:

### `addBooleanMatrices(a, b)`

Performs logical OR addition element-wise.

### `connectionToStation(String, String[], String[][])`

Returns a list of adjacent stations for a given station, optionally filtering for electric lines.

---

## Mathematical Concepts Explained with Java Code

This section dives into the mathematical logic behind key functions in `VerifyTrip2`, especially focusing on **Boolean matrices**, **graph theory**, and **transitive closure**.


### 1. Boolean Adjacency Matrix

#### Concept:
An **adjacency matrix** represents the connections between nodes (in the project context stations). 
So to all the nodes( x1 ) we get all the ones that they are connected with ( x2 ), and we take the position of the array 
which they intersect and change their value to true

### Code:
```java
private boolean[][] initializeAdjacencyMatrix(String[] stations, String[][] connections){
        boolean[][] adjacencyMatrix = new boolean[stations.length][stations.length];
        for(int i = 0; i < stations.length; i++){
            for(int j = 0; j < stations.length; j++){
                adjacencyMatrix[i][j] = false;
            }
        }

        for (int i = 0; i < stations.length; i++){
            int[] indexes = connectionToStation(stations[i], stations, connections);
            for(int j = 0; j < indexes.length; j++){
                adjacencyMatrix[indexes[j]][i] = true;
                adjacencyMatrix[i][indexes[j]] = true;
            }
        }
        return (adjacencyMatrix);
    }
```

## 2. Boolean Matrix Multiplication
### Concept:
Multiplication of BooleanMatrix, that same base as the matrix multiplicity but int which, instead of using
standard arithmetic operations (+, ×), we use:

    Logical OR (||) instead of addition.

    Logical AND (&&) instead of multiplication.

### Code:
```java
private boolean[][] multiplyBooleanMatrices(boolean[][] a, boolean[][] b) {
    int n = a.length;
    boolean[][] result = new boolean[n][n];
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            boolean sum = false;
            for (int k = 0; k < n; k++) {
                sum = sum || (a[i][k] && b[k][j]);
            }
            result[i][j] = sum;
        }
    }
    return result;  
}
```

## 3. Transitive Closure
### Concept:
We want to find if any station can reach any other station, directly or indirectly.
This is called the transitive closure of the graph.

We compute it as:
```
T = A ∨ A^2 ∨ A^3 ∨ … ∨ A^n
```
Where A^n is the adjacency matrix raised to the n^th power using Boolean multiplication

### Code:
```java
public void multiplyNTimes(boolean[][] adjacentMatrix, int size) {
    boolean[][] sum = new boolean[adjacentMatrix.length][adjacentMatrix.length];
    boolean[][] currentPower = adjacentMatrix;

    // Copy A¹ into sum
    for (int i = 0; i < adjacentMatrix.length; i++) {
        System.arraycopy(adjacentMatrix[i], 0, sum[i], 0, adjacentMatrix.length);
    }

    for (int p = 2; p <= size; p++) {
        currentPower = multiplyBooleanMatrices(currentPower, adjacentMatrix);
        sum = addBooleanMatrices(sum, currentPower);
    }

    // Update matrix in-place
    for (int i = 0; i < adjacentMatrix.length; i++) {
        System.arraycopy(sum[i], 0, adjacentMatrix[i], 0, adjacentMatrix.length);
    }
}
```

## 4. Connectivity Check
### Concept:
Function is used to check if the graph has all of its positions checked, this will show us if the graph is fully connect,
and that means that you can go, with the selected train type from every station to another one 
### Code:
```java
public boolean areAllTripsPossible(boolean[][] matrix) {
    int n = matrix.length;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (!matrix[i][j] && i != j) {
                return false;
            }
        }
    }
    return true;
}   
```

## 5. Station Type Filtering and Reachability 
### Concept:
Function is used to check if all stations of a given(one or more) type is reachable.
### Code:
```java
public boolean areAllOfTypeOn(boolean[][] matrix, String[] stations ,int[] stationTypes) {
        boolean toReturn = false;

        // 1 - go through every station
        for (int i = 0; i < stations.length; i++) {
            // 2 - check if it is of one of the wanted type(int[] types)
            if (isOfType(stations[i].charAt(0), stationTypes)) {
                int index = getStationIndex(stations, stations[i]);
                // 3 - check if any of the index of its row in turned on (if so, continue) (if not, return false)
                for (int j = i; j < stations.length; j++) {
                    if (isOfType(stations[j].charAt(0), stationTypes) && !matrix[index][j]){
                        return false;
                    }
                }
            }
        }
        return (true);
    }
    
```

---

#### Java Example:
```java
boolean[][] adjacencyMatrix = new boolean[n][n];
adjacencyMatrix[0][1] = true; // Station 0 connects to 1
adjacencyMatrix[1][2] = true; // Station 1 connects to 2
```

## Notes on Design

- This class encapsulates the logic needed to treat rail networks as graphs.
- The matrix-based approach provides efficient pathfinding and connectivity analysis.
- `multiplyNTimes()` is central to the logic — enabling evaluation of indirect connections using transitive closure.
- The use of `boolean[][]` instead of integer weights simplifies the problem to connectivity rather than cost/distance.

---
