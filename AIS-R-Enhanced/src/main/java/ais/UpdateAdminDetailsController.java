package ais;

import Models.AdminStaff;
import static ais.AdminLoginController.adminIndex;
import static ais.App.DB_NAME;
import static ais.App.DB_PASSWORD;
import static ais.App.DB_URL;
import static ais.App.DB_USER;
import static ais.App.admins;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Controller class for the Admin Details Updation Tab
public class UpdateAdminDetailsController {

    // FXML elements
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
    private TextField staffIdField;

    @FXML
    private ChoiceBox<String> positionTypeChoiceBox;

    AdminStaff admin;

    @FXML
    private void initialize() {
        // Populate the choice box with position types
        positionTypeChoiceBox.getItems().addAll("Full-time", "Part-time", "Volunteer");

        // Disable the username and password fields
        usernameField.setDisable(true);
        passwordField.setDisable(true);

        // Add a listener to the phoneNumberField to check for non-numeric values
        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number cannot be non-numerical.");
                phoneNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Add a listener to the staffIdField to check for non-numeric values
        staffIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Staff ID cannot be non-numerical.");
                staffIdField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        admin = admins.get(adminIndex - 1);
        setExistingDetails(admin);

    }

    // Sets recruit details in the GUI.
    public void setExistingDetails(AdminStaff admin) {
        fullNameField.setText(admin.getFullName());
        addressField.setText(admin.getAddress());
        phoneNumberField.setText(admin.getPhoneNumber());
        emailField.setText(admin.getEmail());
        usernameField.setText(admin.getUsername());
        passwordField.setText(admin.getPassword());
        staffIdField.setText(admin.getStaffId());
        positionTypeChoiceBox.setValue(admin.getPositionType());
    }

    // Method to handle admin details' updation
    @FXML
    private void updateAdmin() {

        // Validate input fields
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
        String staffId = staffIdField.getText();
        String positionType = positionTypeChoiceBox.getValue();

        // Update the admin's details
        admin.updateDetails(fullName, address, phoneNumber, email, username, password, staffId, positionType);

        // update admin details in the database
        updateAdminStaffToDatabase(admin);

        //show pop-up
        try {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Admin's Details have been updated!");
            // Navigate back to AdminStaffPage after successful registration
            App.setRoot("AdminDashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update admin staff's details.");
        }
    }

    // Method to validate input fields
    private boolean isInputValid() {

        // Validate fullname
        if (!fullNameField.getText().isEmpty() && !isValidName(fullNameField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name should be alphabetical.");
            return false;
        }

        // Validate phone number
        if (!phoneNumberField.getText().isEmpty() && !isValidContactNumber(phoneNumberField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone Number should be 10 digits long.");
            return false;
        }

        // Validate email
        if (!emailField.getText().isEmpty() && !isValidEmail(emailField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        return true;
    }

    // Method to validate an email address
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9_+&*-]+(?:\\.[A-Za-z0-9_+&*-]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,7}$";
        return email.matches(emailRegex);
    }

    // Method to validate a contact number
    private boolean isValidContactNumber(String contactPhone) {
        return contactPhone.matches("^\\d{10}$");
    }

    // Method to validate a name
    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    // Method to navigate back to the AdminStaffPage
    public void goBack() throws IOException {
        App.setRoot("AdminDashboard");
    }

    // Method to update Admin's details in the database
    private static void updateAdminStaffToDatabase(AdminStaff admin) {
        String query = "UPDATE AdminStaff SET FullName=?, Address=?, PhoneNumber=?, Email=?, StaffID=?, PositionType=? WHERE Admin_id=?";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getAddress());
            stmt.setString(3, admin.getPhoneNumber());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getStaffId());
            stmt.setString(6, admin.getPositionType());
            stmt.setInt(7, admin.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        admin.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display an alert
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
