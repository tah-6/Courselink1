package com.example.courselink1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.nio.charset.StandardCharsets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AdminManagement {

    private static String promptForInput(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input");
        dialog.setHeaderText(message);
        dialog.setContentText("");
        return dialog.showAndWait().orElse("");

    }
        public static ObservableList<String> loadEvents() {
            ObservableList<String> events = FXCollections.observableArrayList();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Events .csv")), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    events.add(line.trim());
                }
            } catch (NullPointerException e) {
                System.out.println("Error: Events file not found.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return events;
        }



    public static void showSubjectManagement(VBox contentArea) {
        contentArea.getChildren().clear();
        Label label = new Label("Subject Management");
        Button addSubjectButton = new Button("Add Subject");
        Button viewSubjectsButton = new Button("View Subjects");
        addSubjectButton.setOnAction(e -> CSVLoader.addSubject(promptForInput("Enter Subject Name:")));
        viewSubjectsButton.setOnAction(e -> viewSubjects(contentArea));
        contentArea.getChildren().addAll(label, addSubjectButton, viewSubjectsButton);
    }

    public static void viewSubjects(VBox contentArea) {
        contentArea.getChildren().clear();
        TableView<String> table = new TableView<>();
        TableColumn<String, String> column = new TableColumn<>("Subjects");
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(CSVLoader.loadSubjects());
        contentArea.getChildren().add(table);
    }

    public static void showCourseManagement(VBox contentArea) {
        contentArea.getChildren().clear();
        Label label = new Label("Course Management");
        Button addCourseButton = new Button("Add Course");
        Button viewCoursesButton = new Button("View Courses");
        addCourseButton.setOnAction(e -> CSVLoader.addCourse(promptForInput("Enter Course Name:")));
        viewCoursesButton.setOnAction(e -> viewCourses(contentArea));
        contentArea.getChildren().addAll(label, addCourseButton, viewCoursesButton);
    }

    public static void viewCourses(VBox contentArea) {
        contentArea.getChildren().clear();
        TableView<String> table = new TableView<>();
        TableColumn<String, String> column = new TableColumn<>("Courses");
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(CSVLoader.loadCourses());
        contentArea.getChildren().add(table);
    }

    public static void showStudentManagement(VBox contentArea) {
        contentArea.getChildren().clear();
        Label label = new Label("Student Management");
        Button addStudentButton = new Button("Add Student");
        Button viewStudentsButton = new Button("View Students");
        addStudentButton.setOnAction(e -> CSVLoader.addStudent(promptForInput("Enter Student Name:")));
        viewStudentsButton.setOnAction(e -> viewStudents(contentArea));
        contentArea.getChildren().addAll(label, addStudentButton, viewStudentsButton);
    }

    public static void viewStudents(VBox contentArea) {
        contentArea.getChildren().clear();
        TableView<String> table = new TableView<>();
        TableColumn<String, String> column = new TableColumn<>("Students");
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(CSVLoader.loadStudents());
        contentArea.getChildren().add(table);
    }

    public static void showEventManagement(VBox contentArea) {
        contentArea.getChildren().clear();
        Label label = new Label("Event Management");
        Button addEventButton = new Button("Add Event");
        Button viewEventsButton = new Button("View Events");
        addEventButton.setOnAction(e -> CSVLoader.addEvent(promptForInput("Enter Event Name:")));
        viewEventsButton.setOnAction(e -> viewEvents(contentArea));
        contentArea.getChildren().addAll(label, addEventButton, viewEventsButton);
    }

    public static void viewEvents(VBox contentArea) {
        contentArea.getChildren().clear();
        TableView<String> table = new TableView<>();
        TableColumn<String, String> column = new TableColumn<>("Events");
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(CSVLoader.loadEvents());
        contentArea.getChildren().add(table);
    }
}
