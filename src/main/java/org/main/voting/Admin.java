package org.main.voting;

public class Admin {
    private String username;
    private String createdDate;
    
    public Admin(String username, String createdDate) {
        this.username = username;
        this.createdDate = createdDate;
    }
    
    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
