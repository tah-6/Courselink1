package com.example.courselink1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {
    public static Map<String, String> loadUsers(String fileName) {
        Map<String, String> users = new HashMap<>();
        try (InputStream inputStream = CSVReader.class.getResourceAsStream("/" + fileName)) {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 2) {
                        users.put(data[0].trim(), data[1].trim()); // Username, Password
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static ObservableList<String> loadCourses(String fileName) {
        ObservableList<String> courses = FXCollections.observableArrayList();
        try (InputStream inputStream = CSVReader.class.getResourceAsStream("/" + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                courses.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
