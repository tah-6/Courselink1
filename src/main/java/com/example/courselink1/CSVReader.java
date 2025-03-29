package com.example.courselink1;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class CSVReader {
    public static Map<String, String> loadUsers(String fileName) {
        Map<String, String> users = new HashMap<>();
        try (InputStream inputStream = CSVReader.class.getResourceAsStream("/" + fileName)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                        String studentID = data[0].trim();
                        String password = data[11].trim();
                        users.put(studentID, password);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static Map<String, String> loadRoles(String fileName) {
        Map<String, String> roles = new HashMap<>();
        try (InputStream inputStream = CSVReader.class.getResourceAsStream("/" + fileName)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 12) {
                        String studentID = data[0].trim();
                        String role = data[2].trim();
                        roles.put(studentID, role);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roles;
    }
}
