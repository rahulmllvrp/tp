package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAbsolutSinema;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AbsolutSinema;
import seedu.address.model.ReadOnlyAbsolutSinema;

public class JsonAbsolutSinemaStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAbsolutSinemaStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAbsolutSinema_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAbsolutSinema(null));
    }

    private java.util.Optional<ReadOnlyAbsolutSinema> readAbsolutSinema(String filePath) throws Exception {
        return new JsonAbsolutSinemaStorage(Paths.get(filePath)).readAbsolutSinema(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAbsolutSinema("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readAbsolutSinema("notJsonFormatAbsolutSinema.json"));
    }

    @Test
    public void readAbsolutSinema_invalidPersonAbsolutSinema_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAbsolutSinema("invalidPersonAbsolutSinema.json"));
    }

    @Test
    public void readAbsolutSinema_invalidAndValidPersonAbsolutSinema_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAbsolutSinema("invalidAndValidPersonAbsolutSinema.json"));
    }

    @Test
    public void readAndSaveAbsolutSinema_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAbsolutSinema.json");
        AbsolutSinema original = getTypicalAbsolutSinema();
        JsonAbsolutSinemaStorage jsonAbsolutSinemaStorage = new JsonAbsolutSinemaStorage(filePath);

        // Save in new file and read back
        jsonAbsolutSinemaStorage.saveAbsolutSinema(original, filePath);
        ReadOnlyAbsolutSinema readBack = jsonAbsolutSinemaStorage.readAbsolutSinema(filePath).get();
        assertEquals(original, new AbsolutSinema(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonAbsolutSinemaStorage.saveAbsolutSinema(original, filePath);
        readBack = jsonAbsolutSinemaStorage.readAbsolutSinema(filePath).get();
        assertEquals(original, new AbsolutSinema(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonAbsolutSinemaStorage.saveAbsolutSinema(original); // file path not specified
        readBack = jsonAbsolutSinemaStorage.readAbsolutSinema().get(); // file path not specified
        assertEquals(original, new AbsolutSinema(readBack));

    }

    @Test
    public void saveAbsolutSinema_nullAbsolutSinema_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAbsolutSinema(null, "SomeFile.json"));
    }

    /**
     * Saves {@code absolutSinema} at the specified {@code filePath}.
     */
    private void saveAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema, String filePath) {
        try {
            new JsonAbsolutSinemaStorage(Paths.get(filePath))
                    .saveAbsolutSinema(absolutSinema, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAbsolutSinema_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAbsolutSinema(new AbsolutSinema(), null));
    }
}
