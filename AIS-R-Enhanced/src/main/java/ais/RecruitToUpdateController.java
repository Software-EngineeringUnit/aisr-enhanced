/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.TextField;

// Controller class to get the Recruit to be updated
public class RecruitToUpdateController {

    //Variable to store the index of the recruit to be updated
    public static int recruitIndex = 0;

    // FXML elements for username 
    @FXML
    private TextField recruitUserName;

    // Handles the action when the "Update Recruit" button is clicked
    @FXML
    private void updateRecruitDetails() throws IOException {
        // Get the entered username 
        String userName = recruitUserName.getText();

        // Validate username
        if (validateRecruit(userName)) {
            // Redirect to the UpdateRecruitDetails tab
            App.setRoot("UpdateRecruitDetails");
        } else {
            // Display an error message if username is incorrect
            showAlert("Invalid Recruit", "No such Recruit Exists.");
        }
    }

    // Method to navigate back to the AdminDashboard
    public void goBack() throws IOException {
        App.setRoot("AdminDashboard");
    }

    // Method to display an alert with the given title and content
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to validate recruit
    private boolean validateRecruit(String enteredUsername) {

        // Query the database to check if the entered username match
        String query = "SELECT * FROM Recruits WHERE Username = ? ";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, enteredUsername);
            try ( ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    recruitIndex = rs.getInt("Recruit_id");
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
