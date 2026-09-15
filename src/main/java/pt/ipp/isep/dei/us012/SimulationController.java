package pt.ipp.isep.dei.us012;

import pt.ipp.isep.dei.domain.simulation.TimeCounter;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.io.Serializable;

public class SimulationController implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int PLAY = 1;
    private final int PAUSE = 0;
    private Simulation repo;
    private TimeCounter timeCounter;

    /**
     * Default constructor for SimulationController.
     */
    SimulationController() {
    }

    /**
     * Changes the status of the simulation to play or pause.
     *
     * @param status integer representing the desired status (PLAY=1 or PAUSE=0)
     * @return the result message of the status change from the simulation
     */
    public String changeStatus(int status) {
        repo = Simulation.getInstance();
        timeCounter = repo.getTimeCounter();

        if (status == PAUSE)
            return (timeCounter.pause());
        else
            return (timeCounter.play());
    }
}
