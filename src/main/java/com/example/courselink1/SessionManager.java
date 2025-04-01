package com.example.courselink1;

public class SessionManager {
    public static String currentUser;
    public static String currentRole;

    public static void setCurrentUser(String username) {
        currentUser = username;
        currentRole = CSVLoader.roles.get(username);
    }
    public static String setCurrentRole(String role) {
        currentRole = role;
        return currentRole;
    }


    public static String getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

    public static void clearSession() {
        currentUser = null;
        currentRole = null;
    }
}