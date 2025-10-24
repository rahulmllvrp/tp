package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AbsolutSinema;
import seedu.address.model.ReadOnlyAbsolutSinema;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;

/**
 * An Immutable AbsolutSinema that is serializable to JSON format.
 */
@JsonRootName(value = "absolutsinema")
class JsonSerializableAbsolutSinema {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_EVENT = "Events list contains duplicate event(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedEvent> events = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAbsolutSinema} with the given persons and events.
     */
    @JsonCreator
    public JsonSerializableAbsolutSinema(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
            @JsonProperty("events") List<JsonAdaptedEvent> events) {
        this.persons.addAll(persons);
        if (events != null) {
            this.events.addAll(events);
        }
    }

    /**
     * Converts a given {@code ReadOnlyAbsolutSinema} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAbsolutSinema}.
     */
    public JsonSerializableAbsolutSinema(ReadOnlyAbsolutSinema source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        events.addAll(source.getEventList().stream().map(JsonAdaptedEvent::new).collect(Collectors.toList()));
    }

    /**
     * Converts this absolut sinema into the model's {@code AbsolutSinema} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AbsolutSinema toModelType() throws IllegalValueException {
        AbsolutSinema absolutSinema = new AbsolutSinema();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (absolutSinema.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            absolutSinema.addPerson(person);
        }
        for (JsonAdaptedEvent jsonAdaptedEvent : events) {
            Event event = jsonAdaptedEvent.toModelType();
            if (absolutSinema.hasEvent(event)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EVENT);
            }
            absolutSinema.addEvent(event);
        }
        return absolutSinema;
    }

}
