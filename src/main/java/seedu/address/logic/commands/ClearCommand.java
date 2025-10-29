package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Prompts the user for confirmation to clear the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";


    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Are you sure you want to clear the party planner?", new ConfirmClearCommand());
    }
}
