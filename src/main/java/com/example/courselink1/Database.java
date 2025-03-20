package com.example.courselink1;

import java.util.HashMap;
import java.util.Map;

class Database {
    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> roles = new HashMap<>();
    private static final Map<String, Event> events = new HashMap<>(); // Store events

    Database() {}

    public static boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public static String getUserRole(String username) {
        return roles.get(username);
    }

    public static void addEvent(Event event) {
        events.put(event.getEventCode(), event); // Add event
    }

    public static void deleteEvent(String eventCode) {
        events.remove(eventCode); // Delete event
    }

    public static Event getEvent(String eventCode) {
        return events.get(eventCode); // Retrieve event by code
    }

    public static Map<String, Event> getAllEvents() {
        return events; // Get all events
    }

    public static void registerStudentForEvent(String eventCode, String studentName) {
        Event event = events.get(eventCode);
        if (event != null) {
            event.registerStudent(studentName);
        }
    }

    static {
        // Sample users
        users.put("admin", "admin123");
        roles.put("admin", "Admin");
        users.put("student1", "password1");
        roles.put("student1", "Student");
        users.put("faculty1", "password2");
        roles.put("faculty1", "Faculty");

        // Sample event
        events.put("EV001", new Event("EV001", "Tech Seminar", "Learn about the latest tech trends", "Room 101", "2025-05-10 10:00", 50, "Free", "Admin"));
    }
}

