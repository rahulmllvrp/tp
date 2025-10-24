package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.testutil.AbsolutSinemaBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AbsolutSinema(), new AbsolutSinema(modelManager.getAbsolutSinema()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAbsolutSinemaFilePath(Paths.get("absolut/sinema/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAbsolutSinemaFilePath(Paths.get("new/absolut/sinema/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAbsolutSinemaFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAbsolutSinemaFilePath(null));
    }

    @Test
    public void setAbsolutSinemaFilePath_validPath_setsAbsolutSinemaFilePath() {
        Path path = Paths.get("absolut/sinema/file/path");
        modelManager.setAbsolutSinemaFilePath(path);
        assertEquals(path, modelManager.getAbsolutSinemaFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAbsolutSinema_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAbsolutSinema_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void equals() {
        AbsolutSinema absolutSinema = new AbsolutSinemaBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AbsolutSinema differentAbsolutSinema = new AbsolutSinema();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(absolutSinema, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(absolutSinema, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different absolutSinema -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAbsolutSinema, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(absolutSinema, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAbsolutSinemaFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(absolutSinema, differentUserPrefs)));
    }

    @Test
    public void canUndo_noStatesSaved_returnsFalse() {
        assertFalse(modelManager.canUndo());
    }

    @Test
    public void canUndo_statesSaved_returnsTrue() {
        modelManager.saveStateForUndo("test operation");
        assertTrue(modelManager.canUndo());
    }

    @Test
    public void undo_validState_success() {
        // Save initial state
        modelManager.saveStateForUndo("add person");

        // Add a person
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));

        // Undo the operation
        String result = modelManager.undo();

        assertEquals("add person", result);
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void undo_noStateSaved_returnsDefaultMessage() {
        String result = modelManager.undo();
        assertEquals("No operation to undo", result);
    }

    @Test
    public void saveStateForUndo_validOperation_success() {
        assertFalse(modelManager.canUndo());

        modelManager.saveStateForUndo("test operation");

        assertTrue(modelManager.canUndo());
    }

    @Test
    public void undo_afterMultipleOperations_undoesLastSavedState() {
        // Save state before first operation
        modelManager.saveStateForUndo("first operation");

        // Add ALICE
        modelManager.addPerson(ALICE);

        // Save state before second operation
        modelManager.saveStateForUndo("second operation");

        // Add BENSON
        modelManager.addPerson(BENSON);

        // Undo should revert to state after ALICE was added
        String result = modelManager.undo();

        assertEquals("second operation", result);
        assertTrue(modelManager.hasPerson(ALICE));
        assertFalse(modelManager.hasPerson(BENSON));
    }
}
