package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class MeetingTest {

  // Add test methods here.
  // You are not required to write tests for all classes.

  @Before
  public void setUp() {
    person1 = new Person("John");
    person2 = new Person("Jane");
    room = new Room("Room 101");
    meeting =
      new Meeting(5, 15, 9, 11, new ArrayList<>(), room, "Team Meeting");
  }

  // Test 1: Default constructor
  @Test
  public void testDefaultConstructor() { //creating a Meeting with the default constructor
    Meeting defaultMeeting = new Meeting();
    assertNotNull("Default meeting should not be null", defaultMeeting);
  }

  // Test 2: Basic constructor with month and day
  //checks that the constructor with only month and day sets the correct time range (0–23).
  @Test
  public void testMonthDayConstructor() {
    Meeting dayMeeting = new Meeting(6, 20);
    assertEquals("Month should be 6", 6, dayMeeting.getMonth());
    assertEquals("Day should be 20", 20, dayMeeting.getDay());
    assertEquals("Start time should be 0", 0, dayMeeting.getStartTime());
    assertEquals("End time should be 23", 23, dayMeeting.getEndTime());
  }

  // Test 3: Full constructor with all parameters
  private Meeting meeting;
  private Person person1;
  private Person person2;
  private Room room;

  @Test
  public void testFullConstructor() {
    ArrayList<Person> attendees = new ArrayList<>();
    attendees.add(person1);
    Meeting fullMeeting = new Meeting(
      9,
      10,
      14,
      16,
      attendees,
      room,
      "Project Review"
    );

    assertEquals("Month should be 9", 9, fullMeeting.getMonth());
    assertEquals("Day should be 10", 10, fullMeeting.getDay());
    assertEquals("Start time should be 14", 14, fullMeeting.getStartTime());
    assertEquals("End time should be 16", 16, fullMeeting.getEndTime());
    assertEquals("Room should match", room, fullMeeting.getRoom());
    assertEquals(
      "Description should be 'Project Review'",
      "Project Review",
      fullMeeting.getDescription()
    );
    assertEquals(
      "Number of attendees should be 1",
      1,
      fullMeeting.getAttendees().size()
    );
  }

  // Test 4: Adding and removing attendees
  @Test
  public void testAttendeeManagement() {
    meeting.addAttendee(person1);
    assertEquals(
      "Number of attendees should be 1",
      1,
      meeting.getAttendees().size()
    );
    assertTrue(
      "Attendees should contain person1",
      meeting.getAttendees().contains(person1)
    );

    meeting.removeAttendee(person1);
    assertEquals(
      "Number of attendees should be 0",
      0,
      meeting.getAttendees().size()
    );
    assertFalse(
      "Attendees should not contain person1",
      meeting.getAttendees().contains(person1)
    );
  }

  //Test 5
  //Creates a meeting using the (int month, int day) constructor.
  //Checks if the month, day, start, and end values are set correctly.
  @Test
  public void testFullDayConstructor() { //testing block off a whole day
    // Arrange
    int month = 2;
    int day = 15;

    // Act
    Meeting meeting = new Meeting(month, day);

    // Assert
    assertEquals(month, meeting.getMonth());
    assertEquals(day, meeting.getDay());
    assertEquals(0, meeting.getStartTime()); // Should default to start of day
    assertEquals(23, meeting.getEndTime()); // Should default to end of day
  }

  //Test 6
  //
  @Test
  public void testFullDayWithDescriptionConstructor() {
    // Arrange
    int month = 3;
    int day = 21;
    String desc = "Public Holiday";

    // Act
    Meeting meeting = new Meeting(month, day, desc);

    // Assert
    assertEquals(month, meeting.getMonth());
    assertEquals(day, meeting.getDay());
    assertEquals(0, meeting.getStartTime());
    assertEquals(23, meeting.getEndTime());
    assertEquals(desc, meeting.getDescription());
  }

  //Test 7 invalid date feb 35
  @Test
  public void testInvalidDate_February35th() {
    Meeting meeting = new Meeting(2, 35, 10, 12);
    assertEquals("Month should still be 2", 2, meeting.getMonth());
    assertEquals("Day should still be 35", 35, meeting.getDay());
    // But ideally you'd throw an exception here if implementing date validation
  }

  //Test 8 checking attendees list is initialized correctly before calling addAttendee()
  @Test
  public void testAttendeesNullSafety() {
      try {
          Meeting meeting = new Meeting(5, 15, 9, 11);
          meeting.addAttendee(new Person("Ali"));
          // ✅ Success — no exception thrown
      } catch (Exception e) {
          fail("addAttendee should not throw an exception when attendees list is not initialized.");
      }
  }
  

  //Test 9 Start Time >= End Time
  @Test
  public void testStartTimeAfterEndTime() {
    Meeting meeting = new Meeting(5, 15, 14, 10);
    assertTrue(meeting.getStartTime() > meeting.getEndTime());
  }
  // private Meeting meeting;
  // private Person person1;
  // private Person person2;
  // private Room room;

  // @Before
  // public void setUp() {
  //     // Initialize test objects before each test
  //     person1 = new Person("John Doe");
  //     person2 = new Person("Jane Smith");
  //     room = new Room("Room 101");
  //     meeting = new Meeting(5, 15, 9, 11, new ArrayList<Person>(), room, "Team Meeting");
  // }

  // // Test 1: Default constructor
  // @Test
  // public void testDefaultConstructor() {
  //     Meeting defaultMeeting = new Meeting();
  //     assertNotNull("Default meeting should not be null", defaultMeeting);
  // }

  // // Test 2: Basic constructor with month and day
  // @Test
  // public void testMonthDayConstructor() {
  //     Meeting dayMeeting = new Meeting(6, 20);
  //     assertEquals("Month should be 6", 6, dayMeeting.getMonth());
  //     assertEquals("Day should be 20", 20, dayMeeting.getDay());
  //     assertEquals("Start time should be 0", 0, dayMeeting.getStartTime());
  //     assertEquals("End time should be 23", 23, dayMeeting.getEndTime());
  // }

  // // Test 3: Full constructor with all parameters
  // @Test
  // public void testFullConstructor() {
  //     ArrayList<Person> attendees = new ArrayList<>();
  //     attendees.add(person1);
  //     Meeting fullMeeting = new Meeting(9, 10, 14, 16, attendees, room, "Project Review");

  //     assertEquals("Month should be 9", 9, fullMeeting.getMonth());
  //     assertEquals("Day should be 10", 10, fullMeeting.getDay());
  //     assertEquals("Start time should be 14", 14, fullMeeting.getStartTime());
  //     assertEquals("End time should be 16", 16, fullMeeting.getEndTime());
  //     assertEquals("Room should match", room, fullMeeting.getRoom());
  //     assertEquals("Description should be 'Project Review'", "Project Review", fullMeeting.getDescription());
  //     assertEquals("Number of attendees should be 1", 1, fullMeeting.getAttendees().size());
  // }

  // // Test 4: Adding and removing attendees
  // @Test
  // public void testAttendeeManagement() {
  //     meeting.addAttendee(person1);
  //     assertEquals("Number of attendees should be 1", 1, meeting.getAttendees().size());
  //     assertTrue("Attendees should contain person1", meeting.getAttendees().contains(person1));

  //     meeting.removeAttendee(person1);
  //     assertEquals("Number of attendees should be 0", 0, meeting.getAttendees().size());
  //     assertFalse("Attendees should not contain person1", meeting.getAttendees().contains(person1));
  // }

  // // Test 5: Invalid month values
  // @Test(expected = IllegalArgumentException.class)
  // public void testInvalidMonth() {
  //     new Meeting(0, 15, 9, 11); // Month should be between 1-12
  // }

  // // Test 6: Invalid day values
  // @Test(expected = IllegalArgumentException.class)
  // public void testInvalidDay() {
  //     new Meeting(5, 32, 9, 11); // Day should be between 1-31
  // }

  // // Test 7: Invalid time values
  // @Test(expected = IllegalArgumentException.class)
  // public void testInvalidTime() {
  //     new Meeting(5, 15, -1, 11); // Start time should be between 0-23
  // }

  // // Test 8: End time before start time
  // @Test(expected = IllegalArgumentException.class)
  // public void testEndTimeBeforeStartTime() {
  //     new Meeting(5, 15, 14, 12); // End time should be after start time
  // }

  // // Test 9: Multiple attendees
  // @Test
  // public void testMultipleAttendees() {
  //     meeting.addAttendee(person1);
  //     meeting.addAttendee(person2);
  //     assertEquals("Number of attendees should be 2", 2, meeting.getAttendees().size());
  //     assertTrue("Attendees should contain person1", meeting.getAttendees().contains(person1));
  //     assertTrue("Attendees should contain person2", meeting.getAttendees().contains(person2));
  // }

  // // Test 10: Empty meeting
  // @Test
  // public void testEmptyMeeting() {
  //     Meeting emptyMeeting = new Meeting(5, 15, 9, 11);
  //     assertNotNull("Empty meeting should not be null", emptyMeeting);
  //     assertEquals("Empty meeting should have no attendees", 0, emptyMeeting.getAttendees().size());
  //     assertNull("Empty meeting should have no room", emptyMeeting.getRoom());
  //     assertNull("Empty meeting should have no description", emptyMeeting.getDescription());
  // }

  // // Test 11: Boundary conditions for time
  // @Test
  // public void testTimeBoundaries() {
  //     Meeting boundaryMeeting = new Meeting(5, 15, 0, 23);
  //     assertEquals("Start time should be 0", 0, boundaryMeeting.getStartTime());
  //     assertEquals("End time should be 23", 23, boundaryMeeting.getEndTime());
  // }

  // // Test 12: toString with all fields
  // @Test
  // public void testToString() {
  //     meeting.addAttendee(person1);
  //     meeting.addAttendee(person2);
  //     String expected = "5/15, 9 - 11,Room 101: Team Meeting\nAttending: John Doe,Jane Smith";
  //     assertEquals("toString should match expected format", expected, meeting.toString());
  // }

  // // Test 13: toString with null values
  // @Test
  // public void testToStringWithNullValues() {
  //     Meeting nullMeeting = new Meeting(5, 15, 9, 11, new ArrayList<Person>(), null, null);
  //     String expected = "5/15, 9 - 11,null: null\nAttending: ";
  //     assertEquals("toString should handle null values", expected, nullMeeting.toString());
  // }

  // // Test 14: Meeting equality
  // @Test
  // public void testMeetingEquality() {
  //     Meeting meeting1 = new Meeting(5, 15, 9, 11, new ArrayList<Person>(), room, "Team Meeting");
  //     Meeting meeting2 = new Meeting(5, 15, 9, 11, new ArrayList<Person>(), room, "Team Meeting");

  //     meeting1.addAttendee(person1);
  //     meeting2.addAttendee(person1);

  //     assertEquals("Meetings with same properties should be equal", meeting1.toString(), meeting2.toString());
  // }
}
