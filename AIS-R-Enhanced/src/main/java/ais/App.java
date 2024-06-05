package ais;

import Models.AdminStaff;
import Models.ManagementStaff;
import Models.Recruit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;

//Main Class of the Appluaction. The Program starts from this class.
public class App extends Application {

    //LinkedLists to store the admins, managementStaff and recruits
    public static LinkedList<AdminStaff> admins = new LinkedList<>();
    public static LinkedList<ManagementStaff> managementStaff = new LinkedList<>();
    public static LinkedList<Recruit> recruits = new LinkedList<>();

    private static Scene scene;

    //Database Details
    public static final String DB_URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "AIS_R_DB";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "ais_r_24";

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("StartPage"), 640, 480);
        stage.setScene(scene);
        stage.show();
        // Set resizable property of the stage to true
        stage.setResizable(true);
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/View/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    //Main method
    public static void main(String[] args) {
        // Ensure the database and tables are created
        ensureDatabaseAndTablesExist();

        // Load data from CSV files if the tables are empty
        loadDataFromCSVIfTableEmpty();

        // Load data from database into memory
        loadDataFromDatabase();

        int port = 12345; // Choose a port number for your server

        try {
            Server server = new Server(port);
            Thread serverThread = new Thread(server);
            serverThread.start();
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        launch();
    }

    //Checks and creates the database and tables if not exists
    private static void ensureDatabaseAndTablesExist() {
        try ( Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);  Statement stmt = conn.createStatement()) {

            // Create database if it does not exist
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);

            // Switch to the database
            stmt.executeUpdate("USE " + DB_NAME);

            // Create tables if they do not exist
            String createRecruitsTable = "CREATE TABLE IF NOT EXISTS recruits ("
                    + "Recruit_id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "FullName VARCHAR(255),"
                    + "Address VARCHAR(255),"
                    + "PhoneNumber VARCHAR(255),"
                    + "Email VARCHAR(255),"
                    + "Username VARCHAR(255),"
                    + "Password VARCHAR(255),"
                    + "InterviewDate VARCHAR(255),"
                    + "QualificationLevel VARCHAR(255),"
                    + "Location VARCHAR(255),"
                    + "Departments VARCHAR(255)"
                    + ")";
            stmt.executeUpdate(createRecruitsTable);

            //create admin staff table
            String createAdminStaffTable = "CREATE TABLE IF NOT EXISTS AdminStaff ("
                    + "Admin_id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "FullName VARCHAR(255),"
                    + "Address VARCHAR(255),"
                    + "PhoneNumber VARCHAR(255),"
                    + "Email VARCHAR(255),"
                    + "Username VARCHAR(255),"
                    + "Password VARCHAR(255),"
                    + "StaffID VARCHAR(255),"
                    + "PositionType VARCHAR(255)"
                    + ")";
            stmt.executeUpdate(createAdminStaffTable);

            //create ManagementStaff table
            String createManagementStaffTable = "CREATE TABLE IF NOT EXISTS ManagementStaff ("
                    + "Management_id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "FullName VARCHAR(255),"
                    + "Address VARCHAR(255),"
                    + "PhoneNumber VARCHAR(255),"
                    + "Email VARCHAR(255),"
                    + "Username VARCHAR(255),"
                    + "Password VARCHAR(255),"
                    + "StaffID VARCHAR(255),"
                    + "ManagementLevel VARCHAR(255),"
                    + "Branch VARCHAR(255)"
                    + ")";
            stmt.executeUpdate(createManagementStaffTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method to load data into database from csv files
    private static void loadDataFromCSVIfTableEmpty() {
        if (isTableEmpty("recruits")) {
            readRecruitsCSV();
        }
        if (isTableEmpty("AdminStaff")) {
            readAdminStaffCSV();
        }
        if (isTableEmpty("ManagementStaff")) {
            readManagementStaffCSV();
        }
    }

    //checks if the table is empty
    private static boolean isTableEmpty(String tableName) {
        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Method to load data from the database
    private static void loadDataFromDatabase() {
        readRecruitsFromDatabase();
        readAdminStaffFromDatabase();
        readManagementStaffFromDatabase();
    }

    //Method to read Recruits CSV file
    private static void readRecruitsCSV() {
        File file = new File("recruits.csv");
        if (file.exists()) {
            try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isFirstLine = true;

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    String[] fields = line.split(",");
                    Recruit recruit = new Recruit(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7]);

                    writeRecruitToDatabase(recruit);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to read Admin Staff CSV file
    private static void readAdminStaffCSV() {
        File file = new File("staff.csv");
        if (file.exists()) {
            try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isFirstLine = true;

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    String[] fields = line.split(",");
                    if (fields[8].equals("NULL")) {
                        AdminStaff admin = new AdminStaff(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7]);
                        admins.add(admin);
                        writeAdminStaffToDatabase(admin);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to read Management Staff CSV file
    private static void readManagementStaffCSV() {
        File file = new File("staff.csv");
        if (file.exists()) {
            try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isFirstLine = true;

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    String[] fields = line.split(",");
                    if (!fields[8].equals("NULL")) {
                        ManagementStaff management = new ManagementStaff(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[8], fields[9]);
                        managementStaff.add(management);
                        writeManagementStaffToDatabase(management);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to read Recruits from the database 
    private static void readRecruitsFromDatabase() {
        String query = "SELECT * FROM recruits";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Recruit recruit = new Recruit(
                        rs.getString("FullName"),
                        rs.getString("Address"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Email"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("InterviewDate"),
                        rs.getString("QualificationLevel"));
                recruit.setId(rs.getInt("Recruit_id"));
                recruits.add(recruit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Method to read admin staff from the database 
    private static void readAdminStaffFromDatabase() {
        String query = "SELECT * FROM AdminStaff";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                AdminStaff admin = new AdminStaff(
                        rs.getString("FullName"),
                        rs.getString("Address"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Email"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("StaffID"),
                        rs.getString("PositionType"));
                admin.setId(rs.getInt("Admin_id"));
                admins.add(admin);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Method to read Management Staff from the database 
    private static void readManagementStaffFromDatabase() {
        String query = "SELECT * FROM ManagementStaff";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                ManagementStaff management = new ManagementStaff(
                        rs.getString("FullName"),
                        rs.getString("Address"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Email"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("StaffID"),
                        rs.getString("ManagementLevel"),
                        rs.getString("Branch"));
                management.setId(rs.getInt("Management_id"));
                managementStaff.add(management);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Method to write Recruit into the database
    private static void writeRecruitToDatabase(Recruit recruit) {
        String query = "INSERT INTO recruits (FullName, Address, PhoneNumber, Email, Username, Password, InterviewDate, QualificationLevel) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, recruit.getFullName());
            stmt.setString(2, recruit.getAddress());
            stmt.setString(3, recruit.getPhoneNumber());
            stmt.setString(4, recruit.getEmail());
            stmt.setString(5, recruit.getUsername());
            stmt.setString(6, recruit.getPassword());
            stmt.setString(7, recruit.getInterviewDate());
            stmt.setString(8, recruit.getQualificationLevel());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        recruit.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Method to write Admin Staff into the database
    private static void writeAdminStaffToDatabase(AdminStaff admin) {
        String query = "INSERT INTO AdminStaff (FullName, Address, PhoneNumber, Email, Username, Password, StaffID, PositionType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getAddress());
            stmt.setString(3, admin.getPhoneNumber());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getUsername());
            stmt.setString(6, admin.getPassword());
            stmt.setString(7, admin.getStaffId());
            stmt.setString(8, admin.getPositionType());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        admin.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Method to write ManagementStaff into the database
    private static void writeManagementStaffToDatabase(ManagementStaff management) {
        String query = "INSERT INTO ManagementStaff (FullName, Address, PhoneNumber, Email, Username, Password, StaffID, ManagementLevel, Branch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);  PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, management.getFullName());
            stmt.setString(2, management.getAddress());
            stmt.setString(3, management.getPhoneNumber());
            stmt.setString(4, management.getEmail());
            stmt.setString(5, management.getUsername());
            stmt.setString(6, management.getPassword());
            stmt.setString(7, management.getStaffId());
            stmt.setString(8, management.getManagementLevel());
            stmt.setString(9, management.getBranch());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        management.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
