package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Prompts the user for confirmation to clear the absolut sinema.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";


    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Are you sure you want to clear the absolut sinema?", new ConfirmClearCommand());
    }
}
