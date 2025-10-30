package seedu.address.commons.exceptions;

/**
 * Represents a corruption error when loading persisted data (e.g. checksum mismatch).
 */
public class DataCorruptionException extends DataLoadingException {
    public DataCorruptionException(String message) {
        super(message);
    }

    public DataCorruptionException(String message, Exception cause) {
        super(message, cause);
    }

    public DataCorruptionException(Exception cause) {
        super(cause);
    }
}

