package seedu.address.testutil.meeting;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_URL;

import seedu.address.logic.commands.meeting.AddMeetingCommand;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDateTime;

/**
 * This class is a placeholder for MeetingUtil. For reference please refer to the obsolete package.
 */
public class MeetingUtil {
    public static String getAddMeetingCommand(Meeting meeting) {
        return AddMeetingCommand.COMMAND_WORD + " " + getMeetingDetails(meeting);
    }

    public static String getMeetingDetails(Meeting meeting) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + meeting.getName().name + " ");
        sb.append(PREFIX_URL + meeting.getUrl().toString() + " ");
        sb.append(PREFIX_DATETIME + meeting.getDateTime().datetime.format(MeetingDateTime.INPUT_FORMAT) + " ");
        sb.append(PREFIX_DURATION + String.valueOf(meeting.getDuration().duration) + " ");
        sb.append(PREFIX_MODULE + meeting.getModule().code + " ");
        sb.append(PREFIX_RECURRING + meeting.getIsRecurring().toString() + " ");
        meeting.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
