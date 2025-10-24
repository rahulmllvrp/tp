package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAbsolutSinema;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of AbsolutSinema data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AbsolutSinemaStorage absolutSinemaStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code AbsolutSinemaStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(AbsolutSinemaStorage absolutSinemaStorage, UserPrefsStorage userPrefsStorage) {
        this.absolutSinemaStorage = absolutSinemaStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AbsolutSinema methods ==============================

    @Override
    public Path getAbsolutSinemaFilePath() {
        return absolutSinemaStorage.getAbsolutSinemaFilePath();
    }

    @Override
    public Optional<ReadOnlyAbsolutSinema> readAbsolutSinema() throws DataLoadingException {
        return readAbsolutSinema(absolutSinemaStorage.getAbsolutSinemaFilePath());
    }

    @Override
    public Optional<ReadOnlyAbsolutSinema> readAbsolutSinema(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return absolutSinemaStorage.readAbsolutSinema(filePath);
    }

    @Override
    public void saveAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema) throws IOException {
        saveAbsolutSinema(absolutSinema, absolutSinemaStorage.getAbsolutSinemaFilePath());
    }

    @Override
    public void saveAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        absolutSinemaStorage.saveAbsolutSinema(absolutSinema, filePath);
    }

}
