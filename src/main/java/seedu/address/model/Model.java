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
 * - All person data in the absolut sinema
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

    /** Predicate that shows all events without any filtering */
    Predicate<seedu.address.model.event.Event> PREDICATE_SHOW_ALL_EVENTS = unused -> true;

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
     * Returns the file path where absolut sinema data is stored.
     * Used for data persistence and file operations.
     *
     * @return Path to the absolut sinema data file
     */
    Path getAbsolutSinemaFilePath();

    /**
     * Sets the file path where absolut sinema data should be stored.
     * Changes where the application saves/loads data.
     *
     * @param absolutSinemaFilePath New file path for absolut sinema data
     */
    void setAbsolutSinemaFilePath(Path absolutSinemaFilePath);

    // ========================= ABSOLUT SINEMA DATA =========================

    /**
     * Replaces all absolut sinema data with the provided data.
     * This completely overwrites existing person records.
     * Used when loading data from file or resetting the application.
     *
     * @param absolutSinema New absolut sinema data to set
     */
    void setAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema);

    /**
     * Returns the complete absolut sinema data.
     * Contains all person records, regardless of current filtering.
     *
     * @return Read-only view of the entire absolut sinema
     */
    ReadOnlyAbsolutSinema getAbsolutSinema();

    // ========================= PERSON OPERATIONS =========================

    /**
     * Checks if a person with the same identity already exists in the absolut sinema.
     * Identity is typically determined by name (see Person.isSamePerson()).
     * Used to prevent duplicate entries.
     *
     * @param person Person to check for existence
     * @return true if a person with same identity exists, false otherwise
     */
    boolean hasPerson(Person person);

    /**
     * Removes the specified person from the absolut sinema.
     * The person must exist in the absolut sinema, otherwise behavior is undefined.
     * Used by delete commands and UI operations.
     *
     * @param target Person to remove from absolut sinema
     */
    void deletePerson(Person target);

    /**
     * Adds a new person to the absolut sinema.
     * The person must not already exist (use hasPerson() to check first).
     * Used by add commands and import operations.
     *
     * @param person New person to add to absolut sinema
     */
    void addPerson(Person person);

    /**
     * Updates an existing person with new information.
     * The target person must exist in the absolut sinema.
     * The edited person must not have the same identity as another existing person.
     * Used by edit commands to modify person details.
     *
     * @param target The existing person to replace
     * @param editedPerson The updated person information
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Returns true if an event with the same identity as {@code event} exists in the absolut sinema.
     */
    boolean hasEvent(seedu.address.model.event.Event event);

    /**
     * Deletes the given event.
     * The event must exist in the absolut sinema.
     */
    void deleteEvent(seedu.address.model.event.Event target);

    /**
     * Adds the given event.
     * {@code event} must not already exist in the absolut sinema.
     */
    void addEvent(seedu.address.model.event.Event event);

    /**
     * Replaces the given event {@code target} with {@code editedEvent}.
     * {@code target} must exist in the absolut sinema.
     * The event identity of {@code editedEvent} must not be the same as another existing event in the absolut sinema.
     */
    void setEvent(seedu.address.model.event.Event target, seedu.address.model.event.Event editedEvent);

    // ========================= UI FILTERING & DISPLAY =========================

    /**
     * Returns the currently filtered and displayed list of persons.
     * This is what the UI shows to the user - it's a subset of the full absolut sinema
     * based on the current search/filter criteria.
     *
     * This list automatically updates when:
     * - The filter predicate changes (via updateFilteredPersonList)
     * - The underlying absolut sinema data changes (add/edit/delete)
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

    /** Returns an unmodifiable view of the filtered list of events */
    ObservableList<seedu.address.model.event.Event> getFilteredEventList();

    /**
     * Updates the filter of the filtered event list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEventList(Predicate<seedu.address.model.event.Event> predicate);

    /**
     * Saves the current state of the absolut sinema before executing a command that modifies it.
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
