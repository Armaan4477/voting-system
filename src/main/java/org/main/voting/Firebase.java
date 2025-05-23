package org.main.voting;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;

public class Firebase {
    private static String DATABASE_URL;
    private static String AUTH_PARAM;

    static {
        try (InputStream input = Firebase.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            {
                prop.load(input);
                DATABASE_URL = prop.getProperty("firebase.database.url");
                String authKey = prop.getProperty("firebase.auth.key");
                AUTH_PARAM = authKey.equals("none") ? "" : "?auth=" + authKey;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load Firebase configuration", ex);    
        }
    }

    private static final String CANDIDATES_NODE = "candidates";
    private static final String VOTERS_NODE = "voters";
    private static final String VOTES_NODE = "votes";
    private static final String ELECTION_CONFIG_NODE = "election_config";
    private static final String ADMINS_NODE = "admins";

    /**
     * Adds a new candidate to the Firebase database
     */
    public static String addCandidate(Candidate candidate) throws IOException {
        String jsonPayload = String.format(
            "{\"candidate_id\":%d,\"name\":\"%s\",\"party\":\"%s\",\"position\":\"%s\",\"vote_count\":%d}",
            candidate.getCandidateId(), candidate.getName(), candidate.getParty(), 
            candidate.getPosition(), candidate.getVoteCount()
        );

        return postToFirebase(CANDIDATES_NODE, jsonPayload);
    }
    
    /**
     * Registers a new voter in the system
     */
    public static String registerVoter(Voter voter) throws IOException {
        String jsonPayload = String.format(
            "{\"voter_id\":\"%s\",\"name\":\"%s\",\"phone\":\"%s\",\"address\":\"%s\",\"has_voted\":%b}",
            voter.getVoterId(), voter.getName(), voter.getPhone(), voter.getAddress(), voter.getHasVoted()
        );

        return postToFirebase(VOTERS_NODE, jsonPayload);
    }
    
    /**
     * Verifies if a voter is eligible to vote (registered and hasn't voted yet)
     */
    public static boolean canVote(String voterId) throws IOException {
        // First get all voters
        String response = getFromFirebase(VOTERS_NODE);
        
        if (response.equals("null")) {
            return false;
        }
        
        JSONObject jsonResponse = new JSONObject(response);
        
        for (String key : jsonResponse.keySet()) {
            JSONObject voterJson = jsonResponse.getJSONObject(key);
            if (voterJson.getString("voter_id").equals(voterId)) {
                return !voterJson.getBoolean("has_voted");
            }
        }
        
        return false;
    }
    
    /**
     * Records a vote from a voter to a candidate
     */
    public static void castVote(String voterId, int candidateId) throws IOException {
        // Find the voter key first
        String votersResponse = getFromFirebase(VOTERS_NODE);
        JSONObject voters = new JSONObject(votersResponse);
        String voterKey = null;
        
        for (String key : voters.keySet()) {
            JSONObject voter = voters.getJSONObject(key);
            if (voter.getString("voter_id").equals(voterId)) {
                voterKey = key;
                break;
            }
        }
        
        if (voterKey == null) {
            throw new IOException("Voter with ID " + voterId + " not found");
        }
        
        // 1. Record the vote first
        String votePayload = String.format("{\"voter_id\":\"%s\",\"candidate_id\":%d,\"timestamp\":%d}", 
            voterId, candidateId, System.currentTimeMillis());
        String voteResult = postToFirebase(VOTES_NODE, votePayload);
        
        if (voteResult.isEmpty()) {
            throw new IOException("Failed to record vote");
        }
        
        // 2. Mark the voter as having voted
        String voterUrl = DATABASE_URL + VOTERS_NODE + "/" + voterKey + "/has_voted.json" + AUTH_PARAM;
        try {
            putToFirebase(voterUrl, "true");
        } catch (IOException e) {
            // Log the error but continue, as the vote was already recorded
            System.err.println("Warning: Could not mark voter as voted, but vote was recorded: " + e.getMessage());
        }
        
        // 3. Increment the candidate's vote count
        try {
            incrementCandidateVoteCount(candidateId);
        } catch (IOException e) {
            // Log the error but continue, as the vote was already recorded
            System.err.println("Warning: Could not update candidate vote count, but vote was recorded: " + e.getMessage());
        }
    }
    
    /**
     * Increments the vote count for a specific candidate
     */
    private static void incrementCandidateVoteCount(int candidateId) throws IOException {
        // First get the current vote count
        String getUrl = DATABASE_URL + CANDIDATES_NODE + ".json" + AUTH_PARAM;
        URL url = URI.create(getUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        
        // Parse the response to find the candidate
        JSONObject candidates = new JSONObject(response.toString());
        String candidateKey = null;
        int currentVotes = 0;
        
        for (String key : candidates.keySet()) {
            JSONObject candidate = candidates.getJSONObject(key);
            if (candidate.getInt("candidate_id") == candidateId) {
                candidateKey = key;
                currentVotes = candidate.getInt("vote_count");
                break;
            }
        }
        
        if (candidateKey != null) {
            // Update the vote count
            String updateUrl = DATABASE_URL + CANDIDATES_NODE + "/" + candidateKey + "/vote_count.json" + AUTH_PARAM;
            putToFirebase(updateUrl, Integer.toString(currentVotes + 1));
        } else {
            throw new IOException("Candidate with ID " + candidateId + " not found");
        }
    }
    
    /**
     * Gets all candidates from the database
     */
    public static Map<String, Candidate> getAllCandidates() throws IOException {
        Map<String, Candidate> candidates = new HashMap<>();
        String response = getFromFirebase(CANDIDATES_NODE);
        
        if (!response.equals("null")) {
            JSONObject jsonResponse = new JSONObject(response);
            
            for (String key : jsonResponse.keySet()) {
                JSONObject candidateJson = jsonResponse.getJSONObject(key);
                Candidate candidate = new Candidate(
                    candidateJson.getInt("candidate_id"),
                    candidateJson.getString("name"),
                    candidateJson.getString("party"),
                    candidateJson.getString("position"),
                    candidateJson.getInt("vote_count")
                );
                candidates.put(key, candidate);
            }
        }
        
        return candidates;
    }
    
    /**
     * Gets all voters from the database
     */
    public static Map<String, Voter> getAllVoters() throws IOException {
        Map<String, Voter> voters = new HashMap<>();
        String response = getFromFirebase(VOTERS_NODE);
        
        if (!response.equals("null")) {
            JSONObject jsonResponse = new JSONObject(response);
            
            for (String key : jsonResponse.keySet()) {
                JSONObject voterJson = jsonResponse.getJSONObject(key);
                Voter voter = new Voter(
                    voterJson.getString("voter_id"),
                    voterJson.getString("name"),
                    voterJson.getString("phone"),
                    voterJson.getString("address"),
                    voterJson.getBoolean("has_voted")
                );
                voters.put(key, voter);
            }
        }
        
        return voters;
    }
    
    /**
     * Gets a single voter by ID
     */
    public static Voter getVoterById(String voterId) throws IOException {
        // Get all voters and find the one with matching voter_id
        String response = getFromFirebase(VOTERS_NODE);
        
        if (response.equals("null")) {
            return null;
        }
        
        JSONObject jsonResponse = new JSONObject(response);
        
        for (String key : jsonResponse.keySet()) {
            JSONObject voterJson = jsonResponse.getJSONObject(key);
            if (voterJson.getString("voter_id").equals(voterId)) {
                return new Voter(
                    voterJson.getString("voter_id"),
                    voterJson.getString("name"),
                    voterJson.getString("phone"),
                    voterJson.getString("address"),
                    voterJson.getBoolean("has_voted")
                );
            }
        }
        
        return null;
    }
    
    /**
     * Sets or updates election configuration
     */
    public static void setElectionConfig(String electionTitle, String startDate, String endDate, boolean isActive) 
            throws IOException {
        String jsonPayload = String.format(
            "{\"title\":\"%s\",\"start_date\":\"%s\",\"end_date\":\"%s\",\"is_active\":%b}",
            electionTitle, startDate, endDate, isActive
        );
        
        String urlString = DATABASE_URL + ELECTION_CONFIG_NODE + ".json" + AUTH_PARAM;
        putToFirebase(urlString, jsonPayload);
    }
    
    /**
     * Gets the current election configuration
     */
    public static JSONObject getElectionConfig() throws IOException {
        String response = getFromFirebase(ELECTION_CONFIG_NODE);
        if (response.equals("null")) {
            return new JSONObject();
        }
        return new JSONObject(response);
    }
    
    /**
     * Authenticates an admin user
     */
    public static boolean authenticateAdmin(String username, String password) throws IOException {
        // Get all admins
        String response = getFromFirebase(ADMINS_NODE);
        
        if (response.equals("null")) {
            // If there are no admins, create a default admin (for initial setup)
            if (username.equalsIgnoreCase("admin") && password.equals("admin")) {
                addAdmin("admin", "admin");
                return true;
            }
            return false;
        }
        
        JSONObject jsonResponse = new JSONObject(response);
        
        // Check if username exists and password matches
        for (String key : jsonResponse.keySet()) {
            JSONObject adminJson = jsonResponse.getJSONObject(key);
            if (adminJson.getString("username").equals(username)) {
                // Direct password comparison for existing accounts in database
                if (adminJson.getString("password").equals(password)) {
                    return true;
                }
                
                // Also try hashed password comparison for newer accounts
                String hashedPassword = hashPassword(password);
                return adminJson.getString("password").equals(hashedPassword);
            }
        }
        
        return false;
    }
    
    /**
     * Adds a new admin to the system
     */
    public static void addAdmin(String username, String password) throws IOException {
        // Hash the password
        String hashedPassword = hashPassword(password);
        
        // Format the current date
        String createdDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        
        String jsonPayload = String.format(
            "{\"username\":\"%s\",\"password\":\"%s\",\"created_date\":\"%s\"}",
            username, hashedPassword, createdDate
        );
        
        postToFirebase(ADMINS_NODE, jsonPayload);
    }
    
    /**
     * Checks if an admin username already exists
     */
    public static boolean checkAdminExists(String username) throws IOException {
        // Get all admins
        String response = getFromFirebase(ADMINS_NODE);
        
        if (response.equals("null")) {
            return false;
        }
        
        JSONObject jsonResponse = new JSONObject(response);
        
        // Check if username exists
        for (String key : jsonResponse.keySet()) {
            JSONObject adminJson = jsonResponse.getJSONObject(key);
            if (adminJson.getString("username").equals(username)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets all admins from the database
     */
    public static Map<String, Admin> getAllAdmins() throws IOException {
        Map<String, Admin> admins = new HashMap<>();
        String response = getFromFirebase(ADMINS_NODE);
        
        if (!response.equals("null")) {
            JSONObject jsonResponse = new JSONObject(response);
            
            for (String key : jsonResponse.keySet()) {
                JSONObject adminJson = jsonResponse.getJSONObject(key);
                Admin admin = new Admin(
                    adminJson.getString("username"),
                    adminJson.getString("created_date")
                );
                admins.put(key, admin);
            }
        }
        
        return admins;
    }
    
    /**
     * Hash a password with SHA-256
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    // Helper methods for HTTP requests
    private static String postToFirebase(String node, String jsonPayload) throws IOException {
        String urlString = DATABASE_URL + node + ".json" + AUTH_PARAM;
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 300) 
                         ? connection.getInputStream() 
                         : connection.getErrorStream();
                         
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
        }

        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
            System.err.println("Firebase request failed with response code: " + responseCode + 
                              "\nResponse: " + response);
            return "";
        }

        return response.toString();
    }
    
    private static void putToFirebase(String urlString, String data) throws IOException {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = data.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            InputStream errorStream = connection.getErrorStream();
            StringBuilder errorResponse = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
            }
            throw new IOException("PUT request failed with code " + responseCode + ": " + errorResponse);
        }
    }
    
    private static String getFromFirebase(String node) throws IOException {
        String urlString = DATABASE_URL + node + ".json" + AUTH_PARAM;
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 300) 
                         ? connection.getInputStream() 
                         : connection.getErrorStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to get data. Response code: " + responseCode);
        }

        return response.toString();
    }
}
