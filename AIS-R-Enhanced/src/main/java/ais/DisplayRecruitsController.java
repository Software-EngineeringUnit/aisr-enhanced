package ais;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Controller class for reviewing recruits sorted by some criterias.
public class DisplayRecruitsController {

    // FXML Table attributes
    @FXML
    private TableView<String[]> recruitsTableView;
    @FXML
    private TableColumn<String[], String> nameColumn;
    @FXML
    private TableColumn<String[], String> addressColumn;
    @FXML
    private TableColumn<String[], String> phoneColumn;
    @FXML
    private TableColumn<String[], String> emailColumn;
    @FXML
    private TableColumn<String[], String> usernameColumn;
    @FXML
    private TableColumn<String[], String> passwordColumn;
    @FXML
    private TableColumn<String[], String> interviewColumn;
    @FXML
    private TableColumn<String[], String> qualificationColumn;
    @FXML
    private TableColumn<String[], String> locationColumn;
    @FXML
    private TableColumn<String[], String> departmentsColumn;

    // Initialize the controller
    public void initialize() {
        initializeTableView(); // Initialize the TableView and TableColumn
        loadDataFromDatabase(); // Load data from the database
    }

    // Initializes the TableView and TableColumn
    private void initializeTableView() {
        // Associate each TableColumn with its corresponding data field
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));
        interviewColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[6]));
        qualificationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[7]));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[8]));
        departmentsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[9]));

    }

    // Loads data from the database and populates the TableView
    private void loadDataFromDatabase() {
        // Query to retrieve recruits sorted by last name in descending order and grouped by location
        String query = "SELECT * FROM Recruits ORDER BY SUBSTRING_INDEX(FullName, ' ', -1) DESC, Location";

        try ( Connection conn = DriverManager.getConnection(App.DB_URL + App.DB_NAME, App.DB_USER, App.DB_PASSWORD);  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {

            // Create a map to store recruits grouped by location
            Map<String, List<String[]>> recruitsByLocation = new HashMap<>();

            while (rs.next()) {
                // Retrieve data from the ResultSet
                String location = rs.getString("Location");
                String[] data = {
                    rs.getString("FullName"),
                    rs.getString("Address"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Email"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("InterviewDate"),
                    rs.getString("QualificationLevel"),
                    rs.getString("Location"),
                    rs.getString("Departments")
                };

                // Add data to the list of recruits for the corresponding location
                recruitsByLocation.computeIfAbsent(location, k -> new ArrayList<>()).add(data);
            }

            // Populate the TableView with grouped recruits
            recruitsByLocation.forEach((location, recruits) -> {
                // Add a placeholder row for the location
                String[] locationRow = new String[10];
                if (location == null) {
                    locationRow[0] = "Recruits with no assigned location";
                } else {
                    locationRow[0] = "Recruits with Location: " + location;
                }

                //add an empty row
                recruitsTableView.getItems().add(new String[10]);
                //add a haeding row to specify the location
                recruitsTableView.getItems().add(locationRow);

                // Add recruits for the location
                recruitsTableView.getItems().addAll(recruits);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Navigates back to the previous page.
    public void goBack() throws IOException {
        App.setRoot("ReportingAndAnalytics");
    }
}
