package com.example.courselink1;
import com.example.courselink1.AdminManagement;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DashboardController {

    // Constants for styling
    private static final String PRIMARY_COLOR = "#3B82F6";
    private static final String ACCENT_COLOR = "#EC4899";
    private static final String CARD_COLOR = "white";
    private static final String TEXT_COLOR = "#111827";

    // Core UI components
    private BorderPane rootLayout;
    private VBox contentArea;
    private String currentRole;
    private String currentUser;

    // Navigation map to avoid repetitive switch statements
    private final Map<String, Runnable> navigationMap = new HashMap<>();

    public DashboardController(String role, String username) {
        this.currentRole = role;
        this.currentUser = username;

        // Initialize navigation map
        setupNavigationMap();

        // Initialize UI
        rootLayout = new BorderPane();
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(25));
        contentArea.setStyle("-fx-background-color: #F3F4F6;");

        // Setup layout components
        rootLayout.setLeft(createSidebar());
        rootLayout.setTop(createHeader());
        rootLayout.setCenter(new ScrollPane(contentArea));

        // Show initial dashboard content
        showDashboard(role, username);
    }

    private void setupNavigationMap() {
        navigationMap.put("Dashboard", () -> showDashboard(currentRole, currentUser));
        navigationMap.put("Course Management", () -> handleAdminSelection("Course Management"));
        navigationMap.put("Student Management", () -> handleAdminSelection("Student Management"));
        navigationMap.put("Faculty Management", () -> handleAdminSelection("Faculty Management"));
        navigationMap.put("Subject Management", () -> handleAdminSelection("Subject Management"));
        navigationMap.put("Settings", this::showSettingsPage);
        navigationMap.put("Logout", this::showLoginScreen);
    }

    public BorderPane getRoot() {
        return rootLayout;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(5);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: " + PRIMARY_COLOR + ";");
        sidebar.setPadding(new Insets(20, 0, 20, 0));

        // Logo and title
        Label appTitle = new Label("UniManager");
        appTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        appTitle.setPadding(new Insets(0, 0, 20, 15));

        // Create menu items based on role
        VBox menuItems = new VBox(5);
        menuItems.setPadding(new Insets(10, 0, 10, 0));

        // Admin has access to all menu items
        if (currentRole.equals("Admin")) {
            String[][] adminMenuItems = {
                    {"Dashboard", "dashboard"},
                    {"Courses", "book"},
                    {"Students", "users"},
                    {"Faculty", "user"},
                    {"Subjects", "bookmark"}
            };

            for (String[] item : adminMenuItems) {
                menuItems.getChildren().add(createMenuItem(item[0], item[1]));
            }
        } else {
            // Simplified menu for non-admin users
            menuItems.getChildren().addAll(
                    createMenuItem("Dashboard", "dashboard"),
                    createMenuItem("Courses", "book")
            );
        }

        // Settings and Logout options for all users
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: rgba(255,255,255,0.2);");
        separator.setPadding(new Insets(10, 15, 10, 15));

        VBox bottomMenu = new VBox(5);
        bottomMenu.setPadding(new Insets(10, 0, 10, 0));
        bottomMenu.getChildren().addAll(
                createMenuItem("Settings", "settings"),
                createMenuItem("Logout", "logout")
        );

        sidebar.getChildren().addAll(appTitle, menuItems, separator, bottomMenu);
        return sidebar;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setStyle("-fx-background-color: white; -fx-border-color: transparent transparent #E5E7EB transparent;");

        // Search box
        TextField searchBox = new TextField();
        searchBox.setPromptText("Search...");
        searchBox.setPrefWidth(300);
        searchBox.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // User profile button
        Button userButton = createIconButton("user");
        userButton.setOnAction(e -> showUserProfile());

        header.getChildren().addAll(searchBox, spacer, userButton);
        return header;
    }

    private void showDashboard(String role, String username) {
        contentArea.getChildren().clear();
        createDashboardContent();
    }

    private void createDashboardContent() {
        // Title and description
        VBox headerBox = new VBox(5,
                createStyledLabel("Welcome, " + currentUser + "!", "-fx-font-size: 24px; -fx-font-weight: bold;"),
                createStyledLabel("Here's an overview of your university management system.", "-fx-font-size: 14px; -fx-opacity: 0.8;")
        );
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        // Create dashboard tiles
        HBox tilesRow1 = new HBox(20);
        tilesRow1.getChildren().addAll(
                createDashboardTile("book", "Courses", "24", PRIMARY_COLOR),
                createDashboardTile("users", "Students", "120", ACCENT_COLOR),
                createDashboardTile("user", "Faculty", "18", "#10B981")
        );

        HBox tilesRow2 = new HBox(20);
        tilesRow2.getChildren().addAll(
                createDashboardTile("bookmark", "Subjects", "7", "#8B5CF6")
        );

        contentArea.getChildren().addAll(headerBox, tilesRow1, new Region() {{ setPrefHeight(20); }}, tilesRow2);
    }

    private VBox createDashboardTile(String icon, String title, String count, String color) {
        VBox tile = new VBox(15);
        tile.setAlignment(Pos.CENTER);
        tile.setPadding(new Insets(20));
        tile.setPrefSize(200, 150);
        tile.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8px;");

        // Add shadow and components
        tile.setEffect(new DropShadow(5, 0, 2, Color.rgb(0, 0, 0, 0.1)));
        tile.getChildren().addAll(
                new ImageView(new Image(getClass().getResourceAsStream("/icons/" + icon + ".png"), 40, 40, true, true)),
                createStyledLabel(count, "-fx-font-size: 24px; -fx-font-weight: bold;"),
                createStyledLabel(title, "-fx-font-size: 14px;"),
                new Rectangle(150, 5) {{ setFill(Color.web(color)); setArcWidth(5); setArcHeight(5); }}
        );

        // Add hover effect and click handler
        applyHoverEffect(tile, CARD_COLOR);
        tile.setOnMouseClicked(e -> {
            System.out.println("Tile clicked: " + title);
            String managementKey = title + " Management";
            if (navigationMap.containsKey(managementKey)) {
                navigationMap.get(managementKey).run();
            }
            e.consume();
        });

        return tile;
    }

    private HBox createMenuItem(String text, String icon) {
        HBox menuItem = new HBox(10);
        menuItem.setAlignment(Pos.CENTER_LEFT);
        menuItem.setPadding(new Insets(10, 15, 10, 15));

        // Add icon if available
        InputStream iconStream = getClass().getResourceAsStream("/icons/" + icon + ".png");
        if (iconStream != null) {
            menuItem.getChildren().add(new ImageView(new Image(iconStream, 18, 18, true, true)));
        }

        // Add text label
        menuItem.getChildren().add(createStyledLabel(text, "-fx-font-size: 14px; -fx-text-fill: white;"));

        // Add hover effect and click handler
        menuItem.setOnMouseEntered(e -> {
            menuItem.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 5;");
            menuItem.setCursor(Cursor.HAND);
        });

        menuItem.setOnMouseExited(e -> menuItem.setStyle("-fx-background-color: transparent;"));

        menuItem.setOnMouseClicked(e -> {
            System.out.println("Menu item clicked: " + text);
            String managementKey = text + " Management";
            if (navigationMap.containsKey(managementKey)) {
                navigationMap.get(managementKey).run();
            } else if (navigationMap.containsKey(text)) {
                navigationMap.get(text).run();
            }
            e.consume();
        });

        return menuItem;
    }

    private Button createIconButton(String icon) {
        Button button = new Button();
        button.setPrefSize(40, 40);

        InputStream iconStream = getClass().getResourceAsStream("/icons/" + icon + ".png");
        if (iconStream != null) {
            button.setGraphic(new ImageView(new Image(iconStream, 18, 18, true, true)));
        }

        button.setStyle("-fx-background-color: transparent; -fx-border-color: #E5E7EB; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Add hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 5;");
            button.setCursor(Cursor.HAND);
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: transparent; -fx-border-color: #E5E7EB; -fx-border-radius: 5;");
        });

        return button;
    }

    private void handleAdminSelection(String selection) {
        // Clear previous content
        contentArea.getChildren().clear();

        // Create page header with title and description
        VBox pageHeader = new VBox(5);
        pageHeader.getChildren().addAll(
                createStyledLabel(selection, "-fx-font-size: 24px; -fx-font-weight: bold;"),
                createStyledLabel("Manage " + selection.toLowerCase() + " in the university system", "-fx-font-size: 14px; -fx-opacity: 0.8;")
        );

        // Add breadcrumbs with functional navigation
        HBox breadcrumbs = createBreadcrumbs(selection);

        // Create action bar with search and add button
        HBox actionBar = createActionBar(selection);

        // Add components to content area
        contentArea.getChildren().addAll(pageHeader, breadcrumbs, actionBar);

        // Show appropriate management section
        switch (selection) {
            case "Subject Management" -> AdminManagement.showSubjectManagement(contentArea);
            case "Course Management" -> AdminManagement.showCourseManagement(contentArea);
            case "Student Management" -> AdminManagement.showStudentManagement(contentArea);
            case "Faculty Management" -> AdminManagement.showFacultyManagement(contentArea);
            default -> contentArea.getChildren().add(createStyledLabel("This section is not implemented yet.", "-fx-font-size: 16px;"));
        }
    }

    private HBox createBreadcrumbs(String currentPage) {
        HBox breadcrumbs = new HBox(5);
        breadcrumbs.setAlignment(Pos.CENTER_LEFT);
        breadcrumbs.setPadding(new Insets(0, 0, 20, 0));

        Label homeLink = createStyledLabel("Dashboard", "-fx-text-fill: " + PRIMARY_COLOR + "; -fx-cursor: hand;");
        homeLink.setOnMouseClicked(e -> navigationMap.get("Dashboard").run());

        breadcrumbs.getChildren().addAll(
                homeLink,
                createStyledLabel(">", "-fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.5;"),
                createStyledLabel(currentPage, "-fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.8;")
        );

        return breadcrumbs;
    }

    private HBox createActionBar(String section) {
        HBox actionBar = new HBox(10);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(0, 0, 20, 0));

        // Add button with click handler
        Button addButton = new Button("Add New");
        addButton.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        addButton.setOnAction(e -> showAddDialog(section));

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search " + section.toLowerCase() + "...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 5; -fx-padding: 8;");

        actionBar.getChildren().addAll(addButton, searchField);
        return actionBar;
    }

    // Helper methods
    private void showAddDialog(String section) {
        String entityName = section.replace(" Management", "");

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add New " + entityName);
        dialog.setHeaderText("Enter " + entityName.toLowerCase() + " details");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create form content based on entity type
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText(entityName + " Name");

        TextField codeField = new TextField();
        codeField.setPromptText(entityName + " ID/Code");

        ComboBox<String> departmentCombo = new ComboBox<>();
        departmentCombo.getItems().addAll("Computer Science", "Mathematics", "Physics", "Engineering");
        departmentCombo.setPromptText("Select Department");

        grid.add(new Label(entityName + " Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(entityName + " ID/Code:"), 0, 1);
        grid.add(codeField, 1, 1);
        grid.add(new Label("Department:"), 0, 2);
        grid.add(departmentCombo, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the name field by default
        Platform.runLater(() -> nameField.requestFocus());

        // Convert the result when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return nameField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(name -> {
            showAlert(entityName + " Added", "New " + entityName.toLowerCase() + " \"" + name + "\" has been added successfully.");
            handleAdminSelection(section);
        });
    }

    private void showSettingsPage() {
        contentArea.getChildren().clear();

        VBox settingsForm = new VBox(15);
        settingsForm.setPadding(new Insets(20));

        // Add settings components
        ToggleGroup themeGroup = new ToggleGroup();
        HBox themeBox = new HBox(10,
                createFixedWidthLabel("Theme:", 120),
                new RadioButton("Light") {{ setToggleGroup(themeGroup); setSelected(true); }},
                new RadioButton("Dark") {{ setToggleGroup(themeGroup); }}
        );

        // Save button
        Button saveButton = new Button("Save Settings");
        saveButton.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white;");
        saveButton.setOnAction(e -> showAlert("Settings Saved", "Your settings have been updated successfully."));

        contentArea.getChildren().addAll(
                createStyledLabel("Settings", "-fx-font-size: 24px; -fx-font-weight: bold;"),
                settingsForm
        );
        settingsForm.getChildren().addAll(themeBox, saveButton);
    }

    private void showUserProfile() {
        contentArea.getChildren().clear();

        VBox profileBox = new VBox(15);
        profileBox.setPadding(new Insets(20));

        // Profile information
        VBox infoBox = new VBox(5);
        infoBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 5;");
        infoBox.getChildren().addAll(
                new Label("Name: " + currentUser),
                new Label("Role: " + currentRole),
                new Label("Email: " + currentUser.toLowerCase() + "@university.edu")
        );

        // Edit profile button
        Button editButton = new Button("Edit Profile");
        editButton.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white;");
        editButton.setOnAction(e -> showAlert("Edit Profile", "Profile editing functionality would be implemented here."));

        contentArea.getChildren().add(profileBox);
        profileBox.getChildren().addAll(
                createStyledLabel("User Profile", "-fx-font-size: 24px; -fx-font-weight: bold;"),
                infoBox,
                editButton
        );
    }

    private void showLoginScreen() {
        // Just a placeholder - in a real app you would navigate back to login screen
        showAlert("Logout", "You have been logged out successfully.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Utility methods to reduce code duplication
    private Label createStyledLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style + "; -fx-text-fill: " + TEXT_COLOR + ";");
        return label;
    }

    private Label createFixedWidthLabel(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        return label;
    }

    private void applyHoverEffect(Node node, String defaultColor) {
        node.setOnMouseEntered(e -> {
            node.setStyle(node.getStyle().replace(defaultColor, "#F9FAFB"));
            node.setCursor(Cursor.HAND);
        });

        node.setOnMouseExited(e -> {
            node.setStyle(node.getStyle().replace("#F9FAFB", defaultColor));
        });
    }
}