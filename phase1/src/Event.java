import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents an Event
 *
 * Responsibilities:
 * Store name
 * Get name
 *
 * Store eventID
 * Get eventID
 *
 * Store date
 * Get date
 *
 * Store time
 * Get time
 *
 * Store AttendeeIDs
 * Get AttendeesIDs
 * Add AttendeeIDs
 * Remove AttendeeID
 *
 * Store SpeakerID
 * Get SpeakerID
 *
 * @author Justin Chan
 */

public class Event {
    private final String name;
    private final UUID eventID;
    private final int[] date; // The date of the event in the format of { mm, dd, yyyy }
    private final int time; // The time of the event in the format of military time
    private final UUID speakerID;  // The UUID of the Speaker
    private final ArrayList<UUID> attendeeIDs = new ArrayList<UUID>();  // List of attendees by UUID

    /**
     * Event constructor
     *
     * @param eventName the name of the event
     * @param speaker the speaker of the event
     * @param date the date of the event in the format of { mm, dd, yyyy }
     * @param time the time of the event in military time
     */
    Event(String eventName, Speaker speaker, int[] date, int time) {
        eventID = UUID.randomUUID();
        this.name = eventName;
        this.speakerID = speaker.getUserID();
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public UUID getEventID() {
        return eventID;
    }

    public int[] getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public ArrayList<UUID> getAttendeeIDs() {
        return attendeeIDs;
    }

    /**
     * This method adds all the UUIDs of the Attendees in attendeesToAdd to the Event attendees. Duplicate members will
     * not be added.
     *
     * @param attendeesToAdd an ArrayList of Attendee objects to be added to the Event attendees.
     *                     Note that we take an ArrayList of Attendees and NOT UUIDs.
     */
    public void addAttendeeIDs(ArrayList<Attendee> attendeesToAdd) {
        for (Attendee attendee : attendeesToAdd) {
            UUID attendeeID = attendee.getUserID();
            if (!attendeeIDs.contains(attendeeID)) {
                attendeeIDs.add(attendeeID);
            }
        }
    }

    /**
     * This method the UUID of the Attendee attendeesToRemove from the Event attendees. Returns true if the UUID was
     * successfully removed, and false if the UUID was not present in the Event attendees.
     *
     * @param attendeeToRemove an Attendee object to be removed from the Event attendees.
     *                         Note that we take an Attendee and NOT its UUID
     */
    public boolean removeAttendeeID(Attendee attendeeToRemove) {
        return attendeeIDs.remove(attendeeToRemove.getUserID());
    }

    public UUID getSpeakerID() {
        return speakerID;
    }
}
