package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class CalendarTest {
	private Calendar calendar;
	
	@Before
	public void setUp() {
		calendar = new Calendar();
	}
	
	@Test
	public void testAddMeeting_holiday() {
		// Create Janan Luwum holiday
		try {
			Meeting janan = new Meeting(2, 16, "Janan Luwum");
			calendar.addMeeting(janan);
			// Verify that it was added
			Boolean added = calendar.isBusy(2, 16, 0, 23);
			assertTrue("Janan Luwum Day should be marked as busy on the calendar", added);
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testAddMeeting_normalMeeting() {
		try {
			Meeting meeting = new Meeting(3, 15, 10, 12);
			meeting.setDescription("Team Meeting");
			calendar.addMeeting(meeting);
			assertTrue("Calendar should be busy during meeting time", 
				calendar.isBusy(3, 15, 10, 12));
			assertFalse("Calendar should not be busy outside meeting time", 
				calendar.isBusy(3, 15, 13, 14));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test(expected = TimeConflictException.class)
	public void testAddMeeting_conflict() throws TimeConflictException {
		Meeting meeting1 = new Meeting(4, 20, 14, 16);
		Meeting meeting2 = new Meeting(4, 20, 15, 17);
		calendar.addMeeting(meeting1);
		calendar.addMeeting(meeting2); // Should throw TimeConflictException
	}
	
	@Test
	public void testClearSchedule() {
		try {
			Meeting meeting = new Meeting(5, 1, 9, 10);
			meeting.setDescription("Morning Meeting");
			calendar.addMeeting(meeting);
			calendar.clearSchedule(5, 1);
			assertFalse("Calendar should be clear after clearSchedule", 
				calendar.isBusy(5, 1, 9, 10));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test(expected = TimeConflictException.class)
	public void testInvalidDate() throws TimeConflictException {
		calendar.isBusy(13, 1, 0, 1); // Invalid month
	}
	
	@Test(expected = TimeConflictException.class)
	public void testInvalidTime() throws TimeConflictException {
		calendar.isBusy(1, 1, 24, 25); // Invalid hour
	}
	
	@Test
	public void testGetAndRemoveMeeting() {
		try {
			Meeting meeting = new Meeting(6, 15, 13, 14);
			meeting.setDescription("Lunch Meeting");
			calendar.addMeeting(meeting);
			Meeting retrieved = calendar.getMeeting(6, 15, 0);
			assertEquals("Retrieved meeting should match added meeting", 
				meeting.getDescription(), retrieved.getDescription());
			
			calendar.removeMeeting(6, 15, 0);
			assertFalse("Meeting should be removed", 
				calendar.isBusy(6, 15, 13, 14));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testVacationBlocksInCalendar() {
		try {
			Meeting vacation = new Meeting(5, 1, 0, 23);
			vacation.setDescription("Vacation - Test Employee");
			calendar.addMeeting(vacation);
			assertTrue("Calendar should show vacation block", 
				calendar.isBusy(5, 1, 0, 23));
		} catch (TimeConflictException e) {
			fail("Should not throw exception for vacation block");
		}
	}
}
