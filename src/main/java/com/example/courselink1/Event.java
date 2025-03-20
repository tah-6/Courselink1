package com.example.courselink1;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String eventCode;
    private String eventName;
    private String description;
    private String location;
    private String dateTime;
    private int capacity;
    private int currentRegistrations = 0;
    private String cost;
    private List<String> registeredStudents = new ArrayList<>();
    private String headerImage; // Placeholder for header image

    public Event(String eventCode, String eventName, String description, String location, String dateTime, int capacity, String cost, String organizer) {
        this.eventCode = eventCode;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.capacity = capacity;
        this.cost = cost;
        this.headerImage = "default.jpg"; // Default image
    }

    // Getter and Setter methods for all attributes

    public String getEventCode() {
        return eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentRegistrations() {
        return currentRegistrations;
    }

    public String getCost() {
        return cost;
    }

    public List<String> getRegisteredStudents() {
        return registeredStudents;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void registerStudent(String studentName) {
        if (currentRegistrations < capacity) {
            registeredStudents.add(studentName);
            currentRegistrations++;
        }
    }

    public void updateEventDetails(String eventName, String description, String location, String dateTime, int capacity, String cost) {
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.capacity = capacity;
        this.cost = cost;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }
}
