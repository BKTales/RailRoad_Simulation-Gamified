package pt.ipp.isep.dei.domain;

import java.io.Serializable;

/**
 * Exception thrown when a budget is insufficient to complete an operation.
 */
public class InsuficientBudget extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Constructs a new InsuficientBudget exception with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public InsuficientBudget(String message) {
        super(message);
    }

    /**
     * Constructs a new InsuficientBudget exception with a default message.
     */
    public InsuficientBudget() {
        super("Insufficient Budget!");
    }
}
