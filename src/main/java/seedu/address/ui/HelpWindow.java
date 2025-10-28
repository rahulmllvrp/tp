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
                    + "2. add n/NAME p/PHONE_NUMBER e/EMAIL w/WEBSITE b/BUDGET [t/TAG TAG2 ...]:\n"
                    + "   Adds a contact with up to 6 tags.\n\n"
                    + "3. list:\n"
                    + "   Shows all saved contacts and parties.\n\n"
                    + "4. edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [w/WEBSITE] [b/BUDGET]:\n"
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
                    + "11. addp n/NAME d/DATE t/TIME b/BUDGET [c/CONTACT_INDEX ...]:\n"
                    + "    Adds a party to the party list.\n\n"
                    + "12. editp INDEX [n/NAME] [d/DATE] [t/TIME] [b/BUDGET]:\n"
                    + "    Edits a party’s details at the specified index.\n\n"
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
        // Set help message without the URL
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
        Stage stage = getRoot();
        // Defensive reset: ensure the help stage is not left in fullscreen/maximized state
        // (can happen when owner stage was fullscreened previously). This avoids the
        // glitch where the help window appears full-screen or in a broken layout
        // after toggling fullscreen on the primary stage.
        try {
            if (stage.isFullScreen()) {
                stage.setFullScreen(false);
            }
        } catch (UnsupportedOperationException e) {
            // Some platforms may not support full screen checks; ignore safely.
            logger.fine("Platform does not support full screen checks for help window.");
        }
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        }
        // Allow resizing so the window can be adjusted and to avoid fixed large sizes
        // carried over from previous shows.
        stage.setResizable(true);

        stage.show();
        stage.centerOnScreen();

        // Ensure the ScrollPane starts scrolled to the top-left when opened.
        // Use Platform.runLater to reset after the scene graph has been laid out.
        Platform.runLater(() -> {
            try {
                if (helpScrollPane != null) {
                    helpScrollPane.setVvalue(0.0);
                    helpScrollPane.setHvalue(0.0);
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
