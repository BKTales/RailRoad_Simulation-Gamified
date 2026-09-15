package pt.ipp.isep.dei.domain.position;

import pt.ipp.isep.dei.repository.Simulator;

import java.io.Serializable;
import java.util.Random;

/**
 * Utility class to generate a random Position around a given center Position within a specified radius.
 * Ensures the generated Position is within the bounds of the current map.
 */
public class PositionRandomizer implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Random random = new Random();
    private static Simulator simulator = Simulator.getInstance();

    private static int offsetX;
    private static int offsetY;

    /**
     * Generates a random Position around a given center Position, within the specified radius.
     * The generated Position will be within the map boundaries.
     *
     * @param center the center Position around which the random position is generated
     * @return a new Position randomly chosen within the radius around the center
     * @throws IllegalArgumentException if the radius is larger than the current map dimensions
     */
    public static Position getRandomPositionAround(Position center) {
        do {
            offsetX = (int) random.nextGaussian();
            offsetY = (int) random.nextGaussian();

            offsetX += 1;
            offsetY += 1;
        } while (center.getX() + offsetX >= simulator.getScenarioRepository().getActiveScenario().getAttachedMap().getLength()
                || center.getY() + offsetY >= simulator.getScenarioRepository().getActiveScenario().getAttachedMap().getHeight());

        return new Position(center.getX() + offsetX, center.getY() + offsetY);
    }
}
