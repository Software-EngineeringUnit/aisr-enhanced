package ais;

import java.io.IOException;
import javafx.fxml.FXML;

// AdminDashboardController class controls the functionality of the admin dashboard UI
public class AdminDashboardController {

    //Method to update the details of the admin itself
    @FXML
    private void updateMyDetails() throws IOException {
        App.setRoot("UpdateAdminDetails");
    }
    
    // Method to navigate to the RecruitRegistration tab to register a recruit
    @FXML
    private void registerRecruit() throws IOException {
        App.setRoot("RecruitRegistration");
    }
    
    // Method to update the details of a recruit
    @FXML
    private void updateRecruitDetails() throws IOException {
        App.setRoot("RecruitToUpdate");
    }
           
    // Method to navigate back to the AdminLogin tab
    @FXML
    public void goBack() throws IOException {
        App.setRoot("AdminLogin"); 
    }
}
