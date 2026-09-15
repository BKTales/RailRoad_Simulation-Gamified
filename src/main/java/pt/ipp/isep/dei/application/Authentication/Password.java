package pt.ipp.isep.dei.application.Authentication;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a password with specific validation rules.
 * The password must have exactly 7 characters,
 * including exactly 2 digits and 3 uppercase letters.
 */
public class Password implements Serializable {
    private static final long serialVersionUID = 1L;
    private String password;
    private static final int NEEDED_CHARACTERS = 7;
    private static final int NEEDED_DIGITS = 2;
    private static final int NEEDED_CHARACTERS_UPPERCASE = 3;

    /**
     * Constructs a Password object after validating the given password string.
     *
     * @param password the password string to be validated and stored
     * @throws IllegalArgumentException if the password does not meet the required criteria
     */
    public Password(String password) {
        if (validate(password)) {
            this.password = password;
        }
    }

    /**
     * Validates the password based on length, number of digits, and number of uppercase characters.
     *
     * @param password the password string to validate
     * @return true if the password meets all criteria
     * @throws IllegalArgumentException if any validation rule is violated
     */
    private boolean validate(String password) {
        int capitalChars = 0;
        int digits = 0;
        if (password.length() < NEEDED_CHARACTERS) {
            throw new IllegalArgumentException("Password length must be at least " + NEEDED_CHARACTERS);
        }
        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);
            if (Character.isUpperCase(character)) {
                capitalChars++;
            } else if (Character.isDigit(character)) {
                digits++;
            }
        }
        if (digits != NEEDED_DIGITS) {
            throw new IllegalArgumentException("Number of digits must be " + NEEDED_DIGITS);
        }
        if (capitalChars != NEEDED_CHARACTERS_UPPERCASE) {
            throw new IllegalArgumentException("Number of capital letters must be " + NEEDED_CHARACTERS_UPPERCASE);
        }
        return true;
    }

    /**
     * Checks if the given input password matches the stored password after validation.
     *
     * @param inputPassword the password string to check against the stored password
     * @return true if the input password matches the stored password; false otherwise
     */
    public boolean checkPassword(String inputPassword) {
        if (validate(password)) {
            return password.equals(inputPassword);
        }
        return false;
    }

    /**
     * Returns the hash code of the password.
     *
     * @return the hash code value for this password
     */
    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    /**
     * Compares this Password object to another for equality.
     * Two Password objects are equal if their password strings are equal.
     *
     * @param o the object to compare to
     * @return true if both are Password instances with the same password string; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password other = (Password) o;
        return Objects.equals(this.password, other.password);
    }
}
