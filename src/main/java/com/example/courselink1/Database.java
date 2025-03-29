package com.example.courselink1;

import java.util.HashMap;
import java.util.Map;

class Database {
    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> roles = new HashMap<>();
    private static final Map<String, Event> events = new HashMap<>();

    Database() {}

    public static boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public static String getUserRole(String username) {
        return roles.get(username);
    }

    public static void addUser(String username, String password, String role) {
        users.put(username, password);
        roles.put(username, role);
    }

    public static void removeUser(String username) {
        users.remove(username);
        roles.remove(username);
    }

    public static void addEvent(Event event) {
        events.put(event.getEventCode(), event);
    }

    public static void deleteEvent(String eventCode) {
        events.remove(eventCode);
    }

    public static Event getEvent(String eventCode) {
        return events.get(eventCode);
    }

    public static Map<String, Event> getAllEvents() {
        return events;
    }

    public static void registerStudentForEvent(String eventCode, String studentName) {
        Event event = events.get(eventCode);
        if (event != null) {
            event.registerStudent(studentName);
        }
    }

    public static boolean isAdmin(String username) {
        return "Admin".equalsIgnoreCase(getUserRole(username));
    }

    public static boolean isFaculty(String username) {
        return "Faculty".equalsIgnoreCase(getUserRole(username));
    }

    static {
        // Load users from CSV file using CSVLoader
        CSVLoader.loadUsersWithRoles();
        users.putAll(CSVLoader.users);
        roles.putAll(CSVLoader.roles);

        // Sample event
        events.put("EV001", new Event("EV001", "Tech Seminar", "Learn about the latest tech trends", "Room 101", "2025-05-10 10:00", 50, "Free", "Admin"));
    }
}
