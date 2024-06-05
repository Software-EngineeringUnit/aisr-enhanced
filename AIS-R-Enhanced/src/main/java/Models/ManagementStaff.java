package Models;

// ManagementStaff class represents Management Staff members
public class ManagementStaff extends Staff {
    
    // Private member variables 
    private String managementLevel;
    private String branch;

    // Constructor for creating a ManagementStaff object
    public ManagementStaff(String fullName, String address, String phoneNumber, String email, String username, String password, String staffId, String managementLevel, String branch) {
        // Call the constructor of the superclass (Staff)
        super(fullName, address, phoneNumber, email, username, password, staffId);
        // Set the management level and branch specific to management staff
        this.managementLevel = managementLevel;
        this.branch = branch;
    }
    
    // Method to update the details of the Management Staff
    public void updateDetails(String fullName, String address, String phoneNumber, String email, String username, String password, String staffId, String managementLevel, String branch) {
        // Call the same method of the superclass (Staff) 
        super.updateDetails(fullName, address, phoneNumber, email, username, password, staffId);
        // Update the managementLevel and branch
        this.managementLevel = managementLevel;
        this.branch = branch;
    }

    // Method to format the Management Staff details to save in CSV file
    @Override
    public String toString() {
        // Call the toString() method of the superclass (Staff) 
        String superString = super.toString();
        // Appending NULL value for positionType and including management level and branch
        return String.format("%s,%s,%s,%s", superString, "NULL", managementLevel, branch);
    }
    
    // Getter for managementLevel
    public String getManagementLevel() {
        return managementLevel;
    }

    // Getter for branch
    public String getBranch() {
        return branch;
    }

}
