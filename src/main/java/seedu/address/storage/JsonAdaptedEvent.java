package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Jackson-friendly version of {@link Event}.
 */
class JsonAdaptedEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";

    private final String name;
    private final String date;
    private final String time;
    private final List<String> participants;

    /**
     * Constructs a {@code JsonAdaptedEvent} with the given event details.
     */
    @JsonCreator
    public JsonAdaptedEvent(@JsonProperty("name") String name, @JsonProperty("date") String date,
            @JsonProperty("time") String time, @JsonProperty("participants") List<String> participants) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.participants = participants != null ? participants : new ArrayList<>();
    }

    /**
     * Converts a given {@code Event} into this class for Jackson use.
     */
    public JsonAdaptedEvent(Event source) {
        name = source.getName().fullName;
        date = source.getDate().value;
        time = source.getTime().value;
        participants = source.getParticipants().stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Converts this Jackson-friendly adapted event object into the model's {@code Event} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted event.
     */
    public Event toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    EventName.class.getSimpleName()));
        }
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    EventDate.class.getSimpleName()));
        }
        if (time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    EventTime.class.getSimpleName()));
        }
        final EventName modelName = new EventName(name);
        final EventDate modelDate = new EventDate(date);
        final EventTime modelTime = new EventTime(time);
        final List<seedu.address.model.person.PersonId> modelParticipants = new ArrayList<>();
        if (participants != null) {
            for (String id : participants) {
                modelParticipants.add(new seedu.address.model.person.PersonId(id));
            }
        }
        return new Event(modelName, modelDate, modelTime, modelParticipants);
    }

}
