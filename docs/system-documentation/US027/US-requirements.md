# US27 - As a Player, given a scenario with stations and railway lines, I want to get one of the shortest routes between two stations, which goes through an ordered list of stations I choose.

## 1. Requirements Engineering

### 1.1. User Story Description

As a player, given an ordered station list, I want to have the shortest way of traveling between all that stations.

### 1.2. Customer Specifications and Clarifications 

#### From specification document

#### From the client clarifications
> **Q:** 
>
> **A:** 

### 1.3. Acceptance Criteria

- **AC01**: A visualization of the scenario should be displayed to the player,
  where the shortest route is drawn with a different color.
- **AC02**: All implemented procedures (except those used for graphic visualization) must only
 use primitive operations, and not existing functions in JAVA libraries.
- **AC03**: If the path is impossible, the function shall return a negative number.

### 1.4. Found out Dependencies

### 1.5 Input and Output Data

Inputs:
- Stations to be passed by (ordered list);
- Type of train to be assigned 

Outputs:
- List of station that compose the shortest way.

### 1.6. System Sequence Diagram (SSD)

![US027-SSD](svg/US027-SSD.svg)

### 1.7 Other Relevant Remarks

Using the Dijkstra algorithm to find to verify if there is some path between the station and if so it will retrieve the shortest one.
