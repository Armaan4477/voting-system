package org.main.voting;

public class Voter {
    private String voterId;
    private String name;
    private String phone;
    private String address;
    private boolean hasVoted;

    public Voter(String voterId, String name, String phone, String address) {
        this.voterId = voterId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.hasVoted = false;
    }

    public Voter(String voterId, String name, String phone, String address, boolean hasVoted) {
        this.voterId = voterId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.hasVoted = hasVoted;
    }

    // Getters and setters
    public String getVoterId() { return voterId; }
    public void setVoterId(String voterId) { this.voterId = voterId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public boolean getHasVoted() { return hasVoted; }
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }
}
