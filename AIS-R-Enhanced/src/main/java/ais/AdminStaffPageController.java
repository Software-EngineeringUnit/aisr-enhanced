package ais;

import static ais.App.DB_NAME;
import static ais.App.DB_PASSWORD;
import static ais.App.DB_URL;
import static ais.App.DB_USER;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

// Controller class for the Admin Staff Page
public class AdminStaffPageController {

    // Method to handle the action when the "Register Admin" button is clicked
    @FXML
    private void adminRegisterClicked() throws IOException {
        // Redirect to the AdminRegistration tab
        App.setRoot("AdminRegistration");
    }

    // Method to handle the action when the "Admin Login" button is clicked
    @FXML
    private void adminLoginClicked() throws IOException {
        // Check if any admin staff is registered
        if (isAnyAdminRegistered()) // Redirect to the AdminLogin page
        {
            App.setRoot("AdminLogin");
        } else {
            showAlert("No Admin Staff", "No admin staff has been registered yet");
        }
    }

    // Method to check if any admin staff is registered in the database
    private boolean isAnyAdminRegistered() {
        String query = "SELECT COUNT(*) FROM AdminStaff";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Method to navigate back to the StartPage
    public void goBack() throws IOException {
        App.setRoot("StartPage");
    }

    // Method to display an alert dialog with the given title and content
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
