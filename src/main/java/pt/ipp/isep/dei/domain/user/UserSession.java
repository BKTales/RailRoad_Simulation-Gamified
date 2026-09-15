package pt.ipp.isep.dei.domain.user;

import pt.ipp.isep.dei.application.Authentication.Email;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a user session in the system.
 * Encapsulates the current logged-in user and provides access
 * to the user's details and roles.
 */
public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;
    private final User user;

    /**
     * Constructs a UserSession with the specified user.
     *
     * @param user the user associated with this session; may be null to represent no logged-in user
     */
    public UserSession(User user) {
        this.user = user;
    }

    /**
     * Checks if there is an active logged-in user.
     *
     * @return true if a user is logged in; false otherwise
     */
    public boolean isLoggedIn() {
        return user != null;
    }

    /**
     * Returns the email of the logged-in user.
     *
     * @return the user's email if logged in; null otherwise
     */
    public Email getUserEmail() {
        return user != null ? user.getMail() : null;
    }

    /**
     * Returns the user associated with this session.
     *
     * @return the current user; may be null if no user is logged in
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the roles associated with the logged-in user.
     *
     * @return a list of UserRoleDTO objects representing the user's roles
     */
    public ArrayList<UserRoleDTO> getUserRoles() {
        return user.getRoles();
    }
}
