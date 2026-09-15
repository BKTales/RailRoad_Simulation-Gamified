package pt.ipp.isep.dei.authenticationrepository;

import pt.ipp.isep.dei.domain.user.UserSession;

import java.io.Serializable;

/**
 * Repository class responsible for managing user authentication operations.
 * Acts as a facade to the underlying authentication mechanism.
 */
public class AuthenticationRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private final AuthFacade authenticationFacade;

    /**
     * Initializes the AuthenticationRepository with a new instance of AuthFacade.
     */
    public AuthenticationRepository() {
        authenticationFacade = new AuthFacade();
    }

    /**
     * Attempts to log in a user with the given email and password.
     *
     * @param email the email of the user trying to log in
     * @param pwd   the password of the user
     * @return true if login was successful, false otherwise
     */
    public boolean doLogin(String email, String pwd) {
        return authenticationFacade.doLogin(email, pwd).isLoggedIn();
    }

    /**
     * Logs out the current user session.
     */
    public void doLogout() {
        authenticationFacade.doLogout();
    }

    /**
     * Retrieves the current active user session.
     *
     * @return the current UserSession, or null if no user is logged in
     */
    public UserSession getCurrentUserSession() {
        return authenticationFacade.getCurrentUserSession();
    }

    /**
     * Adds a new user with the specified details.
     *
     * @param email    the new user's email
     * @param password the new user's password
     * @param role     the role identifier for the new user
     * @param id       a unique identifier for the new user
     */
    public void addUser(String email, String password, String role, String id) {
        authenticationFacade.addUser(email, password, role, id);
    }

    /**
     * Checks if a user with the specified identifier exists.
     *
     * @param id the identifier to check for user existence
     * @return true if the user exists, false otherwise
     */
    public boolean existsUser(String id) {
        return authenticationFacade.existsUser(id);
    }

    /**
     * Adds a new user role with the given identifier and description.
     *
     * @param id          the role identifier
     * @param description the descriptive name of the role
     * @return true if the role was added successfully, false otherwise
     */
    public boolean addUserRole(String id, String description) {
        return authenticationFacade.addUserRole(id, description);
    }
}
