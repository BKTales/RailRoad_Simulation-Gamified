# ShortestWayPossibleProblem

### As a player, given a scenario with stations and railway lines, I want to get one of the shortest routes between two stations, which goes through an ordered list of stations I choose

## Algorithms and Complexity Analysis:

### 1. Get Shortest Way

#### **Pseudo Code:**

```pseudo
Procedure: Read Stations from File

1a: for (i := 0 i to stationsQuantity)
2a:            costs[i] = 10000000
3a:        costs[startingStation] = 0
4a: for (i := startingStation to stationsQuantity)     
5a:        for (int j := 0 to stationsQuantity) 
6a:               if (i == j & connections[i][j] != 0) then
6b:                     efectiveCost = (costs[i] + connections[i][j])
7a:                    if (effectiveCost < costs[j])
8a:                        costs[j] = effectiveCost
9a:                        stationsFrom[j] = stations[i]
10a:                       stationsFromIndex[j] = i
11a: for (i := 0 to startingStation)     
12a:        for (int j := 0 to stationsQuantity) 
13a:               if (i == j & connections[i][j] != 0) then
6b:                     efectiveCost = (costs[i] + connections[i][j])
14a:                    if (effectiveCost < costs[j])
15a:                        costs[j] = effectiveCost
16a:                        stationsFrom[j] = stations[i]
17a:                        stationsFromIndex[j] = i        
18a: return (transformingAnswerToString(wantedStation, startingPoint))
```

#### **Complexity Analysis:**

| Operations/lines                                    | Algorithm                                                                                                                 |
|-----------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| 1ª `for (i := 0 i to stationsQuantity)`             | (n+1)A ou I + (n+1)C                                                                                                      |
| 2ª `costs[i] = 10000000`                            | nA                                                                                                                        |
| 3ª `costs[startingStation] = 0`                     | 1A                                                                                                                        |
| 4ª `for (i := startingStation to stationsQuantity)` | (n-start+1)A ou I + (n-start+1)C                                                                                          |
| 5ª `for (int j := 0 to stationsQuantity)`           | ((n+1)*(n-start+1))A                                                                                                      |
| 6ª `if (i == j & connections[i][j] != 0) then`      | 2*((n)*(n-start))A                                                                                                        |
| 6b: `efectiveCost = (costs[i] + connections[i][j])` | ((n)*(n-start)) A + ((n)*(n-start)) Op                                                                                    |
| 7ª `if (efectiveCost < costs[j])`                   | ((n)*(n-start)) A + ((n)*(n-start)) C                                                                                     |
| 8ª `costs[j] = efectiveCost`                        | ((n)*(n-start)) A                                                                                                         |
| 9ª `stationsFrom[j] = stations[i]`                  | ((n)*(n-start)) A                                                                                                         |
| 10ª `stationsFromIndex[j] = i`                      | ((n)*(n-start)) A                                                                                                         |
| 11ª `for (i := 0 to startingStation)`               | (start+1) A + ((start+1)*(n+1)) C                                                                                         |
| 12ª `for (int j := 0 to stationsQuantity)`          | ((start+1)*(n+1)) A + ((start+1)*(n+1)) C                                                                                 |
| 13ª `if (i == j & connections[i][j] != 0) then`     | 2 * ((start)*(n)) C                                                                                                       |
| 6b: `efectiveCost = (costs[i] + connections[i][j])` | ((n)*(n-start)) Op + ((start)*(n)) A                                                                                      |
| 14ª `if (efectiveCost < costs[j])`                  | ((start)*(n)) C                                                                                                           |
| 15ª `costs[j] = efectiveCost`                       | ((start)*(n)) A                                                                                                           |
| 16ª `stationsFrom[j] = stations[i]`                 | ((start)*(n)) A                                                                                                           |
| 17ª `stationsFromIndex[j] = i`                      | ((start)*(n)) A                                                                                                           |
| 18ª `return (transformingAnswerToString(wantedStation, startingPoint))` | (8*n + 5) + 1R  |
|                                                     | ** (3*(start*n) + 5*((n)*(n-start)) + ((start+1) + (start+1)*(n+1))+ n + 1 )A + (((n)*(n-start)) + ((n)*(n-start))) Op + ** |
|                                                     | ** ((n-start+1) + (n) * (n-start) + (start+1)*(n+1) + 2 * ((start)*(n))) * C **                                           |
|                                                     | ** (n+2)*(n) + 2(n)*(n) + (2 * 2 * (n*n)) + (3 * (n*n)) + 1 **                                                            |
| **Total**                                           | ** 10n^2 + 10n + 6 **                                                                                       |
| **Time Complexity Estimate (Big O)**                | **O(n^2)**                                                                                                                  |

n
**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return
**Legend:** n = stationsQuantity
 
> This method gets the shortest way between two given stations

### 2. Loop through the wanted stations

#### **Pseudo Code:**

```pseudo
1ª: shortestWay = "";
2ª: s = null;
3ª: for (i := 0 to manyStationsToGoThrough - 1) 
4ª:    startingPoint = stationsToGoThrough[i];
5ª:    s = getShortestWay(stationsToGoThrough[i] ,stationsToGoThrough[i + 1]);
6ª:    if (s == null)
7ª:        return null;
8ª:    shortestWay += s;
9ª:    if (i != stationsToGoThrough.length - 2)
10ª:         shortestWay += " ==> ";
11ª: }
12ª: shortestWay += "\n";
13ª: shortestWay += "total cost = " + totalCostOfTrips;
14ª: return (shortestWay);
```

#### **Complexity Analysis:**

| Operations/lines                                                               | Algorithm                          |
|--------------------------------------------------------------------------------|------------------------------------|
| 1ª `shortestWay = "" `                                                         | 1A                                 |
| 2ª `s = null; `                                                                | 1A                                 |
| 3ª `for (int i = 0; i < manyStationsToGoThrough - 1; i++) { `                  | (m) A + (m) C                      |
| 4ª ` startingPoint = stationsToGoThrough[i]; `                                 | (m) A                              |
| 5ª ` s = getShortestWay(stationsToGoThrough[i] ,stationsToGoThrough[i + 1]); ` | (m) * (n^2)             |
| 6ª ` if (s == null) `                                                          | (m) C                              |
| 7ª ` return null; `                                                            |                                    |
| 8ª ` shortestWay += s; `                                                       | (m) A ou I                         |
| 9ª ` if (i != stationsToGoThrough.length - 2) `                                | (m) C                              |
| 10ª` shortestWay += " ==> "; `                                                 | (m - 2) A                          |
| 11ª` shortestWay += "\n"; `                                                    | 1 A                                |
| 12ª` shortestWay += "total cost = " + totalCostOfTrips; `                      | 1 A                                |
| 13ª` return (shortestWay); `                                                   | 1 R                                |
|                                                                                | **(4 * m + (m-2) + 4)A + (3m) C) + m*n^2 ** |
|                                                                                | ** 4m + m - 2 + 4 + 3m + m*n^2 ** |
|                                                                                | ** 8m + 2 + m*n^2 ** |
| **Time Complexity Estimate (Big O)**                                           | **O(m*n^2)**                           |

**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return
**Legend:** m = manyStationsToGoThrough

> This method runs for manyStationsToGoThrough - 1 selected by the user (minimum to be selected is two) getting the  shortest way between the stations
> and adding them to the final answer to the problem
  
  
### 3. Transforming the result of the algorithms to answer 

#### **Pseudo Code:**

```pseudo
1ª:  ArrayList<String> answer = new ArrayList();
2ª:  String answerString = "";
3ª:  int index;
4ª:  if (costs[wantedStation] == 0)
5ª:      return (null);
6ª:  index = wantedStation;
7ª:  answer.add(stations[index]);
8ª:  totalCostOfTrips += costs[index];
9ª:  while (index != startingPoint)
10ª:      index = stationsFromIndex[index];
11ª:      answer.add(stations[index]);
12ª:  for (int i = (answer.size() -1); i >= 0; i--)
13ª:      answerString += answer.get(i);
14ª:      if (i != 0)
15ª:          answerString += " ==> ";
16ª:  return (answerString);
```

#### **Complexity Analysis:**

| Operations/lines                                   | Algorithm                          |
|----------------------------------------------------|------------------------------------|
| 1ª:  `ArrayList<String> answer = new ArrayList();` | 1 A                                |
| 2ª:  `String answerString = "";`                   | 1 A                                |
| 3ª:  `int index;`                                  |                                    |
| 4ª:  `if (costs[wantedStation] == 0)`              | 1 C                                |
| 5ª:  `    return (null);`                          | 1 R                                |
| 6ª:  `index = wantedStation;`                      | 1 A                                |
| 7ª:  `answer.add(stations[index]);`                | 1 A                                |
| 8ª:  `totalCostOfTrips += costs[index];`           | 1 A                                |
| 9ª:  `while (index != startingPoint)`              | (n) C                              |
| 10ª: `     index = stationsFromIndex[index];`      | (n) A                              |
| 11ª: `     answer.add(stations[index]);`           | (n) A                              |
| 12ª: ` for (i := 0 to (answer.size() -1))`         | (n) C + (n) A                      |
| 13ª: `     answerString += answer.get(i);`         | (n - 1)A                           |
| 14ª: `     if (i != 0)`                            | (n) C                              |
| 15ª: `         answerString += " ==> ";`           | (n-1) A                            |
| 16ª: ` return (answerString);`                     | 1R                                 |
|                                                    | **(4A + 1C + nA)**                 |
| **Total**                                          | ** (5*n + 3)A + (3*n + 1)C  + 1R** |
| **Total**                                          | ** 8*n  + 5** |
| **Time Complexity Estimate (Big O)**               | **O(n)**                           |


**Legend:** A — Assignment | I — Increment | C — Comparison | Op — Arithmetic Operation | R — Return
**Legend:** n = quantityOfStationsInputed
