package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAbsolutSinema;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends AbsolutSinemaStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getAbsolutSinemaFilePath();

    @Override
    Optional<ReadOnlyAbsolutSinema> readAbsolutSinema() throws DataLoadingException;

    @Override
    void saveAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema) throws IOException;

}
