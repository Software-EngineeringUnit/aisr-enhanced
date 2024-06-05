package Models;

// Staff class represents a staff member
public class Staff {

    // Private member variables to store staff information
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private String staffId;
    private int id;

    // Constructor for creating a Staff object
    public Staff(String fullName, String address, String phoneNumber, String email, String username, String password, String staffId) {
        // Initialize staff's information
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
        this.staffId = staffId;
    }

    // Method to update the details of Staff Member
    public void updateDetails(String fullName, String address, String phoneNumber, String email, String username, String password, String staffId) {
        // Update staff's information
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
        this.staffId = staffId;
    }

    // Method to format and return the Recruit details to save in CSV file
    public String toString() {
        // Format and return the string representation of the Staff object
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                fullName, address, phoneNumber, email, username, password, staffId);
    }

    // Getter method for FullName attribute
    public String getFullName() {
        return fullName;
    }

    // Getter method for Address attribute
    public String getAddress() {
        return address;
    }

    // Getter method for PhoneNumber attribute
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Getter method for Email attribute
    public String getEmail() {
        return email;
    }

    // Getter method for Username attribute
    public String getUsername() {
        return username;
    }

    // Getter method for Password attribute
    public String getPassword() {
        return password;
    }

    // Getter method for StaffId attribute
    public String getStaffId() {
        return staffId;
    }

    // Setter method for Id attribute
    public void setId(int id) {
        this.id = id;
    }

    // Getter method for Id attribute
    public int getId() {
        return id;
    }

}
