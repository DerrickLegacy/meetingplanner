package edu.sc.bse3211.meetingplanner;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

public class PersonTest {
    private Person person;

    @Before
    public void setUp() {
        person = new Person("Alice");
    }

    /**
     * Test Case: P1
     * Test Default Constructor
     * 
     * @param name - The name of the person.
     * 
     *             public Person(String name) {
     *             this.name = name;
     *             calendar = new Calendar();
     *             }
     */
    @Test
    public void testDefaultConstructor() {
        person = new Person();
        assertEquals("", person.getName());
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals("Alice", person.getName());
    }

    /**
     * Test Case: P2
     * Tests Adding a meeting to a calendar.
     * 
     * @param month       - The month of the meeting (1-12).
     * @param day         - The day of the meeting (1-31).
     * @param start       - The time the meeting starts (0-23).
     * @param end         - The time the meeting ends (0-23).
     * @param attendees   - The people attending the meeting.
     * @param room        - The room that the meeting is taking place in.
     * @param description - A description of the meeting.
     * 
     *                    public Meeting(int month, int day, int start, int end,
     *                    ArrayList<Person> attendees, Room room, String
     *                    description){
     *                    this.month=month;
     *                    this.day=day;
     *                    this.start=start;
     *                    this.end=end;
     *                    this.attendees = attendees;
     *                    this.room = room;
     *                    this.description = description;
     *                    }
     */
    @Test
    public void testAddValidMeeting() throws TimeConflictException {
        Room room = new Room("4");
        Person attendee = new Person("Alice");
        Meeting meeting = new Meeting(3, 10, 9, 23, new ArrayList<>(Arrays.asList(attendee)), room, "Team Meeting");

        person.addMeeting(meeting);
        assertEquals(meeting.toString(), person.getMeeting(3, 10, 0).toString());
    }

    /**
     * Test Case: P3
     * Test for Conflicting meetings
     * it should fail with a TimeConflictException if the meeting times overlap
     */
    @Test
    public void testAddMeetingConflictDiagnostic() throws TimeConflictException {
        Room room = new Room("A1");
        Person attendee = new Person("Alice");

        Meeting meeting1 = new Meeting(3, 10, 9, 11, new ArrayList<>(Arrays.asList(attendee)), room, "Morning Standup");
        Meeting meeting2 = new Meeting(3, 10, 10, 12, new ArrayList<>(Arrays.asList(attendee)), room,
                "Overlapping Meeting");

        try {
            person.addMeeting(meeting1);
            person.addMeeting(meeting1);
            person.addMeeting(meeting2);
            // System.out.println(person.printAgenda(3, 10));
            fail("THIS TEST SHOULD HAVE FAILED EARLIER");
        } catch (TimeConflictException e) {
            assertTrue(e.getMessage().contains("Alice"));
            // throw e;
        }
    }

    /**
     * Test Case: P4
     * Test Adding a Meeting with Different Time (Non-conflicting)
     * Validate that two meetings on the same day at non-overlapping times both
     * succeed.
     */
    @Test
    public void testAddTwoNonConflictingMeetings() throws TimeConflictException {
        Room room = new Room("B2");
        Person attendee = new Person("Alice");

        Meeting meeting1 = new Meeting(3, 11, 9, 10, new ArrayList<>(Arrays.asList(attendee)), room, "Short Meeting");
        Meeting meeting2 = new Meeting(3, 11, 11, 12, new ArrayList<>(Arrays.asList(attendee)), room, "Follow-Up");
        Meeting meeting3 = new Meeting(3, 13, 11, 14, new ArrayList<>(Arrays.asList(attendee)), room, "Recession");

        person.addMeeting(meeting1);
        person.addMeeting(meeting2);
        person.addMeeting(meeting3);

        String agenda = person.printAgenda(3, 11);
        long meetingLines = Arrays.stream(agenda.split("\n"))
                .filter(line -> !line.isEmpty())
                .filter(line -> !line.startsWith("Agenda for"))
                .count();

        assertEquals(4, meetingLines);
    }

    /**
     * Test Case: P5
     * Test Adding a Meeting with Null Room
     * This ensures edge case robustness, depending on how your constructor handles
     * nulls.
     */

    @Test
    public void testAddMeetingWithNoRoom() throws TimeConflictException {
        Person attendee = new Person("Alice");

        Meeting meeting = new Meeting(4, 1, 14, 16,
                new ArrayList<>(Arrays.asList(attendee)),
                null,
                "Remote Meeting");

        person.addMeeting(meeting);
        Meeting retrieved = person.getMeeting(4, 1, 0);

        assertEquals(4, retrieved.getMonth());
        assertEquals(1, retrieved.getDay());
        assertEquals("Remote Meeting", retrieved.getDescription());
        assertEquals(null, retrieved.getRoom());
        assertNull(retrieved.getRoom());
    }

    /**
     * Test Case: P6
     * Test Adding a Meeting with Empty Attendees
     * This ensures edge case robustness, depending on how your constructor handles
     * nulls.
     */
    @Test
    public void testAddMeetingWithEmptyAttendees() throws TimeConflictException {
        Room room = new Room("C3");

        Meeting meeting = new Meeting(5, 2, 10, 12,
                new ArrayList<>(),
                room,
                "Team Building");

        person.addMeeting(meeting);

        Meeting retrieved = person.getMeeting(5, 2, 0);

        assertEquals(5, retrieved.getMonth());
        assertEquals(2, retrieved.getDay());
        assertEquals("Team Building", retrieved.getDescription());
        assertEquals(room, retrieved.getRoom());
    }

    /**
     * Test Case: P7 - Back-to-Back Meetings
     * Test Adding a Meeting That Ends When Another Starts (Edge of Conflict)
     * These meetings SHOULD conflict (end time of one meeting equals start time of
     * another).
     * 
     * Purpose: Verify system correctly detects back-to-back meetings as conflicts.
     * Test Data:
     * - Meeting 1: 10:00-11:00
     * - Meeting 2: 11:00-12:00
     *
     * Acceptance Criteria:
     * ✅ TimeConflictException thrown when adding conflicting meeting
     * ✅ First meeting remains in agenda, second meeting is rejected
     */
    @Test
    public void testAddBackToBackMeetings() {
        Room room = new Room("C3");
        Person attendee = new Person("Alice");
        Meeting meeting1 = new Meeting(5, 5, 10, 11, new ArrayList<>(Arrays.asList(attendee)), room, "First Slot");
        Meeting meeting2 = new Meeting(5, 5, 11, 12, new ArrayList<>(Arrays.asList(attendee)), room, "Second Slot");

        try {
            person.addMeeting(meeting1);
            person.addMeeting(meeting2);
            fail("Expected TimeConflictException to be thrown");
        } catch (TimeConflictException e) {
            // Expected exception
            assertTrue(e.getMessage().contains("Overlap with another item"));
            assertTrue(e.getMessage().contains("First Slot"));
            assertTrue(person.printAgenda(5, 5).contains("First Slot"));
            assertFalse(person.printAgenda(5, 5).contains("Second Slot"));
        } finally {
            assertEquals(meeting1.toString(), person.getMeeting(5, 5, 0).toString());
        }
    }

    /**
     * Test Case: P8
     * Test Add Meeting on a Different Day
     * Ensure that calendar logic correctly distinguishes different days.
     */

    @Test
    public void testAddMeetingOnDifferentDay() throws TimeConflictException {
        Room room = new Room("D4");
        Person attendee = new Person("Alice");

        Meeting meeting1 = new Meeting(6, 1, 9, 10, new ArrayList<>(Arrays.asList(attendee)), room, "Day 1 Meeting");
        Meeting meeting2 = new Meeting(6, 2, 9, 10, new ArrayList<>(Arrays.asList(attendee)), room, "Day 2 Meeting");

        person.addMeeting(meeting1);
        person.addMeeting(meeting2);

        assertTrue(person.printAgenda(6, 1).contains("Day 1 Meeting"));
        assertTrue(person.printAgenda(6, 2).contains("Day 2 Meeting"));
    }

    // TESTING THE PRINT
    /**
     * Test Case: P9
     * Test for a Valid Agenda
     * Ensure that the agenda prints correctly for a month.
     */
    @Test
    public void testPrintAgendaValid() throws TimeConflictException {
        Room room = new Room("A1");
        Person attendee = new Person("Alice");

        Meeting meeting1 = new Meeting(3, 10, 9, 11, new ArrayList<>(Arrays.asList(attendee)), room, "Morning Standup");
        Meeting meeting2 = new Meeting(3, 10, 12, 14, new ArrayList<>(Arrays.asList(attendee)), room,
                "Project Discussion");

        person.addMeeting(meeting1);
        person.addMeeting(meeting2);

        String agenda = person.printAgenda(3, 10); // Agenda for March 10
        assertTrue(agenda.contains("Morning Standup"));
        assertTrue(agenda.contains("Project Discussion"));
    }

    /**
     * Test Case: P10
     * Test for No Meetings on a Specific Day
     * Print agenda for a day with no meetings scheduled.
     * 
     * Test Description: Ensure that the method handles days with no meetings
     * gracefully, possibly by returning a message like "No meetings scheduled".
     */
    @Test
    public void testPrintAgendaNoMeetings() {
        String agenda = person.printAgenda(4, 21); // Agenda for April 21 (assuming no meetings)
        assertEquals("Agenda for 4/21:", agenda.trim());
    }
 /**
     * Test Case: P11
     * Test for Agenda in a Month with Multiple Days
        * Print agenda for a month with multiple days.
     * Scenario: Print agenda for a month (e.g., March) with multiple meetings.
     * Test Description: Ensure that meetings for the entire month are correctly printed, and not just for one day.
    */

    @Test
    public void testPrintAgendaMonth() throws TimeConflictException {
        Room room = new Room("A1");
        Person attendee = new Person("Alice");
        Person attendee1 = new Person("Derrick");

        Meeting meeting1 = new Meeting(3, 10, 9, 11, new ArrayList<>(Arrays.asList(attendee)), room, "Morning Standup");
        Meeting meeting2 = new Meeting(3, 15, 12, 14, new ArrayList<>(Arrays.asList(attendee1)), room, "Project Review");
        Meeting meeting3 = new Meeting(4, 15, 12, 14, new ArrayList<>(Arrays.asList(attendee)), room, "Project Review");

        person.addMeeting(meeting1);
        person.addMeeting(meeting2);
        person.addMeeting(meeting3);

        String agenda = person.printAgenda(3);  // Agenda for March (whole month)

        assertTrue(agenda.contains("3/"));
        assertTrue(agenda.contains("Project Review"));
    }

    /**
     * Test Case: P12
     * Test for Agenda Format
    * Scenario: Ensure that the agenda is printed in the correct format.

    Test Description: Verify that the agenda is formatted as expected, such as listing meetings by time and description.
*/
@Test
public void testPrintAgendaFormat() throws TimeConflictException {
    Room room = new Room("A1");
    Person attendee = new Person("Alice");

    Meeting meeting1 = new Meeting(3, 10, 9, 11, new ArrayList<>(Arrays.asList(attendee)), room, "Morning Standup");
    person.addMeeting(meeting1);

    String agenda = person.printAgenda(3, 10);
            System.out.println(agenda);
    
    assertTrue(agenda.contains("Morning Standup"));
    // assertTrue(agenda.contains("9:00 AM - 11:00 AM"));

    assertTrue(agenda.contains("3/10, 9 - 11,A1: Morning Standup"));
}

}
