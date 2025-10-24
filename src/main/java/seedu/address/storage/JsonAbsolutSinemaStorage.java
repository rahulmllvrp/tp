package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyAbsolutSinema;

/**
 * A class to access AbsolutSinema data stored as a json file on the hard disk.
 */
public class JsonAbsolutSinemaStorage implements AbsolutSinemaStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonAbsolutSinemaStorage.class);

    private Path filePath;

    public JsonAbsolutSinemaStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getAbsolutSinemaFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyAbsolutSinema> readAbsolutSinema() throws DataLoadingException {
        return readAbsolutSinema(filePath);
    }

    /**
     * Similar to {@link #readAbsolutSinema()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyAbsolutSinema> readAbsolutSinema(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableAbsolutSinema> jsonAbsolutSinema = JsonUtil.readJsonFile(
                filePath, JsonSerializableAbsolutSinema.class);
        if (!jsonAbsolutSinema.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonAbsolutSinema.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema) throws IOException {
        saveAbsolutSinema(absolutSinema, filePath);
    }

    /**
     * Similar to {@link #saveAbsolutSinema(ReadOnlyAbsolutSinema)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema, Path filePath) throws IOException {
        requireNonNull(absolutSinema);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableAbsolutSinema(absolutSinema), filePath);
    }

}
