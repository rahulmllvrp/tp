package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAbsolutSinema;

import org.junit.jupiter.api.Test;

import seedu.address.model.AbsolutSinema;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ConfirmClearCommandTest {

    @Test
    public void execute_emptyAbsolutSinema_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.setAbsolutSinema(new AbsolutSinema());

        assertCommandSuccess(new ConfirmClearCommand(), model, ConfirmClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAbsolutSinema_success() {
        Model model = new ModelManager(getTypicalAbsolutSinema(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAbsolutSinema(), new UserPrefs());
        expectedModel.setAbsolutSinema(new AbsolutSinema());

        assertCommandSuccess(new ConfirmClearCommand(), model, ConfirmClearCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
