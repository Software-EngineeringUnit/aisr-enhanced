package Models;

import java.security.SecureRandom;

// AdminStaff class represents administrative staff members
public class AdminStaff extends Staff {
    
    // Private member variable to store the position type of admin staff
    private String positionType;
    
    //An object of SecureRandom to generate random tokens
    private static final SecureRandom secureRandom = new SecureRandom();

   
    // Constructor for creating an AdminStaff object
    public AdminStaff(String fullName, String address, String phoneNumber, String email, String username, String password, String staffId, String positionType) {
        // Call the constructor of the superclass (Staff) 
        super(fullName, address, phoneNumber, email, username, password, staffId);
        // Set the position type specific to admin staff
        this.positionType = positionType;
    }
    
    // Method to update the details of the admin
    public void updateDetails(String fullName, String address, String phoneNumber, String email, String username, String password, String staffId, String positionType) {
        // Call the same method of the superclass (Staff) 
        super.updateDetails(fullName, address, phoneNumber, email, username, password, staffId);
        // Update the position type specific to admin staff
        this.positionType = positionType;
    }

    //  Method to format the Admin Staff details so as to save in CSV file
    @Override
    public String toString() {
        // Call the toString() method of the superclass (Staff)
        String superString = super.toString();
        // Appending positionType and NULL values to store in CSV file
        return String.format("%s,%s,%s,%s", superString, positionType, "NULL", "NULL");
    }
    
    //getter for position type
    public String getPositionType() {
        return positionType;
    }
    
     // Method to generate a new 6-digit token
    public static String generateToken() {
        int token = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(token);
    }

}
