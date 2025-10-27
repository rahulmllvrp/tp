package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUDGET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_EVENTS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventTime;
import seedu.address.model.person.Budget;

/**
 * Edits the details of an existing event in the address book.
 */
public class EditEventCommand extends Command {

    public static final String COMMAND_WORD = "editp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits party\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_EVENT_NAME + "NAME] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TIME + "TIME] "
            + "[" + PREFIX_BUDGET + "BUDGET] \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_EVENT_NAME + "John's Birthday "
            + PREFIX_DATE + "13-12-2025 "
            + PREFIX_TIME + "19:00" + PREFIX_BUDGET + "500";

    public static final String MESSAGE_EDIT_EVENT_SUCCESS = "Edited Party: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_EVENT = "This party already exists in the party list.";

    private final Index index;
    private final EditEventDescriptor editEventDescriptor;

    /**
     * @param index of the event in the filtered event list to edit
     * @param editEventDescriptor details to edit the event with
     */
    public EditEventCommand(Index index, EditEventDescriptor editEventDescriptor) {
        requireNonNull(index);
        requireNonNull(editEventDescriptor);

        this.index = index;
        this.editEventDescriptor = new EditEventDescriptor(editEventDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.saveStateForUndo("edit party " + index.getOneBased());
        List<Event> lastShownList = model.getFilteredEventList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToEdit = lastShownList.get(index.getZeroBased());
        Event editedEvent = createEditedEvent(eventToEdit, editEventDescriptor);

        if (!eventToEdit.isSameEvent(editedEvent) && model.hasEvent(editedEvent)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }

        model.setEvent(eventToEdit, editedEvent);
        model.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, Messages.format(editedEvent)));
    }

    /**
     * Creates and returns a {@code Event} with the details of {@code eventToEdit}
     * edited with {@code editEventDescriptor}.
     */
    private static Event createEditedEvent(Event eventToEdit, EditEventDescriptor editEventDescriptor) {
        assert eventToEdit != null;
        EventName updatedName = editEventDescriptor.getName().orElse(eventToEdit.getName());
        EventDate updatedDate = editEventDescriptor.getDate().orElse(eventToEdit.getDate());
        EventTime updatedTime = editEventDescriptor.getTime().orElse(eventToEdit.getTime());
        Budget updatedInitialBudget = editEventDescriptor.getBudget().orElse(eventToEdit.getInitialBudget());
        Budget updatedRemainingBudget;
        if (editEventDescriptor.getBudget().isPresent()) {
            updatedRemainingBudget = updatedInitialBudget;
        } else {
            updatedRemainingBudget = eventToEdit.getRemainingBudget();
        }
        // Keep participants unchanged
        return new Event(updatedName, updatedDate, updatedTime, eventToEdit.getParticipants(),
                updatedInitialBudget, updatedRemainingBudget);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditEventCommand)) {
            return false;
        }

        EditEventCommand otherEditEventCommand = (EditEventCommand) other;
        return index.equals(otherEditEventCommand.index)
                && editEventDescriptor.equals(otherEditEventCommand.editEventDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editEventDescriptor", editEventDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the event with. Each non-empty field value will replace the
     * corresponding field value of the event.
     */
    public static class EditEventDescriptor {
        private EventName name;
        private EventDate date;
        private EventTime time;
        private Budget budget;

        public EditEventDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditEventDescriptor(EditEventDescriptor toCopy) {
            setName(toCopy.name);
            setDate(toCopy.date);
            setTime(toCopy.time);
            setBudget(toCopy.budget);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, date, time, budget);
        }

        public void setName(EventName name) {
            this.name = name;
        }

        public Optional<EventName> getName() {
            return Optional.ofNullable(name);
        }

        public void setDate(EventDate date) {
            this.date = date;
        }

        public Optional<EventDate> getDate() {
            return Optional.ofNullable(date);
        }

        public void setTime(EventTime time) {
            this.time = time;
        }

        public Optional<EventTime> getTime() {
            return Optional.ofNullable(time);
        }

        public void setBudget(Budget budget) {
            this.budget = budget;
        }

        public Optional<Budget> getBudget() {
            return Optional.ofNullable(budget);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditEventDescriptor)) {
                return false;
            }

            EditEventDescriptor otherEditEventDescriptor = (EditEventDescriptor) other;
            return Objects.equals(name, otherEditEventDescriptor.name)
                    && Objects.equals(date, otherEditEventDescriptor.date)
                    && Objects.equals(time, otherEditEventDescriptor.time)
                    && Objects.equals(budget, otherEditEventDescriptor.budget);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("date", date)
                    .add("time", time)
                    .add("budget", budget)
                    .toString();
        }
    }
}
