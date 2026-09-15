package pt.ipp.isep.dei.domain.rails;

import java.io.Serializable;

/**
 * Enum representing types of railway tracks.
 * <p>
 * Each track type has an associated cost multiplier that can be used for cost calculations.
 * </p>
 */
public enum TrackType implements Serializable {
    /**
     * Single rail track type with a cost multiplier of 1.
     */
    SINGLE_RAIL(1),

    /**
     * Double rail track type with a cost multiplier of 2.
     */
    DOUBLE_RAIL(2);

    public final double costMultiplier;

    /**
     * Constructs a TrackType with the specified cost multiplier.
     *
     * @param costMultiplier the multiplier applied to track construction cost
     */
    TrackType(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }


    /**
     * Returns a string representation of the track type, including its name and cost multiplier.
     *
     * <p>This method overrides {@code toString()} to provide a formatted summary of the track
     * type details, useful for displaying in UI or logs.</p>
     *
     * @return a string in the format: "TrackType: NAME, multiplier: VALUE"
     */
    @Override
    public String toString() {
        return String.format("TrackType: %s, Price multiplier: %.2f", name(), costMultiplier);
    }

}
