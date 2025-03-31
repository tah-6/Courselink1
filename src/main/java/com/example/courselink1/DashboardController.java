package com.example.courselink1;

import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

/**
 * DashboardController handles all the dashboard interactions and UI updates
 * for the CourseLink application, connecting dashboard UI elements with
 * functionality from AdminManagement.
 */
public class DashboardController {

    private final VBox contentArea;
    private final String PRIMARY_COLOR = "#1E3A8A";
    private final String ACCENT_COLOR = "#3B82F6";
    private final String BACKGROUND_COLOR = "#F0F2F5";
    private final String CARD_COLOR = "#FFFFFF";
    private final String TEXT_COLOR = "#1F2937";

    // Stats counters for dashboard
    private int courseCount = 0;
    private int assignmentCount = 0;
    private int facultyCount = 0;
    private int eventCount = 0;

    public DashboardController(VBox contentArea) {
        this.contentArea = contentArea;

        // Initialize counters
        try {
            courseCount = CSVLoader.loadCourses().size();
            facultyCount = CSVLoader.loadFaculty().size();
            eventCount = AdminManagement.loadEvents().size();
            assignmentCount = CSVLoader.loadCourseAssignments().size();
        } catch (Exception e) {
            System.out.println("Error loading initial counts: " + e.getMessage());
        }
    }

    /**
     * Creates and sets up the dashboard with tiles for quick access
     */
    public void setupDashboard(String username, String role) {
        contentArea.getChildren().clear();

        // Header section
        Label welcomeHeader = new Label("Welcome, " + username);
        welcomeHeader.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        Label roleLabel = new Label("You are logged in as: " + role);
        roleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.8;");

        // Welcome card
        VBox welcomeCard = createCard("Dashboard Overview",
                "Access and manage university resources from your personalized dashboard.");

        // Dashboard tiles row
        HBox dashboardTiles = new HBox(15);
        dashboardTiles.setAlignment(Pos.CENTER);

        // Create the four main dashboard tiles
        VBox coursesTile = createDashboardTile("📚", "Courses", "#3B82F6", String.valueOf(courseCount));
        VBox assignmentsTile = createDashboardTile("📝", "Assignments", "#10B981", String.valueOf(assignmentCount));
        VBox facultyTile = createDashboardTile("👨‍🏫", "Faculty", "#F59E0B", String.valueOf(facultyCount));
        VBox eventsTile = createDashboardTile("📅", "Events", "#EF4444", String.valueOf(eventCount));

        // Add click handlers to tiles
        coursesTile.setOnMouseClicked(e -> handleTileClick("Course Management"));
        assignmentsTile.setOnMouseClicked(e -> handleAssignmentsClick());
        facultyTile.setOnMouseClicked(e -> handleTileClick("Faculty Management"));
        eventsTile.setOnMouseClicked(e -> handleTileClick("Event Management"));

        dashboardTiles.getChildren().addAll(coursesTile, assignmentsTile, facultyTile, eventsTile);

        // Quick actions card
        VBox quickActionsCard = createCard("Quick Actions", "Frequently used functions");

        // Quick action buttons
        HBox quickActions = new HBox(10);
        quickActions.setPadding(new Insets(10, 0, 10, 0));

        // Create action buttons with styling
        Button addCourseBtn = createActionButton("Add Course", "📘");
        Button addStudentBtn = createActionButton("Add Student", "👨‍🎓");
        Button addFacultyBtn = createActionButton("Add Faculty", "👨‍🏫");
        Button addEventBtn = createActionButton("Add Event", "🎭");

        // Add click handlers
        addCourseBtn.setOnAction(e -> showQuickAddDialog("Course"));
        addStudentBtn.setOnAction(e -> CSVLoader.addStudent());
        addFacultyBtn.setOnAction(e -> CSVLoader.addFaculty());
        addEventBtn.setOnAction(e -> showQuickAddDialog("Event"));

        quickActions.getChildren().addAll(addCourseBtn, addStudentBtn, addFacultyBtn, addEventBtn);
        quickActionsCard.getChildren().add(quickActions);

        // Recent activity card (if available)
        VBox recentActivityCard = createCard("Recent Activity", "Your latest actions and notifications");
        ListView<String> activityList = new ListView<>();
        activityList.setPrefHeight(150);
        activityList.getItems().addAll(
                "System: Welcome to CourseLink",
                "Account: Last login on " + java.time.LocalDate.now(),
                "Courses: New course registrations open",
                "Events: Upcoming university event this week"
        );
        recentActivityCard.getChildren().add(activityList);

        // Add all components to content area
        contentArea.getChildren().addAll(
                welcomeHeader,
                roleLabel,
                createSpacer(20),
                welcomeCard,
                createSpacer(20),
                dashboardTiles,
                createSpacer(20),
                quickActionsCard,
                createSpacer(20),
                recentActivityCard
        );

        // Animate the content appearance
        fadeInContent();
    }

    /**
     * Creates a card with title and description
     */
    private VBox createCard(String title, String description) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8;");

        DropShadow cardShadow = new DropShadow();
        cardShadow.setRadius(5);
        cardShadow.setColor(Color.rgb(0, 0, 0, 0.1));
        card.setEffect(cardShadow);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.8;");

        card.getChildren().addAll(titleLabel, descLabel);
        return card;
    }

    /**
     * Creates a dashboard tile with icon, title and statistic
     */
    private VBox createDashboardTile(String icon, String title, String color, String count) {
        VBox tile = new VBox(10);
        tile.setAlignment(Pos.CENTER);
        tile.setPrefWidth(180);
        tile.setPrefHeight(120);
        tile.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8;");
        tile.setCursor(javafx.scene.Cursor.HAND);

        DropShadow tileShadow = new DropShadow();
        tileShadow.setRadius(5);
        tileShadow.setColor(Color.rgb(0, 0, 0, 0.1));
        tile.setEffect(tileShadow);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 30px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        Label countLabel = new Label(count);
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

    /**
     * Creates a styled action button
     */
    private Button createActionButton(String text, String icon) {
        Button button = new Button(icon + " " + text);
        button.setPadding(new Insets(8, 15, 8, 15));
        button.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; -fx-background-radius: 5;");

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-background-radius: 5;"));
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; -fx-background-radius: 5;"));

        return button;
    }

    /**
     * Creates a vertical spacer
     */
    private Region createSpacer(double height) {
        Region spacer = new Region();
        spacer.setPrefHeight(height);
        return spacer;
    }

    /**
     * Handles dashboard tile clicks to navigate to appropriate management screen
     */
    private void handleTileClick(String selection) {
        // Update content area with animation
        fadeOutContent(() -> {
            contentArea.getChildren().clear();

            // Create styled header for the section
            createSectionHeader(selection);

            // Handle the section based on selection
            switch (selection) {
                case "Course Management":
                    showEnhancedCourseManagement();
                    break;
                case "Student Management":
                    showEnhancedStudentManagement();
                    break;
                case "Faculty Management":
                    showEnhancedFacultyManagement();
                    break;
                case "Event Management":
                    showEnhancedEventManagement();
                    break;
            }

            // Add back button
            Button backButton = new Button("← Back to Dashboard");
            backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-weight: bold;");
            backButton.setOnAction(e -> setupDashboard(CSVLoader.getCurrentUser(), CSVLoader.getCurrentRole()));
            contentArea.getChildren().add(0, backButton);

            fadeInContent();
        });
    }

    /**
     * Creates a section header with title and description
     */
    private void createSectionHeader(String title) {
        VBox header = new VBox(5);
        header.setPadding(new Insets(20, 0, 20, 0));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        String description = "Manage " + title.toLowerCase().replace(" management", "s") + " in the university system";
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR + "; -fx-opacity: 0.8;");

        header.getChildren().addAll(titleLabel, descLabel);
        contentArea.getChildren().add(header);
    }

    /**
     * Enhanced assignments tile click handler
     */
    private void handleAssignmentsClick() {
        fadeOutContent(() -> {
            contentArea.getChildren().clear();

            createSectionHeader("Course Assignments");

            HBox actionBar = new HBox(10);
            actionBar.setPadding(new Insets(0, 0, 20, 0));

            Button assignButton = createActionButton("Assign Student to Course", "👨‍🎓");
            assignButton.setOnAction(e -> showAssignmentDialog());

            actionBar.getChildren().add(assignButton);
            contentArea.getChildren().add(actionBar);

            // Create styled table view
            TableView<String> table = createStyledTableView("Assignments");
            TableColumn<String, String> column = new TableColumn<>("Student - Course Assignments");
            column.setPrefWidth(500);
            column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
            table.getColumns().add(column);

            // Load data from CSVLoader
            table.setItems(CSVLoader.loadCourseAssignments());

            VBox tableCard = new VBox(table);
            tableCard.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8; -fx-padding: 15;");

            DropShadow shadow = new DropShadow();
            shadow.setRadius(5);
            shadow.setColor(Color.rgb(0, 0, 0, 0.1));
            tableCard.setEffect(shadow);

            contentArea.getChildren().add(tableCard);

            // Add back button
            Button backButton = new Button("← Back to Dashboard");
            backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-weight: bold;");
            backButton.setOnAction(e -> setupDashboard(CSVLoader.getCurrentUser(), CSVLoader.getCurrentRole()));
            contentArea.getChildren().add(0, backButton);

            fadeInContent();
        });
    }

    /**
     * Shows enhanced Course Management UI
     */
    private void showEnhancedCourseManagement() {
        HBox actionBar = new HBox(10);
        actionBar.setPadding(new Insets(0, 0, 20, 0));

        Button addButton = createActionButton("Add Course", "📘");
        Button viewButton = createActionButton("View Courses", "📋");
        TextField searchField = new TextField();
        searchField.setPromptText("Search courses...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        addButton.setOnAction(e -> CSVLoader.addCourse());
        viewButton.setOnAction(e -> showCoursesTable());

        actionBar.getChildren().addAll(addButton, viewButton, spacer, searchField);
        contentArea.getChildren().add(actionBar);

        // Initial view - show courses table
        showCoursesTable();
    }

    /**
     * Shows styled courses table
     */
    private void showCoursesTable() {
        // Get existing nodes except the last one (which would be the table)
        int nodeCount = contentArea.getChildren().size();
        if (nodeCount > 2) {
            contentArea.getChildren().remove(nodeCount - 1);
        }

        TableView<String> table = createStyledTableView("Courses");
        TableColumn<String, String> column = new TableColumn<>("Available Courses");
        column.setPrefWidth(500);
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(CSVLoader.loadCourses());

        VBox tableCard = new VBox(table);
        tableCard.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8; -fx-padding: 15;");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        tableCard.setEffect(shadow);

        contentArea.getChildren().add(tableCard);
    }

    /**
     * Shows enhanced Student Management UI
     */
    private void showEnhancedStudentManagement() {
        HBox actionBar = new HBox(10);
        actionBar.setPadding(new Insets(0, 0, 20, 0));

        Button addButton = createActionButton("Add Student", "👨‍🎓");
        Button viewButton = createActionButton("View Students", "📋");
        TextField searchField = new TextField();
        searchField.setPromptText("Search students...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        addButton.setOnAction(e -> CSVLoader.addStudent());
        viewButton.setOnAction(e -> showStudentsTable());

        actionBar.getChildren().addAll(addButton, viewButton, spacer, searchField);
        contentArea.getChildren().add(actionBar);

        // Initial view - show students table
        showStudentsTable();
    }

    /**
     * Shows styled students table
     */
    private void showStudentsTable() {
        // Get existing nodes except the last one (which would be the table)
        int nodeCount = contentArea.getChildren().size();
        if (nodeCount > 2) {
            contentArea.getChildren().remove(nodeCount - 1);
        }

        TableView<String> table = createStyledTableView("Students");
        TableColumn<String, String> column = new TableColumn<>("Enrolled Students");
        column.setPrefWidth(500);
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(CSVLoader.loadStudents());

        VBox tableCard = new VBox(table);
        tableCard.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8; -fx-padding: 15;");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        tableCard.setEffect(shadow);

        contentArea.getChildren().add(tableCard);
    }

    /**
     * Shows enhanced Faculty Management UI
     */
    private void showEnhancedFacultyManagement() {
        HBox actionBar = new HBox(10);
        actionBar.setPadding(new Insets(0, 0, 20, 0));

        Button addButton = createActionButton("Add Faculty", "👨‍🏫");
        Button viewButton = createActionButton("View Faculty", "📋");
        TextField searchField = new TextField();
        searchField.setPromptText("Search faculty...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        addButton.setOnAction(e -> CSVLoader.addFaculty());
        viewButton.setOnAction(e -> showFacultyTable());

        actionBar.getChildren().addAll(addButton, viewButton, spacer, searchField);
        contentArea.getChildren().add(actionBar);

        // Initial view - show faculty table
        showFacultyTable();
    }

    /**
     * Shows styled faculty table
     */
    private void showFacultyTable() {
        // Get existing nodes except the last one (which would be the table)
        int nodeCount = contentArea.getChildren().size();
        if (nodeCount > 2) {
            contentArea.getChildren().remove(nodeCount - 1);
        }

        TableView<String> table = createStyledTableView("Faculty");
        TableColumn<String, String> column = new TableColumn<>("Faculty Members");
        column.setPrefWidth(500);
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(CSVLoader.loadFaculty());

        VBox tableCard = new VBox(table);
        tableCard.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8; -fx-padding: 15;");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        tableCard.setEffect(shadow);

        contentArea.getChildren().add(tableCard);
    }

    /**
     * Shows enhanced Event Management UI
     */
    private void showEnhancedEventManagement() {
        HBox actionBar = new HBox(10);
        actionBar.setPadding(new Insets(0, 0, 20, 0));

        Button addButton = createActionButton("Add Event", "🎭");
        Button viewButton = createActionButton("View Events", "📋");
        TextField searchField = new TextField();
        searchField.setPromptText("Search events...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        addButton.setOnAction(e -> showQuickAddDialog("Event"));
        viewButton.setOnAction(e -> showEventsTable());

        actionBar.getChildren().addAll(addButton, viewButton, spacer, searchField);
        contentArea.getChildren().add(actionBar);

        // Initial view - show events table
        showEventsTable();
    }

    /**
     * Shows styled events table
     */
    private void showEventsTable() {
        // Get existing nodes except the last one (which would be the table)
        int nodeCount = contentArea.getChildren().size();
        if (nodeCount > 2) {
            contentArea.getChildren().remove(nodeCount - 1);
        }

        TableView<String> table = createStyledTableView("Events");
        TableColumn<String, String> column = new TableColumn<>("University Events");
        column.setPrefWidth(500);
        column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        table.getColumns().add(column);
        table.setItems(AdminManagement.loadEvents());

        VBox tableCard = new VBox(table);
        tableCard.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 8; -fx-padding: 15;");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        tableCard.setEffect(shadow);

        contentArea.getChildren().add(tableCard);
    }

    /**
     * Shows a styled dialog for quick adding items
     */
    private void showQuickAddDialog(String itemType) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add " + itemType);
        dialog.setHeaderText("Enter " + itemType + " Details");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText(itemType + " Name");

        content.getChildren().add(nameField);

        // Add specific fields based on item type
        if (itemType.equals("Event")) {
            DatePicker datePicker = new DatePicker();
            datePicker.setPromptText("Event Date");
            content.getChildren().add(datePicker);

            TextField locationField = new TextField();
            locationField.setPromptText("Event Location");
            content.getChildren().add(locationField);
        } else if (itemType.equals("Course")) {
            ComboBox<String> subjectCombo = new ComboBox<>();
            subjectCombo.setPromptText("Select Subject");
            subjectCombo.setItems(CSVLoader.loadSubjects());
            content.getChildren().add(subjectCombo);

            TextField codeField = new TextField();
            codeField.setPromptText("Course Code");
            content.getChildren().add(codeField);

            TextField creditsField = new TextField();
            creditsField.setPromptText("Credits");
            content.getChildren().add(creditsField);
        }

        dialog.getDialogPane().setContent(content);

        // Request focus on the name field by default
        nameField.requestFocus();

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return nameField.getText();
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        dialog.showAndWait().ifPresent(result -> {
            if (!result.isEmpty()) {
                if (itemType.equals("Event")) {
                    CSVLoader.addEvent(result);
                    refreshCounts();
                    showAlert("Success", "Event added successfully!");
                } else if (itemType.equals("Course")) {
                    CSVLoader.addCourse();
                    refreshCounts();
                    showAlert("Success", "Course added successfully!");
                }
            }
        });
    }

    /**
     * Shows a styled dialog for assigning students to courses
     */
    private void showAssignmentDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Assign Student to Course");
        dialog.setHeaderText("Select Student and Course");

        // Set the button types
        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        // Create the content
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        ComboBox<String> studentCombo = new ComboBox<>();
        studentCombo.setPromptText("Select Student");
        studentCombo.setItems(CSVLoader.loadStudents());
        studentCombo.setPrefWidth(300);

        ComboBox<String> courseCombo = new ComboBox<>();
        courseCombo.setPromptText("Select Course");
        courseCombo.setItems(CSVLoader.loadCourses());
        courseCombo.setPrefWidth(300);

        content.getChildren().addAll(
                new Label("Student:"),
                studentCombo,
                new Label("Course:"),
                courseCombo
        );

        dialog.getDialogPane().setContent(content);

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                String student = studentCombo.getValue();
                String course = courseCombo.getValue();

                if (student != null && course != null) {
                    CSVLoader.assignStudentToCourse(student, course);
                    refreshCounts();
                    return true;
                }
            }
            return false;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result) {
                showAlert("Success", "Student assigned to course successfully!");
                // Refresh the assignments view
                handleAssignmentsClick();
            }
        });
    }

    /**
     * Creates a styled table view
     */
    private TableView<String> createStyledTableView(String type) {
        TableView<String> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-radius: 5;");
        table.setPrefHeight(400);

        return table;
    }

    /**
     * Shows an alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Refreshes the dashboard counts
     */
    private void refreshCounts() {
        try {
            courseCount = CSVLoader.loadCourses().size();
            facultyCount = CSVLoader.loadFaculty().size();
            eventCount = AdminManagement.loadEvents().size();
            assignmentCount = CSVLoader.loadCourseAssignments().size();
        } catch (Exception e) {
            System.out.println("Error refreshing counts: " + e.getMessage());
        }
    }

    /**
     * Animation to fade in content
     */
    private void fadeInContent() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentArea);
        fadeIn.setFromValue(0);
    }

    private void fadeOutContent(Runnable onFinished) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), contentArea);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            // Execute the provided code after fade out completes
            onFinished.run();
        });
        fadeOut.play();
    }
}