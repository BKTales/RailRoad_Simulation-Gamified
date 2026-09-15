package pt.ipp.isep.dei.application.Authentication;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a user role with an identifier and description.
 * The role ID must be non-null and non-empty after trimming.
 * The description must be non-null and non-empty.
 */
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roleId;
    private String roleDescription;

    /**
     * Constructs a UserRole object with the given ID and description.
     *
     * @param id          the identifier of the role; must not be null or empty (trimmed)
     * @param description the description of the role; must not be null or empty
     * @throws IllegalArgumentException if the id or description are null or empty
     */
    public UserRole(String id, String description) {
        String validId = extractId(id);
        if (validId == null || validId.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        this.roleId = validId;
        this.roleDescription = description;
    }

    /**
     * Returns the role ID.
     *
     * @return the role identifier string
     */
    public String getRoleId() {
        return this.roleId;
    }

    /**
     * Returns the role description.
     *
     * @return the role description string
     */
    public String getRoleDescription() {
        return this.roleDescription;
    }

    /**
     * Extracts and trims the role ID string.
     *
     * @param id the raw ID string
     * @return trimmed ID string or null if input is null
     */
    private String extractId(String id) {
        if (id == null) return null;
        return id.trim();
    }

    /**
     * Changes the role description to the provided new description.
     *
     * @param newDescription the new description to set; must be non-null and non-empty (trimmed)
     * @return true if the description was successfully changed; false otherwise
     */
    public boolean changeDescription(String newDescription) {
        if (newDescription == null || newDescription.trim().isEmpty()) {
            return false;
        }
        this.roleDescription = newDescription.trim();
        return true;
    }

    /**
     * Returns the hash code based on the role ID.
     *
     * @return the hash code of this UserRole
     */
    @Override
    public int hashCode() {
        return Objects.hash(roleId);
    }

    /**
     * Compares this UserRole with another object for equality.
     * Equality is based on case-insensitive comparison of role IDs.
     *
     * @param o the object to compare with
     * @return true if the other object is a UserRole with the same role ID ignoring case; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole other = (UserRole) o;
        return this.roleId.equalsIgnoreCase(other.roleId);
    }

    /**
     * Returns a string representation of the UserRole.
     *
     * @return a string formatted as UserRole{id='roleId', description='roleDescription'}
     */
    @Override
    public String toString() {
        return String.format("UserRole{id='%s', description='%s'}", roleId, roleDescription);
    }
}
