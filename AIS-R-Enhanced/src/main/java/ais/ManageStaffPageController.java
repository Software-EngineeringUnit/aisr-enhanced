package ais;

import static ais.App.managementStaff;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

//Controller class for the Manage Staff Page.
public class ManageStaffPageController {

    // Method to handle the action when the "Register Management Staff" button is clicked
    @FXML
    private void manageRegisterClicked() throws IOException {
        // Redirect to the ManagementRegistration tab
        App.setRoot("ManagementRegistration");
    }

    // Method to handle the action when the "Management Login" button is clicked
    @FXML
    private void manageLoginClicked() throws IOException {
        // Check if any management staff is registered
        if (managementStaff.isEmpty()) {
            showAlert("No Management Staff", "No Management Staff has been registered yet");
        } else // Redirect to the ManagementLogin tab
        {
            App.setRoot("ManagementLogin");
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
