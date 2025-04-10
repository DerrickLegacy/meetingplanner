package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RoomTest {
    private Room room;
    private Meeting meeting;
    
    @Before
    public void setUp() {
        // Initialize a room and a meeting before each test
        room = new Room("TestRoom");
        meeting = new Meeting(1, 1, 9, 10); // January 1st, 9-10 AM
    }
    
    // Test constructor with ID
    @Test
    public void testConstructorWithID() {
        Room testRoom = new Room("Room123");
        assertEquals("Room123", testRoom.getID());
    }
    
    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        Room testRoom = new Room();
        assertEquals("", testRoom.getID());
    }
    
    // Test adding a valid meeting
    @Test
    public void testAddValidMeeting() throws TimeConflictException {
        room.addMeeting(meeting);
        // Verify meeting was added by checking if room is busy during that time
        assertTrue(room.isBusy(1, 1, 9, 10));
    }
    
    
    
    // Test isBusy with no meetings
    @Test
    public void testIsBusyNoMeetings() throws TimeConflictException {
        assertFalse(room.isBusy(1, 1, 9, 10));
    }
    
    // Test isBusy with existing meeting
    @Test
    public void testIsBusyWithMeeting() throws TimeConflictException {
        room.addMeeting(meeting);
        assertTrue(room.isBusy(1, 1, 9, 10));
    }
    
    // Test getting a meeting
    @Test
    public void testGetMeeting() throws TimeConflictException {
        room.addMeeting(meeting);
        Meeting retrievedMeeting = room.getMeeting(1, 1, 0);
        assertEquals(meeting, retrievedMeeting);
    }
    
    // Test removing a meeting
    @Test
    public void testRemoveMeeting() throws TimeConflictException {
        room.addMeeting(meeting);
        room.removeMeeting(1, 1, 0);
        assertFalse(room.isBusy(1, 1, 9, 10));
    }
    
    // Test printAgenda for a month
    @Test
    public void testPrintAgendaMonth() throws TimeConflictException {
        // No meetings added yet, so it should return an empty agenda or a default message
        String agenda = room.printAgenda(1);  // Check agenda for January
        
        assertNotNull("Agenda should not be null", agenda);
        assertTrue("Agenda should indicate no meetings if no meetings are scheduled", agenda.contains("No meetings scheduled for this month"));
    }
    
    
    // Test printAgenda for a specific day
    @Test
    public void testPrintAgendaDay() throws TimeConflictException {
        // No meetings added yet, so it should return an empty agenda or a default message
        String agenda = room.printAgenda(1, 1);  // Check agenda for January 1st
        
        assertNotNull("Agenda should not be null", agenda);
        assertTrue("Agenda should indicate no meetings if no meetings are scheduled", agenda.contains("No meetings scheduled for this day"));
    }
    
    
    // Test adding multiple meetings on different days
    @Test
    public void testMultipleMeetingsDifferentDays() throws TimeConflictException {
        Meeting meeting2 = new Meeting(1, 2, 9, 10); // January 2nd
        room.addMeeting(meeting);
        room.addMeeting(meeting2);
        assertTrue(room.isBusy(1, 1, 9, 10));
        assertTrue(room.isBusy(1, 2, 9, 10));
    }
    
    // Test invalid meeting parameters
    @Test(expected = TimeConflictException.class)
    public void testInvalidMeetingParameters() throws TimeConflictException {
        Meeting invalidMeeting = new Meeting(13, 32, 25, 26); // Invalid month, day, and time
        room.addMeeting(invalidMeeting);
    }

    // Test adding meetings at midnight
    @Test
    public void testMidnightMeeting() throws TimeConflictException {
        Meeting midnightMeeting = new Meeting(1, 1, 0, 1); // January 1st, 12:00-1:00 AM
        room.addMeeting(midnightMeeting);
        assertTrue(room.isBusy(1, 1, 0, 1));
    }

    // Test adding meetings at end of day
    @Test
    public void testEndOfDayMeeting() {
        try {
            Meeting endDayMeeting = new Meeting(1, 1, 23, 24); // January 1st, 11:00 PM - 12:00 AM
            room.addMeeting(endDayMeeting);
            assertTrue(room.isBusy(1, 1, 23, 24));
        } catch (TimeConflictException e) {
            fail("TimeConflictException should not be thrown for end of day meeting.");
        }
    }
   

    // Test adding meetings on last day of month
    @Test
    public void testLastDayOfMonthMeeting() throws TimeConflictException {
        Meeting lastDayMeeting = new Meeting(1, 31, 9, 10); // January 31st
        room.addMeeting(lastDayMeeting);
        assertTrue(room.isBusy(1, 31, 9, 10));
    }

    // Test adding meetings on first day of month
    @Test
    public void testFirstDayOfMonthMeeting() throws TimeConflictException {
        Meeting firstDayMeeting = new Meeting(1, 1, 9, 10); // January 1st
        room.addMeeting(firstDayMeeting);
        assertTrue(room.isBusy(1, 1, 9, 10));
    }

    // Test removing non-existent meeting
    @Test
    public void testRemoveNonExistentMeeting() {
        // Attempt to remove a non-existent meeting (index 0)
        try {
            room.removeMeeting(1, 1, 0);  // Index 0 is invalid since no meetings are added
        } catch (IndexOutOfBoundsException e) {
            // Handle the exception gracefully here
            System.out.println("Caught expected IndexOutOfBoundsException: " + e.getMessage());
        }
    }
    

    // Test getting non-existent meeting
    @Test
    public void testGetNonExistentMeeting() {
        // Try to get a non-existent meeting (index 0, which doesn't exist)
        try {
            Meeting retrievedMeeting = room.getMeeting(1, 1, 0);  // This should return null if the meeting doesn't exist
            assertNull("No meeting should be found at index 0", retrievedMeeting);
        } catch (IndexOutOfBoundsException e) {
            // Handle the exception gracefully, this should not happen in this case
            fail("Unexpected IndexOutOfBoundsException: " + e.getMessage());
        }
    }
    
    

    // Test adding meetings with same start time but different days
    @Test
    public void testSameTimeDifferentDays() throws TimeConflictException {
        Meeting meeting1 = new Meeting(1, 1, 9, 10);
        Meeting meeting2 = new Meeting(1, 2, 9, 10);
        room.addMeeting(meeting1);
        room.addMeeting(meeting2);
        assertTrue(room.isBusy(1, 1, 9, 10));
        assertTrue(room.isBusy(1, 2, 9, 10));
    }

    // Test adding meetings with same day but different months
    @Test
    public void testSameDayDifferentMonths() throws TimeConflictException {
        Meeting meeting1 = new Meeting(1, 1, 9, 10); // January 1st
        Meeting meeting2 = new Meeting(2, 1, 9, 10); // February 1st
        room.addMeeting(meeting1);
        room.addMeeting(meeting2);
        assertTrue(room.isBusy(1, 1, 9, 10));
        assertTrue(room.isBusy(2, 1, 9, 10));
    }

    // Test adding meetings with partial overlap
   
@Test
public void testPartialOverlapMeeting() {
    try {
        Meeting meeting1 = new Meeting(1, 1, 9, 11);
        Meeting meeting2 = new Meeting(1, 1, 10, 12);  // Should partially overlap with meeting1
        room.addMeeting(meeting1);
        room.addMeeting(meeting2);  // Should throw TimeConflictException
        fail("Expected TimeConflictException due to partial overlap.");
    } catch (TimeConflictException e) {
        // Expected exception
        System.out.println("Caught TimeConflictException as expected.");
    } catch (Exception e) {
        fail("Unexpected exception: " + e.getClass().getSimpleName());
    }
}

    // Test adding meetings with exact same time
    @Test
    public void testExactSameTimeMeeting() throws TimeConflictException {
        Meeting meeting1 = new Meeting(1, 1, 9, 10);  // First meeting at 9-10
        Meeting meeting2 = new Meeting(1, 1, 9, 10);  // Second meeting at the same time (9-10)
    
        try {
            room.addMeeting(meeting1);  // Add the first meeting
            room.addMeeting(meeting2);  // This should throw TimeConflictException
            fail("Expected TimeConflictException to be thrown due to overlapping meetings");
        } catch (TimeConflictException e) {
            // This is the expected outcome, test passes
            System.out.println("Caught expected TimeConflictException: " + e.getMessage());
        } catch (Exception e) {
            // Catch unexpected exceptions and fail the test
            fail("Unexpected exception: " + e.getClass().getName());
        }
    }
    
    
}