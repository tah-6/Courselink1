
package com.example.courselink1;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Objects;


public class StudentProfileController {
    private TextField nameField;
    private TextField emailField;
    private TextField telephoneField;
    private Button uploadPhotoButton;
    private ImageView profilePhotoView;
    private String currentUsername;
    private String profileImagePath;


    public VBox createProfileManagementView(String username) {
        currentUsername = username;

        // Create profile management form
        VBox profileForm = new VBox(10);
        profileForm.setPadding(new Insets(20));
        profileForm.setAlignment(Pos.TOP_CENTER);

        // Profile photo display and upload button
        profilePhotoView = new ImageView();
        profilePhotoView.setFitHeight(120);
        profilePhotoView.setFitWidth(120);
        profilePhotoView.setPreserveRatio(true);


        String currentPhoto = getStudentProfilePhoto(username);
        if (currentPhoto.equals("default_picture.png")) {
            try {
                profilePhotoView.setImage(new Image(currentPhoto));
            } catch (Exception e) {
                setDefaultProfileImage();
            }
        } else {
            setDefaultProfileImage();
        }

        uploadPhotoButton = new Button("Change Profile Picture");
        uploadPhotoButton.setOnAction(e -> handlePhotoUpload());

        // Profile information fields
        nameField = new TextField(getStudentName(username));
        emailField = new TextField(getStudentEmail(username));
        telephoneField = new TextField(getStudentTelephone(username));

        // Labels
        Label nameLabel = new Label("Name:");
        Label emailLabel = new Label("Email:");
        Label phoneLabel = new Label("Phone:");

        // Save button
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> saveProfileChanges());

        // Add components to form
        profileForm.getChildren().addAll(
                profilePhotoView, uploadPhotoButton,
                new HBox(10, nameLabel, nameField),
                new HBox(10, emailLabel, emailField),
                new HBox(10, phoneLabel, telephoneField),
                saveButton
        );

        return profileForm;
    }

    private Image loadProfilePicture(String studentId) {
        try {
            // Get the profile photo path from CSV
            Map<String, String> profile = CSVLoader.getStudentProfile(studentId);
            String photoPath = profile.get("Profile Photo");

            if (photoPath == null || photoPath.trim().isEmpty()) {
                return getDefaultProfileImage();
            }

            // Try loading the image from the path
            File imageFile = new File(photoPath);
            if (imageFile.exists()) {
                return new Image("file:" + photoPath);
            } else {
                // If the file doesn't exist at the absolute path, try as a resource
                InputStream resourceStream = getClass().getResourceAsStream(photoPath);
                if (resourceStream != null) {
                    return new Image(resourceStream);
                }
                return getDefaultProfileImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultProfileImage();
        }
    }

    /**
     * Returns the default profile image
     */
    private Image getDefaultProfileImage() {
        // Try to load from resources
        InputStream defaultStream = getClass().getResourceAsStream("/images/default_profile.png");
        if (defaultStream != null) {
            return new Image(defaultStream);
        }

        // If resource not found, create a simple placeholder
        return null; // Return null and handle in UI
    }



    private void handlePhotoUpload() {
        // Create file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null) {
            return; // User canceled
        }

        try {
            // Copy the file to our application directory with a unique name
            String newImagePath = saveProfileImage(selectedFile, currentUsername);

            if (newImagePath != null) {
                // Update the UI
                profilePhotoView.setImage(new Image("file:" + newImagePath));

                // Store the path for later saving to CSV
                profileImagePath = newImagePath;

                // Optional: Save immediately
                updateProfilePhotoInCSV(currentUsername, profileImagePath);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Profile Picture Updated");
                alert.setContentText("Your profile picture has been updated successfully.");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Save Profile Picture");
            alert.setContentText("An error occurred while saving your profile picture.\nError: " + ex.getMessage());
            alert.showAndWait();
            ex.printStackTrace();
        }
    }
    private File ensureProfileImagesDirectory() {
        // Create a directory inside your application's user data folder
        File appDir = new File(System.getProperty("user.home"), ".courselink");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        File imagesDir = new File(appDir, "profile_images");
        if (!imagesDir.exists()) {
            imagesDir.mkdir();
        }

        return imagesDir;
    }

    private String saveProfileImage(File sourceFile, String username) throws IOException {
        // Get our images directory
        File imagesDir = ensureProfileImagesDirectory();

        // Create a filename based on the username and original extension
        String fileName = sourceFile.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        String newFileName = "profile_" + username + extension;

        // Create destination file
        File destFile = new File(imagesDir, newFileName);

        // Copy the file using NIO (better for larger files)
        Files.copy(sourceFile.toPath(), destFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        // Return the absolute path of the saved file
        return destFile.getAbsolutePath();
    }

    public static void updateProfilePhotoInCSV(String studentId, String photoPath) {
        File tempFile = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            // Input file
            File inputFile = new File("src/main/resources/Students.csv");
            // Temporary output file
            tempFile = new File("src/main/resources/Students.csv.temp");

            reader = new BufferedReader(new FileReader(inputFile));
            writer = new BufferedWriter(new FileWriter(tempFile));

            // Read header and write it to temp file
            String line = reader.readLine();
            writer.write(line + System.lineSeparator());

            // Find column index for profile photo
            String[] headers = line.split(",");
            int photoColumnIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equals("Profile Photo")) {
                    photoColumnIndex = i;
                    break;
                }
            }

            if (photoColumnIndex == -1) {
                throw new IOException("Profile Photo column not found in CSV");
            }

            // Process each line
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                // If this is our target student
                if (data.length > 0 && data[0].trim().equals(studentId)) {
                    // Update the photo path
                    if (data.length > photoColumnIndex) {
                        data[photoColumnIndex] = photoPath;
                    } else {
                        // Handle case where data has fewer columns than expected
                        String[] newData = new String[photoColumnIndex + 1];
                        System.arraycopy(data, 0, newData, 0, data.length);
                        data = newData;
                        data[photoColumnIndex] = photoPath;
                    }

                    // Write updated line
                    writer.write(String.join(",", data) + System.lineSeparator());
                } else {
                    // Write unchanged line
                    writer.write(line + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

            // Show error to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Update Profile");
            alert.setContentText("Could not update your profile. Please try again later.\nError: " + e.getMessage());
            alert.showAndWait();

            return;
        } finally {
            // Close resources properly
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Replace old file with new file
        File inputFile = new File("src/main/resources/Students.csv");
        if (!tempFile.renameTo(inputFile)) {
            // If rename fails, try direct copy
            try {
                Files.copy(tempFile.toPath(), inputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                tempFile.delete();
            } catch (IOException e) {
                e.printStackTrace();

                // Show error to user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to Save Changes");
                alert.setContentText("Could not update your profile. Please try again later.");
                alert.showAndWait();
            }
        }
    }




    private void saveProfileChanges() {
        // Validate inputs
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Name and Email are required fields.");
            alert.showAndWait();
            return;
        }

        // Save profile changes to CSV
        updateStudentProfile(
                currentUsername,
                nameField.getText(),
                emailField.getText(),
                telephoneField.getText(),
                profilePhotoView.getImage() != null ? profilePhotoView.getImage().getUrl() : ""
        );

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Profile Updated");
        alert.setContentText("Your profile has been updated successfully.");
        alert.showAndWait();
    }

    private void setDefaultProfileImage() {
        // Set a default profile image
        profilePhotoView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/default_picture.png"))));
    }

    // Helper methods to interact with the CSV data
    private String getStudentProfilePhoto(String username) {
        // Implement method to retrieve profile photo URL from CSV
        // You'll need to add this to the CSVLoader class
        return ""; // Placeholder
    }

    private String getStudentName(String username) {
        // Implement method to get student name from CSV
        return ""; // Placeholder
    }

    private String getStudentEmail(String username) {
        // Implement method to get student email from CSV
        return ""; // Placeholder
    }

    private String getStudentTelephone(String username) {
        // Implement method to get student phone from CSV
        return ""; // Placeholder
    }

    private void updateStudentProfile(String username, String name, String email, String phone, String photoUrl) {
        // Add method to CSVLoader to update student profile
        // This should modify the CSV file with the new data
    }
}