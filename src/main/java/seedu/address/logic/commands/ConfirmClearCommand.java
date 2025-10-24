package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AbsolutSinema;
import seedu.address.model.Model;

/**
 * Clears the absolut sinema upon confirmation.
 */
public class ConfirmClearCommand extends Command {

    public static final String COMMAND_WORD = "confirm_clear";
    public static final String MESSAGE_SUCCESS = "AbsolutSinema has been cleared!";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAbsolutSinema(new AbsolutSinema());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ConfirmClearCommand; // instanceof handles nulls
    }
}
