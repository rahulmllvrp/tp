package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAbsolutSinema;

/**
 * Represents a storage for {@link seedu.address.model.AbsolutSinema}.
 */
public interface AbsolutSinemaStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getAbsolutSinemaFilePath();

    /**
     * Returns AbsolutSinema data as a {@link ReadOnlyAbsolutSinema}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyAbsolutSinema> readAbsolutSinema() throws DataLoadingException;

    /**
     * @see #getAbsolutSinemaFilePath()
     */
    Optional<ReadOnlyAbsolutSinema> readAbsolutSinema(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyAbsolutSinema} to the storage.
     * @param absolutSinema cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema) throws IOException;

    /**
     * @see #saveAbsolutSinema(ReadOnlyAbsolutSinema)
     */
    void saveAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema, Path filePath) throws IOException;

}
