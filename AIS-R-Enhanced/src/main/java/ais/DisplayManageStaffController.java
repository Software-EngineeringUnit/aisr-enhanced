package ais;

import static ais.App.*;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Class to display the tab with all the Management Staff Data
public class DisplayManageStaffController {

    // TableView to display management staff data
    @FXML
    private TableView<String[]> managementStaffTableView;

     // TableColumn instances for different attributes of management staff
    @FXML
    private TableColumn<String[], String> fullNameColumn;

    @FXML
    private TableColumn<String[], String> addressColumn;

    @FXML
    private TableColumn<String[], String> phoneNumberColumn;

    @FXML
    private TableColumn<String[], String> emailColumn;

    @FXML
    private TableColumn<String[], String> usernameColumn;

    @FXML
    private TableColumn<String[], String> passwordColumn;

    @FXML
    private TableColumn<String[], String> staffIDColumn;

    @FXML
    private TableColumn<String[], String> managementLevelColumn;

    @FXML
    private TableColumn<String[], String> branchColumn;

    @FXML
    private void initialize() {
        // Initialize TableView with appropriate cell value factories
        initializeTableView();
        // Load data from the database into the TableView
        loadDataFromDatabase();
    }

    // Initialize TableView columns with cell value factories
    private void initializeTableView() {
        fullNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));
        staffIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[6]));
        managementLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[7]));
        branchColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[8]));
    }

    // Load data from the ManagementStaff table in the database into the TableView
    private void loadDataFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ManagementStaff");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String[] data = new String[9]; 
                data[0] = rs.getString("FullName");
                data[1] = rs.getString("Address");
                data[2] = rs.getString("PhoneNumber");
                data[3] = rs.getString("Email");
                data[4] = rs.getString("Username");
                data[5] = rs.getString("Password");
                data[6] = rs.getString("StaffID");
                data[7] = rs.getString("ManagementLevel");
                data[8] = rs.getString("Branch");
                //add to the table
                managementStaffTableView.getItems().add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Navigates back to the last page.
    public void goBack() throws IOException {
        App.setRoot("ReportingAndAnalytics");
    }
}
