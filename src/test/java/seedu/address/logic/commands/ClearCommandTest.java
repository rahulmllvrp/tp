package seedu.address.logic.commands;

import static seedu.address.testutil.TypicalPersons.getTypicalAbsolutSinema;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAbsolutSinema_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        CommandResult expectedCommandResult =
                new CommandResult("Are you sure you want to clear the absolut sinema?",
                        new ConfirmClearCommand());
        CommandTestUtil.assertCommandSuccess(new ClearCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_nonEmptyAbsolutSinema_success() {
        Model model = new ModelManager(getTypicalAbsolutSinema(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAbsolutSinema(), new UserPrefs());

        CommandResult expectedCommandResult =
                new CommandResult("Are you sure you want to clear the absolut sinema?",
                        new ConfirmClearCommand());
        CommandTestUtil.assertCommandSuccess(new ClearCommand(), model, expectedCommandResult, expectedModel);
    }

}
