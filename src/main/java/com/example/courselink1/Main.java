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



public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private VBox contentArea;
    private String currentUser;


    private static final Map<String, String> users = new HashMap<>();
    private static final ObservableList<String> courses = FXCollections.observableArrayList();


    static {
        // Load Users from CSV using CSVLoader
        CSVLoader.loadStudents();
        users.putAll(CSVLoader.users);


        // Load Courses from CSV using CSVLoader
        CSVLoader.loadCourses();
        courses.addAll(CSVLoader.courses);
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
                currentUser = username;
                showDashboard("Student", username);
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


        contentArea = new VBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.getChildren().add(new Label("Dashboard for " + username));
        rootLayout.setCenter(contentArea);


        primaryStage.setScene(new Scene(rootLayout, 800, 500));
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}






