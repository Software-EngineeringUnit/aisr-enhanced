package ais;

import Models.Recruit;
import static ais.App.DB_NAME;
import static ais.App.DB_PASSWORD;
import static ais.App.DB_URL;
import static ais.App.DB_USER;
import static ais.App.recruits;
import java.io.IOException;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

// Controller class for verifying recruit login credentials.
public class RecruitVerificationController {

    // Index to keep track of the line number in the recruits file.
    public static int recruitIndex = 0;

    // FXML attributes
    @FXML
    private TextField recruitUserName;
    @FXML
    private PasswordField recruitPassword;

    Recruit recruit;
    String location;
    
    // Handles the event when the "Verify" button is clicked.
    @FXML
    private void verifyRecruitClicked() throws IOException {
        // Get the entered username and password
        String userName = recruitUserName.getText();
        String password = recruitPassword.getText();

        // Validate the login credentials
        if (validateRecruitLogin(userName, password)) {
            //get the recruit
            recruit = recruits.get(recruitIndex-1);
            // Redirect to the RecruitAssigning GUI if login is successful
            System.out.print(recruit.getLocation());
            if(location!=null)
                showAlert("Already Assigned", "Location and Department(s) are already assigned to this Recruit.");
            else
            App.setRoot("RecruitAssigning");
        } else {
            // Display an error message if login fails
            showAlert("Login Error", "Invalid username or password. Please try again.");
        }
    }

    // Navigates back to the management dashboard.
    public void goBack() throws IOException {
        App.setRoot("ManagementDashboard");
    }

    // Shows an alert with the given title and content.
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Validates the recruit login credentials.
    private boolean validateRecruitLogin(String enteredUserName, String enteredPassword) {
        String query = "SELECT Recruit_id, Location from recruits WHERE Username = ? AND Password = ?";
    
    try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, enteredUserName);
        stmt.setString(2, enteredPassword);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                 recruitIndex = rs.getInt("Recruit_id");
                 location = rs.getString("Location");
                    return true; // Recruit login successful
                
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Recruit login failed
      
    }
}
