package com.example.courselink1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class CSVLoader {
    public static Map<String, String> users = new HashMap<>();
    public static Map<String, String> roles = new HashMap<>();
    public static Map<String, ObservableList<String>> studentCourses = new HashMap<>();

    public static void loadUsersWithRoles() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Students.csv")), StandardCharsets.UTF_8))) {
            String line;
            boolean firstRow = true;
            while ((line = br.readLine()) != null) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 12) {
                    String username = data[0].trim();
                    String password = data[11].trim();
                    String role;
                    if (username.toLowerCase().contains("admin")) {
                        role = "Admin";
                    } else if (username.toLowerCase().contains("faculty")) {
                        role = "Faculty";
                    } else {
                        role = "Student";
                        studentCourses.put(username, FXCollections.observableArrayList());
                    }
                    users.put(username, password);
                    roles.put(username, role);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> loadSubjects() {
        ObservableList<String> subjects = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Subjects.csv")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                subjects.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public static ObservableList<String> loadCourses() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Courses.csv")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                courses.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static ObservableList<String> loadStudents() {
        ObservableList<String> students = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Students.csv")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                students.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public static ObservableList<String> loadEvents() {
        ObservableList<String> events = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Events.csv")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                events.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    public static void addSubject(String subject) {
        try (PrintWriter out = new PrintWriter(new FileWriter("./Subjects.csv", true))) {
            out.println(subject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addCourse(String course) {
        try (PrintWriter out = new PrintWriter(new FileWriter("./Courses.csv", true))) {
            out.println(course);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addStudent(String student) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter("Students.csv", true))) {
            out.write(student);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addEvent(String event) {
        try (PrintWriter out = new PrintWriter(new FileWriter("./Events.csv", true))) {
            out.println(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ObservableList<String> loadCoursesForStudent(String username) {
        ObservableList<String> studentSpecificCourses = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Courses.csv")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 1 && data[0].equalsIgnoreCase(username)) {
                    studentSpecificCourses.add(data[1].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentSpecificCourses;
    }

}
