package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 *
 * The Model is the central data management layer that handles:
 * - All person data in the address book
 * - User preferences and GUI settings
 * - Filtering and display logic for the UI
 * - Data persistence coordination
 *
 * This follows the MVC pattern where Model manages data,
 * View handles display, and Controller coordinates between them.
 */
public interface Model {
    /** Predicate that shows all persons without any filtering */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    // ========================= USER PREFERENCES =========================

    /**
     * Replaces all user preference data with the provided preferences.
     * This includes GUI settings, file paths, and other user-specific configurations.
     *
     * @param userPrefs The new user preferences to set
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the current user preferences.
     * Contains GUI settings, file paths, and other user-specific configurations.
     *
     * @return Read-only view of user preferences
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the GUI-specific settings from user preferences.
     * Includes window size, position, and theme settings.
     *
     * @return Current GUI settings
     */
    GuiSettings getGuiSettings();

    /**
     * Updates the GUI-specific settings in user preferences.
     * Used when user resizes window, changes theme, etc.
     *
     * @param guiSettings New GUI settings to apply
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the file path where address book data is stored.
     * Used for data persistence and file operations.
     *
     * @return Path to the address book data file
     */
    Path getAddressBookFilePath();

    /**
     * Sets the file path where address book data should be stored.
     * Changes where the application saves/loads data.
     *
     * @param addressBookFilePath New file path for address book data
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    // ========================= ADDRESS BOOK DATA =========================

    /**
     * Replaces all address book data with the provided data.
     * This completely overwrites existing person records.
     * Used when loading data from file or resetting the application.
     *
     * @param addressBook New address book data to set
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /**
     * Returns the complete address book data.
     * Contains all person records, regardless of current filtering.
     *
     * @return Read-only view of the entire address book
     */
    ReadOnlyAddressBook getAddressBook();

    // ========================= PERSON OPERATIONS =========================

    /**
     * Checks if a person with the same identity already exists in the address book.
     * Identity is typically determined by name (see Person.isSamePerson()).
     * Used to prevent duplicate entries.
     *
     * @param person Person to check for existence
     * @return true if a person with same identity exists, false otherwise
     */
    boolean hasPerson(Person person);

    /**
     * Removes the specified person from the address book.
     * The person must exist in the address book, otherwise behavior is undefined.
     * Used by delete commands and UI operations.
     *
     * @param target Person to remove from address book
     */
    void deletePerson(Person target);

    /**
     * Adds a new person to the address book.
     * The person must not already exist (use hasPerson() to check first).
     * Used by add commands and import operations.
     *
     * @param person New person to add to address book
     */
    void addPerson(Person person);

    /**
     * Updates an existing person with new information.
     * The target person must exist in the address book.
     * The edited person must not have the same identity as another existing person.
     * Used by edit commands to modify person details.
     *
     * @param target The existing person to replace
     * @param editedPerson The updated person information
     */
    void setPerson(Person target, Person editedPerson);

    // ========================= UI FILTERING & DISPLAY =========================

    /**
     * Returns the currently filtered and displayed list of persons.
     * This is what the UI shows to the user - it's a subset of the full address book
     * based on the current search/filter criteria.
     *
     * This list automatically updates when:
     * - The filter predicate changes (via updateFilteredPersonList)
     * - The underlying address book data changes (add/edit/delete)
     *
     * @return Observable list of persons currently displayed in UI
     */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter criteria for what persons are displayed in the UI.
     * This is the core method used by search functionality.
     *
     * Examples:
     * - updateFilteredPersonList(person -> true) shows all persons
     * - updateFilteredPersonList(new NameContainsKeywordsPredicate(keywords)) filters by name
     * - updateFilteredPersonList(new TagContainsKeywordsPredicate(keywords)) filters by tags
     *
     * The UI will automatically update to show only persons matching the predicate.
     *
     * @param predicate Filter criteria - determines which persons are displayed
     * @throws NullPointerException if predicate is null
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Saves the current state of the address book before executing a command that modifies it.
     * This enables undo functionality.
     *
     * @param operationDescription A description of the operation about to be performed.
     */
    void saveStateForUndo(String operationDescription);

    /**
     * Checks if an undo operation is possible.
     *
     * @return true if there is a saved state to undo to, false otherwise.
     */
    boolean canUndo();

    /**
     * Undoes the last operation by restoring the previously saved state.
     *
     * @return A description of the operation that was undone.
     */
    String undo();
}
