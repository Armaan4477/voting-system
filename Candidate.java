public class Candidate {// Candidate class to store information about a candidate
    private String name;// Name of the candidate
    private String background;// Background of the candidate
    private String experience;// Experience of the candidate
    private String policies;// Policies of the candidate
    private String additionalInfo;  // Add more information as needed

    public Candidate(String name, String background, String experience, String policies, String additionalInfo) {// Constructor for the Candidate class
        this.name = name;// Initialize the name of the candidate
        this.background = background;// Initialize the background of the candidate
        this.experience = experience;// Initialize the experience of the candidate
        this.policies = policies;// Initialize the policies of the candidate
        this.additionalInfo = additionalInfo;// Initialize the additional information of the candidate
    }

    public void setName(String name) {// Setter for the name of the candidate
        this.name = name;// Set the name of the candidate
    }

    public void setBackground(String background) {// Setter for the background of the candidate
        this.background = background;// Set the background of the candidate
    }

    public void setExperience(String experience) {// Setter for the experience of the candidate
        this.experience = experience;// Set the experience of the candidate
    }

    public void setPolicies(String policies) {// Setter for the policies of the candidate
        this.policies = policies;// Set the policies of the candidate
    }

    public void setAdditionalInfo(String additionalInfo) {// Setter for the additional information of the candidate
        this.additionalInfo = additionalInfo;// Set the additional information of the candidate
    }

    public String getName() {// Getter for the name of the candidate
        return name;// Return the name of the candidate
    }

    public String getBackground() {// Getter for the background of the candidate
        return background;// Return the background of the candidate
    }

    public String getExperience() {// Getter for the experience of the candidate
        return experience;// Return the experience of the candidate
    }

    public String getPolicies() {// Getter for the policies of the candidate
        return policies;// Return the policies of the candidate
    }

    public String getAdditionalInfo() {// Getter for the additional information of the candidate
        return additionalInfo;// Return the additional information of the candidate
    }
}
