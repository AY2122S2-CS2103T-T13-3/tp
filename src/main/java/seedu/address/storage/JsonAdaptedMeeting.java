package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.meeting.IsRecurring;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDateTime;
import seedu.address.model.meeting.MeetingDuration;
import seedu.address.model.meeting.MeetingName;
import seedu.address.model.meeting.MeetingUrl;
import seedu.address.model.module.Module;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Meeting}.
 */
class JsonAdaptedMeeting {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Meeting's %s field is missing!";

    private final String name;
    private final String url;
    private final String dateTime;
    private final String module;
    private final String duration;
    private final String isRecurring;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedMeeting} with the given meeting details.
     */
    @JsonCreator
    public JsonAdaptedMeeting(@JsonProperty("name") String name,
                              @JsonProperty("url") String url,
                              @JsonProperty("dateTime") String dateTime,
                              @JsonProperty("duration") String duration,
                              @JsonProperty("module") String module,
                              @JsonProperty("isRecurring") String isRecurring,
                              @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.url = url;
        this.dateTime = dateTime;
        this.duration = duration;
        this.module = module;
        this.isRecurring = isRecurring;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Meeting} into this class for Jackson use.
     */
    public JsonAdaptedMeeting(Meeting source) {
        name = source.getName().name;
        url = source.getUrl().meetingUrl.toExternalForm();
        dateTime = source.getDateTime().datetime.format(MeetingDateTime.INPUT_FORMAT);
        duration = String.valueOf(source.getDuration().duration);
        module = source.getModule().code;
        isRecurring = source.getIsRecurring().toString();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted meeting object into the model's {@code Meeting} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted meeting.
     */
    public Meeting toModelType() throws IllegalValueException {
        final List<Tag> meetingTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            meetingTags.add(tag.toModelType());
        }

        // Validate name.
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    MeetingName.class.getSimpleName()));
        }
        if (!MeetingName.isValidName(name)) {
            throw new IllegalValueException(MeetingName.MESSAGE_CONSTRAINTS);
        }
        final MeetingName modelName = new MeetingName(name);

        // Validate url.
        if (url == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    MeetingUrl.class.getSimpleName()));
        }
        if (!MeetingUrl.isValidUrl(url)) {
            throw new IllegalValueException(MeetingUrl.MESSAGE_CONSTRAINTS);
        }
        final MeetingUrl modelUrl = new MeetingUrl(url);

        // Validate dateTime.
        if (dateTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    MeetingDateTime.class.getSimpleName()));
        }
        if (!MeetingDateTime.isValidDateTime(dateTime)) {
            throw new IllegalValueException(MeetingDateTime.MESSAGE_CONSTRAINTS);
        }
        final MeetingDateTime modelDateTime = new MeetingDateTime(dateTime);

        // Validate duration.
        if (duration == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    MeetingDuration.class.getSimpleName()));
        }
        if (!MeetingDuration.isValidDuration(duration)) {
            throw new IllegalValueException(MeetingDuration.MESSAGE_CONSTRAINTS);
        }
        final MeetingDuration modelDuration = new MeetingDuration(duration);

        // Validate module.
        if (module == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Module.class.getSimpleName()));
        }
        if (!Module.isValidModule(module)) {
            throw new IllegalValueException(Module.MESSAGE_CONSTRAINTS);
        }
        final Module modelModule = new Module(module);

        // Validate recurring status.
        if (isRecurring == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    IsRecurring.class.getSimpleName()));
        }
        if (!IsRecurring.isValidRecurringStatus(isRecurring)) {
            throw new IllegalValueException(IsRecurring.MESSAGE_CONSTRAINTS);
        }
        final IsRecurring modelIsRecurring = new IsRecurring(isRecurring);

        final Set<Tag> modelTags = new HashSet<>(meetingTags);
        return new Meeting(modelName, modelUrl, modelDateTime, modelDuration,
                modelModule, modelIsRecurring, modelTags);
    }
}
