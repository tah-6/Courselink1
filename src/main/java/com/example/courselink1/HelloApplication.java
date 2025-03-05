package com.example.courselink1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloApplication extends Application {
    private Stage primaryStage;
    private List<String> courses = new ArrayList<>();
    private List<String> students = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    private void showLoginScreen() {
        VBox loginLayout = new VBox(15);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-background-color: #121212;");

        Label titleLabel = new Label("Welcome");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");

        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setStyle("-fx-pref-width: 250px; -fx-background-color: #2C2C2C; -fx-text-fill: white;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle("-fx-pref-width: 250px; -fx-background-color: #2C2C2C; -fx-text-fill: white;");

        Button loginButton = new Button("Log in");
        loginButton.setStyle("-fx-background-color: #1877F2; -fx-text-fill: white; -fx-font-size: 14px;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (Database.authenticate(username, password)) {
                showDashboard(Database.getUserRole(username));
            } else {
                messageLabel.setText("Invalid credentials!");
            }
        });


        loginLayout.getChildren().addAll(titleLabel, userField, passField, loginButton, messageLabel);
        primaryStage.setScene(new Scene(loginLayout, 350, 400));
        primaryStage.setTitle("University Management System");
        primaryStage.show();
    }

    private void showDashboard(String role) {
        VBox dashboardLayout = new VBox(10);
        dashboardLayout.setAlignment(Pos.CENTER);
        dashboardLayout.setStyle("-fx-background-color: #121212;");

        Label welcomeLabel = new Label("Welcome, " + role);
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        Button manageCoursesButton = new Button("Manage Courses");
        manageCoursesButton.setOnAction(e -> showCourseManagement(role));

        Button manageStudentsButton = new Button("Manage Students");
        manageStudentsButton.setOnAction(e -> showStudentManagement(role));

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> showLoginScreen());

        dashboardLayout.getChildren().addAll(welcomeLabel, manageCoursesButton, manageStudentsButton, logoutButton);
        primaryStage.setScene(new Scene(dashboardLayout, 350, 400));
    }

    private void showCourseManagement(String role) {
        VBox courseLayout = new VBox(10);
        Label courseLabel = new Label("Course Management");
        ListView<String> courseList = new ListView<>();
        courseList.getItems().addAll(courses);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showDashboard(role));

        if (role.equals("Admin")) {
            TextField courseField = new TextField();
            Button addCourseButton = new Button("Add Course");
            addCourseButton.setOnAction(e -> {
                String newCourse = courseField.getText();
                if (!newCourse.isEmpty()) {
                    courses.add(newCourse);
                    courseList.getItems().add(newCourse);
                    courseField.clear();
                }
            });
            courseLayout.getChildren().addAll(courseLabel, courseField, addCourseButton, courseList, backButton);
        } else {
            courseLayout.getChildren().addAll(courseLabel, courseList, backButton);
        }
        primaryStage.setScene(new Scene(courseLayout, 400, 300));
    }

    private void showStudentManagement(String role) {
        VBox studentLayout = new VBox(10);
        Label studentLabel = new Label("Student Management");
        ListView<String> studentList = new ListView<>();
        studentList.getItems().addAll(students);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showDashboard(role));

        if (role.equals("Admin")) {
            TextField studentField = new TextField();
            Button addStudentButton = new Button("Add Student");
            addStudentButton.setOnAction(e -> {
                String newStudent = studentField.getText();
                if (!newStudent.isEmpty()) {
                    students.add(newStudent);
                    studentList.getItems().add(newStudent);
                    studentField.clear();
                }
            });
            studentLayout.getChildren().addAll(studentLabel, studentField, addStudentButton, studentList, backButton);
        } else {
            studentLayout.getChildren().addAll(studentLabel, studentList, backButton);
        }
        primaryStage.setScene(new Scene(studentLayout, 400, 300));
    }
}

class Database {
    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> roles = new HashMap<>();

    static {
        users.put("admin", "admin123");
        roles.put("admin", "Admin");

        users.put("student1", "password1");
        roles.put("student1", "Student");

        users.put("faculty1", "password2");
        roles.put("faculty1", "Faculty");
    }

    public static boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public static String getUserRole(String username) {
        return roles.get(username);
    }
}