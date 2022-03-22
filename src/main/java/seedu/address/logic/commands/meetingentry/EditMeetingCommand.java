package seedu.address.logic.commands.meetingentry;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_URL;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_MEETING_ENTRIES;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meetingentry.IsRecurring;
import seedu.address.model.meetingentry.MeetingDateTime;
import seedu.address.model.meetingentry.MeetingDuration;
import seedu.address.model.meetingentry.MeetingEntry;
import seedu.address.model.meetingentry.MeetingName;
import seedu.address.model.meetingentry.MeetingUrl;
import seedu.address.model.module.Module;
import seedu.address.model.tag.Tag;

/**
 * Edits a meeting in LinkyTime.
 */
public class EditMeetingCommand extends Command {
    public static final String COMMAND_WORD = "edit";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the meeting identified "
            + "by the index number used in the displayed list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_URL + "URL] "
            + "[" + PREFIX_DATETIME + "DATETIME] "
            + "[" + PREFIX_DURATION + "DURATION] "
            + "[" + PREFIX_MODULE + "MODULE] "
            + "[" + PREFIX_RECURRING + "IS_RECURRING] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Tutorial "
            + PREFIX_DURATION + "1.0";

    public static final String MESSAGE_EDIT_MEETING_SUCCESS = "Edited meeting: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_MEETING = "This meeting already exists in LinkyTime.";

    private final Index index;
    private final EditMeetingDescriptor editMeetingDescriptor;

    /**
     * @param index of the meeting in the filtered meeting list to edit
     * @param editMeetingDescriptor details to edit the meeting with
     */
    public EditMeetingCommand(Index index, EditMeetingDescriptor editMeetingDescriptor) {
        requireNonNull(index);
        requireNonNull(editMeetingDescriptor);

        this.index = index;
        this.editMeetingDescriptor = new EditMeetingDescriptor(editMeetingDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        final List<MeetingEntry> lastShownList = model.getFilteredMeetingEntryList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEETING_DISPLAYED_INDEX);
        }

        final MeetingEntry meetingToEdit = lastShownList.get(index.getZeroBased());
        final MeetingEntry editedMeeting = createEditedMeeting(meetingToEdit, editMeetingDescriptor);

        if (!meetingToEdit.equals(editedMeeting) && model.hasMeetingEntry(editedMeeting)) {
            throw new CommandException(MESSAGE_DUPLICATE_MEETING);
        }

        model.setMeetingEntry(meetingToEdit, editedMeeting);
        model.updateFilteredMeetingEntryList(PREDICATE_SHOW_ALL_MEETING_ENTRIES);
        return new CommandResult(String.format(MESSAGE_EDIT_MEETING_SUCCESS, editedMeeting));
    }

    /**
     * Creates and returns a {@code Meeting} with the details of {@code meetingToEdit}
     * edited with {@code editMeetingDescriptor}.
     */
    private static MeetingEntry createEditedMeeting(MeetingEntry meetingToEdit,
            EditMeetingDescriptor editMeetingDescriptor) {
        assert meetingToEdit != null;

        final MeetingName updatedName = editMeetingDescriptor.getName().orElse(meetingToEdit.getName());
        final MeetingUrl updatedUrl = editMeetingDescriptor.getUrl().orElse(meetingToEdit.getUrl());
        final MeetingDateTime updatedDateTime = editMeetingDescriptor.getDateTime().orElse(meetingToEdit.getDateTime());
        final MeetingDuration updatedDuration = editMeetingDescriptor.getDuration().orElse(meetingToEdit.getDuration());
        final Module updatedModule = editMeetingDescriptor.getModule().orElse(meetingToEdit.getModule());
        final IsRecurring updatedIsRecurring = editMeetingDescriptor.getIsRecurring().orElse(meetingToEdit
                .getIsRecurring());
        final Set<Tag> updatedTags = editMeetingDescriptor.getTags().orElse(meetingToEdit.getTags());

        return new MeetingEntry(updatedName, updatedUrl, updatedDateTime, updatedDuration, updatedModule,
                updatedIsRecurring, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditMeetingCommand)) {
            return false;
        }

        // state check
        final EditMeetingCommand e = (EditMeetingCommand) other;
        return index.equals(e.index)
                && editMeetingDescriptor.equals(e.editMeetingDescriptor);
    }

    /**
     * Stores the details to edit the meeting with. Each non-empty field value will replace the
     * corresponding field value of the meeting.
     */
    public static class EditMeetingDescriptor {
        private MeetingName name;
        private MeetingUrl url;
        private MeetingDateTime dateTime;
        private MeetingDuration duration;
        private Module module;
        private IsRecurring isRecurring;
        private Set<Tag> tags;

        public EditMeetingDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditMeetingDescriptor(EditMeetingDescriptor toCopy) {
            setName(toCopy.name);
            setUrl(toCopy.url);
            setDateTime(toCopy.dateTime);
            setDuration(toCopy.duration);
            setModule(toCopy.module);
            setIsRecurring(toCopy.isRecurring);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, url, dateTime, duration, module, isRecurring, tags);
        }

        public void setName(MeetingName name) {
            this.name = name;
        }

        public Optional<MeetingName> getName() {
            return Optional.ofNullable(name);
        }

        public void setUrl(MeetingUrl url) {
            this.url = url;
        }

        public Optional<MeetingUrl> getUrl() {
            return Optional.ofNullable(url);
        }

        public void setDateTime(MeetingDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public Optional<MeetingDateTime> getDateTime() {
            return Optional.ofNullable(dateTime);
        }

        public void setDuration(MeetingDuration duration) {
            this.duration = duration;
        }

        public Optional<MeetingDuration> getDuration() {
            return Optional.ofNullable(duration);
        }

        public void setModule(Module module) {
            this.module = module;
        }

        public Optional<Module> getModule() {
            return Optional.ofNullable(module);
        }

        public void setIsRecurring(IsRecurring isRecurring) {
            this.isRecurring = isRecurring;
        }

        public Optional<IsRecurring> getIsRecurring() {
            return Optional.ofNullable(isRecurring);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditMeetingDescriptor)) {
                return false;
            }

            // state check
            final EditMeetingDescriptor e = (EditMeetingDescriptor) other;

            return getName().equals(e.getName())
                    && getUrl().equals(e.getUrl())
                    && getDateTime().equals(e.getDateTime())
                    && getDuration().equals(e.getDuration())
                    && getModule().equals(e.getModule())
                    && getIsRecurring().equals(e.getIsRecurring())
                    && getTags().equals(e.getTags());
        }
    }
}
