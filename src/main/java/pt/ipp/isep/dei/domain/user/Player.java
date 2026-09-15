package pt.ipp.isep.dei.domain.user;

import pt.ipp.isep.dei.domain.Budget;

import java.io.Serializable;

/**
 * Represents a player user who can play the simulation and has an associated budget .
 * Extends the base User class and includes a budget attribute to manage financial game resources.
 */
public class Player extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Budget budget;

    /**
     * Constructs a new Player user with the specified email, password, role ID, and role description.
     *
     * @param email           the email address of the player
     * @param password        the password for the player account
     * @param roleId          the role identifier of the player
     * @param roleDescription the descriptive role name of the player
     */
    public Player(String email, String password, String roleId, String roleDescription) {
        super(email, password, roleId, roleDescription);
    }

    /**
     * Returns the budget associated with this player.
     *
     * @return the player's budget
     */
    public Budget getBudget() {
        return budget;
    }

    public void setBudgetAmount(double amount) {
        this.budget = new Budget(amount);
    }

}
