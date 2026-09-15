package pt.ipp.isep.dei.domain.user;

import java.io.Serializable;

/**
 * Represents an editor user with permissions to modify maps,scenarios,industrys and citys.
 * Extends the base User class inheriting all its attributes and behavior.
 */
public class Editor extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Constructs a new Editor user with the specified email, password, role, and ID.
     *
     * @param email    the email address of the editor user
     * @param password the password for the editor user account
     * @param role     the role assigned to the user, typically "editor"
     * @param id       the unique identifier of the editor user
     */
    public Editor(String email, String password, String role, String id) {
        super(email, password, role, id);
    }
}
