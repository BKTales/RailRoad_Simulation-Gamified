package pt.ipp.isep.dei.domain.scenario;

import java.io.Serializable;

/**
 * Represents a time restriction with a start year and an end year.
 * Ensures that the start year is less than or equal to the end year
 * and that both years are non-negative.
 */
public class TimeRestrictions  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int startYear;
    private int endYear;

    /**
     * Constructs a TimeRestrictions object with the specified start and end years.
     *
     * @param startYear the beginning year of the restriction (must be non-negative)
     * @param endYear the ending year of the restriction (must be non-negative and >= startYear)
     * @throws IllegalArgumentException if startYear is greater than endYear or if any year is negative
     */
    public TimeRestrictions(int startYear, int endYear){
        if (!isValidYear(startYear, endYear)) {
            throw new IllegalArgumentException("Start year must be less than or equal to end year.");
        }
        if (startYear < 0 || endYear < 0) {
            throw new IllegalArgumentException("Years must be non-negative.");
        }

        this.startYear = startYear;
        this.endYear = endYear;
    }

    /**
     * Returns the start year of this time restriction.
     *
     * @return the start year
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * Returns the end year of this time restriction.
     *
     * @return the end year
     */
    public int getEndYear() {
        return endYear;
    }

    /**
     * Checks if the start year is less than or equal to the end year.
     *
     * @param startYear the start year to check
     * @param endYear the end year to check
     * @return true if startYear is less than or equal to endYear, false otherwise
     */
    public boolean isValidYear(int startYear, int endYear) {
        return startYear <= endYear;
    }

}
