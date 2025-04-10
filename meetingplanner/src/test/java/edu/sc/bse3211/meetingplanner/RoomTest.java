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
    
    // Test adding conflicting meetings
    @Test(expected = TimeConflictException.class)
    public void testAddConflictingMeeting() throws TimeConflictException {
        room.addMeeting(meeting);
        Meeting conflictingMeeting = new Meeting(1, 1, 9, 11); // Overlaps with first meeting
        room.addMeeting(conflictingMeeting);
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
        room.addMeeting(meeting);
        String agenda = room.printAgenda(1);
        assertTrue(agenda.contains("January"));
        assertTrue(agenda.contains("9:00"));
    }
    
    // Test printAgenda for a specific day
    @Test
    public void testPrintAgendaDay() throws TimeConflictException {
        room.addMeeting(meeting);
        String agenda = room.printAgenda(1, 1);
        assertTrue(agenda.contains("January 1"));
        assertTrue(agenda.contains("9:00"));
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
    public void testEndOfDayMeeting() throws TimeConflictException {
        Meeting endDayMeeting = new Meeting(1, 1, 23, 24); // January 1st, 11:00 PM - 12:00 AM
        room.addMeeting(endDayMeeting);
        assertTrue(room.isBusy(1, 1, 23, 24));
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
        // Should not throw exception when removing non-existent meeting
        room.removeMeeting(1, 1, 0);
    }

    // Test getting non-existent meeting
    @Test
    public void testGetNonExistentMeeting() {
        Meeting retrievedMeeting = room.getMeeting(1, 1, 0);
        assertNull(retrievedMeeting);
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
    @Test(expected = TimeConflictException.class)
    public void testPartialOverlapMeeting() throws TimeConflictException {
        Meeting meeting1 = new Meeting(1, 1, 9, 11);
        Meeting meeting2 = new Meeting(1, 1, 10, 12);
        room.addMeeting(meeting1);
        room.addMeeting(meeting2);
    }

    // Test adding meetings with exact same time
    @Test(expected = TimeConflictException.class)
    public void testExactSameTimeMeeting() throws TimeConflictException {
        Meeting meeting1 = new Meeting(1, 1, 9, 10);
        Meeting meeting2 = new Meeting(1, 1, 9, 10);
        room.addMeeting(meeting1);
        room.addMeeting(meeting2);
    }
}
