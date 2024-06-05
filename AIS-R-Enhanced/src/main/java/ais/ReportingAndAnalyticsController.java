package ais;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

//Controller class for the reporting and analytics page of the application.
public class ReportingAndAnalyticsController {

    // FXML elements
    @FXML
    private Label titleLabel;

    @FXML
    private Button residentButton;

    @FXML
    private Button adminButton;

    // Handles the action when the Display Recruits button is clicked
    @FXML
    private void displayRecruits() throws IOException {
        App.setRoot("DisplayRecruits");
    }

    // Handles the action when the Display Recruits Qualification button is clicked
    @FXML
    private void displayRecruitsQualification() throws IOException {
        App.setRoot("displayRecruitsQualification");
    }

    // Handles the action when the Display Manage Staff button is clicked
    @FXML
    private void displayManageStaff() throws IOException {
        App.setRoot("displayManageStaff");
    }

    // Handles the action when the button for Report and Analytics is clicked
    @FXML
    private void reportButtonClicked() throws IOException {
        App.setRoot("ReportingAndAnalytics");
    }

    // Method to navigate back to the StartPage
    public void goBack() throws IOException {
        App.setRoot("StartPage");
    }

    // Displays an alert with the given message
    private void showAlert(String message) {
        // Create an information alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
