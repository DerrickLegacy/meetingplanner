package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.Arrays;

public class PersonTest {
    private Person person;
    private Meeting meeting;
    private Room room;

    @Before
    public void setUp() {
        person = new Person("Test Person");
        room = new Room("Test Room");
        ArrayList<Person> attendees = new ArrayList<Person>();
        attendees.add(person);
        meeting = new Meeting(5, 15, 9, 11, attendees, room, "Test Meeting");
    }

    @Test
    public void testDefaultConstructor() {
        Person defaultPerson = new Person();
        assertEquals("Default constructor should initialize name to empty string", "", defaultPerson.getName());
    }

    @Test
    public void testConstructorWithName() {
        Person namedPerson = new Person("John Doe");
        assertEquals("Constructor should set name to the provided value", "John Doe", namedPerson.getName());
    }

    @Test
    public void testGetName() {
        assertEquals("getName should return the person's name", "Test Person", person.getName());
    }

    @Test
    public void testAddMeeting() throws TimeConflictException {
        person.addMeeting(meeting);
        // Verify meeting was added by checking if person is busy during meeting time
        assertTrue("Person should be busy during the meeting time", person.isBusy(5, 15, 9, 11));
    }

    @Test
    public void testAddMeetingConflict() {
        try {
            // Add first meeting
            person.addMeeting(meeting);

            // Try to add a conflicting meeting
            Meeting conflictingMeeting = new Meeting(5, 15, 10, 12, new ArrayList<Person>(), room,
                    "Conflicting Meeting");
            person.addMeeting(conflictingMeeting);

            // If we reach here, the test should fail because no exception was thrown
            fail("Expected TimeConflictException was not thrown when adding conflicting meeting");
        } catch (TimeConflictException e) {
            // Verify the exception message contains relevant information
            assertTrue("Exception message should mention the person's name", e.getMessage().contains("Test Person"));
            assertTrue("Exception message should indicate a conflict", e.getMessage().contains("Conflict"));
        }
    }

    @Test
    public void testPrintAgendaMonth() throws TimeConflictException {
        person.addMeeting(meeting);
        String agenda = person.printAgenda(5);

        // Verify all meeting details are in the agenda
        assertTrue("Agenda should contain the meeting date", agenda.contains("5/15"));
        assertTrue("Agenda should contain the meeting time", agenda.contains("9 - 11"));
        assertTrue("Agenda should contain the meeting description", agenda.contains("Test Meeting"));
        assertTrue("Agenda should contain the room ID", agenda.contains("Test Room"));
    }

    @Test
    public void testPrintAgendaDay() throws TimeConflictException {
        person.addMeeting(meeting);
        String agenda = person.printAgenda(5, 15);

        // Verify all meeting details are in the agenda
        assertTrue("Agenda should contain the meeting date", agenda.contains("5/15"));
        assertTrue("Agenda should contain the meeting time", agenda.contains("9 - 11"));
        assertTrue("Agenda should contain the meeting description", agenda.contains("Test Meeting"));
        assertTrue("Agenda should contain the room ID", agenda.contains("Test Room"));
    }

    @Test
    public void testIsBusy() throws TimeConflictException {
        person.addMeeting(meeting);

        // Test during meeting time
        assertTrue("Person should be busy during the meeting time", person.isBusy(5, 15, 9, 11));

        // Test before meeting time
        assertFalse("Person should not be busy before the meeting time", person.isBusy(5, 15, 7, 8));

        // Test after meeting time
        assertFalse("Person should not be busy after the meeting time", person.isBusy(5, 15, 12, 14));

        // Test on a different day
        assertFalse("Person should not be busy on a different day", person.isBusy(5, 16, 9, 11));
    }

    @Test
    public void testGetMeeting() throws TimeConflictException {
        person.addMeeting(meeting);
        Meeting retrievedMeeting = person.getMeeting(5, 15, 0);

        // Verify all meeting details are correct
        assertEquals("Retrieved meeting should have the correct month", 5, retrievedMeeting.getMonth());
        assertEquals("Retrieved meeting should have the correct day", 15, retrievedMeeting.getDay());
        assertEquals("Retrieved meeting should have the correct start time", 9, retrievedMeeting.getStartTime());
        assertEquals("Retrieved meeting should have the correct end time", 11, retrievedMeeting.getEndTime());
        assertEquals("Retrieved meeting should have the correct description", "Test Meeting",
                retrievedMeeting.getDescription());
        assertEquals("Retrieved meeting should have the correct room", room, retrievedMeeting.getRoom());
    }

    @Test
    public void testRemoveMeeting() throws TimeConflictException {
        person.addMeeting(meeting);
        assertTrue("Person should be busy before removing the meeting", person.isBusy(5, 15, 9, 11));

        person.removeMeeting(5, 15, 0);
        assertFalse("Person should not be busy after removing the meeting", person.isBusy(5, 15, 9, 11));
    }

    @Test
    public void testMultipleMeetings() throws TimeConflictException {
        // Add first meeting
        person.addMeeting(meeting);

        // Add second meeting on a different day
        Meeting secondMeeting = new Meeting(5, 16, 13, 15, new ArrayList<Person>(), room, "Second Meeting");
        person.addMeeting(secondMeeting);

        // Verify both meetings are in the calendar
        assertTrue("Person should be busy during the first meeting time", person.isBusy(5, 15, 9, 11));
        assertTrue("Person should be busy during the second meeting time", person.isBusy(5, 16, 13, 15));

        // Verify agenda contains both meetings
        String monthAgenda = person.printAgenda(5);
        assertTrue("Agenda should contain the first meeting", monthAgenda.contains("Test Meeting"));
        assertTrue("Agenda should contain the second meeting", monthAgenda.contains("Second Meeting"));
    }

    @Test
    public void testEmptyAgenda() {
        String monthAgenda = person.printAgenda(5);
        assertTrue("Empty agenda should return an empty string", monthAgenda.isEmpty() || monthAgenda.equals(""));
    }

    // Additional tests from the user's version

    @Test
    public void testAddValidMeeting() throws TimeConflictException {
        Room room = new Room("4");
        Person attendee = new Person("Alice");
        Meeting meeting = new Meeting(3, 10, 9, 23, new ArrayList<>(Arrays.asList(attendee)), room, "Team Meeting");

        person.addMeeting(meeting);
        assertEquals("Meeting should be correctly added and retrievable", meeting.toString(),
                person.getMeeting(3, 10, 0).toString());
    }

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
            fail("Expected TimeConflictException to be thrown");
        } catch (TimeConflictException e) {
            assertTrue("Exception message should contain the person's name", e.getMessage().contains("Alice"));
        }
    }

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

        assertEquals("Agenda should contain the correct number of meetings", 4, meetingLines);
    }

    @Test
    public void testAddMeetingWithNoRoom() throws TimeConflictException {
        Person attendee = new Person("Alice");

        Meeting meeting = new Meeting(4, 1, 14, 16,
                new ArrayList<>(Arrays.asList(attendee)),
                null,
                "Remote Meeting");

        person.addMeeting(meeting);
        Meeting retrieved = person.getMeeting(4, 1, 0);

        assertEquals("Retrieved meeting should have the correct month", 4, retrieved.getMonth());
        assertEquals("Retrieved meeting should have the correct day", 1, retrieved.getDay());
        assertEquals("Retrieved meeting should have the correct description", "Remote Meeting",
                retrieved.getDescription());
        assertNull("Retrieved meeting should have a null room", retrieved.getRoom());
    }

    @Test
    public void testAddMeetingWithEmptyAttendees() throws TimeConflictException {
        Room room = new Room("C3");

        Meeting meeting = new Meeting(5, 2, 10, 12,
                new ArrayList<>(),
                room,
                "Team Building");

        person.addMeeting(meeting);

        Meeting retrieved = person.getMeeting(5, 2, 0);

        assertEquals("Retrieved meeting should have the correct month", 5, retrieved.getMonth());
        assertEquals("Retrieved meeting should have the correct day", 2, retrieved.getDay());
        assertEquals("Retrieved meeting should have the correct description", "Team Building",
                retrieved.getDescription());
        assertEquals("Retrieved meeting should have the correct room", room, retrieved.getRoom());
    }

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
            assertTrue("Exception message should mention overlap",
                    e.getMessage().contains("Overlap with another item"));
            assertTrue("Exception message should mention the first meeting", e.getMessage().contains("First Slot"));
            assertTrue("First meeting should still be in the agenda", person.printAgenda(5, 5).contains("First Slot"));
            assertFalse("Second meeting should not be in the agenda", person.printAgenda(5, 5).contains("Second Slot"));
        } finally {
            assertEquals("First meeting should be retrievable", meeting1.toString(),
                    person.getMeeting(5, 5, 0).toString());
        }
    }

    @Test
    public void testAddMeetingOnDifferentDay() throws TimeConflictException {
        Room room = new Room("D4");
        Person attendee = new Person("Alice");

        Meeting meeting1 = new Meeting(6, 1, 9, 10, new ArrayList<>(Arrays.asList(attendee)), room, "Day 1 Meeting");
        Meeting meeting2 = new Meeting(6, 2, 9, 10, new ArrayList<>(Arrays.asList(attendee)), room, "Day 2 Meeting");

        person.addMeeting(meeting1);
        person.addMeeting(meeting2);

        assertTrue("Agenda for day 1 should contain the first meeting",
                person.printAgenda(6, 1).contains("Day 1 Meeting"));
        assertTrue("Agenda for day 2 should contain the second meeting",
                person.printAgenda(6, 2).contains("Day 2 Meeting"));
    }

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
        assertTrue("Agenda should contain the first meeting", agenda.contains("Morning Standup"));
        assertTrue("Agenda should contain the second meeting", agenda.contains("Project Discussion"));
    }

    @Test
    public void testPrintAgendaNoMeetings() {
        String agenda = person.printAgenda(4, 21); // Agenda for April 21 (assuming no meetings)
        assertEquals("Agenda for a day with no meetings should show the date", "Agenda for 4/21:", agenda.trim());
    }

    @Test
    public void testPrintAgendaMonthWithMultipleDays() throws TimeConflictException {
        Room room = new Room("A1");
        Person attendee = new Person("Alice");
        Person attendee1 = new Person("Derrick");

        Meeting meeting1 = new Meeting(3, 10, 9, 11, new ArrayList<>(Arrays.asList(attendee)), room, "Morning Standup");
        Meeting meeting2 = new Meeting(3, 15, 12, 14, new ArrayList<>(Arrays.asList(attendee1)), room,
                "Project Review");
        Meeting meeting3 = new Meeting(4, 15, 12, 14, new ArrayList<>(Arrays.asList(attendee)), room, "Project Review");

        person.addMeeting(meeting1);
        person.addMeeting(meeting2);
        person.addMeeting(meeting3);

        String agenda = person.printAgenda(3); // Agenda for March (whole month)

        assertTrue("Agenda should contain the month/day format", agenda.contains("3/"));
        assertTrue("Agenda should contain the meeting description", agenda.contains("Project Review"));
    }

    @Test
    public void testPrintAgendaFormat() throws TimeConflictException {
        Room room = new Room("A1");
        Person attendee = new Person("Alice");

        Meeting meeting1 = new Meeting(3, 10, 9, 11, new ArrayList<>(Arrays.asList(attendee)), room, "Morning Standup");
        person.addMeeting(meeting1);

        String agenda = person.printAgenda(3, 10);

        assertTrue("Agenda should contain the meeting description", agenda.contains("Morning Standup"));
        assertTrue("Agenda should contain the meeting time", agenda.contains("9 - 11"));
        assertTrue("Agenda should contain the meeting details in the correct format",
                agenda.contains("3/10, 9 - 11,A1: Morning Standup"));
    }
}
