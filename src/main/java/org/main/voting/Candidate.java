package org.main.voting;

public class Candidate {
    private int candidateId;
    private String name;
    private String party;
    private String position;
    private int voteCount;

    public Candidate(int candidateId, String name, String party, String position) {
        this.candidateId = candidateId;
        this.name = name;
        this.party = party;
        this.position = position;
        this.voteCount = 0;
    }

    public Candidate(int candidateId, String name, String party, String position, int voteCount) {
        this.candidateId = candidateId;
        this.name = name;
        this.party = party;
        this.position = position;
        this.voteCount = voteCount;
    }

    // Getters and setters required for JavaFX PropertyValueFactory
    public int getCandidateId() { return candidateId; }
    public void setCandidateId(int candidateId) { this.candidateId = candidateId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
    
    public void incrementVote() {
        this.voteCount++;
    }
}
