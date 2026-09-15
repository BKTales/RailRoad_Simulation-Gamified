# US027

## Dijkstra's Algorithm

The algorithm we choose to use was the Dijkstra's Algorithm, because it would give us the wanted output for the
userStory(check if a trip was possible or not) and it would also give us the best way to get into that other station

## Explanation given by the material given from the teachers:

#### Input
- An undirected, connected graph G = (V, E, W) with positive edge weights.
- A starting vertex `a` and a target vertex `z`.

#### Output
- A simple path with the lowest cost between vertices `a` and `z`.
- The cost of this path.

#### Algorithm Steps

1. Mark vertex `a` with a permanent label of 0 (since dist(a, a) = 0), and all other vertices with a temporary label of ∞:
  - `Label(a) = 0`
  - `Label(v) = ∞` for all `v ≠ a`

2. Define the set of temporarily labeled vertices:
  - `Temporaries = V(G) \ {a}`
  - Set the vertex with the minimum label as `v* = a`

3. For each vertex `v ∈ Temporaries`, update the label according to the rule:
  - Label(v) = Label(v*) + w_{vv} if Label(v) > Label(v) + w_{v*v}  Label(v)

4. If the label of vertex `v` was updated in step 3, then:
  - `Predecessor(v) = v*`

5. Select a new vertex `v*` with the minimum label among the vertices in `Temporaries`. If there is a tie, choose one arbitrarily. Remove this vertex from `Temporaries`, making its label permanent.

6. If `v* ≠ z`, return to Step 3.

7. The algorithm returns:
  - The cost of the shortest path from `a` to `z`, which is `Label(z)`
  - The actual path, reconstructed using the `Predecessor` values.


## Implementation we've used:

### Step 1 - Validations
Validate the inputted Stations to see if they have connections to other stations

### Step 2 - Initialize the variables to be used variables

Initializations:
- Array of the station is initialized with the departure station as being the first one in the array
- Arrays have format as int[2], with int[0] (station index)  int[1] (accumulated cost)
- Get the index of the desired arrival station inside the stations(stations array)
- Start the first pathOption(newOption) int array with the departure station and cost 0
  - the Arrays are inside this structure allOptions(array<arrays>) contains array<int[2]>
  
```java

```

  ### Step 3 - Algorithm loop

The first loop is the one that will occur until all the options inside the list have no more new additions to
be done to that list. To know if they still have addition to be made we get the rails that connect that station
see the station that they connect with, and compare them to the ones we have in our list(array<int[2]>).

```java

```

#### Inside the loop:
- #### Step 1 - Refresh the lists of the trip
  This is a loop that will go through every railway line of the lowestCostStation searching for new lines(new line is
  a line that haven't being passed by that path in array) for their array (this array will be set as temp in the beginning
  of the function). When a new line is found we will copy the current list and append the found station to it, after this
  being done to all the new station we will free the list from the beginning of the loop
```java

```

- #### Step 2 - get the cheapest(smallest distance) last node of the list
  This is a loop that will pass through each of the lists, get the last node of each one of them and see the one that
  has the lowest accumulated cost.
```java

```

### Step 4 - Clean all the option gotten from the loop
This will clean all the option that are held in the array that do not have the wanted station as the last one from the node
```java

```

### Step 5 - Check if gotten into the wanted station
This basically check if the array has any array inside it, if so, it means that it got to the wanted station, so it
will go to the last node to get the total cost of the trip 
```java

```
