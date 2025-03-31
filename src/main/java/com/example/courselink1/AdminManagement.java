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
        Button assignStudentButton = new Button("Assign Student to Course");
        Button viewAssignmentsButton = new Button("View Course Assignments");
        addCourseButton.setOnAction(e -> CSVLoader.addCourse());
        viewCoursesButton.setOnAction(e -> viewCourses(contentArea));
        assignStudentButton.setOnAction(e -> showCourseAssignmentDialog(contentArea));
        viewAssignmentsButton.setOnAction(e -> viewCourseAssignments(contentArea));
        contentArea.getChildren().addAll(label, addCourseButton, viewCoursesButton, assignStudentButton,
                viewAssignmentsButton);
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
        addStudentButton.setOnAction(e -> CSVLoader.addStudent());
        viewStudentsButton.setOnAction(e -> viewStudents(contentArea));
        contentArea.getChildren().addAll(label, addStudentButton, viewStudentsButton);
    }

    public static void showFacultyManagement(VBox contentArea) {
        contentArea.getChildren().clear();
        Label label = new Label("Student Management");
        Button addStudentButton = new Button("Add Faculty");
        Button viewStudentsButton = new Button("View Faculty");
        addStudentButton.setOnAction(e -> CSVLoader.addFaculty());
        viewStudentsButton.setOnAction(e -> viewFaculty(contentArea));
        contentArea.getChildren().addAll(label, addStudentButton, viewStudentsButton);
    }
    public static void viewFaculty(VBox contentArea) {
        contentArea.getChildren().clear();
        TableView<String> table = new TableView<>();
        TableColumn<String, String> column = new TableColumn<>("Faculty");
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(CSVLoader.loadFaculty());
        contentArea.getChildren().add(table);
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

    private static void showCourseAssignmentDialog(VBox contentArea) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Assign Student to Course");
        dialog.setHeaderText("Select Student and Course");

        // Create the dialog content
        VBox dialogContent = new VBox(10);
        ComboBox<String> studentCombo = new ComboBox<>();
        ComboBox<String> courseCombo = new ComboBox<>();

        // Load students and courses
        studentCombo.setItems(CSVLoader.loadStudents());
        courseCombo.setItems(CSVLoader.loadCourses());

        dialogContent.getChildren().addAll(
                new Label("Select Student:"),
                studentCombo,
                new Label("Select Course:"),
                courseCombo);

        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle the result
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String selectedStudent = studentCombo.getValue();
                String selectedCourse = courseCombo.getValue();
                if (selectedStudent != null && selectedCourse != null) {
                    CSVLoader.assignStudentToCourse(selectedStudent, selectedCourse);
                    showAlert("Success", "Student assigned to course successfully!");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    public static void viewCourseAssignments(VBox contentArea) {
        contentArea.getChildren().clear();
        TableView<String> table = new TableView<>();
        TableColumn<String, String> column = new TableColumn<>("Course Assignments");
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(CSVLoader.loadCourseAssignments());
        contentArea.getChildren().add(table);
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
