package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's website in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidWebsite(String)}
 */
public class Website {

    public static final String MESSAGE_CONSTRAINTS =
            "Websites [optional] should be a valid domain (e.g., example.com, www.example.com) "
                    + "without spaces and should not be blank if present";

    /*
     * The first character of the website must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    /*
     * The website should be a valid domain format without spaces.
     * Allows optional protocol (http/https), optional www subdomain,
     * domain name with at least one dot and valid TLD.
     */
    public static final String VALIDATION_REGEX =
            "^(?:https?://)?(?:www\\.)?[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?"
                    + "(\\.[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?)*\\.[a-zA-Z]{2,}(?:/.*)?$";

    public final String value;

    /**
     * Constructs an {@code Website}.
     *
     * @param website A valid website.
     */
    public Website(String website) {
        requireNonNull(website);
        checkArgument(website.isEmpty() || isValidWebsite(website), MESSAGE_CONSTRAINTS);
        value = website;
    }

    /**
     * Returns true if a given string is a valid website.
     */
    /**
     * Returns true if a given string is a valid website.
     */
    public static boolean isValidWebsite(String test) {
        if (test.isEmpty()) {
            return true; // Empty string is valid for optional field
        }

        // Reject any input containing spaces
        if (test.contains(" ")) {
            return false;
        }

        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Website)) {
            return false;
        }

        Website otherWebsite = (Website) other;
        return value.equals(otherWebsite.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
