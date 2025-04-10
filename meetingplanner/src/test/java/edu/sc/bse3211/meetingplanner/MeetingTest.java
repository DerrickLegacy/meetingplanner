package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class MeetingTest {

  private Meeting meeting;
  private Person person1;
  private Person person2;
  private Room room;

  @Before
  public void setUp() {
    person1 = new Person("John");
    person2 = new Person("Jane");
    room = new Room("Room 101");
    meeting =
      new Meeting(5, 15, 9, 11, new ArrayList<>(), room, "Team Meeting");
  }

  // ===== Constructor Tests =====
  
  // Test Case 1: Default Constructor
  // Verifies that a meeting created with default constructor has expected initial values
  @Test
  public void testDefaultConstructor() {
    Meeting defaultMeeting = new Meeting();
    assertNotNull("Default meeting should not be null", defaultMeeting);
    assertEquals("Default month should be 0", 0, defaultMeeting.getMonth());
    assertEquals("Default day should be 0", 0, defaultMeeting.getDay());
    assertEquals("Default start time should be 0", 0, defaultMeeting.getStartTime());
    assertEquals("Default end time should be 0", 0, defaultMeeting.getEndTime());
    assertNull("Default room should be null", defaultMeeting.getRoom());
    assertNull("Default description should be null", defaultMeeting.getDescription());
    assertNotNull("Default attendees list should not be null", defaultMeeting.getAttendees());
  }

  // Test Case 2: Month-Day Constructor
  // Verifies that a meeting created with month and day has correct values and full day range
  @Test
  public void testMonthDayConstructor() {
    Meeting dayMeeting = new Meeting(6, 20);
    assertEquals("Month should be 6", 6, dayMeeting.getMonth());
    assertEquals("Day should be 20", 20, dayMeeting.getDay());
    assertEquals("Start time should be 0", 0, dayMeeting.getStartTime());
    assertEquals("End time should be 23", 23, dayMeeting.getEndTime());
  }

  // Test Case 3: Month-Day-Description Constructor
  // Verifies that a meeting created with month, day, and description has correct values
  @Test
  public void testMonthDayDescriptionConstructor() {
    Meeting meeting = new Meeting(3, 21, "Public Holiday");
    assertEquals("Month should be 3", 3, meeting.getMonth());
    assertEquals("Day should be 21", 21, meeting.getDay());
    assertEquals("Start time should be 0", 0, meeting.getStartTime());
    assertEquals("End time should be 23", 23, meeting.getEndTime());
    assertEquals("Description should match", "Public Holiday", meeting.getDescription());
  }

  // Test Case 4: Full Constructor
  // Verifies that a meeting created with all parameters has correct values
  @Test
  public void testFullConstructor() {
    ArrayList<Person> attendees = new ArrayList<>();
    attendees.add(person1);
    Meeting fullMeeting = new Meeting(9, 10, 14, 16, attendees, room, "Project Review");

    assertEquals("Month should be 9", 9, fullMeeting.getMonth());
    assertEquals("Day should be 10", 10, fullMeeting.getDay());
    assertEquals("Start time should be 14", 14, fullMeeting.getStartTime());
    assertEquals("End time should be 16", 16, fullMeeting.getEndTime());
    assertEquals("Room should match", room, fullMeeting.getRoom());
    assertEquals("Description should be 'Project Review'", "Project Review", fullMeeting.getDescription());
    assertEquals("Number of attendees should be 1", 1, fullMeeting.getAttendees().size());
  }

  // ===== Attendee Management Tests =====

  // Test Case 5: Adding Attendees
  // Verifies that attendees can be added correctly
  @Test
  public void testAddAttendee() {
    meeting.addAttendee(person1);
    assertEquals("Number of attendees should be 1", 1, meeting.getAttendees().size());
    assertTrue("Attendees should contain person1", meeting.getAttendees().contains(person1));
  }

  // Test Case 6: Removing Attendees
  // Verifies that attendees can be removed correctly
  @Test
  public void testRemoveAttendee() {
    meeting.addAttendee(person1);
    meeting.removeAttendee(person1);
    assertEquals("Number of attendees should be 0", 0, meeting.getAttendees().size());
    assertFalse("Attendees should not contain person1", meeting.getAttendees().contains(person1));
  }

  // Test Case 7: Multiple Attendees
  // Verifies that multiple attendees can be managed correctly
  @Test
  public void testMultipleAttendees() {
    meeting.addAttendee(person1);
    meeting.addAttendee(person2);
    assertEquals("Number of attendees should be 2", 2, meeting.getAttendees().size());
    assertTrue("Attendees should contain person1", meeting.getAttendees().contains(person1));
    assertTrue("Attendees should contain person2", meeting.getAttendees().contains(person2));
  }

  // ===== Time Management Tests =====

  // Test Case 8: Valid Time Range
  // Verifies that valid time ranges are accepted
  @Test
  public void testValidTimeRange() {
    Meeting timeMeeting = new Meeting(5, 15, 9, 11);
    assertEquals("Start time should be 9", 9, timeMeeting.getStartTime());
    assertEquals("End time should be 11", 11, timeMeeting.getEndTime());
  }

  // Test Case 9: Invalid Time Range
  // Verifies that invalid time ranges are handled correctly
  @Test
  public void testInvalidTimeRange() {
    Meeting invalidTimeMeeting = new Meeting(5, 15, 14, 10);
    assertTrue("Start time should be greater than end time", 
              invalidTimeMeeting.getStartTime() > invalidTimeMeeting.getEndTime());
  }

  // ===== Date Validation Tests =====

  // Test Case 10: Invalid Date
  // Verifies that invalid dates are handled correctly
  @Test
  public void testInvalidDate() {
    Meeting invalidDateMeeting = new Meeting(2, 35, 10, 12);
    assertEquals("Month should be 2", 2, invalidDateMeeting.getMonth());
    assertEquals("Day should be 35", 35, invalidDateMeeting.getDay());
  }

  // ===== String Representation Tests =====

  // Test Case 11: toString with All Fields
  // Verifies that toString method formats meeting information correctly
  @Test
public void testToStringWithNullValues() {
  Meeting nullMeeting = new Meeting(5, 15, 9, 11, new ArrayList<>(), null, null);
  try {
    String result = nullMeeting.toString();
    String expected = "5/15, 9 - 11,null: null\nAttending: ";
    assertEquals("toString should handle null values", expected, result);
  } catch (NullPointerException e) {
    fail("toString() threw NullPointerException when handling null values: " + e.getMessage());
  }
}



  // Test Case 12: Boundary Conditions for Time
  // Verifies that meetings can be scheduled at the earliest and latest possible times
  @Test
  public void testTimeBoundaries() {
    Meeting earlyMeeting = new Meeting(5, 15, 0, 1);
    assertEquals("Earliest start time should be 0", 0, earlyMeeting.getStartTime());
    
    Meeting lateMeeting = new Meeting(5, 15, 22, 23);
    assertEquals("Latest end time should be 23", 23, lateMeeting.getEndTime());
    
    Meeting fullDayMeeting = new Meeting(5, 15, 0, 23);
    assertEquals("Full day start time should be 0", 0, fullDayMeeting.getStartTime());
    assertEquals("Full day end time should be 23", 23, fullDayMeeting.getEndTime());
  }

  // Test Case 13: Meeting Equality
  // Verifies that two meetings with the same properties are considered equal
  @Test
  public void testMeetingEquality() {
    ArrayList<Person> attendees1 = new ArrayList<>();
    ArrayList<Person> attendees2 = new ArrayList<>();
    attendees1.add(person1);
    attendees2.add(person1);
    
    Meeting meeting1 = new Meeting(5, 15, 9, 11, attendees1, room, "Team Meeting");
    Meeting meeting2 = new Meeting(5, 15, 9, 11, attendees2, room, "Team Meeting");
    
    assertEquals("Meetings with same properties should have same string representation", 
                meeting1.toString(), meeting2.toString());
  }

  // Test Case 14: Empty Meeting
  // Verifies that a meeting can be created with minimal parameters
  @Test
  public void testEmptyMeeting() {
      Meeting emptyMeeting = new Meeting(5, 15, 9, 11);
      assertNotNull("Empty meeting should not be null", emptyMeeting);
      
      if (emptyMeeting.getAttendees() != null) {
          assertEquals("Empty meeting should have no attendees", 0, emptyMeeting.getAttendees().size());
      } else {
          fail("Attendees list should be initialized");
      }
      
      assertNull("Empty meeting should have no room", emptyMeeting.getRoom());
      assertNull("Empty meeting should have no description", emptyMeeting.getDescription());
  }

  
  // Test Case 15: Meeting Description Update
  // Verifies that meeting description can be updated correctly
  @Test
  public void testDescriptionUpdate() {
    String initialDesc = "Initial Meeting";
    String updatedDesc = "Updated Meeting";
    
    Meeting meeting = new Meeting(5, 15, 9, 11, new ArrayList<>(), room, initialDesc);
    assertEquals("Initial description should match", initialDesc, meeting.getDescription());
    
    meeting.setDescription(updatedDesc);
    assertEquals("Updated description should match", updatedDesc, meeting.getDescription());
    
    // Test setting description to null
    meeting.setDescription(null);
    assertNull("Description should be null after setting to null", meeting.getDescription());
  }
}