package seedu.address.commons.exceptions;

/**
 * Represents an error during loading of data from a file.
 */
public class DataLoadingException extends Exception {
    public DataLoadingException(Exception cause) {
        super(cause);
    }

    // Additional constructors for richer error reporting
    public DataLoadingException(String message) {
        super(message);
    }

    public DataLoadingException(String message, Exception cause) {
        super(message, cause);
    }
}
