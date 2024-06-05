package ais;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.sql.*;

// Controller class for reviewing recruits sorted by interview date.
public class RecruitsByInterviewDateController {

    // FXML attributes
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

    private void loadDataFromDatabase() {

        // Query to retrieve data from the database, ordered by interview date
        String query = "SELECT * FROM recruits ORDER BY InterviewDate";

        try ( Connection conn = DriverManager.getConnection(App.DB_URL + App.DB_NAME, App.DB_USER, App.DB_PASSWORD);  PreparedStatement pstmt = conn.prepareStatement(query);  ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Retrieve data from the ResultSet and add it to the TableView
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
                recruitsTableView.getItems().add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Navigates back to the management dashboard.
    public void goBack() throws IOException {
        App.setRoot("ManagementDashboard");
    }
}
