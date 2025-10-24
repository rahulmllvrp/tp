package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalPersons.getTypicalAbsolutSinema;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AbsolutSinema;
import seedu.address.model.ReadOnlyAbsolutSinema;
import seedu.address.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonAbsolutSinemaStorage absolutSinemaStorage = new JsonAbsolutSinemaStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(absolutSinemaStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void absolutSinemaReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAbsolutSinemaStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAbsolutSinemaStorageTest} class.
         */
        AbsolutSinema original = getTypicalAbsolutSinema();
        storageManager.saveAbsolutSinema(original);
        ReadOnlyAbsolutSinema retrieved = storageManager.readAbsolutSinema().get();
        assertEquals(original, new AbsolutSinema(retrieved));
    }

    @Test
    public void getAbsolutSinemaFilePath() {
        assertNotNull(storageManager.getAbsolutSinemaFilePath());
    }

}
