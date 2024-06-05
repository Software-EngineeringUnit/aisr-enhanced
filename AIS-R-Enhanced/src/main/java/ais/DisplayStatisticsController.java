package ais;

import java.io.IOException;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//Controller class for displaying statistics
public class DisplayStatisticsController {

    //FXML Attributes
    @FXML
    private Label adminCountLabel;

    @FXML
    private Label managementCountLabel;

    @FXML
    private Label recruitCountLabel;

    public void initialize() {
        loadStatistics(); //invoke the method for displaying statistics
    }

    //Method to load the statistics
    private void loadStatistics() {
        try ( Connection conn = DriverManager.getConnection(App.DB_URL + App.DB_NAME, App.DB_USER, App.DB_PASSWORD)) {
            // Query to get the count of admin staff
            String adminQuery = "SELECT COUNT(*) AS admin_count FROM adminstaff";
            try ( PreparedStatement adminStmt = conn.prepareStatement(adminQuery);  ResultSet adminResult = adminStmt.executeQuery()) {
                if (adminResult.next()) {
                    int adminCount = adminResult.getInt("admin_count");
                    //display the count of admin Staff
                    adminCountLabel.setText("The number of Admim Staff Registered: " + String.valueOf(adminCount));
                }
            }

            // Query to get the count of management staff
            String managementQuery = "SELECT COUNT(*) AS management_count FROM managementstaff";
            try ( PreparedStatement managementStmt = conn.prepareStatement(managementQuery);  ResultSet managementResult = managementStmt.executeQuery()) {
                if (managementResult.next()) {
                    int managementCount = managementResult.getInt("management_count");
                    //display the count of Management Staff
                    managementCountLabel.setText("The number of Management Staff Registered: " + String.valueOf(managementCount));
                }
            }

            // Query to get the count of recruits
            String recruitQuery = "SELECT COUNT(*) AS recruit_count FROM recruits";
            try ( PreparedStatement recruitStmt = conn.prepareStatement(recruitQuery);  ResultSet recruitResult = recruitStmt.executeQuery()) {
                if (recruitResult.next()) {
                    int recruitCount = recruitResult.getInt("recruit_count");
                    //display the count of Recruits
                    recruitCountLabel.setText("The number of Recruits Registered: " + String.valueOf(recruitCount));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Navigates back to the StartPage
    public void goBack() throws IOException {
        App.setRoot("StartPage");
    }
}
