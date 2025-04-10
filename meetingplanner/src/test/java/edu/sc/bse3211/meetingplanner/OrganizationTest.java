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


    //Test if the room is valid
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
    //Check if employee exists
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
        // Check for immutability
        assertSame(employees, org.getEmployees());
    }

    @Test
    public void testGetRooms() {
        Organization org = new Organization();
        ArrayList<Room> rooms = org.getRooms();
        assertEquals(5, rooms.size());
        // Check for  immutability
        assertSame(rooms, org.getRooms());
    }

}
