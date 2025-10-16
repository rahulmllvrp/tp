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

    public static final String HELP_MESSAGE =
            "1. help:\n"
                    + "   Shows a basic help message with a link to the AbsolutSin-ema User Guide.\n\n"
                    + "2. add n/NAME p/PHONE_NUMBER e/EMAIL w/WEBSITE [t/TAG TAG2 ...]:\n"
                    + "   Adds a contact with up to 6 tags.\n\n"
                    + "3. list:\n"
                    + "   Shows all saved contacts.\n\n"
                    + "4. edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [w/WEBSITE]:\n"
                    + "   Edits a contact’s personal detail(s) at the specified index.\n\n"
                    + "5. edit INDEX t/TAG1 TAG2 ...:\n"
                    + "   Replaces all tags with the specified tag(s).\n\n"
                    + "6. edit INDEX t/:\n"
                    + "   Removes all tags from the contact.\n\n"
                    + "7. find KEYWORD1 [KEYWORD2 ...]:\n"
                    + "   Finds contacts whose names contain any of the given keywords (case-insensitive).\n\n"
                    + "8. delete INDEX:\n"
                    + "   Deletes the contact in the specified index.\n\n"
                    + "9. clear:\n"
                    + "   Clears all contacts (after a confirmation message).\n\n"
                    + "10. exit:\n"
                    + "    Exits the program.\n\n"
                    + "For more detailed help, refer to the AbsolutSin-ema User Guide.\n"
                    + "https://ay2526s1-cs2103t-t12-4.github.io/tp/UserGuide.html";

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
