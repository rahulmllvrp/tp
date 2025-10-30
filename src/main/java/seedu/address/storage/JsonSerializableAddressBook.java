package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataCorruptionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_EVENT = "Events list contains duplicate event(s).";

    private static final Logger logger = LogsCenter.getLogger(JsonSerializableAddressBook.class);

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedEvent> events = new ArrayList<>();
    // Optional integrity checksum (hex-encoded SHA-256 of the canonical persons+events JSON snapshot)
    private final String checksum;

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons and events.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
            @JsonProperty("events") List<JsonAdaptedEvent> events,
            @JsonProperty("checksum") String checksum) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        if (events != null) {
            this.events.addAll(events);
        }
        this.checksum = checksum; // may be null for legacy files
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        events.addAll(source.getEventList().stream().map(JsonAdaptedEvent::new).collect(Collectors.toList()));
        // Compute checksum for persistence
        this.checksum = computeChecksum();
    }

    /**
     * Validates the integrity checksum (if present). Throws DataCorruptionException when it does not match.
     * Legacy files without a checksum are accepted and will be upgraded on next save.
     */
    void validateIntegrityOrThrow() throws DataCorruptionException {
        if (checksum == null || checksum.isEmpty()) {
            logger.info("No checksum present in data file (legacy or missing); skipping integrity validation.");
            return; // no checksum present (legacy file) -> accept
        }
        String expected = computeChecksum();
        logger.info("Checksum present; stored=" + checksum + ", expected=" + expected);
        if (!checksum.equalsIgnoreCase(expected)) {
            throw new DataCorruptionException(
                    "Your data file appears to be corrupted or has been manually modified. "
                    + "Please delete the data file and relaunch the application to regenerate a fresh one.");
        }
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }
        for (JsonAdaptedEvent jsonAdaptedEvent : events) {
            Event event = jsonAdaptedEvent.toModelType();
            if (addressBook.hasEvent(event)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EVENT);
            }
            addressBook.addEvent(event);
        }
        return addressBook;
    }

    // Diagnostic helpers (package-private)
    String getStoredChecksum() {
        return this.checksum;
    }

    String getComputedChecksumForDiagnostics() {
        return computeChecksum();
    }

    // Helper snapshot and checksum logic
    private String computeChecksum() {
        try {
            Snapshot snapshot = new Snapshot(persons, events);
            String json = JsonUtil.toJsonString(snapshot);
            return sha256Hex(json);
        } catch (Exception e) {
            // If hashing fails for some reason, treat as no checksum
            return null;
        }
    }

    private static String sha256Hex(String input) throws java.security.NoSuchAlgorithmException {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static class Snapshot {
        public final List<JsonAdaptedPerson> persons;
        public final List<JsonAdaptedEvent> events;

        Snapshot(List<JsonAdaptedPerson> persons, List<JsonAdaptedEvent> events) {
            this.persons = persons != null ? persons : new ArrayList<>();
            this.events = events != null ? events : new ArrayList<>();
        }
    }

}
