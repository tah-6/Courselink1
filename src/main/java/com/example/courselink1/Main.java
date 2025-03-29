package com.example.courselink1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private VBox contentArea;
    private String currentUser;

    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> roles = new HashMap<>();

    static {
        CSVLoader.loadUsersWithRoles();
        users.putAll(CSVLoader.users);
        roles.putAll(CSVLoader.roles);
    }

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

        Label titleLabel = new Label("");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        TextField userField = new TextField();
        userField.setPromptText("Username");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Button loginButton = new Button("Log in");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (users.containsKey(username) && users.get(username).equals(password)) {
                currentUser = username;
                String role = roles.getOrDefault(username, "Student");
                CSVLoader.loadCoursesForStudent(username);
                showDashboard(role, username);
            } else {
                messageLabel.setText("Invalid credentials!");
            }
        });

        loginLayout.getChildren().addAll(titleLabel, userField, passField, loginButton, messageLabel);
        primaryStage.setScene(new Scene(loginLayout, 350, 400));
        primaryStage.setTitle("University Management System");
        primaryStage.show();
    }

    private void showDashboard(String role, String username) {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: #F0F2F5;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1E3A8A; -fx-padding: 15px;");

        Label greeting = new Label("Welcome, " + username + " (" + role + ")");
        greeting.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        header.getChildren().add(greeting);

        if (role.equals("Admin")) {
            ComboBox<String> adminMenu = new ComboBox<>();
            adminMenu.getItems().addAll("Subject Management", "Course Management", "Student Management", "Faculty Management", "Event Management", "Logout");
            adminMenu.setPromptText("Select Option");
            adminMenu.setOnAction(e -> handleAdminSelection(adminMenu.getValue()));
            header.getChildren().add(adminMenu);
        }

        contentArea = new VBox(10);
        contentArea.setPadding(new Insets(20));
        contentArea.setAlignment(Pos.TOP_CENTER);

        rootLayout.setTop(header);
        rootLayout.setCenter(contentArea);
        primaryStage.setScene(new Scene(rootLayout, 900, 600));
    }

    private void handleAdminSelection(String selection) {
        switch (selection) {
            case "Subject Management":
                AdminManagement.showSubjectManagement(contentArea);
                break;
            case "Course Management":
                AdminManagement.showCourseManagement(contentArea);
                break;
            case "Student Management":
                AdminManagement.showStudentManagement(contentArea);
                break;
         //   case "Faculty Management":
        //        AdminManagement.showFacultyManagement(contentArea);
          //      break;
            case "Event Management":
                AdminManagement. showEventManagement(contentArea);
                break;
            case "Logout":
                currentUser = null;
                showLoginScreen();
                break;
        }
    }
}
