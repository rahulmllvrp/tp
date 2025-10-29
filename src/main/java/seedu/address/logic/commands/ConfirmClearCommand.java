package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book upon confirmation.
 */
public class ConfirmClearCommand extends Command {

    public static final String COMMAND_WORD = "confirm_clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";

    private static final Logger logger = LogsCenter.getLogger(ConfirmClearCommand.class);

    private final ClearTarget target;

    public ConfirmClearCommand() {
        this(ClearTarget.ALL);
    }

    public ConfirmClearCommand(ClearTarget target) {
        this.target = target == null ? ClearTarget.ALL : target;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        // Save state for undo
        model.saveStateForUndo("clear " + target.name().toLowerCase());
        logger.fine(() -> "Executing ConfirmClearCommand for target: " + target);

        String message;
        switch (target) {
        case CONTACTS:
            model.clearContacts();
            message = "Contacts have been cleared!";
            logger.info("Cleared all contacts");
            break;
        case EVENTS:
            model.clearEvents();
            message = "Parties have been cleared!";
            logger.info("Cleared all parties");
            break;
        case ALL:
        default:
            model.setAddressBook(new AddressBook());
            message = MESSAGE_SUCCESS;
            logger.info("Cleared entire address book");
            break;
        }

        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ConfirmClearCommand)) {
            return false;
        }
        ConfirmClearCommand o = (ConfirmClearCommand) other;
        return this.target == o.target;
    }
}
