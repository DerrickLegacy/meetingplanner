package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class OrganizationTest {

    @Test
    public void testConstructor() {
        Organization org = new Organization();

        // Test employees initialization
        assertEquals(5, org.getEmployees().size());
        assertEquals("Namugga Martha", org.getEmployees().get(0).getName());

        // Test rooms initialization
        assertEquals(5, org.getRooms().size());
        assertEquals("LLT6A", org.getRooms().get(0).getID());
    }

    @Test
    public void testGetEmployee_Valid() throws Exception {
        Organization org = new Organization();
        Person person = org.getEmployee("Acan Brenda");
        assertEquals("Acan Brenda", person.getName());
    }

    @Test(expected = Exception.class)
    public void testGetEmployee_Invalid() throws Exception {
        Organization org = new Organization();
        org.getEmployee("Nonexistent Person");
    }

    @Test
    public void testGetEmployees() {
        Organization org = new Organization();
        ArrayList<Person> employees = org.getEmployees();
        assertEquals(5, employees.size());
        assertSame(employees, org.getEmployees());
    }

    @Test
    public void testGetRoom_Valid() throws Exception {
        Organization org = new Organization();
        Room room = org.getRoom("LLT6B");
        assertEquals("LLT6B", room.getID());
    }

    @Test(expected = Exception.class)
    public void testGetRoom_Invalid() throws Exception {
        Organization org = new Organization();
        org.getRoom("INVALID_ROOM");
    }

    @Test
    public void testGetRooms() {
        Organization org = new Organization();
        ArrayList<Room> rooms = org.getRooms();
        assertEquals(5, rooms.size());
        assertSame(rooms, org.getRooms());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGetEmployee_Null() throws Exception {
        Organization org = new Organization();
        org.getEmployee(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoom_Null() throws Exception {
        Organization org = new Organization();
        org.getRoom(null);
    }

    @Test(expected = Exception.class)
    public void testGetEmployee_EmptyString() throws Exception {
        Organization org = new Organization();
        org.getEmployee("");
    }

    @Test(expected = Exception.class)
    public void testGetRoom_EmptyString() throws Exception {
        Organization org = new Organization();
        org.getRoom("");
    }

    @Test
    public void testGetEmployee_CaseSensitivity() throws Exception {
        Organization org = new Organization();
        // Assuming case-sensitive matching
        try {
            org.getEmployee("acan brenda"); // Lowercase version
            fail("Expected exception for case mismatch");
        } catch (Exception e) {
            assertEquals("Requested employee does not exist", e.getMessage());
        }
    }

    @Test
    public void testDuplicateEmployees() {
        Organization org = new Organization();
        // Add duplicate employee
        org.getEmployees().add(new Person("Acan Brenda"));
        assertEquals(6, org.getEmployees().size());
        // Should return first occurrence
        try {
            Person p = org.getEmployee("Acan Brenda");
            assertEquals("Acan Brenda", p.getName());
        } catch (Exception e) {
            fail("Should find duplicate employee");
        }
    }
}