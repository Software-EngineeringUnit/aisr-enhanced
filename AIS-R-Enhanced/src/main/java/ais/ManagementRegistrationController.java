package ais;

import Models.ManagementStaff;
import static ais.App.DB_NAME;
import static ais.App.DB_PASSWORD;
import static ais.App.DB_URL;
import static ais.App.DB_USER;
import static ais.App.managementStaff;
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

// Controller class for the Management Registration GUI.
public class ManagementRegistrationController {

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
    private TextField staffIdField;

    @FXML
    private ChoiceBox<String> levelChoiceBox;

    @FXML
    private ChoiceBox<String> branchChoiceBox;

    // Initializes the ChoiceBox components.
    @FXML
    private void initialize() {

        //add the options for the dropdown list
        levelChoiceBox.getItems().addAll("Senior Manager", "Mid-level Manager", "Supervisor");
        branchChoiceBox.getItems().addAll("Melbourne", "Sydney", "Brisbane", "Adelaide");

        // Listens for changes in the phone number field to ensure it contains only numerical values.
        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number cannot be non-numerical.");
                phoneNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Listens for changes in the staff ID field to ensure it contains only numerical values.
        staffIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Staff ID cannot be non-numerical.");
                staffIdField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    // Handles the registration process for management staff.
    @FXML
    private void registerManagement() {

        //validate the inputs
        if (!isInputValid()) {
            return;
        }

        //get the inputs
        String fullName = fullNameField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String staffId = staffIdField.getText();
        String level = levelChoiceBox.getValue();
        String branch = branchChoiceBox.getValue();

        //create an object of ManagementStaff class
        ManagementStaff management = new ManagementStaff(fullName, address, phoneNumber, email, username, password, staffId, level, branch);

        //add in the list
        managementStaff.add(management);
        //save in the database
        saveManagementStaffToDatabase(management);

        //show a pop-up
        try {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Management staff registered successfully!");
            App.setRoot("ManageStaffPage");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to register management staff.");
        }

    }

    // Validates user input for registration.
    private boolean isInputValid() {

        //checks if name is valid
        if (!fullNameField.getText().isEmpty() && !isValidName(fullNameField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid name.");
            return false;
        }

        //checks if phone number is valid
        if (!phoneNumberField.getText().isEmpty() && !isValidContactNumber(phoneNumberField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone Number should be 10 digits long.");
            return false;
        }

        //checks if email is valid
        if (!emailField.getText().isEmpty() && !isValidEmail(emailField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        //checks if username is not empty
        if (usernameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username is mandatory to fill.");
            return false;
        }

        //checks if password is not empty
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

    // Validates an email address using a regular expression.
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9_+&*-]+(?:\\.[A-Za-z0-9_+&*-]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,7}$";
        return email.matches(emailRegex);
    }

    // Validates whether the given string represents a valid contact number.
    private boolean isValidContactNumber(String contactPhone) {
        return contactPhone.matches("^\\d{10}$");
    }

    // Validates a name using a regular expression.
    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    // Saves management details to a file.
    private void saveManagementDetails(ManagementStaff management) {
        try ( FileWriter writer = new FileWriter("staff.csv", true)) {
            writer.write(management.toString() + "\n");
            writer.flush();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Management staff registered successfully!");
            App.setRoot("ManageStaffPage");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to register management staff.");
        }
    }

    //Method to save the data in the database
    private static void saveManagementStaffToDatabase(ManagementStaff management) {
        //query to insert the data into the database
        String query = "INSERT INTO ManagementStaff (FullName, Address, PhoneNumber, Email, Username, Password, StaffID, ManagementLevel, Branch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, management.getFullName());
            stmt.setString(2, management.getAddress());
            stmt.setString(3, management.getPhoneNumber());
            stmt.setString(4, management.getEmail());
            stmt.setString(5, management.getUsername());
            stmt.setString(6, management.getPassword());
            stmt.setString(7, management.getStaffId());
            stmt.setString(8, management.getManagementLevel());
            stmt.setString(9, management.getBranch());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        management.setId(rs.getInt(1));
                        System.out.println(management.getId());
                    }

                }
            }
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

    // Navigates to the last page.
    public void goBack() throws IOException {
        App.setRoot("ManageStaffPage");
    }
}
