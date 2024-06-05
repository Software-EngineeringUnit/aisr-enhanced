package ais;

import java.io.IOException;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller class for the Management Login page.
 */
public class ManagementLoginController {

    //variable to store index of the management staff logging in
    public static int manageIndex = 0;

    // Text field for entering the management username
    @FXML
    private TextField manageUserName;

    // Password field for entering the management password
    @FXML
    private PasswordField managePassword;

    // Method to handle the action when the "Manage Login" button is clicked
    @FXML
    private void manageLoginClicked() throws IOException {
        // Get the entered username and password
        String userName = manageUserName.getText();
        String password = managePassword.getText();

        // Validate the management login credentials
        if (validateManageLogin(userName, password)) {
            // Redirect to the Management Dashboard GUI
            App.setRoot("ManagementDashboard");
        } else {
            // Display an error message if the login credentials are invalid
            showAlert("Login Error", "Invalid username or password. Please try again.");
        }
    }

    // Method to display an alert dialog with the given title and content
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to navigate back to the Manage Staff Page
    public void goBack() throws IOException {
        App.setRoot("ManageStaffPage");
    }

    // Method to validate the management login credentials in the database
    private boolean validateManageLogin(String manageUserName, String managePassword) {
        //query to validate the given credentials in the database
        String query = "SELECT * FROM ManagementStaff WHERE Username = ? AND Password = ?";

        try ( Connection conn = DriverManager.getConnection(App.DB_URL + App.DB_NAME, App.DB_USER, App.DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, manageUserName);
            stmt.setString(2, managePassword);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Management Staff exists in the database
                    manageIndex = rs.getInt("Management_id");
                    System.out.println(manageIndex);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Management login failed
    }
}
