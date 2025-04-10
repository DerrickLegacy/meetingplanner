package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class CalendarTest {

  private Calendar calendar;
  private Meeting meeting1;
  private Meeting meeting2;
  private Room room;
  private Person person1;
  private Person person2;

  @Before
  public void setUp() {
    calendar = new Calendar();
    person1 = new Person("John");
    person2 = new Person("Jane");
    room = new Room("Room 101");
    meeting1 =
      new Meeting(5, 15, 9, 11, new ArrayList<>(), room, "Team Meeting");
    meeting2 =
      new Meeting(5, 15, 14, 16, new ArrayList<>(), room, "Project Review");
  }

  // 1. Test adding a holiday and ensuring it's marked as busy
  @Test
  public void testAddMeeting_holiday() {
    try {
      Meeting janan = new Meeting(2, 16, "Janan Luwum");
      calendar.addMeeting(janan);
      Boolean added = calendar.isBusy(2, 16, 0, 23);
      assertTrue(
        "Janan Luwum Day should be marked as busy on the calendar",
        added
      );
    } catch (TimeConflictException e) {
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  // 2. Test adding a regular meeting and checking busy hours
  @Test
  public void testAddMeeting_normalMeeting() {
    try {
      Meeting meeting = new Meeting(3, 15, 10, 12);
      meeting.setDescription("Team Meeting");
      calendar.addMeeting(meeting);
      assertTrue(
        "Calendar should be busy during meeting time",
        calendar.isBusy(3, 15, 10, 12)
      );
      assertFalse(
        "Calendar should not be busy outside meeting time",
        calendar.isBusy(3, 15, 13, 14)
      );
    } catch (TimeConflictException e) {
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  // 3. Test conflict detection when two meetings overlap
  @Test
  public void testAddMeeting_conflict() {
	  try {
		  // Create two overlapping meetings
		  Meeting meeting1 = new Meeting(4, 20, 14, 16);  // 14:00 - 16:00
		  Meeting meeting2 = new Meeting(4, 20, 15, 17);  // 15:00 - 17:00 (overlaps with meeting1)
  
		  // Add the first meeting
		  calendar.addMeeting(meeting1);
		  
		  // Add the second meeting and expect an exception
		  calendar.addMeeting(meeting2); // Should throw TimeConflictException
		  
		  // Fail the test if no exception is thrown
		  fail("Expected TimeConflictException to be thrown due to overlapping meetings");
		  
	  } catch (TimeConflictException e) {
		  // Expected outcome: TimeConflictException is thrown
		  System.out.println("Caught TimeConflictException as expected: " + e.getMessage());
		  
		  // Optional: Add more checks on the exception message if necessary
		  assertTrue("Exception message should contain 'overlap' or 'conflict'",
					 e.getMessage().contains("overlap") || e.getMessage().contains("conflict"));
	  } catch (Exception e) {
		  // Catch any other exceptions and fail the test with an error message
		  fail("Unexpected exception was thrown: " + e.getClass().getSimpleName());
	  }
  }
  
  

  // 4. Test clearing the schedule for a specific day
  @Test
  public void testClearSchedule() {
    try {
      Meeting meeting = new Meeting(5, 1, 9, 10);
      meeting.setDescription("Morning Meeting");
      calendar.addMeeting(meeting);
      calendar.clearSchedule(5, 1);
      assertFalse(
        "Calendar should be clear after clearSchedule",
        calendar.isBusy(5, 1, 9, 10)
      );
    } catch (TimeConflictException e) {
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  // 5. Test handling of invalid month input
  @Test(expected = TimeConflictException.class)
  public void testInvalidDate() throws TimeConflictException {
    calendar.isBusy(13, 1, 0, 1); // Invalid month
  }

  // 6. Test handling of invalid hour input
  @Test(expected = TimeConflictException.class)
  public void testInvalidTime() throws TimeConflictException {
    calendar.isBusy(1, 1, 24, 25); // Invalid hour
  }

  // 7. Test retrieving and removing a meeting
  @Test
  public void testGetAndRemoveMeeting() {
    try {
      Meeting meeting = new Meeting(6, 15, 13, 14);
      meeting.setDescription("Lunch Meeting");
      calendar.addMeeting(meeting);
      Meeting retrieved = calendar.getMeeting(6, 15, 0);
      assertEquals(
        "Retrieved meeting should match added meeting",
        meeting.getDescription(),
        retrieved.getDescription()
      );

      calendar.removeMeeting(6, 15, 0);
      assertFalse("Meeting should be removed", calendar.isBusy(6, 15, 13, 14));
    } catch (TimeConflictException e) {
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  // 8. Test full-day vacation block
  @Test
  public void testVacationBlocksInCalendar() {
    try {
      Meeting vacation = new Meeting(5, 1, 0, 23);
      vacation.setDescription("Vacation - Test Employee");
      calendar.addMeeting(vacation);
      assertTrue(
        "Calendar should show vacation block",
        calendar.isBusy(5, 1, 0, 23)
      );
    } catch (TimeConflictException e) {
      fail("Should not throw exception for vacation block");
    }
  }

  // 9. Test adding two meetings on the same day that don't overlap
  @Test
  public void testMultipleMeetingsSameDay() throws TimeConflictException {
    calendar.addMeeting(meeting1);
    calendar.addMeeting(meeting2);

    assertTrue(
      "First meeting should be in calendar",
      calendar.isBusy(5, 15, 9, 11)
    );
    assertTrue(
      "Second meeting should be in calendar",
      calendar.isBusy(5, 15, 14, 16)
    );
  }

  // 10. Test removing a meeting
  @Test
  public void testMeetingRemoval() throws TimeConflictException {
    calendar.addMeeting(meeting1);
    calendar.removeMeeting(5, 15, 0);
    assertFalse("Meeting should be removed", calendar.isBusy(5, 15, 9, 11));
  }

  // 11. Test clearing multiple meetings on the same day
  @Test
  public void testClearScheduleAgain() throws TimeConflictException {
    calendar.addMeeting(meeting1);
    calendar.addMeeting(meeting2);
    calendar.clearSchedule(5, 15);
    assertFalse("Schedule should be cleared", calendar.isBusy(5, 15, 9, 11));
    assertFalse("Schedule should be cleared", calendar.isBusy(5, 15, 14, 16));
  }

  // 12. Test retrieving a specific meeting
  @Test
  public void testGetMeeting() throws TimeConflictException {
    calendar.addMeeting(meeting1);
    Meeting retrieved = calendar.getMeeting(5, 15, 0);
    assertEquals(
      "Retrieved meeting should match added meeting",
      meeting1.toString(),
      retrieved.toString()
    );
  }

  // 13. Test printing agenda for a whole month
  @Test
  public void testPrintAgendaMonth() throws TimeConflictException {
    calendar.addMeeting(meeting1);
    calendar.addMeeting(meeting2);
    String agenda = calendar.printAgenda(5);
    assertTrue(
      "Agenda should contain first meeting",
      agenda.contains("Team Meeting")
    );
    assertTrue(
      "Agenda should contain second meeting",
      agenda.contains("Project Review")
    );
  }

  // 14. Test printing agenda for a specific day
  @Test
  public void testPrintAgendaDay() throws TimeConflictException {
    calendar.addMeeting(meeting1);
    String agenda = calendar.printAgenda(5, 15);
    assertTrue(
      "Agenda should contain meeting",
      agenda.contains("Team Meeting")
    );
  }

  // 15. Test another invalid date (e.g., Feb 30)
  @Test(expected = TimeConflictException.class)
  public void testInvalidDateAgain() throws TimeConflictException {
    calendar.isBusy(2, 30, 9, 11); // February 30th
  }

  // 16. Test another invalid time
  @Test(expected = TimeConflictException.class)
  public void testInvalidTimeAgain() throws TimeConflictException {
    calendar.isBusy(5, 15, 25, 26); // Invalid hours
  }
}
