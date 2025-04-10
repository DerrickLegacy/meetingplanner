package com.meetingplanner.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class RoomTest {
    private Room room;

    @BeforeEach
    void setUp() {
        // Initialize a new room before each test
        room = new Room("R001", "Conference Room A", 20);
    }

    // Test case 1: Test room initialization
    @Test
    void testRoomInitialization() {
        assertEquals("R001", room.getRoomId());
        assertEquals("Conference Room A", room.getName());
        assertEquals(20, room.getCapacity());
        assertTrue(room.isAvailable());
        assertTrue(room.getEquipment().isEmpty());
    }

    // Test case 2: Test equipment management
    @Test
    void testEquipmentManagement() {
        // Test adding equipment
        room.addEquipment("Projector");
        room.addEquipment("Whiteboard");
        assertTrue(room.hasEquipment("Projector"));
        assertTrue(room.hasEquipment("Whiteboard"));
        
        // Test adding duplicate equipment
        room.addEquipment("Projector");
        assertEquals(2, room.getEquipment().size());
        
        // Test removing equipment
        room.removeEquipment("Projector");
        assertFalse(room.hasEquipment("Projector"));
        assertTrue(room.hasEquipment("Whiteboard"));
    }

    // Test case 3: Test availability status
    @Test
    void testAvailabilityStatus() {
        assertTrue(room.isAvailable());
        room.setAvailable(false);
        assertFalse(room.isAvailable());
        room.setAvailable(true);
        assertTrue(room.isAvailable());
    }

    // Test case 4: Test equipment list immutability
    @Test
    void testEquipmentListImmutability() {
        room.addEquipment("Projector");
        List<String> equipment = room.getEquipment();
        equipment.add("Whiteboard"); // This should not affect the original list
        assertFalse(room.hasEquipment("Whiteboard"));
    }

    // Test case 5: Test room capacity
    @Test
    void testRoomCapacity() {
        assertEquals(20, room.getCapacity());
        // Test with different capacity
        Room smallRoom = new Room("R002", "Small Room", 5);
        assertEquals(5, smallRoom.getCapacity());
    }

    // Test case 6: Test room identification
    @Test
    void testRoomIdentification() {
        assertEquals("R001", room.getRoomId());
        assertEquals("Conference Room A", room.getName());
        
        // Test with different room
        Room otherRoom = new Room("R002", "Conference Room B", 15);
        assertEquals("R002", otherRoom.getRoomId());
        assertEquals("Conference Room B", otherRoom.getName());
    }

    // Test case 7: Test multiple equipment operations
    @Test
    void testMultipleEquipmentOperations() {
        // Add multiple equipment items
        room.addEquipment("Projector");
        room.addEquipment("Whiteboard");
        room.addEquipment("Video Conference");
        
        // Verify all equipment is present
        assertTrue(room.hasEquipment("Projector"));
        assertTrue(room.hasEquipment("Whiteboard"));
        assertTrue(room.hasEquipment("Video Conference"));
        
        // Remove all equipment
        room.removeEquipment("Projector");
        room.removeEquipment("Whiteboard");
        room.removeEquipment("Video Conference");
        
        // Verify all equipment is removed
        assertTrue(room.getEquipment().isEmpty());
    }
} 