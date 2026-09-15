package pt.ipp.isep.dei.domain.user;

import java.io.Serializable;

/**
 * Represents an administrative user with permission to add new users.
 * Inherits all properties and behavior from the base User class.
 */
public class Admin extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Constructs a new Admin user with the specified email, password, role, and ID.
     *
     * @param email    the email address of the admin user
     * @param password the password for the admin user account
     * @param role     the role assigned to the user, typically "admin"
     * @param id       the unique identifier of the admin user
     */
    public Admin(String email, String password, String role, String id) {
        super(email, password, role, id);
    }
}
