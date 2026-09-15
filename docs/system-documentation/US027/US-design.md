# US013 - As a Player, given a railway with stations and lines connecting pairs of stations, I want to verify if a specific train (steam, diesel, or electric powered) can travel between two stations belonging to the rail network (or from any type of station to another of the same type).

## 3. Design

### 3.1. Rationale

**The rationale grounds on the SSD interactions and the identified input/output data.**

| Interaction ID                                    | Question: Which class is responsible for... | Answer             | Justification (with patterns) |
|:--------------------------------------------------|:--------------------------------------------|:-------------------|:------------------------------|
| Step 1: receive the input                         | 	receive the input to get the shortest way? | `getShortestWayUI` | Pure Fabrication              |
| Step 2 : ask for the wanted scenario              | 	receive the wanted scenario input ?        | `getShortestWayUI` | Pure Fabrication              |
| Step 3: List the stations in scenario             | 	listing the stations in scenario?          | `readCsv`          | Pure Fabrication              |
| Step 4: display lists and ask the wanted stations | 	get the wanted stations?                   | `getShortestWayUI` | Pure Fabrication              |
|                                                   | 	list the trains?                           | `TrainRepository`  | Pure Fabrication              |
| Step 5: calculate the shortest way                | 	list the stations?                         | `getShortestWay`   | Pure Fabrication              |
| Step 6: Display the shortest way                  | 	list the trains?                           | `getShortestWayUI` | Pure Fabrication              |

### Systematization ##

According to the taken rationale, the conceptual classes promoted to software classes are:

* getShortestWay

Other software classes (i.e. Pure Fabrication) identified:

* getShortestWayUI  
* getShortestWayController

## 3.2. Sequence Diagram (SD)

_In this section, it is suggested to present an UML dynamic view representing the sequence of interactions between software objects that allows to fulfill the requirements._

![US027-SD](svg/US027-SD.svg)

## 3.3. Class Diagram (CD)

_In this section, it is suggested to present an UML static view representing the main related software classes that are involved in fulfilling the requirements as well as their relations, attributes and methods._

![US027-CD](svg/US013-CD.svg)
