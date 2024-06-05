package Models;

// Recruit class represents a recruit for a job position
public class Recruit {
    // Private member variables to store recruit details
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private String interviewDate;
    private String qualificationLevel;
    private String department;
    private String location;
    private int id;
    
    
    // Constructor for creating a Recruit object
    public Recruit(String fullName, String address, String phoneNumber, String email, String username, String password, String interviewDate, String qualificationLevel) {
        // Initialize recruit's basic information
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
        this.interviewDate = interviewDate;
        this.qualificationLevel = qualificationLevel;
    }
    
    // Method to assign department and location to the recruit
    public void assignLocationAndDepartment(String department, String location) {
        // Set the department and location for the recruit
        this.department = department;
        this.location = location;
    }
    
    // Method to update the details of the recruit
    public void updateDetails(String fullName, String address, String phoneNumber, String email, String username, String password, String interviewDate, String qualificationLevel) {
        // Update recruit's  information
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
        this.interviewDate = interviewDate;
        this.qualificationLevel = qualificationLevel;
    }
    
    //Method to return and format the Recruit's details to save in CSV file
    public String toString() {
        // Format and return the string representation of the Recruit object
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                fullName, address, phoneNumber, email, username, password, interviewDate, qualificationLevel);
    }
    
    // Getter method for fullName attribute
    public String getFullName() {
        return fullName;
    }

    // Getter method for address attribute
    public String getAddress() {
        return address;
    }

    // Getter method for phoneNumber attribute
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Getter method for email attribute
    public String getEmail() {
        return email;
    }

    // Getter method for username attribute
    public String getUsername() {
        return username;
    }

    // Getter method for password attribute
    public String getPassword() {
        return password;
    }

    // Getter method for interviewDate attribute
    public String getInterviewDate() {
        return interviewDate;
    }

    // Getter method for qualificationLevel attribute
    public String getQualificationLevel() {
        return qualificationLevel;
    }

    // Getter method for department attribute
    public String getDepartment() {
        return department;
    }

    // Getter method for location attribute
    public String getLocation() {
        return location;
    }
    
    //Setter method for id attribute
    public void setId(int id)
    {
        this.id=id;
    }
    
    // Getter method for id attribute
    public int getId() {
        return id;
    }
}
