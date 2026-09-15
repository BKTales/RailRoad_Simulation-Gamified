package pt.ipp.isep.dei.domain.user;

import pt.ipp.isep.dei.application.Authentication.Email;
import pt.ipp.isep.dei.application.Authentication.Password;
import pt.ipp.isep.dei.application.Authentication.UserRole;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract base class representing a system user.
 * Encapsulates user identity, authentication credentials, and role information.
 * Manages a static list of available user roles in the system.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Email id;
    private Password password;
    private UserRole userRole;
    private static ArrayList<UserRole> roles = new ArrayList<>();

    static {
        roles.add(new UserRole("EDITOR", "Editor role"));
        roles.add(new UserRole("PLAYER", "Player role"));
        roles.add(new UserRole("ADMIN", "Admin role"));
    }

    /**
     * Constructs a new User with the specified email, password, role ID, and role description.
     *
     * @param id              the user's email address (identifier)
     * @param password        the user's password
     * @param roleId          the identifier of the user's role
     * @param roleDescription the descriptive name of the user's role
     */
    public User(String id, String password, String roleId, String roleDescription) {
        this.password = new Password(password);
        this.id = new Email(id);
        this.userRole = new UserRole(roleId, roleDescription);
    }

    /**
     * Returns a list of all available user roles as DTOs.
     *
     * @return an ArrayList of UserRoleDTO representing all system roles
     */
    public ArrayList<UserRoleDTO> getRoles() {
        ArrayList<UserRoleDTO> dtoList = new ArrayList<>();
        for (UserRole role : roles) {
            dtoList.add(new UserRoleDTO(role.getRoleId(), role.getRoleDescription()));
        }
        return dtoList;
    }

    /**
     * Returns the email address (identifier) of this user.
     *
     * @return the user's email as an Email object
     */
    public Email getMail() {
        return id;
    }

    /**
     * Returns the password of this user.
     *
     * @return the user's password as a Password object
     */
    public Password getPassword() {
        return password;
    }

    /**
     * Returns the role of this user as a Data Transfer Object (DTO).
     *
     * @return a UserRoleDTO representing the user's role
     */
    public UserRoleDTO getUserRole() {
        return new UserRoleDTO(userRole.getRoleId(), userRole.getRoleDescription());
    }

    /**
     * Returns the static list of all available user roles.
     *
     * @return an ArrayList of UserRole representing all roles
     */
    public static ArrayList<UserRole> getUserRoles() {
        return roles;
    }

    /**
     * Adds a new role to the static list of user roles.
     *
     * @param role the UserRole to add
     * @return true if the role was successfully added, false otherwise
     */
    public static boolean addRole(UserRole role) {
        return roles.add(role);
    }

    /**
     * Returns a string representation of the user, including email and password.
     *
     * @return a string describing the user
     */
    @Override
    public String toString() {
        return String.format("User[mail=%s, password=%s]", id, password);
    }
}
