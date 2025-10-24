package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventNameContainsKeywordsPredicate;
import seedu.address.testutil.EditEventDescriptorBuilder;

/**
 * Contains helper methods for testing event commands.
 */
public class EventCommandTestUtil {

    public static final String VALID_NAME_BIRTHDAY = "Birthday Party";
    public static final String VALID_DATE_BIRTHDAY = "12-12-2025";
    public static final String VALID_TIME_BIRTHDAY = "18:00";

    public static final String VALID_NAME_MEETING = "Project Meeting";
    public static final String VALID_DATE_MEETING = "01-01-2024";
    public static final String VALID_TIME_MEETING = "10:00";

    public static final String NAME_DESC_BIRTHDAY = " " + PREFIX_EVENT_NAME + VALID_NAME_BIRTHDAY;
    public static final String DATE_DESC_BIRTHDAY = " " + PREFIX_DATE + VALID_DATE_BIRTHDAY;
    public static final String TIME_DESC_BIRTHDAY = " " + PREFIX_TIME + VALID_TIME_BIRTHDAY;

    public static final String NAME_DESC_MEETING = " " + PREFIX_EVENT_NAME + VALID_NAME_MEETING;
    public static final String DATE_DESC_MEETING = " " + PREFIX_DATE + VALID_DATE_MEETING;
    public static final String TIME_DESC_MEETING = " " + PREFIX_TIME + VALID_TIME_MEETING;

    public static final String INVALID_NAME_DESC = " " + PREFIX_EVENT_NAME + ""; // empty string not allowed for names
    public static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "32-13-2025"; // invalid date format
    public static final String INVALID_TIME_DESC = " " + PREFIX_TIME + "25:00"; // invalid time format

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditEventCommand.EditEventDescriptor DESC_BIRTHDAY;
    public static final EditEventCommand.EditEventDescriptor DESC_MEETING;

    static {
        DESC_BIRTHDAY = new EditEventDescriptorBuilder().withName(VALID_NAME_BIRTHDAY)
                .withDate(VALID_DATE_BIRTHDAY).withTime(VALID_TIME_BIRTHDAY).build();
        DESC_MEETING = new EditEventDescriptorBuilder().withName(VALID_NAME_MEETING)
                .withDate(VALID_DATE_MEETING).withTime(VALID_TIME_MEETING).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered event list and selected event in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Event> expectedFilteredList = new ArrayList<>(actualModel.getFilteredEventList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredEventList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the event at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showEventAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredEventList().size());

        Event event = model.getFilteredEventList().get(targetIndex.getZeroBased());
        final String[] splitName = event.getName().fullName.split("\\s+");
        model.updateFilteredEventList(new EventNameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredEventList().size());
    }

}
