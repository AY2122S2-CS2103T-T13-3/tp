package seedu.address.testutil.meeting;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.meeting.IsRecurring;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDateTime;
import seedu.address.model.meeting.MeetingDuration;
import seedu.address.model.meeting.MeetingName;
import seedu.address.model.meeting.MeetingUrl;
import seedu.address.model.module.Module;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Meeting objects.
 */
public class MeetingBuilder {
    public static final String DEFAULT_NAME = "CS2103T Lecture";
    public static final String DEFAULT_URL = "https://legit-uni.zoom.us/j/344299221?pwd=F3a99221";
    public static final String DEFAULT_DATETIME = "18-03-2022 3:00pm";
    public static final String DEFAULT_DURATION = "2";
    public static final String DEFAULT_MODULE = "CS2103";
    public static final String DEFAULT_RECURRENCE = "N";

    private MeetingName name;
    private MeetingUrl url;
    private MeetingDateTime dateTime;
    private MeetingDuration duration;
    private Module module;
    private IsRecurring isRecurring;
    private Set<Tag> tags;

    /**
     * Creates a {@code MeetingBuilder} with the default details.
     */
    public MeetingBuilder() {
        name = new MeetingName(DEFAULT_NAME);
        url = new MeetingUrl(DEFAULT_URL);
        dateTime = new MeetingDateTime(DEFAULT_DATETIME);
        duration = new MeetingDuration(DEFAULT_DURATION);
        module = new Module(DEFAULT_MODULE);
        isRecurring = new IsRecurring(DEFAULT_RECURRENCE);
        tags = new HashSet<>();
    }

    /**
     * Initializes the MeetingBuilder with the data of {@code meetingToCopy}.
     */
    public MeetingBuilder(Meeting meetingToCopy) {
        name = meetingToCopy.getName();
        url = meetingToCopy.getUrl();
        dateTime = meetingToCopy.getDateTime();
        duration = meetingToCopy.getDuration();
        module = meetingToCopy.getModule();
        isRecurring = meetingToCopy.getIsRecurring();
        tags = new HashSet<>(meetingToCopy.getTags());
    }

    /**
     * Sets the {@code MeetingName} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withName(String name) {
        this.name = new MeetingName(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Meeting} that we are building.
     */
    public MeetingBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code MeetingUrl} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withUrl(String url) {
        this.url = new MeetingUrl(url);
        return this;
    }

    /**
     * Sets the {@code MeetingDateTime} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withDateTime(String dateTime) {
        this.dateTime = new MeetingDateTime(dateTime);
        return this;
    }

    /**
     * Sets the {@code MeetingDuration} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withDuration(String duration) {
        this.duration = new MeetingDuration(duration);
        return this;
    }

    /**
     * Sets the {@code IsRecurring} field of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withIsRecurring(String isRecurring) {
        this.isRecurring = new IsRecurring(isRecurring);
        return this;
    }

    /**
     * Sets the {@code Module} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withModule(String module) {
        this.module = new Module(module);
        return this;
    }

    /**
     * Creates the {@code Meeting} represented by this {@code MeetingBuilder} instance.
     */
    public Meeting build() {
        return new Meeting(name, url, dateTime, duration, module, isRecurring, tags);
    }
}
