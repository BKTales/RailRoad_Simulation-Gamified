package pt.ipp.isep.dei.authenticationrepository;

import pt.ipp.isep.dei.application.Authentication.UserRole;
import pt.ipp.isep.dei.domain.user.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Facade class responsible for managing user authentication and user management operations.
 * Provides login, logout, user addition, and role management functionalities.
 */
public class AuthFacade implements Serializable {
    private static final long serialVersionUID = 1L;
    // Simulated database of users mapped by email
    private static Map<String, User> usersDB = new HashMap<>() {{
        put("editor@gmail.com", new Editor("editor@gmail.com", "ADMin12", "EDITOR", "Editor role"));
        put("user@gmail.com", new Player("user@gmail.com", "ADMin12", "PLAYER", "Player role"));
        put("ber@gmail.com", new Player("ber@gmail.com", "BB2b4cC", "ADMIN", "Admin role"));
    }};

    private UserSession currentSession = null;

    /**
     * Attempts to log in a user using the provided email and password.
     *
     * @param email the user's email
     * @param pwd   the user's password
     * @return a UserSession representing the logged-in user or an empty session if login failed
     */
    public UserSession doLogin(String email, String pwd) {
        User user = usersDB.get(email);
        if (user != null && isCorrectPassword(user, pwd)) {
            this.currentSession = new UserSession(user);
            return currentSession;
        }
        return new UserSession(null);
    }

    /**
     * Returns the current active user session.
     *
     * @return the current UserSession, or null if no user is logged in
     */
    public UserSession getCurrentUserSession() {
        return currentSession;
    }

    /**
     * Logs out the current user by clearing the current session.
     */
    public void doLogout() {
        currentSession = null;
    }

    /**
     * Adds a new user to the system with the specified email, password, role, and identifier.
     * Throws an IllegalArgumentException if the email already exists.
     *
     * @param email    the email of the new user
     * @param password the password for the new user
     * @param role     the role identifier (e.g., ADMIN, PLAYER, EDITOR)
     * @param id       a unique identifier or description for the user role
     */
    public void addUser(String email, String password, String role, String id) {
        String normalizedEmail = normalizeEmail(email);
        if (usersDB.containsKey(normalizedEmail)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        switch (role) {
            case "ADMIN":
                Admin admin = new Admin(normalizedEmail, password, role, id);
                usersDB.put(normalizedEmail, admin);
                break;
            case "PLAYER":
                Player player = new Player(normalizedEmail, password, role, id);
                usersDB.put(normalizedEmail, player);
                break;
            case "EDITOR":
                Editor editor = new Editor(normalizedEmail, password, role, id);
                usersDB.put(normalizedEmail, editor);
                break;
            default:
                throw new IllegalArgumentException("Unsupported role: " + role);
        }
    }

    /**
     * Checks if a user with the specified email exists in the system.
     *
     * @param email the email to check
     * @return true if the user exists, false otherwise
     */
    public boolean existsUser(String email) {
        return usersDB.containsKey(email);
    }

    /**
     * Adds a new user role to the system.
     *
     * @param id          the role identifier
     * @param role        the descriptive name of the role
     * @return true if the role was successfully added, false otherwise
     */
    public boolean addUserRole(String id, String role) {
        UserRole userRole = new UserRole(id, role);
        return User.addRole(userRole);
    }

    /**
     * Checks if the given password matches the user's stored password.
     *
     * @param user the user to check
     * @param pwd  the password to verify
     * @return true if the password is correct, false otherwise
     */
    private boolean isCorrectPassword(User user, String pwd) {
        return user.getPassword().checkPassword(pwd);
    }

    /**
     * Normalizes an email string by trimming whitespace.
     *
     * @param email the email to normalize
     * @return the trimmed email string
     */
    private String normalizeEmail(String email) {
        return email.trim();
    }
}
