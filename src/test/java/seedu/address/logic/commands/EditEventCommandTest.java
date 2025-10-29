package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.EventCommandTestUtil.DESC_BIRTHDAY;
import static seedu.address.logic.commands.EventCommandTestUtil.DESC_MEETING;
import static seedu.address.logic.commands.EventCommandTestUtil.VALID_DATE_MEETING;
import static seedu.address.logic.commands.EventCommandTestUtil.VALID_NAME_MEETING;
import static seedu.address.logic.commands.EventCommandTestUtil.VALID_TIME_MEETING;
import static seedu.address.logic.commands.EventCommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.EventCommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.EventCommandTestUtil.showEventAtIndex;
import static seedu.address.testutil.TypicalEvents.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EVENT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditEventCommand.EditEventDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.Event;
import seedu.address.testutil.EditEventDescriptorBuilder;
import seedu.address.testutil.EventBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditEventCommand.
 */
public class EditEventCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Event editedEvent = new EventBuilder().withName("Unique Event Name").build();
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder(editedEvent).build();
        EditEventCommand editEventCommand = new EditEventCommand(INDEX_FIRST_EVENT, descriptor);

        String expectedMessage = String.format(
                EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, Messages.format(editedEvent));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setEvent(model.getFilteredEventList().get(0), editedEvent);

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastEvent = Index.fromOneBased(model.getFilteredEventList().size());
        Event lastEvent = model.getFilteredEventList().get(indexLastEvent.getZeroBased());

        EventBuilder eventInList = new EventBuilder(lastEvent);
        Event editedEvent = eventInList.withName("Another Unique Event Name").withDate(VALID_DATE_MEETING)
                .withTime(VALID_TIME_MEETING).build();

        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName("Another Unique Event Name")
                .withDate(VALID_DATE_MEETING).withTime(VALID_TIME_MEETING).build();
        EditEventCommand editEventCommand = new EditEventCommand(indexLastEvent, descriptor);

        String expectedMessage = String.format(
                EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, Messages.format(editedEvent));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setEvent(lastEvent, editedEvent);

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditEventCommand editEventCommand = new EditEventCommand(INDEX_FIRST_EVENT, new EditEventDescriptor());
        Event editedEvent = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());

        String expectedMessage = String.format(
                EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, Messages.format(editedEvent));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        Event eventInFilteredList = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        Event editedEvent = new EventBuilder(eventInFilteredList).withName("Filtered Unique Event Name").build();
        EditEventCommand editEventCommand = new EditEventCommand(INDEX_FIRST_EVENT,
                new EditEventDescriptorBuilder().withName("Filtered Unique Event Name").build());

        String expectedMessage = String.format(
                EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, Messages.format(editedEvent));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setEvent(model.getFilteredEventList().get(0), editedEvent);

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateEventUnfilteredList_failure() {
        Event firstEvent = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder(firstEvent).build();
        EditEventCommand editEventCommand = new EditEventCommand(INDEX_SECOND_EVENT, descriptor);

        assertCommandFailure(editEventCommand, model, EditEventCommand.MESSAGE_DUPLICATE_EVENT);
    }

    @Test
    public void execute_duplicateEventFilteredList_failure() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        // edit event in filtered list into a duplicate in address book
        Event eventInList = model.getAddressBook().getEventList().get(INDEX_SECOND_EVENT.getZeroBased());
        EditEventCommand editEventCommand = new EditEventCommand(INDEX_FIRST_EVENT,
                new EditEventDescriptorBuilder(eventInList).build());

        assertCommandFailure(editEventCommand, model, EditEventCommand.MESSAGE_DUPLICATE_EVENT);
    }

    @Test
    public void execute_invalidEventIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEventList().size() + 1);
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_NAME_MEETING).build();
        EditEventCommand editEventCommand = new EditEventCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidEventIndexFilteredList_failure() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);
        Index outOfBoundIndex = INDEX_SECOND_EVENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getEventList().size());

        EditEventCommand editEventCommand = new EditEventCommand(outOfBoundIndex,
                new EditEventDescriptorBuilder().withName(VALID_NAME_MEETING).build());

        assertCommandFailure(editEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditEventCommand standardCommand = new EditEventCommand(INDEX_FIRST_EVENT, DESC_BIRTHDAY);

        // same values -> returns true
        EditEventDescriptor copyDescriptor = new EditEventDescriptor(DESC_BIRTHDAY);
        EditEventCommand commandWithSameValues = new EditEventCommand(INDEX_FIRST_EVENT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditEventCommand(INDEX_SECOND_EVENT, DESC_BIRTHDAY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditEventCommand(INDEX_FIRST_EVENT, DESC_MEETING)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditEventDescriptor editEventDescriptor = new EditEventDescriptor();
        EditEventCommand editEventCommand = new EditEventCommand(index, editEventDescriptor);
        String expected = EditEventCommand.class.getCanonicalName() + "{index=" + index + ", editEventDescriptor="
                + editEventDescriptor + "}";
        assertEquals(expected, editEventCommand.toString());
    }

    @Test
    public void execute_editBudgetWithAssignedContacts_remainingBudgetCalculatedCorrectly() {
        // Create an event with initial budget 1000 and remaining budget 700 (300 already assigned)
        Event eventWithAssignedContacts = new EventBuilder()
                .withName("Test Party")
                .withBudget("1000")
                .withRemainingBudget("700")
                .build();

        Model testModel = new ModelManager(new AddressBook(), new UserPrefs());
        testModel.addEvent(eventWithAssignedContacts);

        // Edit the budget to 1500
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withBudget("1500").build();
        EditEventCommand editEventCommand = new EditEventCommand(INDEX_FIRST_EVENT, descriptor);

        // Expected: new remaining budget = 1500 - (1000 - 700) = 1500 - 300 = 1200
        Event expectedEvent = new EventBuilder()
                .withName("Test Party")
                .withBudget("1500")
                .withRemainingBudget("1200.0")
                .build();

        String expectedMessage = String.format(
                EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, Messages.format(expectedEvent));

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addEvent(expectedEvent);

        assertCommandSuccess(editEventCommand, testModel, expectedMessage, expectedModel);
    }

}
