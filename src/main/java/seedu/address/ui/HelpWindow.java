package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String HELP_MESSAGE = "AbsolutSin-ema Command List\n\n"
        + "==============================\n"
        + "Add Contact\n"
        + "  Command: add n/NAME p/PHONE e/EMAIL w/WEBSITE [t/TAG]...\n"
        + "  Example: add n/Alice Tan p/91234567 e/alice@example.com w/www.alicetphotography.com t/photographer t/wedding\n"
        + "  - NAME: Alphabets, spaces, hyphens, apostrophes.\n"
        + "  - PHONE: 8-digit SG number starting with 6, 8, or 9.\n"
        + "  - EMAIL: Standard format (user@example.com).\n"
        + "  - WEBSITE: Starts with www. or https://\n"
        + "  - TAG: Alphanumeric, single word, optional.\n"
        + "\n"
        + "Delete Contact\n"
        + "  Command: delete INDEX\n"
        + "  Example: delete 1\n"
        + "  - INDEX: Positive integer (see list).\n"
        + "\n"
        + "View Contact List\n"
        + "  Command: list\n"
        + "  - Shows all contacts.\n"
        + "\n"
        + "Tag/Untag Contact\n"
        + "  Tag:   edit INDEX t/NEW_TAG...\n"
        + "  Untag: edit INDEX t/\n"
        + "  - Tags must be alphanumeric.\n"
        + "\n"
        + "For more details, refer to the user guide or documentation.";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }
}
