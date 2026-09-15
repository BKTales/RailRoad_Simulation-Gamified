package pt.ipp.isep.dei.application.Authentication;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an email address with validation for a specific format.
 * Only allows emails that end with "@gmail.com".
 */
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;
    private String email;

    /**
     * Constructs an Email object after validating the given email string.
     *
     * @param email the email address to be validated and stored
     * @throws IllegalArgumentException if the email format is invalid
     */
    public Email(String email) {
        if (validate(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    /**
     * Validates the email by checking its format.
     *
     * @param email the email address to validate
     * @return true if the email format is valid, false otherwise
     */
    private boolean validate(String email) {
        return checkFormat(email);
    }

    /**
     * Checks if the email is not null, contains '@' and ends with "@gmail.com".
     *
     * @param email the email address to check
     * @return true if the email matches the required format, false otherwise
     */
    private boolean checkFormat(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        if (email.endsWith("@gmail.com")) {
            return true;
        }
        return false;
    }

    /**
     * Returns the email address string.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the hash code value for this email.
     *
     * @return the hash code of the email string
     */
    @Override
    public int hashCode() {
        return email.hashCode();
    }

    /**
     * Compares this Email object to another object for equality.
     * Two Email objects are equal if their email strings are equal.
     *
     * @param o the object to compare with
     * @return true if both objects are Email instances with equal email strings, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email other = (Email) o;
        return Objects.equals(this.email, other.email);
    }

    /**
     * Returns a string representation of this Email.
     *
     * @return a string in the format "Email: {email}"
     */
    @Override
    public String toString() {
        return String.format("Email: %s", email);
    }
}
