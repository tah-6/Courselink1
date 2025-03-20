package com.example.courselink1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private VBox contentArea;
    private String currentUser;  // Store logged-in user

    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> roles = new HashMap<>();
    private static final ObservableList<String> courses = FXCollections.observableArrayList();
    private static final Map<String, ObservableList<String>> studentEnrollments = new HashMap<>();

    static {
        // Default Admin Account
        users.put("admin", "admin123");
        roles.put("admin", "Admin");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    // Show login screen
    private void showLoginScreen() {
        VBox loginLayout = new VBox(15);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-background-color: #121212;");

        Label titleLabel = new Label("Welcome");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");

        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setStyle("-fx-pref-width: 250px;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle("-fx-pref-width: 250px;");

        Button loginButton = new Button("Log in");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (users.containsKey(username) && users.get(username).equals(password)) {
                currentUser = username; // Store current user
                showDashboard(roles.get(username), username);
            } else {
                messageLabel.setText("Invalid credentials!");
            }
        });

        loginLayout.getChildren().addAll(titleLabel, userField, passField, loginButton, messageLabel);
        primaryStage.setScene(new Scene(loginLayout, 350, 400));
        primaryStage.setTitle("University Management System");
        primaryStage.show();
    }

    // Show dashboard with Logout functionality
    private void showDashboard(String role, String username) {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: #F0F2F5;");

        // Top Bar
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #1E3A8A; -fx-padding: 10px;");
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setSpacing(10);

        // Three-Dot Menu Button
        Button menuButton = new Button("⋮");
        menuButton.setStyle("-fx-font-size: 20px; -fx-background-color: transparent; -fx-text-fill: white;");

        // Dropdown Menu (Context Menu)
        ContextMenu menu = new ContextMenu();
        MenuItem eventsItem = new MenuItem("📅 Events");
        MenuItem coursesItem = new MenuItem("📖 Courses");
        MenuItem studentsItem = new MenuItem("👥 Students");
        MenuItem adminPanel = new MenuItem("⚙️ Admin Panel");
        MenuItem logoutItem = new MenuItem("🚪 Logout");  // Logout Option

        if (role.equals("Admin")) {
            menu.getItems().addAll(adminPanel, eventsItem, coursesItem, studentsItem, new SeparatorMenuItem(), logoutItem);
            adminPanel.setOnAction(e -> showAdminPanel());
        } else {
            menu.getItems().addAll(eventsItem, coursesItem, studentsItem, new SeparatorMenuItem(), logoutItem);
        }

        menuButton.setOnAction(e -> menu.show(menuButton, javafx.geometry.Side.BOTTOM, 0, 0));

        // Logout Functionality (Return to Login)
        logoutItem.setOnAction(e -> {
            currentUser = null;  // Clear session data
            showLoginScreen();
        });

        eventsItem.setOnAction(e -> showEventManagement());
        coursesItem.setOnAction(e -> showCourseManagement());
        studentsItem.setOnAction(e -> showStudentManagement(username));

        topBar.getChildren().add(menuButton);
        rootLayout.setTop(topBar);

        contentArea = new VBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.getChildren().add(new Label("Select an option from the menu"));
        rootLayout.setCenter(contentArea);

        primaryStage.setScene(new Scene(rootLayout, 800, 500));
    }

    // Admin Panel (Add Students & Courses)
    private void showAdminPanel() {
        VBox adminLayout = new VBox(15);
        adminLayout.setPadding(new Insets(20));
        adminLayout.getChildren().add(new Label("⚙️ Admin Panel"));

        // Add Student Section
        TextField studentNameField = new TextField();
        studentNameField.setPromptText("Enter student username");
        Button addStudentButton = new Button("Add Student");

        addStudentButton.setOnAction(e -> {
            String studentName = studentNameField.getText();
            if (!studentName.isEmpty() && !users.containsKey(studentName)) {
                users.put(studentName, "password123"); // Default password
                roles.put(studentName, "Student");
                studentEnrollments.put(studentName, FXCollections.observableArrayList());
                showAlert("Success", "Student added! Default password: password123");
                studentNameField.clear();
            }
        });

        // Add Course Section
        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Enter course name");
        Button addCourseButton = new Button("Add Course");

        addCourseButton.setOnAction(e -> {
            String courseName = courseNameField.getText();
            if (!courseName.isEmpty() && !courses.contains(courseName)) {
                courses.add(courseName);
                showAlert("Success", "Course added!");
                courseNameField.clear();
            }
        });

        adminLayout.getChildren().addAll(new Label("Add Student"), studentNameField, addStudentButton,
                new Label("Add Course"), courseNameField, addCourseButton);
        contentArea.getChildren().setAll(adminLayout);
    }

    // Show Event Management
    private void showEventManagement() {
        VBox eventLayout = new VBox(10);
        eventLayout.setPadding(new Insets(20));
        eventLayout.getChildren().add(new Label("📅 Event Management"));
        ListView<String> eventList = new ListView<>();
        eventList.getItems().addAll("Tech Seminar", "Workshop on AI", "Networking Event");
        eventLayout.getChildren().add(eventList);
        contentArea.getChildren().setAll(eventLayout);
    }

    // Show Course Management
    private void showCourseManagement() {
        VBox courseLayout = new VBox(10);
        courseLayout.setPadding(new Insets(20));
        courseLayout.getChildren().add(new Label("📖 Course Management"));
        ListView<String> courseList = new ListView<>(courses);
        courseLayout.getChildren().add(courseList);
        contentArea.getChildren().setAll(courseLayout);
    }

    // Show Student Management (Admin Can Enroll Students)
    private void showStudentManagement(String username) {
        VBox studentLayout = new VBox(10);
        studentLayout.setPadding(new Insets(20));
        studentLayout.getChildren().add(new Label("👥 Student Management"));

        if ("Admin".equals(roles.get(username))) {
            ComboBox<String> studentDropdown = new ComboBox<>(FXCollections.observableArrayList(users.keySet()));
            ComboBox<String> courseDropdown = new ComboBox<>(courses);
            Button enrollButton = new Button("Enroll Student");

            enrollButton.setOnAction(e -> {
                String student = studentDropdown.getValue();
                String course = courseDropdown.getValue();
                if (student != null && course != null) {
                    studentEnrollments.get(student).add(course);
                    showAlert("Success", "Student enrolled!");
                }
            });

            studentLayout.getChildren().addAll(new Label("Enroll Students"), studentDropdown, courseDropdown, enrollButton);
        }

        contentArea.getChildren().setAll(studentLayout);
    }

    // Utility: Show Alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
