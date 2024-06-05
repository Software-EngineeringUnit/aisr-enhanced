package ais;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

//Controller class for the Management Dashboard Page
public class ManagementDashboardController {

    // ChoiceBoxes for selecting qualification level and location
    @FXML
    private ChoiceBox<String> qualificationLevelChoiceBox;
    @FXML
    private ChoiceBox<String> locationChoiceBox;

    // Initialize method to populate the qualificationLevelChoiceBox and locationChoiceBox
    @FXML
    private void initialize() {
        qualificationLevelChoiceBox.getItems().addAll("Bachelors", "Masters", "PhD");
        locationChoiceBox.getItems().addAll("Melbourne", "Sydney", "Brisbane", "Adelaide");
    }

    // String to store selected qualification level
    public static String qualification;
    // String to store selected location 
    public static String location;

    // Method to handle the action when the "UpdateManagementDetails" button is clicked
    @FXML
    private void updateManagementDetails() throws IOException {
        // Redirect to the UpdateManagementDetails tab
        App.setRoot("UpdateManagementDetails");
    }

    // Method to handle the action when the "Verify Recruit" button is clicked
    @FXML
    private void verifyRecruitClicked() throws IOException {
        // Redirect to the RecruitVerification tab
        App.setRoot("RecruitVerification");
    }

    // Method to handle the action when the "Review Recruits" button is clicked
    @FXML
    private void reviewRecruitsClicked() throws IOException {
        // Redirect to the ReviewAllRecruits tab
        App.setRoot("ReviewAllRecruits");
    }

    // Method to handle the action when the "Filter Recruits By InterviewDate" button is clicked
    @FXML
    private void recruitsByInterviewClicked() throws IOException {
        // Redirect to the RecruitsByInterviewDate tab
        App.setRoot("RecruitsByInterviewDate");
    }

    // Method to handle the action when the "Filter By Qualification" button is clicked
    @FXML
    private void filterByQualificationClicked() throws IOException {
        // Get selected qualification level
        qualification = qualificationLevelChoiceBox.getValue();
        // Check if a qualification level is selected or not
        if (qualification == null) {
            showAlert("No Level Selected", "Please select the qualification level!!");
        } else // Redirect to the RecruitbyQualification tab
        {
            App.setRoot("RecruitbyQualification");
        }
    }

    // Method to handle the action when the "Filter By Location" button is clicked
    @FXML
    private void filterByLocationClicked() throws IOException {
        // Get selected Location 
        location = locationChoiceBox.getValue();
        // Check if a Location is selected or not
        if (location == null) {
            showAlert("No Location Selected", "Please select the location!!");
        } else // Redirect to the RecruitsbyLocation tab
        {
            App.setRoot("RecruitsbyLocation");
        }
    }

    // Method to navigate back to the Management Login Page
    public void goBack() throws IOException {
        App.setRoot("ManagementLogin");
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
