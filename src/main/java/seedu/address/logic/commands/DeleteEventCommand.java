package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;

/**
 * Deletes an event identified using it's displayed index from the party list.
 */
public class DeleteEventCommand extends Command {

    public static final String COMMAND_WORD = "deletep";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the party identified by the index number used in the displayed party list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted Party: %1$s";

    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to delete this party?";

    private final Index targetIndex;

    private final boolean isConfirmed;

    public DeleteEventCommand(Index targetIndex) {
        this(targetIndex, false);
    }

    /**
     * @param targetIndex of the event in the filtered event list to delete
     * @param isConfirmed to confirm the deletion
     */
    public DeleteEventCommand(Index targetIndex, boolean isConfirmed) {
        this.targetIndex = targetIndex;
        this.isConfirmed = isConfirmed;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Event> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        if (!isConfirmed) {
            return new CommandResult(MESSAGE_CONFIRMATION, new DeleteEventCommand(targetIndex, true));
        }

        model.saveStateForUndo("delete party " + targetIndex.getOneBased());
        Event eventToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteEvent(eventToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, Messages.format(eventToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteEventCommand)) {
            return false;
        }

        DeleteEventCommand otherDeleteEventCommand = (DeleteEventCommand) other;
        return targetIndex.equals(otherDeleteEventCommand.targetIndex)
                && isConfirmed == otherDeleteEventCommand.isConfirmed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("isConfirmed", isConfirmed)
                .toString();
    }
}
