package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAbsolutSinema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UndoCommand.
 */
public class UndoCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAbsolutSinema(), new UserPrefs());
        expectedModel = new ModelManager(model.getAbsolutSinema(), new UserPrefs());
    }

    @Test
    public void execute_validUndo_success() throws Exception {
        // Save state before adding person
        model.saveStateForUndo("add person");

        // Add a person
        model.addPerson(new PersonBuilder().withName("John Doe").build());

        UndoCommand undoCommand = new UndoCommand();

        String expectedMessage = String.format(UndoCommand.MESSAGE_SUCCESS, "add person");

        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noCommandToUndo_throwsCommandException() {
        UndoCommand undoCommand = new UndoCommand();

        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_NO_COMMAND_TO_UNDO);
    }

    @Test
    public void execute_canUndo_returnsTrue() throws Exception {
        assertFalse(model.canUndo());

        model.saveStateForUndo("test operation");

        assertTrue(model.canUndo());
    }

    @Test
    public void execute_undoAfterDeletePerson_success() throws Exception {
        // Add a person to delete
        model.addPerson(new PersonBuilder().withName("Test Person").build());

        // Save state before deleting
        model.saveStateForUndo("delete person");

        // Delete the person
        model.deletePerson(model.getFilteredPersonList().get(model.getFilteredPersonList().size() - 1));

        UndoCommand undoCommand = new UndoCommand();
        String expectedMessage = String.format(UndoCommand.MESSAGE_SUCCESS, "delete person");

        // Expected model should have the person back
        expectedModel.addPerson(new PersonBuilder().withName("Test Person").build());

        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        UndoCommand undoCommand1 = new UndoCommand();
        UndoCommand undoCommand2 = new UndoCommand();

        // same object -> returns true
        assertTrue(undoCommand1.equals(undoCommand1));

        // same type -> returns true
        assertTrue(undoCommand1.equals(undoCommand2));

        // different types -> returns false
        assertFalse(undoCommand1.equals(1));

        // null -> returns false
        assertFalse(undoCommand1.equals(null));
    }
}
