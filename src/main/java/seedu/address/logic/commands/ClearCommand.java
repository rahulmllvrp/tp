package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Prompts the user for confirmation to clear the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";


    @Override
    public CommandResult execute(Model model) {
<<<<<<< HEAD
        return new CommandResult("Are you sure you want to clear the address book?", new ConfirmClearCommand());
=======
        requireNonNull(model);
        int personCount = model.getAddressBook().getPersonList().size();
        model.saveStateForUndo("clear (" + personCount + " contacts)");
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
>>>>>>> 2ed3cb3d (Add Undo Comman and Tests)
    }
}
