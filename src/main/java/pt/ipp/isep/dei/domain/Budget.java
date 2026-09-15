package pt.ipp.isep.dei.domain;

import java.io.Serializable;

/**
 * Represents a financial budget that tracks available funds.
 * Provides basic operations for managing and querying the budget balance.
 */
public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;
    private double funds;

    /**
     * Constructs a new Budget instance with the specified initial funds.
     *
     * @param value the initial amount of funds to set for this budget
     */
    public Budget(double value) {
        this.funds = value;
    }

    /**
     * Gets the current amount of available funds in this budget.
     *
     * @return the current funds balance as a double value
     */
    public double getFunds() {
        return funds;
    }

    /**
     * Adds the specified amount to the current funds.
     *
     * @param value the positive amount to add to the current funds
     */
    public void addFunds(double value) {
        this.funds += value;
    }

    /**
     * Subtracts the specified amount from the current funds.
     *
     * @param value the positive amount to subtract from current funds
     */
    public void subtractFunds(double value) {
        this.funds -= value;
    }

    /**
     * Checks whether the budget has at least the specified amount of funds available.
     *
     * @param value the amount to check against current funds
     * @return true if current funds are greater than or equal to the specified value,
     *         false otherwise
     */
    public boolean hasEnoughFunds(double value) {
        if (this.funds >= value) {
            return true;
        }
        return false;
    }

    /**
     * Attempts to deduct a specified amount from the available budget.
     * <p>
     * If the current funds are insufficient to cover the specified amount,
     * an {@link IllegalArgumentException} is thrown to indicate the failure.
     * </p>
     *
     * @param amount the amount to deduct from the budget
     * @throws IllegalArgumentException if the available funds are less than the specified amount
     */
    public void purchase(double amount) {
        funds -= amount;
    }
}