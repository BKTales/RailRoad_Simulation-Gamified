package pt.ipp.isep.dei.domain.train;

import java.io.Serializable;

/**
 * Enum representing the different types of locomotives.
 * <p>
 * This enum categorizes locomotives based on their propulsion system.
 * </p>
 */
public enum LocomotiveType implements Serializable {
    DIESEL_LOCOMOTIVE,
    ELECTRICITY_LOCOMOTIVE,
    STEAM_LOCOMOTIVE,
}