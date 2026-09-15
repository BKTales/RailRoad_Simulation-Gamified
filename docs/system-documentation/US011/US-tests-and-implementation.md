# US011 - As a Player, I want to list all trains.

_XXX stands for User Story number and YYY for User Story description (e.g. US006 - Create a Task)_

## 4. Tests
_In this section, it is suggested to systematize how the tests were designed to allow a correct measurement of requirements fulfilling._ 

**_DO NOT COPY ALL DEVELOPED TESTS HERE_**

**Test 1:** Check that it is not possible to create an instance of the Example class with null values. 

	@Test(expected = IllegalArgumentException.class)
		public void ensureNullIsNotAllowed() {
		Exemplo instance = new Exemplo(null, null);
	}

_It is also recommended to organize this content by subsections._


## 5. Construction (Implementation)

_In this section, it is suggested to provide, if necessary, some evidence that the construction/implementation is in accordance with the previously carried out design. Furthermore, it is recommeded to mention/describe the existence of other relevant (e.g. configuration) files and highlight relevant commits._

_It is also recommended to organize this content by subsections._ 


#### UI

```java
public void run() {
    List<String> trains = controller.getListOfAllTrains();

    if (trains == null || trains.isEmpty()) {
        try {
                pt.ipp.isep.dei.utils.Utils.displayReturnPlayer("There are no trains purchased yet.");
        } catch (Exception e) {
                e.printStackTrace();
        }
    } else {
        DisplayDynamicList2.displayList(trains, new DisplayListControllerInterface(){
            @Override
            public void initialize(List<String> list) {
            }

            @Override
            public void onItemSelected(int index) {
                Platform.runLater(() -> {
                    try {
                        PlayerMenu playerMenu = new PlayerMenu();
                        playerMenu.start(App.getPrimaryStage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
```

#### Controller

```java
private void initializeRepositories() {
    instance = Simulation.getInstance();
    this.trainRepository= instance.getTrainRepository();
}
```



```java
public ListAllTrainsController() {
    initializeRepositories();
    this.sortTrains = new SortTrains();
}
```

```java
public List<Train> getAllTrainsPurchased() {
    return trainRepository.getTrains();
}

```


```java
public List<Train> getSortedListOfAllTrainsPurchased(List<Train> allTrains) {
    return sortTrains.sortTrains(allTrains);
}
```

```java
public List<String> getListOfAllTrains() {
    List<Train> allTrains = getAllTrainsPurchased();
    List<Train> sortedTrains = getSortedListOfAllTrainsPurchased(allTrains);
    String trainsString = sortTrains.convertTrainListToString(sortedTrains);

    if (trainsString == null || trainsString.isEmpty()) {
        return Collections.emptyList();
    }
    return Arrays.asList(trainsString.split("\\r?\\n"));
}
```


#### SortTrains


```java
public List<Train> sortTrains(List<Train> allTrains) {
    Map<String, List<Train>> groupedByType = new HashMap<>();
    for (Train train : allTrains) {
        String locomotiveType = train.getLocomotive().getLocomotiveType().name();
        if (!groupedByType.containsKey(locomotiveType)) {
            groupedByType.put(locomotiveType, new ArrayList<>());
        }
        groupedByType.get(locomotiveType).add(train);
    }

    List<Train> sortedTrains = new ArrayList<>();

    for (Map.Entry<String, List<Train>> entry : groupedByType.entrySet()) {
        List<Train> trainsByType = entry.getValue();

        Collections.sort(trainsByType, new Comparator<Train>() {
            @Override
            public int compare(Train train1, Train train2) {
                return train1.getLocomotive().getName().compareTo(train2.getLocomotive().getName());
            }
        });

        sortedTrains.addAll(trainsByType);
    }

    return sortedTrains;
}
```

```java
public String convertTrainListToString(List<Train> trainList) {
    StringBuilder builder = new StringBuilder();

    for (Train train : trainList) {
        String name = train.getLocomotive().getName();
        String type = train.getLocomotive().getLocomotiveType().name();
        ArrayList<Cargo> cargo = train.getCargo();
        int id = train.getId();

        builder.append("Name: ").append(name)
                .append("| ID: ").append(id)
                .append("| Type: ").append(type)
                .append("| Cargo: ").append(cargo)
                .append("\n");
    }

    return builder.toString();
}
```

#### Train Repository

```java
public List<Train> getTrains() {
    return new ArrayList<>(trains);
}
```


## 6. Integration and Demo 

_In this section, it is suggested to describe the efforts made to integrate this functionality with the other features of the system._

###### List of purchased Trais organize according the AC from the client

![](images/ListOfPurchasedTrains.png.png)


## 7. Observations

_In this section, it is suggested to present a critical perspective on the developed work, pointing, for example, to other alternatives and or future related work._