package ais;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

//Controller class for the start page of the application.
public class StartPageController {

    // FXML elements
    @FXML
    private Label titleLabel;

    @FXML
    private Button residentButton;

    @FXML
    private Button adminButton;

    // Handles the action when the management staff button is clicked
    @FXML
    private void manageButtonClicked() throws IOException {
        App.setRoot("ManageStaffPage");
    }

    // Handles the action when the admin staff button is clicked
    @FXML
    private void adminButtonClicked() throws IOException {
        App.setRoot("AdminStaffPage");
    }
    
    // Handles the action when the recruit button is clicked
    @FXML
    private void recruitButtonClicked() throws IOException {
        App.setRoot("RecruitPage");
    }
    
    // Handles the action when the button for Report and Analytics is clicked
    @FXML
    private void reportButtonClicked() throws IOException {
        App.setRoot("ReportingAndAnalytics");
    }
    
    // Handles the action when the button for Display Statistics is clicked
    @FXML
    private void displayStatisticsButtonClicked() throws IOException {
        App.setRoot("DisplayStatistics");
    }

    // Handles the action when the exit button is clicked
    @FXML
    private void exitButtonClicked() {
        // Create a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");

        // Show a confirmation message and exit if the user clicks OK
        showAlert("Thank you for using AIS-R.");

        System.exit(0);
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
