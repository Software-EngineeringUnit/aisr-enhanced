package ais;

import static ais.App.DB_NAME;
import static ais.App.DB_PASSWORD;
import static ais.App.DB_URL;
import static ais.App.DB_USER;
import java.io.IOException;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

// Controller class for the Admin Login page
public class AdminLoginController {

    //Variable to store the index of the admin logging in
    public static int adminIndex = 0;
    // Variable to store the admin's name after successful login
    public static String adminName;

    // FXML elements for username and password input fields
    @FXML
    private TextField adminUserName;

    @FXML
    private PasswordField adminPassword;

    // Handles the action when the "Admin Login" button is clicked
    @FXML
    private void adminLoginClicked() throws IOException {
        // Get the entered username and password
        String userName = adminUserName.getText();
        String password = adminPassword.getText();

        // Validate admin login credentials
        if (validateAdminLogin(userName, password)) {
            // Redirect to the AdminDashboard tab on successful login
            App.setRoot("AdminDashboard");
        } else {
            // Display an error message if login fails
            showAlert("Login Error", "Invalid username or password. Please try again.");
        }
    }

    // Method to navigate back to the AdminStaffPage
    public void goBack() throws IOException {
        App.setRoot("AdminStaffPage");
    }

    // Method to display an alert with the given title and content
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to validate admin login credentials
    private boolean validateAdminLogin(String enteredUsername, String enteredPassword) {
        // Query the database to check if the entered username and password are valid or not
        String query = "SELECT * FROM AdminStaff WHERE Username = ? AND Password = ?";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, enteredUsername);
            stmt.setString(2, enteredPassword);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    adminIndex = rs.getInt("Admin_id");
                    return true; // Returns true if a matching record is found
                } else {
                    return false; // No matching record found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
