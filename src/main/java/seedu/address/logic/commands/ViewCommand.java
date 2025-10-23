package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;

/**
 * Views all people assigned to a party (event) by its index.
 */
public class ViewCommand extends Command {
    public static final String COMMAND_WORD = "view";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views all people assigned to the party at the given index. "
            + "\nParameters: INDEX (must be a positive integer)"
            + "\nExample: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_INVALID_EVENT_INDEX = "The event index provided is invalid.";
    public static final String MESSAGE_SUCCESS = "Listed all people assigned to party: %1$s";

    private final Index targetIndex;

    public ViewCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Event> lastShownEventList = model.getFilteredEventList();
        if (targetIndex.getZeroBased() >= lastShownEventList.size()) {
            throw new CommandException(MESSAGE_INVALID_EVENT_INDEX);
        }
        Event event = lastShownEventList.get(targetIndex.getZeroBased());
        model.updateFilteredPersonList(new seedu.address.model.person.PersonInEventPredicate(event));
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(event)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof ViewCommand && targetIndex.equals(((ViewCommand) other).targetIndex));
    }
}
