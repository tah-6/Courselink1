package com.example.courselink1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class CSVLoader {
    public static Map<String, String> users = new HashMap<>();
    public static ObservableList<String> courses = FXCollections.observableArrayList();


    public static void loadStudents() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Students .csv"))))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    String studentID = data[0];
                    String password = data[1];
                    users.put(studentID, password);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadCourses() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Courses.csv"))))) {
            String line;
            while ((line = br.readLine()) != null) {
                courses.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

