package pt.ipp.isep.dei.domain.simulation;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.application.UserMenus.PlayerMenu;
import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.industries.Industry;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.route.Route;
import pt.ipp.isep.dei.domain.route.Trip;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.repository.*;
import pt.ipp.isep.dei.utils.ReadStringInput;
import pt.ipp.isep.dei.utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a simulation that manages the passage of years and updates industries accordingly.
 * <p>
 * The simulation tracks a current year, supports pausing and resuming, and updates industry production
 * as time progresses.
 * </p>
 */
public class    TimeCounter implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int CONVERSION_SEC_YEARS = 60;
    private final int MONTHS_PER_YEAR = 12;
    private static final String[] MONTH_NAMES = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };
    private final int startYear;

    private long timePausedStart;
    private boolean isPaused;
    private long timeStarted;

    private int endYear;
    private int currentYear;

    private double moneyEarned;
    private int speedMultiplier = 1;

    /**
     * Constructs a Simulation based on the scenario name.
     * Initializes the start and end year from the scenario's time restrictions.
     *
     * @param startYear the start year from scenario
     * @param endYear the end year from scenario
     */
    public TimeCounter(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
        timeStarted = Utils.getCurrentTime();
        currentYear = startYear;
        isPaused = false;
    }

    //  ============= play and pause ============== //

    /**
     * Pauses the simulation.
     * @return a confirmation message "Paused"
     */
    public String pause() {
        if (!isPaused) {
            isPaused = true;
            timePausedStart = Utils.getCurrentTime();
        }
        return "Paused";
    }

    /**
     * Resumes the simulation after being paused, adjusting timers to account for pause duration.
     *
     * @return a confirmation message "Played"
     */
    public String play() {
        if (isPaused) {
            long timeDiff = Utils.getCurrentTime() - timePausedStart;
            System.out.println("time Difference is " + timeDiff);
            refreshTimeAfterPause(Simulation.getInstance(), timeDiff);
            timeStarted += timeDiff;
            isPaused = false;
        }
        return "Played";
    }

    public void cycleSpeed() {
        long now = Utils.getCurrentTime();
        int oldMultiplier = speedMultiplier;

        switch (speedMultiplier) {
            case 1:  speedMultiplier = 2;  break;
            case 2:  speedMultiplier = 5;  break;
            case 5:  speedMultiplier = 10; break;
            default: speedMultiplier = 1;  break;
        }

        long gameElapsed = (now - timeStarted) * oldMultiplier;
        timeStarted = now - gameElapsed / speedMultiplier;
    }

    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    public long getGameTime() {
        long now = Utils.getCurrentTime();
        return (now - timeStarted) * speedMultiplier;
    }

    /**
     * Will go through every object of industry city and route, and refresh their time summing it to
     * the difference between the start and end of the pause
     * @param timeDiff
     */
    private void refreshTimeAfterPause(Simulation simulation, long timeDiff){
        List<Route> routes = simulation.getRouteRepository().getAllRoutes();
        for (Route r : routes){
            r.updateStartTimePause(timeDiff);
        }
        ArrayList<Industry> industries = simulation.getIndustryRepository().getIndustries();
        for (Industry i : industries){
            i.updateStartTimePause(timeDiff);
        }
    }

    // ============== Simulation Status  ==============

    /**
     * Advances the simulation time and updates all industries if the simulation is not paused.
     */
    public String refresh(Simulation simulation) {
        if (!isPaused && currentYear <= endYear) {
            String refresh = "";
            refresh += refreshCurrentYear(simulation);
            refresh += refreshCities(simulation.getCityRepository().getCities());
            refresh += refreshIndustries(simulation.getIndustryRepository().getIndustries());
            refreshStations(simulation.getStationRepository().getStations());
            refresh += refreshRoutes(simulation.getRouteRepository().getAllRoutes());

            return (refresh);
        }
        return (null);
    }

    /**
     * See if it is needed to refresh the current year, if so will refresh it.
     * In case of the refreshed year being the last of the simulation it will
     * end the simulation.
     */
    public String refreshCurrentYear(Simulation simulation){
        long timeNow = Utils.getCurrentTime();
        int yearToSum = (int) ((timeNow - timeStarted) * speedMultiplier / CONVERSION_SEC_YEARS);

        if ((yearToSum + startYear) > currentYear){
            currentYear++;
            if (currentYear == endYear)
                endDateReached();
            else {
                for (StationType st : simulation.getStationRepository().getStations()) {
                    st.refreshAnualCargoeValues();
                }
                return (displayChangeOfYear(simulation));
            }
        }
        return ("");
    }

    /**
     * Will get all the info to be displayed and return it to the previous method.
     * Info to be diplayed is:
     * - year and financial result
     * - track maintenance
     * - train maintenance
     * - train fuel
     * - Money generated
     *
     * @param simulation simulationsitoru
     * @return info from the year summary
     */
    private String displayChangeOfYear(Simulation simulation) {
        long totalCost = 0;
        double moneyEarnedTemp = moneyEarned;
        moneyEarned = 0;

        // - train maintenance - train fuel
        for (Train temp : simulation.getTrainRepository().getTrains()) {
            totalCost += temp.getLocomotive().getMaintenance();
            totalCost += temp.getLocomotive().getFuelCoast();
        }
        // - track maintenance
        for (RailwayLine temp : simulation.getRailwaylineRepository().getRails()){
            totalCost += (long) (temp.getDistance()
                                * temp.getRailType().maintainancePrice
                                * temp.getTrackType().costMultiplier);
        }
        Budget budget = Simulation.getInstance().getCurrentScenario().getBudget();
        budget.subtractFunds(totalCost);
        if (budget.getFunds() < 0){
            try {
                PlayerMenu pm = new PlayerMenu();
                pm.stopRefreshing();
                Utils.displayReturnPlayerSimulation("You went bankrupt!! Try again next time!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // - Money generated
        // - year and financial result
        return "In " + currentYear +
                " operational costs were $" +  totalCost +
                " and the profit was $" +  moneyEarnedTemp +
                ", generating a difference of $" + (moneyEarnedTemp - totalCost) + "\n";

    }

    /**
     * Method will show what happened during the simulation
     * - Different from initial budget
     * - Routes information
     * - Trains information
     *
     * <p> OBS: call the Ui with a new menu that will show that the simulation is over and
     * on button click will close the application </p>
     */
    private void endSimulation(){
        String allSimulation = "Simulation ended! Thanks for playing!\n";
        Simulation simulation = Simulation.getInstance();
        double budgedDifference = simulation.getCurrentScenario().getInitialBudget().getFunds();
        String routesInfo = simulation.getRouteRepository().getRoutesInfo();
        String stationsInformation = simulation.getStationRepository().getStationsInfo();
        String trainInformation = simulation.getTrainRepository().getTrainsInfo();
        allSimulation += stationsInformation + trainInformation + routesInfo;
        try{
            Utils.displayReturnPlayerSimulation(allSimulation);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method will add time to the current simulation to make it still playable
     * @param timeAskedQuestion time used to refresh the start time of the simulation
     */
    private void keepSimulating(long timeAskedQuestion){
        try {
            endYear += 50;
            timeStarted += Utils.getCurrentTime() - timeAskedQuestion;
            Utils.displayReturnPlayer("Simulation end year was incremented on 50 years! Now is until "
                    + endYear);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method that will handle the end date reached, will ask if the player still wants
     * to be playing and handle that input
     */
    private void endDateReached() {
        PlayerMenu pm = new PlayerMenu();
        pm.stopRefreshing();
        App.setMessage("Do you want to keep simulating(y/n)");
        long timeAskedQuestion = Utils.getCurrentTime();
        ReadStringInput.requestUserInputString(userInput -> {
            if (userInput.equals("y"))
                keepSimulating(timeAskedQuestion);
            else if (userInput.equals("n"))
                endSimulation();
        });
    }

    /**
     * Start the simulation, go through every object of Industry, Route and City to start their production
     * and trip.
     * @param industryRepository Repository where the industries are
     * @param routeRepository Repository where the route are
     * @param cityRepository Repository where the cities are
     */
    public void startSimulation(IndustryRepository industryRepository, RouteRepository routeRepository, CityRepository cityRepository) {
        long timeNow = getGameTime();

        for (Industry i : industryRepository.getIndustries()){
            i.startProduction(timeNow);
        }
        for (Route r : routeRepository.getAllRoutes()){
            r.startProduction(timeNow);
        }
    }

    // ============== Refresh  ==============

    /**
     * Function will go through every Station and check for changing in their activities
     * if it has produced something
     */
    private void refreshStations(ArrayList<StationType> stations) {
        long timeNow = Utils.getCurrentTime();
        String stationsUpdates = "";

        for (StationType st : stations){
            st.productsTransformation();
        }
    }

    /**
     * Function will go through every Industry and check for changing in their activities
     * if it has produced something
     */
    private String refreshIndustries(ArrayList<Industry> industries){
        long timeNow = getGameTime();
        String industriesUpdates = "";

        for (Industry i : industries){
            industriesUpdates += i.updateProduction(timeNow);
        }
        return (industriesUpdates);
    }

    /**
     * Function will go through every route and check for changing in their activities
     */

    private String refreshRoutes(List<Route> routes) {
        String routesUpdates = "";
        long timeNow = Utils.getCurrentTime();

        for (Route route : routes) {
            // Atualiza estado da rota (libertar próximas trips, reiniciar ciclo, etc.)
            String routeLog = route.updateStatus(timeNow);
            if (routeLog != null && !routeLog.isEmpty()) {
                routesUpdates += routeLog;
            }

            for (Trip trip : route.getTrips()) {
                String tripLog = trip.updateStatus(timeNow, moneyEarned);

                if (tripLog != null && !tripLog.isEmpty()) {
                    String[] logLines = tripLog.split("\t", 2);
                    double earned = Double.parseDouble(logLines[0]);
                    String tripLogBody = logLines.length > 1 ? logLines[1] : "";

                    moneyEarned = earned;

                    routesUpdates += tripLogBody;
                }
            }
        }

        return routesUpdates;
    }

    /**
     * Function will go through every City and check for changing in their activities(production
     * of people and mail and their quantity in the city)
     */
    private String refreshCities(ArrayList<City> cities){
        long timeNow = getGameTime();
        String citiesUpdates = "";

        for (City c : cities){
            citiesUpdates += c.updateProduction(timeNow);
        }
        return citiesUpdates;
    }

    /**
     * Refreshes the status of all trips in the provided list by updating each trip
     * based on the current simulation time. Aggregates and prints any updates
     * returned by the trips.
     *
     * @param trips the list of trips to refresh
     */
    public void refreshTrips(List<Trip> trips) {
        System.out.println("======== refreshing Trips ========");
        System.out.println("DEBUG: Trips a serem atualizados:");
        for (Trip t : trips) {
            System.out.println(" - Trip: " + t.getOrigin().getName() + " -> " + t.getDestination().getName());
        }

        long timeNow = Utils.getCurrentTime(); // obter o tempo atual da simulação
        String tripUpdates = "";

        for (Trip trip : trips) {
            tripUpdates += trip.updateStatus(timeNow, moneyEarned);
        }

        if (!tripUpdates.isEmpty()) {
            System.out.println(tripUpdates);
        }
    }

    // ================ Getters ================ //

    /**
     * Returns the current year in the simulation.
     *
     * @return the current simulation year
     */
    public int getCurrentYear() {
        return currentYear;
    }

    public String getCurrentDate() {
        long totalGameTime = getGameTime();
        long secondsIntoYear = totalGameTime % CONVERSION_SEC_YEARS;

        int month = (int) ((double) secondsIntoYear / CONVERSION_SEC_YEARS * MONTHS_PER_YEAR);
        if (month >= MONTHS_PER_YEAR) month = MONTHS_PER_YEAR - 1;

        return MONTH_NAMES[month] + " " + currentYear;
    }

    /**
     * Returns the simulation's end year.
     *
     * @return the end year for the simulation
     */
    public int getEndYear() {
        return endYear;
    }
}
