package seedu.address.testutil;

import seedu.address.logic.commands.EditEventCommand.EditEventDescriptor;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventTime;
import seedu.address.model.person.Budget;

/**
 * A utility class to help with building EditEventDescriptor objects.
 */
public class EditEventDescriptorBuilder {

    private EditEventDescriptor descriptor;

    public EditEventDescriptorBuilder() {
        descriptor = new EditEventDescriptor();
    }

    public EditEventDescriptorBuilder(EditEventDescriptor descriptor) {
        this.descriptor = new EditEventDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditEventDescriptor} with fields containing {@code event}'s details
     */
    public EditEventDescriptorBuilder(Event event) {
        descriptor = new EditEventDescriptor();
        descriptor.setName(event.getName());
        descriptor.setDate(event.getDate());
        descriptor.setTime(event.getTime());
    }

    /**
     * Sets the {@code EventName} of the {@code EditEventDescriptor} that we are building.
     */
    public EditEventDescriptorBuilder withName(String name) {
        descriptor.setName(new EventName(name));
        return this;
    }

    /**
     * Sets the {@code EventDate} of the {@code EditEventDescriptor} that we are building.
     */
    public EditEventDescriptorBuilder withDate(String date) {
        descriptor.setDate(new EventDate(date));
        return this;
    }

    /**
     * Sets the {@code EventTime} of the {@code EditEventDescriptor} that we are building.
     */
    public EditEventDescriptorBuilder withTime(String time) {
        descriptor.setTime(new EventTime(time));
        return this;
    }

    /**
     * Sets the {@code Budget} of the {@code EditEventDescriptor} that we are building.
     */
    public EditEventDescriptorBuilder withBudget(String budget) {
        descriptor.setBudget(new Budget(budget));
        return this;
    }

    public EditEventDescriptor build() {
        return descriptor;
    }
}
