package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the absolut sinema data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AbsolutSinema absolutSinema;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final SortedList<Person> sortedFilteredPersons;
    private final FilteredList<seedu.address.model.event.Event> filteredEvents;
    private AbsolutSinemaSnapshot undoSnapshot; // Stores the previous state for undo

    /**
     * Initializes a ModelManager with the given absolutSinema and userPrefs.
     */
    public ModelManager(ReadOnlyAbsolutSinema absolutSinema, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(absolutSinema, userPrefs);

        logger.fine("Initializing with absolut sinema: " + absolutSinema + " and user prefs " + userPrefs);

        this.absolutSinema = new AbsolutSinema(absolutSinema);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.absolutSinema.getPersonList());
        sortedFilteredPersons = new SortedList<>(filteredPersons, (person1, person2) ->
                person1.getName().fullName.compareToIgnoreCase(person2.getName().fullName));
        filteredEvents = new FilteredList<>(this.absolutSinema.getEventList());
    }

    public ModelManager() {
        this(new AbsolutSinema(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAbsolutSinemaFilePath() {
        return userPrefs.getAbsolutSinemaFilePath();
    }

    @Override
    public void setAbsolutSinemaFilePath(Path absolutSinemaFilePath) {
        requireNonNull(absolutSinemaFilePath);
        userPrefs.setAbsolutSinemaFilePath(absolutSinemaFilePath);
    }

    //=========== AbsolutSinema ================================================================================

    @Override
    public void setAbsolutSinema(ReadOnlyAbsolutSinema absolutSinema) {
        this.absolutSinema.resetData(absolutSinema);
    }

    @Override
    public ReadOnlyAbsolutSinema getAbsolutSinema() {
        return absolutSinema;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return absolutSinema.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        absolutSinema.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        absolutSinema.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        absolutSinema.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasEvent(seedu.address.model.event.Event event) {
        requireNonNull(event);
        return absolutSinema.hasEvent(event);
    }

    @Override
    public void deleteEvent(seedu.address.model.event.Event target) {
        absolutSinema.removeEvent(target);
    }

    @Override
    public void addEvent(seedu.address.model.event.Event event) {
        absolutSinema.addEvent(event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
    }

    @Override
    public void setEvent(seedu.address.model.event.Event target, seedu.address.model.event.Event editedEvent) {
        requireAllNonNull(target, editedEvent);

        absolutSinema.setEvent(target, editedEvent);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAbsolutSinema}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return sortedFilteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public ObservableList<seedu.address.model.event.Event> getFilteredEventList() {
        return filteredEvents;
    }

    @Override
    public void updateFilteredEventList(Predicate<seedu.address.model.event.Event> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
    }

    //=========== Undo Functionality ==========================================================================

    @Override
    public void saveStateForUndo(String operationDescription) {
        undoSnapshot = new AbsolutSinemaSnapshot(absolutSinema, operationDescription);
    }

    @Override
    public boolean canUndo() {
        return undoSnapshot != null;
    }

    @Override
    public String undo() {
        if (undoSnapshot == null) {
            return "No operation to undo";
        }

        String description = undoSnapshot.getOperationDescription();
        AbsolutSinema restoredBook = undoSnapshot.restoreAbsolutSinema();
        absolutSinema.resetData(restoredBook);

        // Clear the undo snapshot after using it
        undoSnapshot = null;

        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return absolutSinema.equals(otherModelManager.absolutSinema)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
