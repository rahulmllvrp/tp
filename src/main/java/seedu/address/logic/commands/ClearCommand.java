package seedu.address.logic.commands;

import java.util.Objects;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;

/**
 * Prompts the user for confirmation to clear parts of the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_USAGE = COMMAND_WORD + " [all/parties/contacts]";

    private static final Logger logger = LogsCenter.getLogger(ClearCommand.class);

    private final ClearTarget target;

    public ClearCommand() {
        this(ClearTarget.ALL);
    }

    public ClearCommand(ClearTarget target) {
        this.target = Objects.requireNonNullElse(target, ClearTarget.ALL);
    }

    // Getter for tests and external callers
    public ClearTarget getTarget() {
        return target;
    }

    @Override
    public CommandResult execute(Model model) {
        Objects.requireNonNull(model);
        logger.fine(() -> "Prepare to clear target: " + target);

        String message;
        switch (target) {
        case CONTACTS:
            message = "Are you sure you want to clear all contacts?";
            break;
        case EVENTS:
            message = "Are you sure you want to clear all parties?";
            break;
        case ALL:
        default:
            message = "Are you sure you want to clear the party planner?";
            break;
        }

        return new CommandResult(message, new ConfirmClearCommand(target));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ClearCommand)) {
            return false;
        }
        ClearCommand o = (ClearCommand) other;
        return this.target == o.target;
    }

    @Override
    public int hashCode() {
        return Objects.hash(target);
    }
}
