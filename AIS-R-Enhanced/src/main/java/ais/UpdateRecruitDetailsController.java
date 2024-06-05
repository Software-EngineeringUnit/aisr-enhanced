package ais;

import Models.Recruit;
import static ais.AdminLoginController.adminName;
import static ais.App.DB_NAME;
import static ais.App.DB_PASSWORD;
import static ais.App.DB_URL;
import static ais.App.DB_USER;
import static ais.App.recruits;
import static ais.RecruitLoginController.isRecruitLogin;
import static ais.RecruitVerificationController.recruitIndex;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Controller class for updating recruits details
public class UpdateRecruitDetailsController {

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

    Recruit recruit;

    // Initializes the controller.
    @FXML
    private void initialize() {

        // Add qualification levels to the ChoiceBox
        qualificationLevelChoiceBox.getItems().addAll("Bachelors", "Masters", "PhD");

        // Disable the username and password fields
        usernameField.setDisable(true);
        passwordField.setDisable(true);

        // Add a listener to the phoneNumberField to allow only numerical input
        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number cannot contain non-numeric characters.");
                phoneNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        System.out.println("initial - isRecruitLogin is " + isRecruitLogin);

        recruit = recruits.get(recruitIndex - 1);
        setRecruitDetails(recruit);

    }

    // Sets recruit details in the GUI.
    public void setRecruitDetails(Recruit recruit) {
        fullNameField.setText(recruit.getFullName());
        addressField.setText(recruit.getAddress());
        phoneNumberField.setText(recruit.getPhoneNumber());
        emailField.setText(recruit.getEmail());
        usernameField.setText(recruit.getUsername());
        passwordField.setText(recruit.getPassword());
        interviewDatePicker.setValue(LocalDate.parse(recruit.getInterviewDate()));
        qualificationLevelChoiceBox.setValue(recruit.getQualificationLevel());
    }

    // Handles the event when the "Update" button is clicked.
    @FXML
    private void updateRecruitDetails() {
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
        recruit.updateDetails(fullName, address, phoneNumber, email, username, password, interviewDate.toString(), qualificationLevel);

        // Update recruit details in the database
        updateRecruitInDatabase(recruit);
        try {
            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Recruit's Details updated successfully!");
            //navigate to the previous page
            if (isRecruitLogin) {
                isRecruitLogin = false;
                App.setRoot("RecruitPage");
            } else {
                App.setRoot("AdminDashboard");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update recruit's details.");
        }
    }

    // Validates input fields.
    private boolean isInputValid() {

        ///check if all the fields are filled or not 
        if (fullNameField.getText().isEmpty() || addressField.getText().isEmpty()
                || phoneNumberField.getText().isEmpty() || emailField.getText().isEmpty()
                || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()
                || interviewDatePicker.getValue() == null || qualificationLevelChoiceBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all the fields.");
            return false;
        }

        // Validate full name
        if (!isValidName(fullNameField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid name of atleast two words.");
            return false;
        }

        // Validate phone number
        String phoneNumber = phoneNumberField.getText();
        if (!isValidPhoneNumber(phoneNumber)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Please enter a valid phone number.");
            return false;
        }

        // Validate email address
        String email = emailField.getText();
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        // Validate username
        if (!isValidName(usernameField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid Username.");
            return false;
        }

        // Validate interview date
        LocalDate interviewDate = interviewDatePicker.getValue();
        if (interviewDate.isBefore(LocalDate.now())) {
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

    // Validates a phone number.
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{10}$");
    }

    // Validates a name using a regular expression.
    private boolean isValidName(String name) {
        // Split the name into words
        String[] words = name.split("\\s+");
        // Check if there are at least two words and is valid
        return name.matches("^[a-zA-Z ]+$") && words.length >= 2;
    }

    //Method to update the Recruit's details in the database
    private static void updateRecruitInDatabase(Recruit recruit) {
        String query = "UPDATE recruits SET FullName = ?, Address = ?, "
                + "PhoneNumber = ?, Email = ?, InterviewDate = ?, QualificationLevel = ? "
                + "WHERE Recruit_id = ?";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, recruit.getFullName());
            stmt.setString(2, recruit.getAddress());
            stmt.setString(3, recruit.getPhoneNumber());
            stmt.setString(4, recruit.getEmail());
            stmt.setString(5, recruit.getInterviewDate());
            stmt.setString(6, recruit.getQualificationLevel());
            stmt.setInt(7, recruit.getId()); // Assuming ID is the primary key of the table

            int affectedRows = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Shows an alert with the given type, title, and content.
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Navigates back to the previous tab.
    public void goBack() throws IOException {
        System.out.println("goback - isRecruitLogin is " + isRecruitLogin);
        if (isRecruitLogin) {
            isRecruitLogin = false;
            App.setRoot("RecruitPage");

        } else {
            App.setRoot("AdminDashboard");
        }
    }
}
