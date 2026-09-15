package pt.ipp.isep.dei.domain.rails;

import pt.ipp.isep.dei.domain.station.*;

import java.io.Serializable;

/**
 * Represents a railway line connecting two stations with specified rail and track types.
 * Implements Comparable to allow sorting by station names.
 */
public class RailwayLine implements Comparable<RailwayLine> , Serializable {
    private static final long serialVersionUID = 1L;
    private static final double COST_DISTANCE_MONEY_CONVERSION = 2;
    private TrackType trackType;
    private RailType railType;
    private StationType station1;
    private StationType station2;
    private double distance;

    /**
     * Constructs a RailwayLine between two stations with given rail and track types and a distance.
     *
     * @param station1  the first station of the railway line
     * @param station2  the second station of the railway line
     * @param railType  the type of rail used in this railway line
     * @param trackType the type of track used in this railway line
     * @param distance  the distance between the two stations in appropriate units
     */
    public RailwayLine(StationType station1, StationType station2, RailType railType, TrackType trackType, double distance) {
        if (station1 == null || station2 == null) {
            throw new IllegalArgumentException("Stations cannot be null");
        }


        if (railType == null || trackType == null) {
            throw new IllegalArgumentException("RailType and TrackType cannot be null");
        }


        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be positive");
        }

        this.station1 = station1;
        this.station2 = station2;
        this.distance = distance;
        this.railType = railType;
        this.trackType = trackType;
    }

    /**
     * Returns the track type of this railway line.
     *
     * @return the track type
     */
    public TrackType getTrackType() {
        return trackType;
    }

    /**
     * Returns the rail type of this railway line.
     *
     * @return the rail type
     */
    public RailType getRailType() {
        return railType;
    }

    /**
     * Returns the first station of this railway line.
     *
     * @return the first station
     */
    public StationType getStation1() {
        return station1;
    }

    /**
     * Returns the second station of this railway line.
     *
     * @return the second station
     */
    public StationType getStation2() {
        return station2;
    }

    /**
     * Returns the distance between the two stations.
     *
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Calculates the Euclidean distance between two stations.
     *
     * @param st1 the first station
     * @param st2 the second station
     * @return the distance between the two stations as a double
     */
    public static double calculateDistBetweenStations(StationType st1, StationType st2, int scale){
        int xSt1 = st1.getLocation().getPosition().getX();
        int ySt1 = st1.getLocation().getPosition().getY();
        int xSt2 = st2.getLocation().getPosition().getX();
        int ySt2 = st2.getLocation().getPosition().getY();

        return ((Math.abs(xSt2 - xSt1) + Math.abs(ySt2 - ySt1)) * scale);
    }

    /**
     * Calculates the total price for railway line construction.
     * @param distance distance between stations
     * @param pricePerUnit price per unit distance
     * @return total calculated price
     */
    public static double calculatePrice(double distance, double pricePerUnit) {
        return distance * COST_DISTANCE_MONEY_CONVERSION * pricePerUnit ;
    }
    /**
     * Compares this railway line with another railway line based on the names of their stations.
     * Primary comparison is by the name of the first station, then by the second station.
     *
     * @param other the other railway line to compare with
     * @return a negative integer, zero, or a positive integer as this railway line is less than,
     * equal to, or greater than the specified railway line.
     */
    @Override
    public int compareTo(RailwayLine other) {
        int cmp = station1.getName().compareTo(other.station1.getName());
        if (cmp == 0) {
            return station2.getName().compareTo(other.station2.getName());
        }
        return cmp;
    }

    /**
     * Returns a string representation of the railway line, including stations, track and rail types, and distance.
     *
     * @return a string describing the railway line
     */
    @Override
    public String toString() {
        return "RailwayLine : " +
                ", trackType=" + trackType +
                ", railType=" + railType +
                ", station1=" + station1.toString() +
                ", station2=" + station2.toString() +
                ", distance=" + distance +
                '\n';
    }
}
