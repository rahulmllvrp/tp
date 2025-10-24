package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Undoes the previous command that modified the absolut sinema.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undoes the previous command that modified the absolut sinema.";

    public static final String MESSAGE_SUCCESS = "Previous command undone: %1$s";
    public static final String MESSAGE_NO_COMMAND_TO_UNDO = "No command to undo.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!model.canUndo()) {
            throw new CommandException(MESSAGE_NO_COMMAND_TO_UNDO);
        }

        String undoDescription = model.undo();
        return new CommandResult(String.format(MESSAGE_SUCCESS, undoDescription));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        return other instanceof UndoCommand;
    }
}
