package ais;

import static ais.App.recruits;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

// Controller class for the Recruit Page
public class RecruitPageController {

    //boolean variable used while navigating to different tabs
    public static boolean isRecruit;

    // Method to handle the action when the "Register Recruit" button is clicked
    @FXML
    private void recruitRegisterClicked() throws IOException {

        isRecruit = true;
        // Redirect to the RecruitRegistration tab
        App.setRoot("RecruitRegistration");
    }

    // Method to handle the action when the "Update Recruit" button is clicked
    @FXML
    private void updateRecruitDetails() throws IOException {
        // Check if any recruit is registered
        if (recruits.isEmpty()) {
            showAlert("No Recruit", "No recruit has been registered yet");
        } else // Redirect to the RecruitLogin page
        {
            App.setRoot("RecruitLogin");
        }
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
