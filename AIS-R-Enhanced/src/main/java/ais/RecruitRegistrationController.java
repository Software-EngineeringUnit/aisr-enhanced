package ais;

import Models.Recruit;
import static ais.App.DB_NAME;
import static ais.App.DB_PASSWORD;
import static ais.App.DB_URL;
import static ais.App.DB_USER;
import static ais.App.recruits;
import static ais.RecruitPageController.isRecruit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

// Controller class for registering recruits by the admin.
public class RecruitRegistrationController {

    // FXML attributes
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker interviewDatePicker;
    @FXML
    private ChoiceBox<String> qualificationLevelChoiceBox;

    // Initializes the controller.
    @FXML
    private void initialize() {

        // Add qualification levels to the ChoiceBox
        qualificationLevelChoiceBox.getItems().addAll("Bachelors", "Masters", "PhD");

        // Add a listener to the phoneNumberField to allow only numerical input
        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number cannot contain non-numeric characters.");
                phoneNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    // Handles the event when the "Register" button is clicked.
    @FXML
    private void registerRecruit() {

        //validates the inputs
        if (!isInputValid()) {
            return;
        }

        // Get input values
        String fullName = fullNameField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        LocalDate interviewDate = interviewDatePicker.getValue();
        String qualificationLevel = qualificationLevelChoiceBox.getValue();

        // Create a new recruit object
        Recruit recruit = new Recruit(fullName, address, phoneNumber, email, username, password, interviewDate.toString(), qualificationLevel);

        // Add the recruit to the list
        recruits.add(recruit);

        // Save recruit details in the database
        saveRecruittoDatabase(recruit);
        try {
            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Recruit registered successfully!");
            if (isRecruit) {
                isRecruit = false;
                App.setRoot("RecruitPage");
            } else // Navigate to admin dashboard
            {
                App.setRoot("AdminDashboard");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to register recruit.");
        }
    }

    // Method to save in the database
    private static void saveRecruittoDatabase(Recruit recruit) {

        // Insert recruit data into the database
        String query = "INSERT INTO recruits (fullName, address, phoneNumber, email, username, password, interviewDate, qualificationLevel) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                // Establish database connection
                 Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, recruit.getFullName());
            stmt.setString(2, recruit.getAddress());
            stmt.setString(3, recruit.getPhoneNumber());
            stmt.setString(4, recruit.getEmail());
            stmt.setString(5, recruit.getUsername());
            stmt.setString(6, recruit.getPassword());
            stmt.setString(7, recruit.getInterviewDate());
            stmt.setString(8, recruit.getQualificationLevel());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        recruit.setId(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error registering recruit: " + e.getMessage());
        }
    }

    // Validates input fields.
    private boolean isInputValid() {

        //check if all the fields are filled or not 
        if (fullNameField.getText().isEmpty() || addressField.getText().isEmpty()
                || phoneNumberField.getText().isEmpty() || emailField.getText().isEmpty()
                || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()
                || interviewDatePicker.getValue() == null || qualificationLevelChoiceBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all the fields.");
            return false;
        }

        // Validate full name
        if (!fullNameField.getText().isEmpty() && !isValidName(fullNameField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid name with at least two words.");
            return false;
        }

        // Validate phone number
        String phoneNumber = phoneNumberField.getText();
        if (!phoneNumber.isEmpty() && !isValidPhoneNumber(phoneNumber)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Please enter a valid phone number.");
            return false;
        }

        // Validate email address
        String email = emailField.getText();
        if (!email.isEmpty() && !isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        //checks if username is empty
        if (usernameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username is mandatory to fill.");
            return false;
        }

        //checks if password is empty
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password is mandatory to fill.");
            return false;
        }

        // Validate username
        if (!isValidUserName(usernameField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username cannot be non-alphabetical. Please enter a valid Username.");
            return false;
        }

        // Validate interview date
        LocalDate interviewDate = interviewDatePicker.getValue();
        if (interviewDatePicker.getValue() != null && interviewDate.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date", "Interview date cannot be in the past.");
            return false;
        }
        return true;
    }

    // Validates an email address using a regular expression.
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9_+&*-]+(?:\\.[A-Za-z0-9_+&*-]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,7}$";
        return email.matches(emailRegex);
    }
    // Method to validate a name

    private boolean isValidUserName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    // Validates a phone number.
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{10}$");
    }

    // Validates a name using a regular expression.
    private boolean isValidName(String name) {

        // Split the name into words
        String[] words = name.split("\\s+");
        // Check if there are at least two words
        return name.matches("^[a-zA-Z ]+$") && words.length >= 2;
    }

    // Shows an alert with the given type, title, and content.
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Navigates back to the admin dashboard.
    public void goBack() throws IOException {
        if (isRecruit) {
            isRecruit = false;
            App.setRoot("RecruitPage");
        } else {
            App.setRoot("AdminDashboard");
        }
    }
}
