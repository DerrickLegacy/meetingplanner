package com.meetingplanner.model;

import java.util.List;
import java.util.ArrayList;

public class Room {
    private String roomId;
    private String name;
    private int capacity;
    private List<String> equipment;
    private boolean isAvailable;

    public Room(String roomId, String name, int capacity) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.equipment = new ArrayList<>();
        this.isAvailable = true;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getEquipment() {
        return new ArrayList<>(equipment);
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void addEquipment(String equipmentItem) {
        if (!equipment.contains(equipmentItem)) {
            equipment.add(equipmentItem);
        }
    }

    public void removeEquipment(String equipmentItem) {
        equipment.remove(equipmentItem);
    }

    public boolean hasEquipment(String equipmentItem) {
        return equipment.contains(equipmentItem);
    }
} 