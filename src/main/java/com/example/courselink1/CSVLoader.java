package com.example.courselink1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class CSVLoader {
    public static Map<String, String> users = new HashMap<>();
    public static Map<String, String> roles = new HashMap<>();
    public static Map<String, ObservableList<String>> studentCourses = new HashMap<>();

    public static void loadUsersWithRoles() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Students.csv")),
                StandardCharsets.UTF_8))) {
            String line;
            boolean firstRow = true;
            while ((line = br.readLine()) != null) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 12) {
                    String username = data[0].trim();
                    String password = data[11].trim();
                    String role;
                    if (username.toLowerCase().contains("admin")) {
                        role = "Admin";
                    } else if (username.toLowerCase().contains("faculty")) {
                        role = "Faculty";
                    } else {
                        role = "Student";
                        studentCourses.put(username, FXCollections.observableArrayList());
                    }
                    users.put(username, password);
                    roles.put(username, role);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> loadSubjects() {
        ObservableList<String> subjects = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Subjects.csv")),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                subjects.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public static ObservableList<String> loadCourses() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Courses.csv")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                courses.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static ObservableList<String> loadFaculty() {
        ObservableList<String> faculty = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Faculties.csv")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                faculty.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return faculty;
    }

    public static ObservableList<String> loadStudents() {
        ObservableList<String> students = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Students.csv")),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                students.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public static ObservableList<String> loadEvents() {
        ObservableList<String> events = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Events.csv")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                events.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    public static void addSubject(String subject) {
        try (PrintWriter out = new PrintWriter(new FileWriter("src/main/resources/Subjects.csv", true))) {
            out.println(subject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addFaculty() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter("src/main/resources/Faculties.csv", true))) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Faculty");

            dialog.setHeaderText("Enter Faculty ID:");
            String facultyID = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Name:");
            String name = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Degree:");
            String degree = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Research Interest:");
            String researchInterest = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Email:");
            String email = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Office Location:");
            String officeLocation = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Courses Offered:");
            String coursesOffered = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Password:");
            String password = dialog.showAndWait().orElse("");

            String line = String.join(",", facultyID, name, degree, researchInterest, email, officeLocation, coursesOffered, password);
            out.write(line);
            out.newLine();

            System.out.println("Faculty added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addCourse() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter("src/main/resources/Courses.csv", true))) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Course");

            dialog.setHeaderText("Enter Course Code:");
            String courseCode = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Course Name:");
            String courseName = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Subject Code:");
            String subjectCode = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Section Number:");
            String sectionNumber = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Capacity:");
            String capacity = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Lecture Time:");
            String lectureTime = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Final Exam Date/Time:");
            String examDateTime = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Location:");
            String location = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Teacher Name:");
            String teacherName = dialog.showAndWait().orElse("");

            String line = String.join(",", courseCode, courseName, subjectCode, sectionNumber, capacity, lectureTime,
                    examDateTime, location, teacherName);
            out.write(line);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addStudent() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter("src/main/resources/Students.csv", true))) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Student");

            dialog.setHeaderText("Enter Student ID:");
            String studentID = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Name:");
            String name = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Address:");
            String address = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Telephone:");
            String telephone = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Email:");
            String email = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Academic Level:");
            String academicLevel = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Current Semester:");
            String currentSemester = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Profile Photo:");
            String profilePhoto = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Subjects Registered:");
            String subjectsRegistered = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Thesis Title:");
            String thesisTitle = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Progress:");
            String progress = dialog.showAndWait().orElse("");

            dialog.setHeaderText("Enter Password:");
            String password = dialog.showAndWait().orElse("");

            String line = String.join(",", studentID, name, address, telephone, email, academicLevel, currentSemester,
                    profilePhoto, subjectsRegistered, thesisTitle, progress, password);
            out.write(line);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void assignCourseToUser(String username, String course) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter("src/main/resources/Students.csv", true))) {
            out.write(username + "," + course);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addEvent(String event) {
        try (PrintWriter out = new PrintWriter(new FileWriter("src/main/resources/Events.csv", true))) {
            out.println(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> loadCoursesForStudent(String username) {
        ObservableList<String> studentSpecificCourses = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/Courses.csv")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 1 && data[0].equalsIgnoreCase(username)) {
                    studentSpecificCourses.add(data[1].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentSpecificCourses;
    }

    public static void assignStudentToCourse(String studentId, String courseCode) {
        try (BufferedWriter out = new BufferedWriter(
                new FileWriter("src/main/resources/CourseAssignments.csv", true))) {
            String line = studentId + "," + courseCode;
            out.write(line);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> loadCourseAssignments() {
        ObservableList<String> assignments = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/CourseAssignments.csv")),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                assignments.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignments;
    }

    public static ObservableList<String> getStudentCourses(String studentId) {
        ObservableList<String> studentCourses = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(CSVLoader.class.getResourceAsStream("/CourseAssignments.csv")),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].equals(studentId)) {
                    studentCourses.add(data[1].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentCourses;
    }

    public static String getCurrentUser() {
        // Implementation to return the current user
        // You'll need to maintain state of who is logged in
        return null; // Replace with actual implementation
    }

    public static String getCurrentRole() {
        // Implementation to return the role of the current user
        // This should likely use the getCurrentUser method and look up the role
        String user = getCurrentUser();
        return user != null ? roles.get(user) : null;
    }
    public static void updateStudentProfile(String studentId, String name, String email, String phone, String photoUrl) {
        try {
            // Read all students
            File file = new File("src/main/resources/Students.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String header = reader.readLine();

            StringBuilder newContent = new StringBuilder();
            newContent.append(header).append("\n");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals(studentId)) {
                    // Update the fields
                    data[1] = name;
                    data[4] = email;
                    data[3] = phone;
                    data[7] = photoUrl;

                    // Reconstruct the line
                    newContent.append(String.join(",", data)).append("\n");
                } else {
                    newContent.append(line).append("\n");
                }
            }
            reader.close();

            // Write updated content back to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(newContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getStudentProfile(String studentId) {
        Map<String, String> profile = new HashMap<>();

        try {
            File file = new File("src/main/resources/Students.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String header = reader.readLine();
            String[] headerFields = header.split(",");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals(studentId)) {
                    for (int i = 0; i < headerFields.length && i < data.length; i++) {
                        profile.put(headerFields[i], data[i]);
                    }
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return profile;
    }

}
