package pt.ipp.isep.dei.domain.user;


import java.io.Serializable;

/**
 * Data Transfer Object (DTO) for transferring user role data between layers,
 * typically from the domain model to the user interface.
 * Encapsulates the role identifier and a formatted role description.
 */
public class UserRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roleId;
    private String roleDescription;

    /**
     * Constructs a UserRoleDTO with the specified role ID and description.
     *
     * @param id          the unique identifier of the role; must not be null or empty
     * @param description the formatted description of the role; must not be null or empty
     * @throws IllegalArgumentException if id or description is null or empty
     */
    public UserRoleDTO(String id, String description) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        this.roleId = id.trim();
        this.roleDescription = description.trim();
    }

    /**
     * Returns the role identifier.
     *
     * @return the role ID string
     */
    public String getId() {
        return roleId;
    }

    /**
     * Returns the formatted role description.
     *
     * @return the role description string
     */
    public String getDescription() {
        return roleDescription;
    }

    /**
     * Returns a string representation of this UserRoleDTO.
     *
     * @return a string in the format UserRoleDTO{id='roleId', description='roleDescription'}
     */
    @Override
    public String toString() {
        return String.format("UserRoleDTO{id='%s', description='%s'}", roleId, roleDescription);
    }
}
