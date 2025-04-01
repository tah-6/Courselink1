package com.example.courselink1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private VBox contentArea;
    String currentUser = SessionManager.getCurrentUser();
    String currentRole = SessionManager.getCurrentRole();
    private final String PRIMARY_COLOR = "#1E3A8A";
    private final String ACCENT_COLOR = "#3B82F6";
    private final String BACKGROUND_COLOR = "#F0F2F5";
    private final String CARD_COLOR = "#FFFFFF";
    private final String TEXT_COLOR = "#1F2937";

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
        this.primaryStage.setTitle("CourseLink - University Management System");
        showLoginScreen();
    }

    private void showLoginScreen() {
        // Main container with background
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Login card with shadow effect
        VBox loginCard = new VBox(20);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(40));
        loginCard.setMaxWidth(400);
        loginCard.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 10;");

        // Add drop shadow effect to card
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        loginCard.setEffect(shadow);

        // Logo and title
        try {
            InputStream iconStream = getClass().getResourceAsStream("/images/courselink_logo.png");
            if (iconStream != null) {
                ImageView logoView = new ImageView(new Image(iconStream));
                logoView.setFitHeight(80);
                logoView.setPreserveRatio(true);
                loginCard.getChildren().add(logoView);
            }
        } catch (Exception e) {
            // Fallback to text if image isn't available
            Label logoLabel = new Label("CourseLink");
            logoLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-size: 32px; -fx-font-weight: bold;");
            loginCard.getChildren().add(logoLabel);
        }

        Label subtitleLabel = new Label("University Management System");
        subtitleLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 16px; -fx-opacity: 0.8;");

        // Input fields with improved styling
        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setMaxWidth(300);
        userField.setPrefHeight(40);
        userField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(300);
        passField.setPrefHeight(40);
        passField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");

        // Login button with hover effect
        Button loginButton = new Button("Log in");
        loginButton.setPrefWidth(300);
        loginButton.setPrefHeight(40);
        loginButton.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        // Hover effect for button
        loginButton.setOnMouseEntered(e ->
                loginButton.setStyle("-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
        loginButton.setOnMouseExited(e ->
                loginButton.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));

        // Error message with better visibility
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 14px;");

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both username and password");
                shakeNode(messageLabel);
                return;
            }

            if (users.containsKey(username) && users.get(username).equals(password)) {
                SessionManager.setCurrentUser(username);
                String role = roles.getOrDefault(username, "Student");
                CSVLoader.loadCoursesForStudent(username);

                fadeTransition(loginCard, false, () -> showDashboard(role, username));
            } else {
                messageLabel.setText("Invalid username or password");
                shakeNode(messageLabel);
            }
        });

        loginCard.getChildren().addAll(
                subtitleLabel,
                new Separator(),
                userField,
                passField,
                loginButton,
                messageLabel
        );

        root.getChildren().add(loginCard);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Animate login card appearance
        fadeTransition(loginCard, true, null);
    }

    private void showDashboard(String role, String username) {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Left sidebar for navigation
        VBox sidebar = createSidebar(role);

        // Top header with user info and quick actions
        HBox header = createHeader(username, role);

        contentArea = new VBox(15);
        contentArea.setPadding(new Insets(20));
        contentArea.setAlignment(Pos.TOP_CENTER);

        // Welcome card for dashboard
        VBox welcomeCard = new VBox(10);
        welcomeCard.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8; -fx-padding: 20;");
        welcomeCard.setMaxWidth(800);

        DropShadow cardShadow = new DropShadow();
        cardShadow.setRadius(5);
        cardShadow.setColor(Color.rgb(0, 0, 0, 0.1));
        welcomeCard.setEffect(cardShadow);

        Label welcomeLabel = new Label("Welcome to CourseLink");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        Label infoLabel = new Label("Your one-stop platform for university management.");
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.8;");

        welcomeCard.getChildren().addAll(welcomeLabel, infoLabel);

        // Dashboard tiles for quick statistics or navigation
        HBox dashboardTiles = new HBox(15);
        dashboardTiles.setAlignment(Pos.CENTER);

        String[] tileColors = {"#3B82F6", "#10B981", "#F59E0B", "#EF4444"};
        String[] tileIcons = {"📚", "📝", "👨‍🏫", "📅"};
        String[] tileTitles = {"Courses", "Assignments", "Faculty", "Events"};

        for (int i = 0; i < 4; i++) {
            VBox tile = createDashboardTile(tileIcons[i], tileTitles[i], tileColors[i]);
            dashboardTiles.getChildren().add(tile);
        }

        // Initial dashboard content
        contentArea.getChildren().addAll(welcomeCard, dashboardTiles);

        rootLayout.setTop(header);
        rootLayout.setLeft(sidebar);
        rootLayout.setCenter(contentArea);

        Scene scene = new Scene(rootLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        // Animate transition
        fadeTransition(rootLayout, true, null);
    }

    private VBox createSidebar(String role) {
        VBox sidebar = new VBox(5);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: " + PRIMARY_COLOR + ";");
        sidebar.setPadding(new Insets(20, 10, 20, 10));

        Label appNameLabel = new Label("CourseLink");
        appNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        appNameLabel.setPadding(new Insets(0, 0, 20, 10));

        sidebar.getChildren().add(appNameLabel);

        // Common menu items for all users
        String[][] menuItems = {
                {"Dashboard", "🏠"},
                {"Courses", "📚"},

        };

        for (String[] item : menuItems) {
            HBox menuItem = createMenuItem(item[0], item[1]);
            sidebar.getChildren().add(menuItem);
        }

        // Admin-specific menu items
        if (role.equals("Admin")) {
            sidebar.getChildren().add(new Separator());

            Label adminLabel = new Label("ADMINISTRATION");
            adminLabel.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 12px; -fx-font-weight: bold;");
            adminLabel.setPadding(new Insets(15, 0, 5, 10));
            sidebar.getChildren().add(adminLabel);

            String[][] adminItems = {
                    {"Subject Management", "📋"},
                    {"Course Management", "📘"},
                    {"Student Management", "👨‍🎓"},
                    {"Faculty Management", "👨‍🏫"},
                    {"Event Management", "🎭"}
            };

            for (String[] item : adminItems) {
                HBox menuItem = createMenuItem(item[0], item[1]);
                menuItem.setOnMouseClicked(e -> handleAdminSelection(item[0]));
                sidebar.getChildren().add(menuItem);
            }
        }

            // Admin-specific menu items
            if (role.equals("Faculty")) {
                sidebar.getChildren().add(new Separator());

                Label facultyLabel = new Label("FACULTY MANAGEMENT");
                facultyLabel.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 12px; -fx-font-weight: bold;");
                facultyLabel.setPadding(new Insets(15, 0, 5, 10));
                sidebar.getChildren().add(facultyLabel);

                String[][] facultyItems = {
                        {"Course Management", "📘"},
                        {"Student Management", "👨‍🎓"},
                        {"Event Management", "🎭"}
                };

                for (String[] item : facultyItems) {
                    HBox menuItem = createMenuItem(item[0], item[1]);
                    menuItem.setOnMouseClicked(e -> handleAdminSelection(item[0]));
                    sidebar.getChildren().add(menuItem);
                }
            }


        // Logout at bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox logoutItem = createMenuItem("Logout", "🚪");
        logoutItem.setOnMouseClicked(e -> {
            SessionManager.clearSession();
            fadeTransition(rootLayout, false, this::showLoginScreen);
        });

        sidebar.getChildren().addAll(spacer, new Separator(), logoutItem);

        return sidebar;
    }

    private HBox createMenuItem(String text, String icon) {
        HBox menuItem = new HBox(10);
        menuItem.setAlignment(Pos.CENTER_LEFT);
        menuItem.setPadding(new Insets(8, 15, 8, 15));
        menuItem.setStyle("-fx-background-radius: 5;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        menuItem.getChildren().addAll(iconLabel, textLabel);

        // Hover effect
        menuItem.setOnMouseEntered(e ->
                menuItem.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 5;"));
        menuItem.setOnMouseExited(e ->
                menuItem.setStyle("-fx-background-radius: 5;"));

        return menuItem;
    }

    private HBox createHeader(String username, String role) {
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setSpacing(15);
        header.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-border-color: #E5E7EB; -fx-border-width: 0 0 1 0;");

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Notification icons
        Button notificationBtn = createIconButton("🔔");
        Button messageBtn = createIconButton("✉️");

        // User profile
        HBox userProfile = new HBox(10);
        userProfile.setAlignment(Pos.CENTER);

        Label userLabel = new Label("Hi, " + username);
        userLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-weight: bold;");

        Label roleLabel = new Label("(" + role + ")");
        roleLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.7;");

        userProfile.getChildren().addAll(userLabel, roleLabel);

        header.getChildren().addAll(searchField, spacer, notificationBtn, messageBtn, userProfile);

        return header;
    }

    private Button createIconButton(String icon) {
        Button button = new Button(icon);
        button.setStyle("-fx-background-color: transparent; -fx-font-size: 16px;");

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #F3F4F6; -fx-font-size: 16px; -fx-background-radius: 50%;"));
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: transparent; -fx-font-size: 16px;"));

        return button;
    }

    private VBox createDashboardTile(String icon, String title, String color) {
        VBox tile = new VBox(10);
        tile.setAlignment(Pos.CENTER);
        tile.setPrefWidth(180);
        tile.setPrefHeight(120);
        tile.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8;");

        DropShadow tileShadow = new DropShadow();
        tileShadow.setRadius(5);
        tileShadow.setColor(Color.rgb(0, 0, 0, 0.1));
        tile.setEffect(tileShadow);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 30px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        Label countLabel = new Label("0");
        countLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: " + color + "; -fx-font-weight: bold;");

        tile.getChildren().addAll(iconLabel, titleLabel, countLabel);

        // Hover effect
        tile.setOnMouseEntered(e -> {
            tile.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8; -fx-border-color: " + color + "; -fx-border-width: 2; -fx-border-radius: 8;");
        });
        tile.setOnMouseExited(e -> {
            tile.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8;");
        });

        return tile;
    }

    private void handleAdminSelection(String selection) {
        // Clear previous content
        contentArea.getChildren().clear();

        // Create page header
        VBox pageHeader = new VBox(5);
        pageHeader.setAlignment(Pos.CENTER_LEFT);
        pageHeader.setPadding(new Insets(0, 0, 20, 0));

        Label pageTitle = new Label(selection);
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        Label pageDescription = new Label("Manage " + selection.toLowerCase() + " in the university system");
        pageDescription.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.8;");

        pageHeader.getChildren().addAll(pageTitle, pageDescription);
        contentArea.getChildren().add(pageHeader);

        // Add breadcrumbs
        HBox breadcrumbs = new HBox(5);
        breadcrumbs.setAlignment(Pos.CENTER_LEFT);
        breadcrumbs.setPadding(new Insets(0, 0, 20, 0));

        Label homeLink = new Label("Dashboard");
        homeLink.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-cursor: hand;");

        Label separator = new Label(">");
        separator.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.5;");

        Label currentPage = new Label(selection);
        currentPage.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.8;");

        breadcrumbs.getChildren().addAll(homeLink, separator, currentPage);
        contentArea.getChildren().add(breadcrumbs);

        // Create action buttons bar
        HBox actionBar = new HBox(10);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(0, 0, 20, 0));

        Button addButton = new Button("Add New");
        addButton.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search " + selection.toLowerCase() + "...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");

        actionBar.getChildren().addAll(addButton, searchField);
        contentArea.getChildren().add(actionBar);

        // Use the existing admin management functions but with the enhanced UI
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
            case "Faculty Management":
                AdminManagement.showFacultyManagement(contentArea);
                break;
            case "Event Management":
                AdminManagement.showEventManagement(contentArea);
                break;
        }
    }

    // Animation utilities
    private void fadeTransition(javafx.scene.Node node, boolean fadeIn, Runnable onFinished) {
        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(fadeIn ? 0 : 1);
        fade.setToValue(fadeIn ? 1 : 0);

        if (onFinished != null) {
            fade.setOnFinished(e -> onFinished.run());
        }

        fade.play();
    }

    private void shakeNode(javafx.scene.Node node) {
        double originalX = node.getLayoutX();
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.ZERO, new javafx.animation.KeyValue(node.translateXProperty(), 0)),
                new javafx.animation.KeyFrame(Duration.millis(50), new javafx.animation.KeyValue(node.translateXProperty(), -5)),
                new javafx.animation.KeyFrame(Duration.millis(100), new javafx.animation.KeyValue(node.translateXProperty(), 5)),
                new javafx.animation.KeyFrame(Duration.millis(150), new javafx.animation.KeyValue(node.translateXProperty(), -5)),
                new javafx.animation.KeyFrame(Duration.millis(200), new javafx.animation.KeyValue(node.translateXProperty(), 0))
        );
        timeline.play();
    }
}