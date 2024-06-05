package ais;

import Models.Recruit;
import static ais.App.DB_PASSWORD;
import static ais.App.DB_URL;
import static ais.App.DB_USER;
import static ais.App.recruits;
import static ais.RecruitVerificationController.recruitIndex;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

//Controller class for assigning location and department to the Recruit 
public class RecruitAssigningController {

    // FXML attributes
    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label interviewDateLabel;
    @FXML
    private Label qualificationLabel;
    @FXML
    private ChoiceBox<String> locationChoiceBox;
    @FXML
    private CheckBox softwareEngineeringCheckBox;
    @FXML
    private CheckBox aerospaceEngineeringCheckBox;
    @FXML
    private CheckBox mechanicalEngineeringCheckBox;
    @FXML
    private CheckBox electronicsEngineeringCheckBox;
    @FXML
    private PieChart departmentPieChart;
    @FXML
    private PieChart locationPieChart;

    // Recruit instance
    private Recruit recruit;

    // HashSet to store departments selected
    HashSet<String> selectedDepartments;

    // Initializes the controller.
    @FXML
    private void initialize() {
        // Get the recruit at the specified index
        recruit = recruits.get(recruitIndex - 1);
        // Set recruit details in the GUI
        setRecruitDetails(recruit);
        // Initialize location ChoiceBox
        locationChoiceBox.getItems().addAll("Melbourne", "Sydney", "Brisbane", "Adelaide");
    }

    // Sets recruit details in the GUI.
    private void setRecruitDetails(Recruit recruit) {
        nameLabel.setText("Name: " + recruit.getFullName());
        addressLabel.setText("Address: " + recruit.getAddress());
        phoneNumberLabel.setText("Phone Number: " + recruit.getPhoneNumber());
        emailLabel.setText("Email: " + recruit.getEmail());
        usernameLabel.setText("Username: " + recruit.getUsername());
        passwordLabel.setText("Password: " + recruit.getPassword());
        interviewDateLabel.setText("Interview Date: " + recruit.getInterviewDate());
        qualificationLabel.setText("Qualification: " + recruit.getQualificationLevel());
    }

    // Handles the event when the "Save Details" button is clicked.
    @FXML
    private void saveDetailsClicked() throws IOException {
        //location selected
        String location = locationChoiceBox.getValue();

        // Collect selected departments
        selectedDepartments = new HashSet<String>();
        if (softwareEngineeringCheckBox.isSelected()) {
            selectedDepartments.add(softwareEngineeringCheckBox.getText());
        }
        if (aerospaceEngineeringCheckBox.isSelected()) {
            selectedDepartments.add(aerospaceEngineeringCheckBox.getText());
        }
        if (mechanicalEngineeringCheckBox.isSelected()) {
            selectedDepartments.add(mechanicalEngineeringCheckBox.getText());
        }
        if (electronicsEngineeringCheckBox.isSelected()) {
            selectedDepartments.add(electronicsEngineeringCheckBox.getText());
        }

        // Convert selected departments to a comma-separated string
        String departments = selectedDepartments.stream().collect(Collectors.joining(", "));

        // Check if location is selected or not
        if (location == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please Assign the Location.");
            return;
        }

        // Check if any departments are selected
        if (selectedDepartments.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please assign at least one department.");
            return;
        }

        // Assign location and departments to the recruit
        recruit.assignLocationAndDepartment(location, departments);

        //save to the database
        saveToDatabase(location, departments);

        //display the pie-chart
        populatePieCharts();
        //show the pop-up
        showAlert(Alert.AlertType.INFORMATION, "Assigned Successfully", "Location and department have been assigned to the recruit.");

    }

    //Method to save in the database 
    public void saveToDatabase(String location, String departments) {
        // Prepare the SQL statement to update the location and department columns
        String query = "UPDATE recruits SET Location = ?, Departments = ? WHERE Recruit_id = ?";

        try ( Connection conn = DriverManager.getConnection(DB_URL + App.DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            pstmt.setString(1, location);
            pstmt.setString(2, departments);
            pstmt.setInt(3, recruit.getId());

            // Execute the update statement
            int rowsUpdated = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error assigning location and department: " + e.getMessage());
        }
    }

    // Navigates back to the management dashboard.
    @FXML
    private void goBack() throws IOException {
        App.setRoot("ManagementDashboard");
    }

    // Shows an alert with the given type, title, and content.
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //method to populate and display PieCharts
    private void populatePieCharts() {
        // Clear existing data
        departmentPieChart.getData().clear();
        locationPieChart.getData().clear();

        // Maps to hold the counts of departments and locations
        Map<String, Integer> departmentCounts = new HashMap<>();
        Map<String, Integer> locationCounts = new HashMap<>();

        try ( Connection conn = DriverManager.getConnection(DB_URL + App.DB_NAME, DB_USER, DB_PASSWORD)) {
            // Query to fetch departments and locations
            String query = "SELECT Departments, Location FROM recruits";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();

            // Process the result set
            while (result.next()) {
                // Handle Departments
                String departments = result.getString("Departments");
                if (departments != null && !departments.isEmpty()) {
                    String[] departmentArray = departments.split(", ");
                    for (String department : departmentArray) {
                        if (selectedDepartments.contains(department)) {
                            departmentCounts.put(department, departmentCounts.getOrDefault(department, 0) + 1);
                        }
                    }
                }

                // Handle Location
                String location = result.getString("Location");
                if (location != null && !location.isEmpty()) {
                    locationCounts.put(location, locationCounts.getOrDefault(location, 0) + 1);
                }
            }

            // Update departmentPieChart
            for (Map.Entry<String, Integer> entry : departmentCounts.entrySet()) {
                String department = entry.getKey();
                int count = entry.getValue();
                PieChart.Data data = new PieChart.Data(department + " (" + count + ")", count);
                System.out.println(department + " (" + count + ")");
                departmentPieChart.getData().add(data);
            }

            // Enable labels for departmentPieChart
            departmentPieChart.setLabelsVisible(true);
            // Update locationPieChart
            for (Map.Entry<String, Integer> entry : locationCounts.entrySet()) {
                String location = entry.getKey();
                int count = entry.getValue();
                PieChart.Data data = new PieChart.Data(location + " (" + count + ")", count);
                System.out.println(location + " (" + count + ")");
                locationPieChart.getData().add(data);

            }

            // Enable labels for locationPieChart
            locationPieChart.setLabelsVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
