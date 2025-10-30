package seedu.address.ui;

import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String HELP_MESSAGE =
            "1. help:\n"
            + "   Shows a basic help message with a link to the AbsolutSin-ema User Guide.\n\n"
            + "2. add n/NAME p/PHONE_NUMBER e/EMAIL [w/WEBSITE] b/BUDGET [t/TAG t/TAG2 ...]:\n"
            + "   Adds a contact with up to 6 tags.\n"
            + "   BUDGET refers to how much it costs to hire the contact.\n\n"
            + "3. list:\n"
            + "   Shows all saved contacts and parties.\n\n"
            + "4. listtags:\n"
            + "   Shows all unique tags from all contacts.\n\n"
            + "5. edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [w/WEBSITE] [b/BUDGET] [t/TAG]:\n"
            + "   Edits a contact's personal detail(s) at the specified index.\n\n"
            + "6. edit INDEX t/:\n"
            + "   Removes all tags from the contact.\n\n"
            + "7. find KEYWORD1 [KEYWORD2 ...]:\n"
            + "   Finds contacts whose names contain any of the given keywords (case-insensitive).\n\n"
            + "8. delete INDEX:\n"
            + "   Deletes the contact in the specified index.\n\n"
            + "9. clear [all/contacts/parties]:\n"
            + "   Clears all data, contacts only, or parties only (after a confirmation message).\n\n"
            + "10. exit:\n"
            + "    Exits the program.\n\n"
            + "11. addp n/NAME d/DATE t/TIME b/BUDGET [c/CONTACT_INDEX ...]:\n"
            + "    Adds a party to the party list.\n\n"
            + "12. editp INDEX [n/NAME] [d/DATE] [t/TIME] [b/BUDGET]:\n"
            + "    Edits a party's details at the specified index.\n\n"
            + "13. deletep INDEX:\n"
            + "    Deletes the party at the specified index.\n\n"
            + "14. assign INDEX c/CONTACT_INDEX[,CONTACT_INDEX ...]:\n"
            + "    Assigns contacts to a specific party. E.g. assign 1 c/1,2\n\n"
            + "15. unassign INDEX c/CONTACT_INDEX[,CONTACT_INDEX ...]:\n"
            + "    Unassigns contacts from a specific party. E.g. unassign 1 c/1,2\n\n"
            + "16. view INDEX:\n"
            + "    Views all people assigned to a party at the given index.\n\n"
            + "For more detailed help, refer to the AbsolutSin-ema User Guide.\n"
            + "https://ay2526s1-cs2103t-t12-4.github.io/tp/UserGuide.html";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    @SuppressWarnings("unused")
    private Button copyButton;

    @FXML
    @SuppressWarnings("unused")
    private Label helpMessage;

    @FXML
    @SuppressWarnings("unused")
    private Hyperlink userGuideLink;

    @FXML
    @SuppressWarnings("unused")
    private ScrollPane helpScrollPane;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        // Basic defensive checks: FXML fields should have been injected.
        // Use assertions as these should never fail in production if FXML is correct.
        // This documents the invariant and helps catch configuration problems early.
        String helpText = HELP_MESSAGE.substring(0, HELP_MESSAGE.lastIndexOf("https://"));
        helpMessage.setText(helpText.trim());
        // Set up the hyperlink for the User Guide
        String userGuideUrl = "https://ay2526s1-cs2103t-t12-4.github.io/tp/UserGuide.html";
        userGuideLink.setText(userGuideUrl);
        userGuideLink.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI(userGuideUrl));
            } catch (Exception ex) {
                logger.warning("Failed to open user guide URL: " + ex.getMessage());
            }
        });

        // Ensure FXML injection worked correctly; these are invariants.
        assert helpMessage != null : "helpMessage Label not injected: check HelpWindow.fxml";
        assert userGuideLink != null : "userGuideLink Hyperlink not injected: check HelpWindow.fxml";
        assert helpScrollPane != null : "helpScrollPane ScrollPane not injected: check HelpWindow.fxml";
        logger.fine("HelpWindow initialized with help message length=" + helpText.length());
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application
     *             Thread.
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
        Stage stage = getRoot();
        boolean isFullScreen = stage.isFullScreen();
        boolean isMaximized = stage.isMaximized();
        logger.fine("Help stage state before show: fullScreen=" + isFullScreen + ", maximized=" + isMaximized);

        try {
            if (isFullScreen) {
                stage.setFullScreen(false);
            }
        } catch (UnsupportedOperationException e) {
            logger.fine("Platform does not support full screen checks for help window.");
        }

        if (isMaximized) {
            stage.setMaximized(false);
        }

        stage.setResizable(true);

        stage.show();
        stage.centerOnScreen();

        Platform.runLater(() -> {
            try {
                if (helpScrollPane != null) {
                    helpScrollPane.setVvalue(0.0);
                    helpScrollPane.setHvalue(0.0);
                    logger.fine("Help scroll position reset to top-left.");
                }
            } catch (Exception e) {
                logger.fine("Unable to reset help scroll position: " + e.getMessage());
            }
        });
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
