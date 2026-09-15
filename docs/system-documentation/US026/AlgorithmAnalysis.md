# Railway Maintenance Route Problem

### US014 - As a Player, given a railway with stations and railway lines, I want to see a route that passes once, and only once, by each railway line to carry out maintenance on the lines.

## Algorithms and Complexity Analysis:

### 1. Read Stations file

#### **Pseudo Code:**

```pseudo
Procedure: Read Stations from File

1a: stations := null
2a: try (br := new BufferedReader(new FileReader(stationFile)))
3a:    line := br.readLine()
4a:    if line ≠ null then
5a:        stations := split(line, ";")
6a: catch IOException
7a:    print("Error reading the file")
8a: return stations
```

#### **Complexity Analysis:**

| Operations/lines                         | Algorithm           |
|------------------------------------------|---------------------|
| 1ª `stations := null`                    | 1A                  |
| 3ª `line := br.readLine()`               | 1A                  |
| 4ª `if (line != null)`                   | 1C                  |
| 5ª `stations := split(line, ";")`        | nA                  |
| 7ª `return stations`                     | 1R                  |
|                                          | **(2 + n)A + 1C + 1R**  |
| **Total**                                | **n + 5**           |
| **Time Complexity Estimate (Big O)**     | **O(n)**            |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This method reads the first line of a CSV file, splits it by the delimiter ";" to extract station names, and returns an array of these stations. The time complexity is O(n), where n is the number of stations in the first line.

### 2. Read lines file

#### **Pseudo Code:**

```pseudo
Procedure: Read lines from File

1a: lines := empty list of string arrays
2a: try (br := new BufferedReader(new FileReader(stationFile)))
3a:     while (line := br.readLine() ≠ null)
4a:         values := line.split(";");
5a:         row := new string array of size 3
6a:         row[0] := values[0]
7a:         row[1] := values[1]
8a:         row[2] := values[2]
9a:         add row to lines
10a: catch IOException
11a:    print("Error reading the file")
12a: a := convert lines list to 2D string array with dimensions [lines.size()][3]
13a:    return a
```

#### **Complexity Analysis:**

| Operations/lines                     | Algorithm                             |
| ------------------------------------ | ------------------------------------- |
| 1ª `lines := empty list`             | 1A                                    |
| 3ª `while (line := br.readLine())`   | nA + (n + 1)C                         |
| 4ª `values := line.split(";")`       | nA                                    |
| 5ª `row := new string array [3]`     | (n × k)A                              |
| 6ª `row[0] := values[0]`             | nA                                    |
| 7ª `row[1] := values[1]`             | nA                                    |
| 8ª `row[2] := values[2]`             | nA                                    |
| 9ª `add row to lines`                | nA                                    |
| 11ª `convert list to 2D array`       | nA                                    |
| 12ª `return a`                       | 1R                                    |
|                                      | **(5 + k)nA + (n + 1)C + 4A + 1R**    |
| **Total**                            | **(8 + k)n + 6**, where k is constant |
| **Time Complexity Estimate (Big O)** | **O(n)**                              |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return

>This method reads all lines from a CSV file, splits each line by the delimiter `";"` into arrays of strings (considering only the first 3 elements), and stores them in a list. It then converts the list into a 2D array and returns it.  
🔍 **Note:** Although `k` (the average number of columns per line) appears in the complexity expression, it is constant in this case (each line has a fixed, small format).  
Therefore, the final complexity remains **O(n)**, where `n` is the number of lines.
If `k` were not constant (e.g. variable-length lines or fields), the complexity would be **O(n × k)**.


### 3. Clean station names

#### **Pseudo Code:**

```pseudo
Procedure: Clean station names

1a: stationsValidName := new string array of size stations.length
2a: for i:= 1 to stations.length - 1
3a:     stationsValidName[i] := stations[i].replace("_", "").trim()
4a: return stationsValidName
```

#### **Complexity Analysis:**

| Operations/lines                                                   | Algortithom                                 |
|--------------------------------------------------------------------|---------------------------------------------|
| 1ª `stationsValidName := new string array of size stations.length` | nA                                          |
| 2ª `for i := 0 to stations.length - 1  `                           | (n + 1)(AouI + C)                           |
| 3ª `stationsValidName[i] := stations[i].replace("_", "").trim()`   | (n × k)A                                    |
| 4ª `return stationsValidName`                                      | 1R                                          |
|                                                                    | **(kn + n)A + (n + 1)AouI + (n + 1)C + 1R** |
| **Total**                                                          | **(3 + k)n + 4**                            |
| **Time Complexity Estimate (Big O)**                               | **O(n × k)**                                |

**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This method iterates through all station names, cleaning each one by removing underscores and trimming whitespace. Since string operations like `replace()` and `trim()` process each character of the input string, their cost is proportional to the length of each string (`k`).  
>Thus, the overall time complexity is **O(n × k)**, where:  
>- `n` is the number of station names  
>- `k` is the average length of each name 

### 4. Clean line names

#### **Pseudo Code:**

```pseudo
Procedure: Clean line names

1a: linesValidName := new 2D string array of size [lines.length][lines[0].length]
2a: for i := 0 to lines.length - 1
3a:     for j := 0 to lines[0].length - 1
4a:         if j == 0 or j == 1 then
5a:             linesValidName[i][j] := replace(lines[i][j], "_", "").trim()
6a:         else
7a:             linesValidName[i][j] := lines[i][j]
8a: return linesValidName

```

#### **Complexity Analysis:**

| Operations/lines                                                                   | Algortithom                                                     |
|------------------------------------------------------------------------------------|-----------------------------------------------------------------|
| 1ª `linesValidName := new 2D string array of size [lines.length][lines[0].length]` | (n × m)A                                                        |
| 2ª `for i := 0 to lines.length - 1`                                                | (n + 1)(AouI + C)                                               |
| 3ª `for j := 0 to lines[0].length - 1`                                             | n × (m + 1)(AouI + C)                                           |
| 4ª `if j == 0 or j == 1`                                                           | (2m × n)C                                                       |
| 5ª `linesValidName[i][j] := replace(lines[i][j], "_", "").trim()`                  | (2n × k)A                                                       |
| 6ª `linesValidName[i][j] := lines[i][j]`                                           | ((m - 2) × n)A                                                  |
| 7ª `return linesValidName`                                                         | 1R                                                              |
|                                                                                    | **(2nk + 2nm - n)A + (2n + nm + 1)AouI + (2n + 3nm + 1)C + 1R** |
| **Total**                                                                          | **(3 + k + 6m)n + 3**                                           |
| **Time Complexity Estimate (Big O)**                                               | **O(n × k)**                                                    |



**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


> This method iterates over a 2D array of strings representing lines, cleaning the values in the first two columns (`j == 0` or `j == 1`) by removing underscores and trimming whitespace.  
> All other columns are copied directly without modification.  
>
> 🔍 **Note:** While `k` (the average number of characters per string) appears in the complexity expression due to `replace()` and `trim()`, it is applied only to two columns per row.  
> The number of columns `m` is small and constant (e.g. 3), so it does not influence the Big O complexity.  
>
> Therefore, the final time complexity is **O(n × k)**, where:  
> - `n` is the number of rows (lines)  
> - `k` is the average length of the modified strings (first two columns)  
>
> If either `m` or `k` were not constant, the complexity could approach **O(n × m × k)**.

### 5. Filter Lines

#### **Pseudo Code:**

```pseudo
Procedure: filterLines(allLines, maintenanceType)

1a: if maintenanceType == 0 then
2a:     return allLines
3a: filteredList := empty list of string arrays
4a: for each line in allLines do
5a:     if line.length >= 3 then
6a:         lineType := parseInt(line[2])
7a:             if lineType == 1 then
8a:                 add line to filteredList
9a: return filteredList converted to String[][]
```

#### **Complexity Analysis:**

| Operations/lines                         | Algorithm                              |
|------------------------------------------|----------------------------------------|
| 1ª `if maintenanceType == 0`             | 1C                                     |
| 2ª `return allLines`                     | 1R                                     |
| 3ª `filteredList := empty list`          | 1A                                     |
| 4ª `for each line in allLines`           | nAouI + (n + 1)C                       |
| 5ª `if line.length >= 3`                 | nC                                     |
| 6ª `parseInt(line[2])`                   | ≤ nA                                   |
| 7ª `if lineType == 1`                    | nC                                     |
| 8ª `add line to filteredList`            | ≤ nA                                   |
| 9ª `return list converted to String[][]` | nA + 1R                                |
|                                          | **(3n + 1)A + nAouI + (3n + 2)C + 1R** |
| **Total**                                | **8n + 4**                             |
| **Time Complexity Estimate (Big O)**     | **O(n)**                               |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


> This method filters a 2D array of strings based on a maintenance type. It iterates through all lines, checks a condition on each, and adds the matching ones to a new list. The time complexity is **O(n)**, where n is the number of lines.

### 6. Graph Connected

#### **Pseudo Code:**

```pseudo
Procedure: isConnected(stations, lines)

1a: if stations == null or stations.length == 0 or lines == null or lines.length == 0 then
2a:     return false
3a: stationIndex := getStationIndexMap(lines)
4a: visited := new boolean of stationIndex size
5a: keys := array of keys from stationIndex
6a: startStation := keys[0]
7a: call dfs(startStation, lines, stationIndex, visited)
8a: for each v in visited do
9a:     if v == false then
10a:        return false
11a: return true
```

#### **Complexity Analysis:**

| Operations/lines                                                                       | Algorithm                                          |
|----------------------------------------------------------------------------------------|----------------------------------------------------|
| 1a `if stations == null or stations.length == 0 or lines == null or lines.length == 0` | ≤ 4C                                               |
| 2a `return false`                                                                      | 1R                                                 |
| 3a `stationIndex := getStationIndexMap()` — [Station Index Map](#7-station-index-map)  | O(n) + 1A                                          |
| 4a `visited := new boolean of stationIndex size`                                       | 1A                                                 |
| 5a `keys := array of keys...`                                                          | nA                                                 |
| 6a `startStation := keys[0]`                                                           | 1A                                                 |
| 7a `call dfs(...)` —  [DFS](#8-DFS)                                                    | O(n × m)                                           |
| 8a `for each v in visited`                                                             | nAouI + (n + 1)C                                   |
| 9a `if v == false`                                                                     | nC                                                 |
| 10a `return false`                                                                     | 1R                                                 |
| 11a `return true`                                                                      | 1R                                                 |
|                                                                                        | O(n × m) + O(n) + (n + 2)A + (n + 5)C + nAouI + 1R |
|                                                                                        | O(n × m) + O(n) + 3n + 8                           |
| **Total (approx.)**                                                                    | **O(m × n)**                                       |



**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>The isConnected method checks if all stations are reachable using DFS. It builds an index map and traverses the network, visiting all connected stations. Its time complexity is **O(m × n)**, where m is the number of stations and n the number of lines.



### 7. Station Index Map

#### **Pseudo Code:**

```
Procedure: getStationIndexMap(lines)
1a: map := new map
2a: index := 0
3a: for each line in lines do
4a:     s1 := line[0]
5a:     s2 := line[1]
6a:     if s1 ≠ null AND s1 ≠ "" AND map does not contain s1 then
7a:         map.add(s1, index)
8a:         index++
9a:     if s2 ≠ null AND s2 ≠ "" AND map does not contain s2 then
10a:        map.add(s2, index)
11a:        index++
12a: return map

```

| Operations/lines                                          | Algorithm                                |
|-----------------------------------------------------------|------------------------------------------|
| 1b `map := new map`                                       | 1A                                       |
| 2b `index := 0`                                           | 1A                                       |
| 3b `for each line in lines`                               | nAouI + (n + 1)C                         |
| 4b `s1 := line[0]`                                        | nA                                       |
| 5b `s2 := line[1]`                                        | nA                                       |
| 6b `if s1 ≠ null AND s1 ≠ "" AND map does not contain s1` | ≤ 3nC                                    |
| 7b `map.add(s1, index)`                                   | ≤ nA                                     |
| 8b `index++`                                              | ≤ nI                                     |
| 9b `if s2 ≠ null AND s2 ≠ "" AND map does not contain s2` | ≤ 3nC                                    |
| 10b `map.add(s2, index)`                                  | ≤ nA                                     |
| 11b `index++`                                             | ≤ nI                                     |
| 12b `return map`                                          | 1R                                       |
|                                                           | (2 + 4n)A + nAouI + (1 + 6n)C + 2nI + 1R |
| **Total**                                                 | 13n + 4                                  |
| **Time Complexity (Big O)**                               | **O(n)**                                 |

**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


> This method constructs a map that assigns a unique index to each distinct station name found in a 2D array of lines. It iterates through each line, extracts two station names, and conditionally adds them to the map if they are valid and not already present. The method ensures uniqueness and handles input validation through multiple comparisons. The total time complexity is **O(n)**, where n is the number of lines.

### 8. DFS

#### **Pseudo Code:**

```
Procedure: dfs(start, lines, stationIndex, visited)
1a: n := stationIndex.size
2a: stack := new string array of size n
3a: top := 0
4a: stack[top] := start
5a: top++
6a: visited[stationIndex[start]] := true

7a: while top > 0 do
8a:     --top
9a:     current := stack[top]

10a:    for each line in lines do
12a:        if line == null or line.length < 2 then
13a:            continue
14a:        s1 := line[0]
15a:        s2 := line[1]
16a:        if s1 == null or s2 == null or s1 == "" or s2 == "" then
17a:            continue

18a:        neighbor := null
19a:        if s1 == current then
20a:            neighbor := s2
21a:        else if s2 == current then
22a:            neighbor := s1

23a:        if neighbor ≠ null then
24a:            neighborIndex := stationIndex[neighbor]
25a:            if not visited[neighborIndex] then
26a:                visited[neighborIndex] := true
27a:                stack[top] := neighbor
28a:                top++1

```

| Operations/lines                                          | Algorithm                                                     |
|-----------------------------------------------------------|---------------------------------------------------------------|
| 1c `n := stationIndex.size`                               | 1A                                                            |
| 2c `stack := new string array of size n`                  | 1A                                                            |
| 3c `top := 0`                                             | 1A                                                            |
| 4c `stack[top] := start`                                  | 1A                                                            |
| 5c `top++`                                                | 1I                                                            |
| 6c `visited[stationIndex[start]] := true`                 | 1A                                                            |
| 7c `while top > 0`                                        | (n + 1)C                                                      |
| 8c `--top`                                                | nI                                                            |
| 9c `current := stack[top]`                                | nA                                                            |
| 10c `for each line in lines do     `                      | n(mAouI + (m+1)C)                                             |
| 12c `if line == null or line.length < 2`                  | ≤ 2m × nC                                                     |
| 14c `s1 := line[0]`                                       | m × nA                                                        |
| 15c `s2 := line[1]`                                       | m × nA                                                        |
| 16c `if s1 == null or s2 == null or s1 == "" or s2 == ""` | ≤ 4m × nC                                                     |
| 18c `neighbor := null`                                    | ≤ m × nA                                                      |
| 19c `if s1 == current`                                    | ≤ m × nC                                                      |
| 20c `neighbor := s2`                                      | ≤ m × nA                                                      |
| 21c `else if s2 == current`                               | ≤ m × nC                                                      |
| 22c `neighbor := s1`                                      | ≤ m × nA                                                      |
| 23c `if neighbor ≠ null`                                  | ≤ m × nC                                                      |
| 24c `neighborIndex := stationIndex[neighbor]`             | ≤ m × nA                                                      |
| 25c `if not visited[neighborIndex]`                       | ≤ m × nC                                                      |
| 26c `visited[neighborIndex] := true`                      | ≤ m × nA                                                      |
| 27c `stack[top] := neighbor`                              | ≤ m × nA                                                      |
| 28c `top++`                                               | ≤ m × nI                                                      |
|                                                           | (8mn + n + 5)A + (mn + n + 1)I + (10mn + n + m + 2)C + mnAouI |
| **Total**                                                 | 20mn + 3n + m + 8                                             |
| **Time Complexity (Big O)**                               | **O(m × n)**                                                  |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This method performs an iterative depth-first search (DFS) over a graph represented by a 2D array of station connections. It processes each station and scans all lines to find neighbors, resulting in a nested loop structure. The total time complexity is **O(m × n)**, where n is the number of stations and m is the number of lines.

### 9. Vertex Degrees

#### **Pseudo Code:**

```
Procedure: calculateVertexDegrees(lines,stations)
1a: degrees := new map
2a: for each station in stations do
3a:     degree := 0
4a:     for each line in railwayLines do
5a:         if line[0] == station or line[1] == station then
6a:             degree := degree + 1
7a:      degrees.add(station, degree)
8a: return degrees
```

| Operations/lines                                 | Algorithm                                             |
|--------------------------------------------------|-------------------------------------------------------|
| 1a `degrees := new map`                          | 1A                                                    |
| 2a `for each station in stations`                | nAouI + (n + 1)C                                      |
| 3a `degree := 0`                                 | nA                                                    |
| 4a `for each line in railwayLines`               | n(mAouI + (m + 1)C)                                   |
| 5a `if line[0] == station or line[1] == station` | ≤ 2m × nC                                             |
| 6a `degree := degree + 1` (conditionally)        | ≤ m × nI                                              |
| 7a `degrees.add(station, degree)`                | nA                                                    |
| 8a `return degrees`                              | 1R                                                    |
|                                                  | (2n + 1)A + mnI + (3mn + n + 2)C + (mn + n)AouI  + 1R |
| **Total**                                        | 3mn + 4n + 4                                          |
| **Time Complexity (Big O)**                      | **O(m × n)**                                          |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This method calculates the degree of each station by iterating through all stations and, for each, scanning all railway lines to count connections. The nested loops over n stations and m lines result in a time complexity of **O(m × n)**.

### 10. Vertex Odd Degrees

#### **Pseudo Code:**

```
Procedure: countOddDegreeVertices(degrees)
1o: oddDegrees := new map
2o: for each element station in the key set of the map degrees do
3o:     if degrees[station] mod 2 ≠ 0 then
4o:         oddDegrees.add(station,degrees[station])
5o: return oddDegrees

```

| Operations/lines                                                   | Algorithm                         |
|--------------------------------------------------------------------|-----------------------------------|
| 1o `oddDegrees := new map`                                         | 1A                                |
| 2o `for each element station in the key set of the map degrees do` | nAouI + (n + 1)C                  |
| 3o `if degrees[station] mod 2 ≠ 0`                                 | nC                                |
| 4o `oddDegrees.add(station, degrees[station])`                     | ≤ nA                              |
| 5o `return oddDegrees`                                             | 1R                                |
|                                                                    | (n + 1)A + (2n + 1)C + nAouI + 1R |
| **Total**                                                          | 4n + 3                            |
| **Time Complexity (Big O)**                                        | **O(n)**                          |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure iterates over all station-degree pairs and checks for odd degrees, storing them in a new map. Each operation is performed once per station, resulting in a total time complexity of **O(n)**, where n is the number of stations in the degree map.

### 11. Valid Start Stations

#### **Pseudo Code:**

```
Procedure: getValidStations(oddCount, degrees, stations)
1a: if oddCount.size() == 2 then
2a:     validStations := new list initialized with keys of oddCount
3a: else
4a:     validStations := new empty list
5a:     for each station in stations do
6a:         if degrees[station] ≠ 0 then
7a:             validStations.add(station)
8a: return validStations

```

| Operations/lines                                     | Algorithm                             |
|------------------------------------------------------|---------------------------------------|
| 1a `if oddCount.size() == 2`                         | 1C                                    |
| 2a `validStations := new list initialized with keys` | ≤ kA                                  |
| 4a `validStations := new empty list`                 | 1A                                    |
| 5a `for each station in stations`                    | nAouI + (n + 1)C                      |
| 6a `if degrees[station] ≠ 0`                         | nC                                    |
| 7a `validStations.add(station)`                      | ≤ nA                                  |
| 8a `return validStations`                            | 1R                                    |
|                                                      | (n + k + 1)A + (2n + 2)C + nAouI + 1R |
| **Total**                                            | 4n + k + 4                            |
| **Time Complexity (Big O)**                          | **O(n)**                              |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure verifies whether there are exactly two stations with an odd degree. If so, it collects them directly; otherwise, it iterates through all stations and selects those with a non-zero degree. As each station is processed at most once, the total time complexity is **O(n)**, where *n* is the number of stations.

### 12. Create Adjacency Matrix

#### **Pseudo Code:**

```
Procedure: createAdjacencyMatrix(railwayLines, indexMap)
1a: size := indexMap.size()
2a: adjacencyMatrix := new int[size][size]

3a: for each railwayLine in railwayLines do
4a:     if railwayLine.length < 2 then
5a:         continue
6a:     station1 := railwayLine[0]
7a:     station2 := railwayLine[1]
8a:     s1 := indexMap[station1]
9a:     s2 := indexMap[station2]

10a:    if s1 ≠ null and s2 ≠ null then
11a:        adjacencyMatrix[s1][s2]++
12a:        adjacencyMatrix[s2][s1]++
13a: return adjacencyMatrix
```

| **Operations/lines**                        | **Algorithm**                                 |
|---------------------------------------------|-----------------------------------------------|
| 1a `size := indexMap.size`                  | 1A                                            |
| 2a `adjacencyMatrix := new int[size][size]` | n²A                                           |
| 3a `for each railwayLine in railwayLines`   | mAouI + (m + 1)C                              |
| 4a `if railwayLine.length < 2`              | mC                                            |
| 6a `station1 := railwayLine[0]`             | ≤ mA                                          |
| 7a `station2 := railwayLine[1]`             | ≤ mA                                          |
| 8a `s1 := indexMap[station1]`               | ≤ mA                                          |
| 9a `s2 := indexMap[station2]`               | ≤ mA                                          |
| 10a `if s1 ≠ null and s2 ≠ null`            | ≤ 2mC                                         |
| 11a `adjacencyMatrix[s1][s2]++`             | ≤ mI                                          |
| 12a `adjacencyMatrix[s2][s1]++`             | ≤ mI                                          |
| 13a `return adjacencyMatrix`                | 1R                                            |
|                                             | (n² + 4m + 1)A + (4m + 1)C + mAouI + 2mI + 1R |
| **Total**                                   | n² + 11m + 3                                  |
| **Time Complexity (Big O)**                 | **O(n²)**                                     |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This method builds an adjacency matrix for a graph with n stations by initializing an n² matrix and updating it for each of the m railway lines. The overall time complexity is O(n²), dominated by the matrix initialization.

### 13. Copy Lines 

#### **Pseudo Code:**

```
Procedure cloneArray(array)
1a: n := length(array)          
2a: m := length(array[0])        
3a: copy := new 2D array[n][m]
4a: for i from 0 to n-1 do
5a:     for j from 0 to m-1 do
6a:         copy[i][j] := array[i][j]
7a: return copy

```

| **Operations/lines**            | **Algorithm**                                  |
|---------------------------------|------------------------------------------------|
| 1  `n := length(array)`         | 1A                                             |
| 2  `m := length(array[0])`      | 1A                                             |
| 3  `copy := new 2D array[n][m]` | n × m A                                        |
| 4  `for i from 0 to n-1`        | n AouI + (n+1)C                                |
| 5  `for j from 0 to m-1`        | n × (m AouI + (m+1)C)                          |
| 6  `copy[i][j] := array[i][j]`  | n × m A                                        |
| 7  `return copy`                | 1R                                             |
|                                 | (2mn + 2)A + (2n + 1 +mn)C + (n + mn)AouI + 1R |
| **Total**                       | 4mn  + 3n + 4                                  |
| **Time Complexity (Big O)**     | **O(m × n)**                                   |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


> This procedure copies a 2D array by iterating through all its elements. Since each of the n × m elements is visited once, the time complexity is O(n × m). In this case m equals n so the time complexity can be described as O(n²).

### 14. Execute path

#### **Pseudo Code:**

```
Procedure execute(graph, path, adjacencyMatrix, indexMap, lines, degrees)
1a:     while countTotalEdges(adjacencyMatrix) > 0 do
2a:         current := path[path.size() - 1]
3a:         i := indexMap[current]

4a:         /*node := graph.getNode(current)
5a:         if node ≠ null then
6a:             node.setAttribute("ui.class", "atual")*/

7a:         toRemoveLater := findAndProcessNextLine(path, current, i, adjacencyMatrix, indexMap, lines, degrees)
8a:         if toRemoveLater = null then
9a:             break

10a:        node1 := toRemoveLater[0]
11a:        node2 := toRemoveLater[1]

12a:        /*for each edge in graph.edges() do
13a:            src := edge.getSourceNode().getId()
14a:            tgt := edge.getTargetNode().getId()
15a:            if (src = node1 and tgt = node2) or (src = node2 and tgt = node1) then
16a:                graph.removeEdge(edge)
17a:                break*/ 

18a:        try
19a:            sleep(10)
20a:        catch InterruptedException
21a:            interrupt current thread

22a:        for k := 0 to lines.length - 1 do
23a:            if lines[k] = toRemoveLater then
24a:                lines[k] := null
25a:                break

```

| **Operations / Lines**                                                                       | **Category / Cost**                 |
|----------------------------------------------------------------------------------------------|-------------------------------------|
| `1a` `while countTotalEdges(...) > 0` — [Count Total Edges](#19-count-total-edges)           | ≤ nO(n²) + nC                       |
| `2a` `current := path[path.size() - 1]`                                                      | ≤ nA                                |
| `3a` `i := indexMap[current]`                                                                | ≤ nA                                |
| `7a` `toRemoveLater := findAndProcessNextLine(...)` — [Find and Process](#15-find-next-line) | ≤ n × O(n³) + nA                    |
| `8a` `if toRemoveLater = null`                                                               | ≤ nC                                |
| `9a` `break`                                                                                 | ≤ nR                                |
| `10a` `node1 := toRemoveLater[0]`                                                            | ≤ nA                                |
| `11a` `node2 := toRemoveLater[1]`                                                            | ≤ nA                                |
| `22a` `for k := 0 to lines.length - 1`                                                       | ≤ n(nA + (n+1)C)                    |
| `23a` `if lines[k] = toRemoveLater`                                                          | ≤ nC                                |
| `24a` `lines[k] := null`                                                                     | ≤ nA                                |
| `25a` `break`                                                                                | ≤ 1R                                |
|                                                                                              | n × O(n³) + nO(n²) + 6nA + 3nC + nR |
| **Total**                                                                                    | n × O(n³) + nO(n²) + 10n            |
| **Time Complexity (Big O)**                                                                  | **O(n⁴)**                           |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


> The `execute(...)` procedure repeatedly traverses and updates the graph structure until all edges are removed. According to the cost table, the most expensive operation is the call to `findAndProcessNextLine(...)`, which may run in **O(n³)** time. Since this call occurs inside a `while` loop that can iterate up to **n** times, it results in a **n × O(n³)** contribution. Therefore, the overall time complexity of the procedure is **O(n⁴)**.

### 15. Find Next Line

#### **Pseudo Code:**

```
Procedure findAndProcessNextLine(path, current, i, adjacencyMatrix, indexMap, lines, degrees)
1a:     totalEdges := countTotalEdges(adjacencyMatrix)

2a:     for each line in lines do
3a:         if line is null then
4a:             continue

5a:         station1 := line[0]
6a:         station2 := line[1]

7a:         if station1 = current then
8a:             destination := station2
9a:         else if station2 = current then
10a:            destination := station1
11a:        else
12a:            destination := null

13a:        if destination == null then
14a:            continue

15a:        j := indexMap[destination]

16a:        if adjacencyMatrix[i][j] > 0 then
17a:            if totalEdges == 1 then
18a:                return processEdge(path, adjacencyMatrix, degrees, i, j, line)

19a:            if isNotBridge(adjacencyMatrix, indexMap, current, destination) then
20a:                return processEdge(path, adjacencyMatrix, degrees, i, j, line)

21a:    for each line in lines do
22a:        if line is null then
23a:            continue

24a:        station1 := line[0]
25a:        station2 := line[1]

26a:        if station1 = current then
27a:            destination := station2
28a:        else if station2 = current then
29a:            destination := station1
30a:        else
31a:            destination := null

32a:        if destination == null then
33a:            continue

34a:        j := indexMap[destination]

35a:        if adjacencyMatrix[i][j] > 0 then
36a:            return processEdge(path, adjacencyMatrix, degrees, i, j, line)

37a:    return null

```

| **Operations / Lines**                                                                   | **Algorithm**                                       |
|------------------------------------------------------------------------------------------|-----------------------------------------------------|
| `1a` `totalEdges := countTotalEdges(...)`  —  [Count Total Edges](#19-count-total-edges) | O(n²) + 1A                                          |
| `2a` `for each line in lines`                                                            | nAouI + (n+1)C                                      |
| `3a` `if line is null then`                                                              | nC                                                  |
| `5a` `station1 := line[0]`                                                               | nA                                                  |
| `6a` `station2 := line[1]`                                                               | nA                                                  |
| `7a` `if station1 = current`                                                             | nC                                                  |
| `8a` `destination := station2`                                                           | ≤ nA                                                |
| `9a` `else if station2 = current`                                                        | ≤ nC                                                |
| `10a` `destination := station1`                                                          | ≤ nA                                                |
| `12a` `destination := null`                                                              | ≤ nA                                                |
| `13a` `if destination == null`                                                           | ≤ nC                                                |
| `15a` `j := indexMap[destination]`                                                       | ≤ nA                                                |
| `16a` `if adjacencyMatrix[i][j] > 0`                                                     | ≤ nC                                                |
| `17a` `if totalEdges == 1`                                                               | ≤ nC                                                |
| `18a` `return processEdge(...)`                                                          | O(1) + 1R                                           |
| `19a` `if isNotBridge(...)` — [Is Not Bridge](#16-is-not-bridge)                         | ≤ n × O(n²) + nC                                    |
| `20a` `return processEdge(...)`                                                          | O(1) + 1R                                           |
| `21a` `for each line in lines`                                                           | nAouI + (n+1)C                                      |
| `22a` `if line is null then`                                                             | nC                                                  |
| `24a` `station1 := line[0]`                                                              | nA                                                  |
| `25a` `station2 := line[1]`                                                              | nA                                                  |
| `26a` `if station1 = current`                                                            | nC                                                  |
| `27a` `destination := station2`                                                          | ≤ nA                                                |
| `28a` `else if station2 = current`                                                       | ≤ nC                                                |
| `29a` `destination := station1`                                                          | ≤ nA                                                |
| `31a` `destination := null`                                                              | ≤ nA                                                |
| `32a` `if destination == null`                                                           | ≤ nC                                                |
| `34a` `j := indexMap[destination]`                                                       | ≤ nA                                                |
| `35a` `if adjacencyMatrix[i][j] > 0`                                                     | ≤ nC                                                |
| `36a` `return processEdge(...)` — [Process Edge](#18-process-edge)                       | O(1) + 1R                                           |
| `37a` `return null`                                                                      | 1R                                                  | 
|                                                                                          | n × O(n²) + 2 × O(1) 8nA + (13n + 2)C + 2nAouI + 1R |
| **Total**                                                                                | n × O(n²) + 2 × O(1) + 23n + 3                      |
| **Time Complexity (Big O)**                                                              | **O(n³)**                                           |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>The procedure performs two main loops over the lines, each with O(n) iterations. Inside these loops, the function isNotBridge is called, which has a complexity of O(n²). This nested call results in an overall worst-case time complexity of O(n³). The number of assignments, comparisons, and other operations grow linearly with n, but the dominant factor is the cubic complexity from the bridge-checking step.

### 16. Is not Bridge

#### **Pseudo Code:**

```
Procedure isNotBridge(originalMatrix, indexMap, u, v)
1a: n := length(originalMatrix)          
2a: m := length(originalMatrix[0])        
3a: testMatrix := new 2D originalMatrix[n][n]
4a: for i from 0 to n-1 do
5a:     for j from 0 to m-1 do
6a:         testMatrix[i][j] := originalMatrix[i][j]
7a:     i := indexMap[u]
8a:     j := indexMap[v]

9a:     testMatrix[i][j]--
10a:    testMatrix[j][i]--

11a:    countBefore := dfsCount(originalMatrix, i, new visited[n])
12a:    countAfter := dfsCount(testMatrix, i, new visited[n])

13a:    return countAfter ≥ countBefore

```

| **Operations/lines**                                             | **Algorithm**                                                        |
|------------------------------------------------------------------|----------------------------------------------------------------------|
| 1  `n := length(originalMatrix)`                                 | 1A                                                                   |
| 2  `m := length(originalMatrix[0])`                              | 1A                                                                   |
| 3  `testMatrix := new 2D originalMatrix[n][m]`                   | n²A                                                                  |
| 4  `for i from 0 to n-1`                                         | n AouI + (n+1)C                                                      |
| 5  `for j from 0 to m-1`                                         | n × (m AouI + (m+1)C)                                                |
| 6  `testMatrix[i][j] := originalMatrix[i][j]`                    | n × m A                                                              |
| `7a: i := indexMap[u]`                                           | 1A                                                                   |
| `8a: j := indexMap[v]`                                           | 1A                                                                   |
| `9a: testMatrix[i][j]--`                                         | 1I                                                                   |
| `10a: testMatrix[j][i]--`                                        | 1I                                                                   |
| `11a: countBefore := dfsCount(...)` — [DFS Count](#17-DFS-count) | O(n²) + 1A                                                           |
| `12a: countAfter := dfsCount(...)`                               | O(n²) + 1A                                                           |
| `13a: return countAfter ≥ countBefore`                           | 1R                                                                   |
|                                                                  | 2 × O(n²) + (n² + mn + 6)A + (2n + 1 + mn)C + 2I + (n + mn)AouI + 1R |
| **Total**                                                        | 2 × O(n²) + n² + 3mn + 3n + 10                                       |
| **Time Complexity (Big O)**                                      | **O(n²)**                                                            |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure analyzes whether removing an edge affects graph connectivity. It copies a 2D adjacency matrix (visiting all n × m elements once) and performs two full DFS traversals. Since each DFS explores up to n² entries in the worst case, the overall time complexity is O(n²).
### 17. DFS Count

#### **Pseudo Code:**

```
Procedure dfsCount(adjacencyMatrix, startNode, visited)
1a:     n := length(adjacencyMatrix)
2a:     toVisit := new boolean array of size n
3a:     top := 0

4a:     toVisit[top] := startNode
5a:     top++
6a:     visited[startNode] := true
7a:     count := 1

8a:     while top > 0 do
9a:         top--
10a:        node := toVisit[top]

11a:        for i from 0 to n - 1 do
12a:            ff adjacencyMatrix[node][i] > 0 and visited[i] = false then
13a:                visited[i] := true
14a:                count++
15a:                toVisit[top] := i
16a:                top++

17a:    return count

```

| **Operations/lines**                     | **Algorithm**                                     |
|------------------------------------------|---------------------------------------------------|
| 1a `n := length(adjacencyMatrix)`        | 1A                                                |
| 2a `toVisit := new array of size n`      | nA                                                |
| 3a `top := 0`                            | 1A                                                |
| 4a `toVisit[top] := startNode`           | 1A                                                |
| 5a `top++`                               | 1I                                                |
| 6a `visited[startNode] := true`          | 1A                                                |
| 7a `count := 1`                          | 1A                                                |
| 8a `while top > 0`                       | ≤ nC                                              |
| 9a `top--`                               | ≤ nI                                              |
| 10a `node := toVisit[top]`               | ≤ nA                                              |
| 11a `for i from 0 to n-1`                | ≤ n × ((n + 1)AouI + (n + 1)C)                    |
| 12a `if adjacencyMatrix[node][i] > 0...` | ≤ n²C                                             |
| 13a `visited[i] := true`                 | ≤ n²A                                             |
| 14a `count++`                            | ≤ n²I                                             |
| 15a `toVisit[top] := i`                  | ≤ n²A                                             |
| 16a `top++`                              | ≤ n²I                                             |
| 17a `return count`                       | 1R                                                |
|                                          | (2n² + 2n + 5)A + (2n² + 2n)C + (n² + n)AouI + 1R |
| **Total**                                | 5n² + 4n + 6                                      |
| **Time Complexity (Big O)**              | **O(n²)**                                         |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure performs a depth-first search (DFS). In the worst case, when the graph is fully connected (dense), each node can check all other nodes, resulting in up to n² operations. Therefore, the time complexity is O(n²).

### 18. Process Edge

#### **Pseudo Code:**

```
Procedure processEdge(path, adjacencyMatrix, degrees, i, j, line)
1a:     adjacencyMatrix[i][j]--
2a:     adjacencyMatrix[j][i]--

3a:     current := last element of path
4a:     destination := if line[0] = current then line[1] else line[0]

5a:     append(destination, path)
6a:     degrees.add(current,degrees[current] - 1)
7a:     degrees.add(destination,degrees[destination] - 1)

8a:     return line


```

| **Operations/lines**                                               | **Algorithm**           |
|--------------------------------------------------------------------|-------------------------|
| 1a `adjacencyMatrix[i][j]--`                                       | 1I                      |
| 2a `adjacencyMatrix[j][i]--`                                       | 1I                      |
| 3a `current := last element of path`                               | 1A                      |
| 4a `destination := if line[0] = current then line[1] else line[0]` | 1C + 1A                 |
| 5a `append(destination, path)`                                     | 1A                      |
| 6a `degrees.add(current, degrees[current] - 1)`                    | 1A + 1Op                |
| 7a `degrees.add(destination, degrees[destination] - 1)`            | 1A + 1Op                |
| 8a `return line`                                                   | 1R                      |
|                                                                    | 6A + 2C + 2Op + 2I + 1R |
| **Total**                                                          | 31                      |
| **Time Complexity (Big O)**                                        | **O(1)**                |



**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure performs a constant number of operations—assignments, comparisons, and arithmetic updates—regardless of input size. Therefore, its time complexity is O(1).

### 19. Count Total Edges

#### **Pseudo Code:**

```
Procedure countTotalEdges(matrix)
1a:     count := 0

2a:     for i from 0 to length(matrix) - 1 do
3a:         for j from i + 1 to length(matrix[i]) - 1 do
4a:             count := count + matrix[i][j]

5a:     return count


```

| **Operations/lines**                             | **Algorithm**                                                              |
|--------------------------------------------------|----------------------------------------------------------------------------|
| 1a `count := 0`                                  | 1A                                                                         |
| 2a `for i from 0 to length(matrix)-1`            | n AouI + (n+1)C                                                            |
| 3a `for j from i+1 to length(matrix[i])-1`       | (n(n-1)/2) AouI + ((n(n-1)/2)+n) C                                         |
| 4a `count := count + matrix[i][j]`               | (n(n-1)/2) (A + I)                                                         |
| 5a `return count`                                | 1R                                                                         |
|                                                  |                                                                            |
|                                                  | ((n² - n + 2)/2)A + ((n² + 3n + 2)/2)C + ((n² + n)/2)AouI + n(n-1)/2I + 1R |
| **Total**                                        | (3n² + 3n + 6)/2                                                           |
| **Time Complexity (Big O)**                      | **O(n²)**                                                                  |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure counts the edges in an undirected graph by checking only the upper triangle of the adjacency matrix to avoid double counting. The time complexity is O(n²), where n is the number of nodes in the graph.


# Verify Trip Problem

###  US013 - As a Player, given a railway with stations and lines connecting pairs of stations, I want to verify if a specific train (steam, diesel, or electric powered) can travel between two stations belonging to the rail network (or from any type of station to another of the same type).

## Algorithms and Complexity Analysis:

### 1. Verify Trip Two Stations

#### **Pseudo Code:**

```
Procedure verifyTripTwoStations(lineFile, station1, station2, stations, trainType)
1a:     if trainType == ELECTRIC then
2a:         ignoreNormalRails := true

3a:     connections := readCSVTo2DArray(lineFile)
4a:     adjacentMatrix := initializeAdjacencyMatrix(stations, connections)

5a:     multiplyNTimes(adjacentMatrix, length(stations))

6a:     if adjacentMatrix[getStationIndex(stations, station1)]
                   [getStationIndex(stations, station2)] then
7a:         return 1
8a:     return 0

```

| **Operations / Lines**                                                                   | **Algorithm**                           |
|------------------------------------------------------------------------------------------|-----------------------------------------|
| 1a `if trainType == ELECTRIC`                                                            | 1C                                      |
| 2a `ignoreNormalRails := true`                                                           | 1A                                      |
| 3a `connections := readCSVTo2DArray(lineFile)`                                           | O(n)                                    |
| 4a `adjacentMatrix := initializeAdjacencyMatrix(...)`                                    | O(n² × m)                               |
| 5a `multiplyNTimes(...)`  — [Multiply N Matrices](#7-multiply-n-matrices)                | O(n⁴)                                   |
| 6a `if adjacentMatrix[getStationIndex(...)]` — [Get Station Index](#6-get-station-index) | O(n) + 1C                               |
| 7a `return 1`                                                                            | 1R                                      |
| 8a `return 0`                                                                            | 1R                                      |
|                                                                                          |                                         |                                               |
|                                                                                          | O(n⁴) + O(n² × m) + O(n) + 1A + 2C + 1R |
| **Total**                                                                                | O(n⁴) + O(n² × m) + O(n) + 4            |
| **Time Complexity (Big O)**                                                              | **O(n⁴)**                               |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


> This procedure verifies whether a direct or indirect path exists between two stations. It reads the connections, builds the adjacency matrix, multiplies it up to n times to capture all possible paths, and checks the specific connection. The overall time complexity is O(n⁴).


### 2. Verify Trip Station Type

#### **Pseudo Code:**

```
Procedure verifyTripStationType(lineFile, stationTypes, stations, trainType)
1a:     if trainType == ELECTRIC then
2a:         ignoreNormalRails := true

3a:     if length(stationTypes) == 3 then
4a:         return verifyPossibilityOfAllStations(lineFile, stations)

5a:    connections := readCSVTo2DArray(lineFile)
6a:    adjacentMatrix := initializeAdjacencyMatrix(stations, connections)
7a:    multiplyNTimes(adjacentMatrix, length(stations))

8a:    if areAllOfTypeOn(adjacentMatrix, stations, stationTypes) then
9a:        return 1
10a:    return 0
```

| **Operations / Lines**                                                                                                        | **Algorithm**                            |
|-------------------------------------------------------------------------------------------------------------------------------|------------------------------------------|
| 1a `if trainType == ELECTRIC then`                                                                                            | 1C                                       |
| 2a `ignoreNormalRails := true`                                                                                                | 1A                                       |
| 3a `if length(stationTypes) == 3 then`                                                                                        | 1C                                       |
| 4a `return verifyPossibilityOfAllStations(...)` — [Verify Possibility of All Stations](#3-verify-possibility-of-all-stations) | O(n⁴) + 1R                               |
| 5a `connections := readCSVTo2DArray(lineFile)`                                                                                | O(n)                                     |
| 6a `adjacentMatrix := initializeAdjacencyMatrix(...)` — [Initialize Adjancency Matrix](#4-initialize-adjancency-matrix)       | O(n² × m)                                |
| 7a `multiplyNTimes(adjacentMatrix, length(stations))`                                                                         | O(n⁴)                                    |
| 8a `if areAllOfTypeOn(adjacentMatrix, stations, stationTypes)`  — [Are All of Type On](#11-are-all-type-on)                   | O(n³) + 1C                               |
| 9a `return 1`                                                                                                                 | 1R                                       |
| 10a `return 0`                                                                                                                | 1R                                       |
|                                                                                                                               | O(n⁴) + O(n² × m) + O(n³) + 1A + 3C + 1R |
| **Total**                                                                                                                     | O(n⁴) + O(n² × m) + O(n³) + 5            |
| **Time Complexity (Big O)**                                                                                                   | **O(n⁴)**                                |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


> This procedure verifies whether a trip can be made based on the train type and station types. If all three station types are present, it delegates to a simpler check using `verifyPossibilityOfAllStations`, with a cost of **O(n⁴)**. Otherwise, it builds and processes an adjacency matrix through matrix multiplications and type checks, also dominated by **O(n⁴)** operations.
> **Thus, the overall time complexity is O(n⁴)**, where *n* is the number of stations.

### 3. Verify Possibility of All Stations

#### **Pseudo Code:**

```
Procedure verifyPossibilityOfAllStations(lineFile, stations)
1a:     connections := readCSVTo2DArray(lineFile)
2a:     adjacentMatrix := initializeAdjacencyMatrix(stations, connections)
3a:     multiplyNTimes(adjacentMatrix, stations.length)
4a:     if areAllTripsPossible(adjacentMatrix) then
5a:         return 1
6a:     return 0

```

| **Operations / Lines**                                                                                                  | **Algorithm**                         |
|-------------------------------------------------------------------------------------------------------------------------|---------------------------------------|
| 1a `connections := readCSVTo2DArray(lineFile)`                                                                          | O(n)                                  |
| 2a `adjacentMatrix := initializeAdjacencyMatrix(...)` — [Initialize Adjancency Matrix](#4-initialize-adjancency-matrix) | O(n² × m)                             |
| 3a `multiplyNTimes(adjacentMatrix, stations.length)` — [Multiply N Matrices](#7-multiply-n-matrices)                    | O(n⁴)                                 |
| 4a `if areAllTripsPossible(adjacentMatrix)` — [Are All Trips Possible](#10-are-all-trips-possible)                      | O(n²)                                 |
| 5a `return 1`                                                                                                           | 1R                                    |
| 6a `return 0`                                                                                                           | 1R                                    |
|                                                                                                                         | O(n⁴) + O(n² × m) + O(n²) + O(n) + 1R |
| **Total**                                                                                                               | O(n⁴) + O(n² × m) + O(n²) + O(n) + 1  |
| **Time Complexity (Big O)**                                                                                             | **O(n⁴)**                             |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure verifies whether all stations are reachable from one another. It first reads the station connections, builds the adjacency matrix, multiplies it up to n times to find all reachable paths, and checks if all trips are possible. The total time complexity is O(n⁴).

### 4. Initialize Adjancency Matrix

#### **Pseudo Code:**

```
Procedure initializeAdjacencyMatrix(stations, connections)
1a: n := length(stations)
2a: adjacencyMatrix := new boolean[n][n]

3a: for i from 0 to n-1 do
4a:     for j from 0 to n-1 do
5a:         adjacencyMatrix[i][j] := false

6a: for i from 0 to n-1 do
7a:     indexes := connectionToStation(stations[i], stations, connections)
8a:     for j from 0 to length(indexes)-1 do
9a:         adjacencyMatrix[indexes[j]][i] := true
10a:        adjacencyMatrix[i][indexes[j]] := true

11a: return adjacencyMatrix

```

| **Operations / Lines**                        | **Algorithm**                                                                          |
|-----------------------------------------------|----------------------------------------------------------------------------------------|
| 1a `n := length(stations)`                    | 1A                                                                                     |
| 2a `adjacencyMatrix := new boolean[n][n]`     | O(n²)                                                                                  |
| 3a `for i from 0 to n-1`                      | nAouI + (n+1)C                                                                         |
| 4a `for j from 0 to n-1`                      | n × (nAouI + (n+1)C)                                                                   |
| 5a `adjacencyMatrix[i][j] := false`           | n²A                                                                                    |
| 6a `for i from 0 to n-1`                      | nAouI + (n+1)C                                                                         |
| 7a `indexes := connectionToStation(...)`      | nO(n × m)                                                                              |
| 8a `for j from 0 to length(indexes)-1`        | (n² × m)AouI  +(n² × m - 1)C                                                           |
| 9a `adjacencyMatrix[indexes[j]][i] := true`   | n² × mA                                                                                |
| 10a `adjacencyMatrix[i][indexes[j]] := true`  | n² × mA                                                                                |
| 11a `return adjacencyMatrix`                  | 1R                                                                                     |
|                                               | O(n²) + nO(n × m) +  (2n² × m + n² + 1)A + (2n + n² × m)AouI + (n² +2n + n² × m)C + 1R |
| **Total**                                     | O(n²) + nO(n × m) +  4n² × m + 3n² + 2n + 2                                            |
| **Time Complexity (Big O)**                   | **O(n² × m)**                                                                          |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return

> This procedure builds the adjacency matrix of an undirected graph based on a list of stations and their connections. The most computationally expensive operation is the repeated call to the `connectionToStation` procedure, which scans all connections (`m`) for each station (`n`). Therefore, the total time complexity is O(n² × m), where `n` is the number of stations and `m` is the number of connections.

---

Let me know if you'd like the table or pseudocode translated or improved as well.


### 5. Connection to Station

#### **Pseudo Code:**

```
1a: list := new empty list of integers

2a: for i from 0 to length(connections) - 1 do
3a:     if connections[i][0] = stationName and (not ignoreNormalRails or connections[i][2] = "1") then
4a:         list.add(getStationIndex(stations, connections[i][1]))

5a:     if connections[i][1] = stationName and (not ignoreNormalRails or connections[i][2] = "1") then
6a:         list.add(getStationIndex(stations, connections[i][0]))

7a: result := new integer array of size list.size()

8a: for i from 0 to list.size() - 1 do
9a:     result[i] := list.get(i)

10a: return result

```

| **Operations / Lines**                                                           | **Algorithm**                                                         |
|----------------------------------------------------------------------------------|-----------------------------------------------------------------------|
| 1a `list := new empty list`                                                      | O(1)                                                                  |
| 2a `for i from 0 to length(connections)-1`                                       | nAouI + (n+1)C                                                        |
| 3a `if connections[i][0] = stationName ...`                                      | nC                                                                    |
| 4a `list.add(getStationIndex(...))` — [Get Station Index](#6-get-station-index)  | ≤ n × (O(m) + 1A)                                                     |
| 5a `if connections[i][1] = stationName ...`                                      | nC                                                                    |
| 6a `list.add(getStationIndex(...))` — [Get Station Index](#6-get-station-index)  | ≤ n × (O(m) + 1A)                                                     |
| 7a `result := new integer array`                                                 | O(k)                                                                  |
| 8a `for i from 0 to list.size() - 1`                                             | kAouI + (k+1)C                                                        |
| 9a `result[i] := list.get(i)`                                                    | kA                                                                    |
| 10a `return result`                                                              | 1R                                                                    |
|                                                                                  | n × O(m)  + O(1) + O(k) + (n + k)AouI +  (n + k)A + (n + k + 2)C + 1R |
| **Total**                                                                        | n × O(m)  + O(1) + O(k) + 3n + 3k +3                                  |
| **Time Complexity (Big O)**                                                      | **O(n × m)**                                                          |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>Here’s the conclusion in English:

---

> This procedure iterates through all connections to determine which stations are directly connected to a given station. For each match, it calls a helper method (`getStationIndex`) that runs in linear time relative to the number of stations (*m*), resulting in an overall time complexity of **O(n × m)**, where *n* is the number of connections and *m* is the number of stations.
> After identifying all connected stations, their indices are stored in an array. The additional time to create and fill this array is **O(k)**, where *k* is the number of matches found.
> Therefore, the execution time is primarily dominated by the nested calls, making the worst-case complexity **O(n × m)**.


### 6. Get Station Index

#### **Pseudo Code:**

```
Procudure getStationIndex(stations, name)
1a: for i from 0 to length(stations) - 1 do
2a:     if stations[i] = name then
3a:         return i

4a: return -1
```

| **Operations / Lines**                  | **Algorithm**                |
| --------------------------------------- |------------------------------|
| 1a `for i from 0 to length(stations)-1` | (n + 1)AouI + (n+1)C         |
| 2a `if stations[i] = name`              | ≤ nC                         |
| 3a `return i`                           | 1R                           |
| 4a `return -1`                          | 1R                           |
|                                         | (n + 1)AouI + (2n + 1)C + 1R |
| **Total**                               | 3n + 3                       |
| **Time Complexity (Big O)**             | **O(n)**                     |

**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure searches for a station's index by iterating over the entire array of station names and comparing each one to the target name. It performs at most n comparisons and accesses, leading to a worst-case time complexity of O(n).

### 7. Multiply N Matrices

#### **Pseudo Code:**

```
Procedure: multiplyNTimes(adjacentMatrix, size)
1a: sum := new boolean[adjacentMatrix,lenght][adjacentMatrix.lenght]
2a: currentPower := adjacentMatrix

3a: for i from 0 to length(adjacentMatrix) - 1 do
4a:     for j from 0 to length(adjacentMatrix) - 1 do
5a:         sum[i][j] := adjacentMatrix[i][j]

6a: for p from 2 to(<=) size do
7a:     currentPower := multiplyBooleanMatrices(currentPower, adjacentMatrix)
8a:     sum := addBooleanMatrices(sum, currentPower)

9a: for i from 0 to length(adjacentMatrix) - 1 do
10a:    for j from 0 to length(adjacentMatrix) - 1 do
11a:        adjacentMatrix[i][j] := sum[i][j]

```

| **Operations / Lines**                                                                        | **Algorithm**                                                                                    |
|-----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| 1a `sum := new boolean matrix`                                                                | n²A                                                                                              |
| 2a `currentPower := adjacentMatrix`                                                           | 1A                                                                                               |
| 3a `for i from 0 to n-1`                                                                      | (n + 1)AouI + (n+1)C                                                                             |
| 4a `for j from 0 to n-1`                                                                      | n × ((n + 1)AouI + (n+1)C)                                                                       |
| 5a `sum[i][j] := adjacentMatrix[i][j]`                                                        | n²A                                                                                              |
| 6a `for p from 2 to size`                                                                     | (size-1)AouI + (size)C                                                                           |
| 7a `multiplyBooleanMatrices(...)` — [Multiply Boolean Matrices](#8-multiply-boolean-matrices) | (size-1) × O(n³)                                                                                 |
| 8a `addBooleanMatrices(...)` — [Add Boolean Matrices](#9-add-boolean-matrices)                | (size-1) × O(n²)                                                                                 |
| 9a `for i from 0 to n-1`                                                                      | (n + 1)AouI + (n+1)C                                                                             |
| 10a `for j from 0 to n-1`                                                                     | n × ((n + 1)AouI + (n+1)C)                                                                       |
| 11a `adjacentMatrix[i][j] := sum[i][j]`                                                       | n²A                                                                                              |
|                                                                                               | (size-1) × O(n³) +  (size-1) × O(n²) + (3n² + 1)A  + (2n² + 4n + size + 1)AouI + (2n² + 4n + 2)C |
| **Total**                                                                                     | (size-1) × O(n³) +  (size-1) × O(n²) + 7n² + 8n + size + 4                                       |
| **Time Complexity (Big O)**                                                                   | **O(size × n³)**                                                                                 |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure multiplies a boolean adjacency matrix successively, accumulating the results up to the power size. The dominant operations are the boolean matrix multiplications, each with a complexity of O(n³), repeated (size - 1) times, leading to a total cost of O(size × n³).
>Since in this case size is equal to the number of stations n, the overall complexity becomes O(n⁴).

### 8. Multiply Boolean Matrices

#### **Pseudo Code:**

```
Procedure: multiplyBooleanMatrices(a, b)
1a: n := length of a
2a: result := new boolean matrix of size n×n

3a: for i from 0 to n - 1 do
4a:     for j from 0 to n - 1 do
5a:         sum := false
6a:         for k from 0 to n - 1 do
7a:             sum := sum OR (a[i][k] AND b[k][j])
8a:         result[i][j] := sum

9a: return result


```

| **Operations / Lines**                   | **Algorithm**                                                               |
|------------------------------------------|-----------------------------------------------------------------------------|
| 1a `n := length of a`                    | 1A                                                                          |
| 2a `result := new boolean[n][n]`         | n²A                                                                         |
| 3a `for i from 0 to n - 1`               | (n + 1)AouI + (n+1)C                                                        |
| 4a `for j from 0 to n - 1`               | n × ((n + 1)nAouI + (n+1)C)                                                 |
| 5a `sum := false`                        | n²A                                                                         |
| 6a `for k from 0 to n - 1`               | n² × ((n + 1)AouI + (n+1)C)                                                 |
| 7a `sum := sum OR (a[i][k] AND b[k][j])` | n³Op + n³A                                                                  |
| 8a `result[i][j] := sum`                 | n²A                                                                         |
| 9a `return result`                       | 1R                                                                          |
|                                          | (n³ + 2n²)A + (n³ + 2n² + 2n + 1)AouI + (n³ + 2n² + 2n + 1)C + n³Ops + 1R   |
| **Total**                                | 4n³ +  6n² + 4n + 3                                                         |
| **Time Complexity (Big O)**              | **O(n³)**                                                                   |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure multiplies two n × n boolean matrices using triple nested loops, performing O(n³) logical operations. Its time complexity is O(n³).

### 9. Add Boolean Matrices

#### **Pseudo Code:**

```
Procedure addBooleanMatrices(matrix1, matrix2)
1a: n := length of matrix1
2a: result := new boolean matrix of size n × n

3a: for i from 0 to n-1 do
4a:     for j from 0 to n-1 do
5a:         result[i][j] := matrix1[i][j] OR matrix2[i][j]

6a: return result

```

| **Operations / Lines**                              | **Algorithm**                                              |
| --------------------------------------------------- |------------------------------------------------------------|
| 1a `n := length of matrix1`                         | 1A                                                         |
| 2a `result := new boolean[n][n]`                    | n²A                                                        |
| 3a `for i from 0 to n-1`                            | (n + 1)AouI + (n+1)C                                       |
| 4a `for j from 0 to n-1`                            | n × ((n + 1)AouI + (n+1)C)                                 |
| 5a `result[i][j] := matrix1[i][j] OR matrix2[i][j]` | n²Op + n²A                                                 |
| 6a `return result`                                  | 1R                                                         |
|                                                     | (n² + 1)A + (n² + 2n + 1)AouI + (n² + 2n + 2)C + n²Op + 1R |
| **Total**                                           | 3n² + 4n + 5                                               |
| **Time Complexity (Big O)**                         | **O(n²)**                                                  |



**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure performs element-wise logical OR between two n×n boolean matrices. It iterates over all elements, resulting in a time complexity of O(n²). 

### 10. Are all trips possible

#### **Pseudo Code:**

```
Procedure: areAllTripsPossible(matrix)
1a: n := length(matrix)
2a: for i from 0 to n - 1 do
3a:     for j from 0 to n - 1 do
4a:         if matrix[i][j] == false AND i ≠ j then
5a:             return false
6a: return true

```

| **Operations / Lines**                  | **Algorithm**                                 |
|-----------------------------------------|-----------------------------------------------|
| 1a `n := length(matrix)`                | 1A                                            |
| 2a `for i from 0 to n - 1`              | (n + 1)AouI + (n+1)C                          |
| 3a `for j from 0 to n - 1`              | n × ((n + 1)AouI + (n+1)C)                    |
| 4a `if !matrix[i][j] && i ≠ j`          | 2n²C                                          |
| 5a `return false`                       | 1R                                            |
| 6a `return true`                        | 1R                                            |
|                                         | 1A + (n² + 2n + 1)AouI + (2n² + 2n + 1)C + 1R |
| **Total**                               | 3n² + 4n + 4                                  |
| **Time Complexity (Big O)**             | **O(n²)**                                     |



**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure checks all station pairs in an n × n matrix. Since it scans all entries once, the time complexity is O(n²). 

### 11. Are All Type On

#### **Pseudo Code:**

```
Procedure: areAllOfTypeOn(matrix, stations, stationTypes)
1a: for i from 0 to stations.length - 1 do
2a:     if isOfType(stations[i][0], stationTypes) then
3a:         index := getStationIndex(stations, stations[i])
4a:         for j from i to stations.length - 1 do
5a:             if isOfType(stations[j][0], stationTypes) AND matrix[index][j] == false then
6a:                 return false
7a: return true


```

| **Operations / Lines**                                                                                    | **Algorithm**                                         |
|-----------------------------------------------------------------------------------------------------------|-------------------------------------------------------|
| 1a `for i = 0 to stations.length - 1`                                                                     | (n + 1)AouI + (n + 1)C                                |
| 2a `if isOfType(stations[i].charAt(0), stationTypes)` — [Is of Type](#12-is-of-type)                      | ≤ nO(n)                                               |
| 3a `index := getStationIndex(stations, stations[i])`                                                      | ≤ nO(n) + nA                                          |
| 4a `for j = i to stations.length - 1`                                                                     | ≤ n × ((n + 1)AouI + (n + 1)C)                        |
| 5a `if isOfType(stations[j].charAt(0), stationTypes) && !matrix[index][j]` — [Is of Type](#12-is-of-type) | ≤ n²O(n)                                              |
| 6a `return false`                                                                                         | 1R                                                    |
| 7a `return true`                                                                                          | 1R                                                    |
|                                                                                                           | (n² + 2n)O(n) (n² + 2n + 1)AouI + (n² + 2n + 1)C + 1R |
| **Total**                                                                                                 | (n² + 2n)O(n) + 2n² + 4n + 3                          |
| **Time Complexity (Big O)**                                                                               | **O(n³)**                                             |



**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure checks all station pairs in an n × n matrix. Since it scans all entries once, the time complexity is O(n²). 


### 12. Is of Type

#### **Pseudo Code:**

```
Procedure: isOfType(type, stationTypes)
1a: for i from 0 to length(stationTypes) - 1 do
2a:     if stationTypes[i] == DEPOT and type == DEPOT_CHAR then
3a:         return true
4a:     else if stationTypes[i] == STATION and type == STATION_CHAR then
5a:         return true
6a:     else if stationTypes[i] == TERMINAL and type == TERMINAL_CHAR then
7a:         return true
8a: return false



```

| **Operations / Lines**                                             | **Algorithm**                |
|--------------------------------------------------------------------|------------------------------|
| 1a `for i from 0 to length(stationTypes) - 1`                      | (n + 1)AouI + (n + 1)C       |
| 2a `if stationTypes[i] == DEPOT and type == DEPOT_CHAR`            | ≤ n × 2C                     |
| 3a `return true`                                                   | ≤ 1R                         |
| 4a `else if stationTypes[i] == STATION and type == STATION_CHAR`   | ≤ n × 2C                     |
| 5a `return true`                                                   | ≤ 1R                         |
| 6a `else if stationTypes[i] == TERMINAL and type == TERMINAL_CHAR` | ≤ n × 2C                     |
| 7a `return true`                                                   | ≤ 1R                         |
| 8a `return false`                                                  | 1R                           |
|                                                                    | (n + 1)AouI + (5n + 1)C + 1R |
| **Total**                                                          | 6n + 3                       |
| **Time Complexity (Big O)**                                        | **O(n)**                     |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return


>This procedure checks whether a given type matches any of the allowed station types by iterating through the array and performing up to three comparisons per element. Since each element is processed once, the overall time complexity is O(n).