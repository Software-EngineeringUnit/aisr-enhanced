package ais;

import Models.AdminStaff;
import static ais.App.DB_NAME;
import static ais.App.DB_PASSWORD;
import static ais.App.DB_URL;
import static ais.App.DB_USER;
import static ais.App.admins;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Controller class for the Admin Registration Tab
public class AdminRegistrationController {

    // FXML elements for user input
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

    @FXML
    private void initialize() {
        // Populate the choice box with position types
        positionTypeChoiceBox.getItems().addAll("Full-time", "Part-time", "Volunteer");

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
    }

    // Method to handle admin registration
    @FXML
    private void registerAdmin() {

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

        // Create an admin staff object
        AdminStaff admin = new AdminStaff(fullName, address, phoneNumber, email, username, password, staffId, positionType);

        // Add the admin to the list of admins
        admins.add(admin);

        // Save admin details to database
        saveAdminToDatabase(admin);
    }

    //Method to save the Admin in the Database
    private static void saveAdminToDatabase(AdminStaff admin) {
        //Query to insert the Admin in the Databse
        String query = "INSERT INTO AdminStaff (FullName, Address, PhoneNumber, Email, Username, Password, StaffID, PositionType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getAddress());
            stmt.setString(3, admin.getPhoneNumber());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getUsername());
            stmt.setString(6, admin.getPassword());
            stmt.setString(7, admin.getStaffId());
            stmt.setString(8, admin.getPositionType());

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

        // Validate UserName
        if (usernameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username is mandatory to fill.");
            return false;
        }

        // Validate Password
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password is mandatory to fill.");
            return false;
        }
        
        //checks if username is valid
        if (!isValidName(usernameField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username should be alphabetical.");
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
        App.setRoot("AdminStaffPage");
    }

    // Method to save admin details to file
    private void saveAdminDetails(AdminStaff admin) {
        try ( FileWriter writer = new FileWriter("staff.csv", true)) {
            writer.write(admin.toString() + "\n");
            writer.flush();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Admin staff registered successfully!");
            // Navigate back to AdminStaffPage after successful registration
            App.setRoot("AdminStaffPage");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to register admin staff.");
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
