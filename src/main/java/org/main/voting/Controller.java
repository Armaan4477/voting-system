package org.main.voting;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.json.JSONObject;

public class Controller {
    // Vote Tab Controls
    @FXML private TextField voterIdField;
    @FXML private Button verifyVoterButton;
    @FXML private Label voterStatusLabel;
    @FXML private TableView<Candidate> candidateTable;
    @FXML private TableColumn<Candidate, Integer> candidateIdColumn;
    @FXML private TableColumn<Candidate, String> candidateNameColumn;
    @FXML private TableColumn<Candidate, String> partyColumn;
    @FXML private TableColumn<Candidate, String> positionColumn;
    @FXML private TableColumn<Candidate, Integer> voteColumn;
    @FXML private Button castVoteButton;
    @FXML private Button refreshCandidatesButton;
    
    // Candidate Management Tab
    @FXML private TextField candidateIdField;
    @FXML private TextField candidateNameField;
    @FXML private TextField partyField;
    @FXML private TextField positionField;
    @FXML private Button addCandidateButton;
    @FXML private TableView<Candidate> candidateManageTable;
    @FXML private TableColumn<Candidate, Integer> candidateIdManageColumn;
    @FXML private TableColumn<Candidate, String> candidateNameManageColumn;
    @FXML private TableColumn<Candidate, String> partyManageColumn;
    @FXML private TableColumn<Candidate, String> positionManageColumn;
    @FXML private TableColumn<Candidate, Integer> voteCountManageColumn;
    
    // Voter Registration Tab
    @FXML private TextField registerVoterIdField;
    @FXML private TextField voterNameField;
    @FXML private TextField voterPhoneField;
    @FXML private TextField voterAddressField;
    @FXML private Button registerVoterButton;
    @FXML private Button refreshVotersButton;
    @FXML private TableView<Voter> voterTable;
    @FXML private TableColumn<Voter, String> voterIdColumn;
    @FXML private TableColumn<Voter, String> voterNameColumn;
    @FXML private TableColumn<Voter, String> voterPhoneColumn;
    @FXML private TableColumn<Voter, String> voterAddressColumn;
    @FXML private TableColumn<Voter, Boolean> hasVotedColumn;
    
    // Election Settings Tab
    @FXML private TextField electionTitleField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private CheckBox electionActiveCheckbox;
    @FXML private Label electionStatusLabel;
    
    // Results Tab
    @FXML private TableView<Candidate> resultsTable;
    @FXML private TableColumn<Candidate, Integer> resultCandidateIdColumn;
    @FXML private TableColumn<Candidate, String> resultCandidateNameColumn;
    @FXML private TableColumn<Candidate, String> resultPartyColumn;
    @FXML private TableColumn<Candidate, String> resultPositionColumn;
    @FXML private TableColumn<Candidate, Integer> resultVoteCountColumn;
    
    // Admin Tab Controls
    @FXML private VBox adminLoginForm;
    @FXML private TextField adminUsernameField;
    @FXML private PasswordField adminPasswordField;
    @FXML private Button adminLoginButton;
    @FXML private Label adminLoginStatus;
    @FXML private TabPane adminTabPane;
    @FXML private HBox adminLogoutContainer;
    @FXML private Button adminLogoutButton;
    
    // Admin Management Tab
    @FXML private TextField newAdminUsernameField;
    @FXML private PasswordField newAdminPasswordField;
    @FXML private PasswordField confirmAdminPasswordField;
    @FXML private Button addAdminButton;
    @FXML private TableView<Admin> adminTable;
    @FXML private TableColumn<Admin, String> adminUsernameColumn;
    @FXML private TableColumn<Admin, String> adminCreatedColumn;
    
    // Observable lists for table views
    private ObservableList<Candidate> candidatesList = FXCollections.observableArrayList();
    private ObservableList<Voter> votersList = FXCollections.observableArrayList();
    private ObservableList<Admin> adminsList = FXCollections.observableArrayList();
    
    // Current voter being verified
    private String currentVoterId = null;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        // Initialize Vote Tab
        setupCandidateTable(candidateTable, candidateIdColumn, candidateNameColumn, 
                           partyColumn, positionColumn, voteColumn);
        candidateTable.setItems(candidatesList);
        
        // Initialize Candidate Management Tab
        setupCandidateTable(candidateManageTable, candidateIdManageColumn, candidateNameManageColumn, 
                           partyManageColumn, positionManageColumn, voteCountManageColumn);
        candidateManageTable.setItems(candidatesList);
        
        // Initialize Voter Registration Tab
        setupVoterTable();
        voterTable.setItems(votersList);
        
        // Initialize Results Tab
        setupCandidateTable(resultsTable, resultCandidateIdColumn, resultCandidateNameColumn,
                           resultPartyColumn, resultPositionColumn, resultVoteCountColumn);
        resultsTable.setItems(candidatesList);
        
        // Initialize Admin Table
        setupAdminTable();
        adminTable.setItems(adminsList);
        
        // Load initial data
        handleRefreshCandidates();
        handleRefreshVoters();
        handleLoadElectionSettings();
    }
    
    private void setupCandidateTable(TableView<Candidate> table, 
                                   TableColumn<Candidate, Integer> idColumn,
                                   TableColumn<Candidate, String> nameColumn,
                                   TableColumn<Candidate, String> partyColumn,
                                   TableColumn<Candidate, String> positionColumn,
                                   TableColumn<Candidate, Integer> voteColumn) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("candidateId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partyColumn.setCellValueFactory(new PropertyValueFactory<>("party"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        voteColumn.setCellValueFactory(new PropertyValueFactory<>("voteCount"));
    }
    
    private void setupVoterTable() {
        voterIdColumn.setCellValueFactory(new PropertyValueFactory<>("voterId"));
        voterNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        voterPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        voterAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        hasVotedColumn.setCellValueFactory(new PropertyValueFactory<>("hasVoted"));
    }
    
    private void setupAdminTable() {
        adminUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        adminCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
    }

    // Vote Tab Methods
    @FXML
    private void handleVerifyVoter() {
        String voterId = voterIdField.getText().trim();
        if (voterId.isEmpty()) {
            showAlert("Error", "Please enter a Voter ID");
            return;
        }
        
        try {
            Voter voter = Firebase.getVoterById(voterId);
            voterStatusLabel.setVisible(true);
            
            if (voter != null) {
                if (!voter.getHasVoted()) {
                    voterStatusLabel.setText("Voter verified: " + voter.getName() + ". Please select a candidate and cast your vote.");
                    voterStatusLabel.setTextFill(Color.GREEN);
                    castVoteButton.setDisable(false);
                    currentVoterId = voterId;
                } else {
                    voterStatusLabel.setText("This voter has already cast a vote.");
                    voterStatusLabel.setTextFill(Color.RED);
                    castVoteButton.setDisable(true);
                    currentVoterId = null;
                }
            } else {
                voterStatusLabel.setText("Voter ID not found in the system. Please check the ID or register first.");
                voterStatusLabel.setTextFill(Color.RED);
                castVoteButton.setDisable(true);
                currentVoterId = null;
            }
        } catch (Exception e) {
            showAlert("Error", "Error verifying voter: " + e.getMessage());
            e.printStackTrace();
            voterStatusLabel.setVisible(false);
        }
    }
    
    @FXML
    private void handleCastVote() {
        if (currentVoterId == null) {
            showAlert("Error", "Please verify a voter first");
            return;
        }
        
        Candidate selectedCandidate = candidateTable.getSelectionModel().getSelectedItem();
        if (selectedCandidate == null) {
            showAlert("Error", "Please select a candidate");
            return;
        }
        
        try {
            // Check election status first
            JSONObject config = Firebase.getElectionConfig();
            if (!config.isEmpty() && !config.getBoolean("is_active")) {
                showAlert("Error", "Voting is currently closed");
                return;
            }
            
            // Confirm vote with the user
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Vote");
            confirmAlert.setHeaderText("Vote Confirmation");
            confirmAlert.setContentText("Are you sure you want to vote for " + selectedCandidate.getName() + "?\nThis action cannot be undone.");
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Firebase.castVote(currentVoterId, selectedCandidate.getCandidateId());
                        
                        showAlert("Success", "Vote cast successfully!");
                        
                        // Reset UI
                        voterIdField.clear();
                        voterStatusLabel.setVisible(false);
                        castVoteButton.setDisable(true);
                        currentVoterId = null;
                        
                        // Refresh candidates to show updated vote count
                        handleRefreshCandidates();
                    } catch (Exception e) {
                        showAlert("Error", "Error casting vote: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            showAlert("Error", "Error preparing vote: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleRefreshCandidates() {
        try {
            Map<String, Candidate> candidates = Firebase.getAllCandidates();
            candidatesList.clear();
            candidatesList.addAll(candidates.values());
        } catch (Exception e) {
            showAlert("Error", "Error loading candidates: " + e.getMessage());
        }
    }
    
    // Candidate Management Tab Methods
    @FXML
    private void handleAddCandidate() {
        try {
            String candidateIdText = candidateIdField.getText().trim();
            String name = candidateNameField.getText().trim();
            String party = partyField.getText().trim();
            String position = positionField.getText().trim();
            
            if (candidateIdText.isEmpty() || name.isEmpty() || party.isEmpty() || position.isEmpty()) {
                showAlert("Error", "Please fill all fields");
                return;
            }
            
            int candidateId = Integer.parseInt(candidateIdText);
            
            Candidate candidate = new Candidate(candidateId, name, party, position);
            Firebase.addCandidate(candidate);
            
            clearCandidateFields();
            handleRefreshCandidates();
            showAlert("Success", "Candidate added successfully!");
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Candidate ID must be a number");
        } catch (Exception e) {
            showAlert("Error", "Error adding candidate: " + e.getMessage());
        }
    }
    
    private void clearCandidateFields() {
        candidateIdField.clear();
        candidateNameField.clear();
        partyField.clear();
        positionField.clear();
    }
    
    // Voter Registration Tab Methods
    @FXML
    private void handleRegisterVoter() {
        try {
            String voterId = registerVoterIdField.getText().trim();
            String name = voterNameField.getText().trim();
            String phone = voterPhoneField.getText().trim();
            String address = voterAddressField.getText().trim();
            
            if (voterId.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                showAlert("Error", "Please fill all fields");
                return;
            }
            
            Voter voter = new Voter(voterId, name, phone, address);
            Firebase.registerVoter(voter);
            
            clearVoterFields();
            handleRefreshVoters();
            showAlert("Success", "Voter registered successfully!");
            
        } catch (Exception e) {
            showAlert("Error", "Error registering voter: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRefreshVoters() {
        try {
            // Clear current list
            votersList.clear();
            
            // Show a loading indicator or message
            voterTable.setPlaceholder(new Label("Loading voters..."));
            
            // Get voters in a background thread to avoid UI freezing
            new Thread(() -> {
                try {
                    Map<String, Voter> voters = Firebase.getAllVoters();
                    
                    // Update UI on JavaFX thread
                    javafx.application.Platform.runLater(() -> {
                        if (voters.isEmpty()) {
                            voterTable.setPlaceholder(new Label("No registered voters found"));
                        } else {
                            votersList.addAll(voters.values());
                        }
                    });
                } catch (Exception e) {
                    final String errorMessage = e.getMessage();
                    javafx.application.Platform.runLater(() -> {
                        voterTable.setPlaceholder(new Label("Error loading voters"));
                        showAlert("Error", "Error loading voters: " + errorMessage);
                    });
                }
            }).start();
        } catch (Exception e) {
            showAlert("Error", "Error starting voter refresh: " + e.getMessage());
        }
    }
    
    /**
     * Clears all fields in the voter registration form
     */
    private void clearVoterFields() {
        registerVoterIdField.clear();
        voterNameField.clear();
        voterPhoneField.clear();
        voterAddressField.clear();
    }
    
    // Election Settings Tab Methods
    @FXML
    private void handleSaveElectionSettings() {
        try {
            String title = electionTitleField.getText().trim();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            boolean isActive = electionActiveCheckbox.isSelected();
            
            if (title.isEmpty() || startDate == null || endDate == null) {
                showAlert("Error", "Please fill all fields");
                return;
            }
            
            String formattedStartDate = startDate.format(dateFormatter);
            String formattedEndDate = endDate.format(dateFormatter);
            
            Firebase.setElectionConfig(title, formattedStartDate, formattedEndDate, isActive);
            showAlert("Success", "Election settings saved successfully!");
            updateElectionStatus(title, isActive);
            
        } catch (Exception e) {
            showAlert("Error", "Error saving election settings: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLoadElectionSettings() {
        try {
            JSONObject config = Firebase.getElectionConfig();
            
            if (!config.isEmpty()) {
                electionTitleField.setText(config.getString("title"));
                startDatePicker.setValue(LocalDate.parse(config.getString("start_date"), dateFormatter));
                endDatePicker.setValue(LocalDate.parse(config.getString("end_date"), dateFormatter));
                electionActiveCheckbox.setSelected(config.getBoolean("is_active"));
                
                updateElectionStatus(config.getString("title"), config.getBoolean("is_active"));
            } else {
                electionStatusLabel.setText("No election configured");
            }
            
        } catch (Exception e) {
            showAlert("Error", "Error loading election settings: " + e.getMessage());
        }
    }
    
    private void updateElectionStatus(String title, boolean isActive) {
        String status = isActive ? "ACTIVE" : "CLOSED";
        String color = isActive ? "green" : "red";
        electionStatusLabel.setText("Election: " + title + " - Status: " + status);
        electionStatusLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 16px; -fx-font-weight: bold;");
    }
    
    // Results Tab Methods
    @FXML
    private void handleRefreshResults() {
        handleRefreshCandidates();  // Reuse the candidate refresh method
    }

    // Admin Authentication Methods
    @FXML
    private void handleAdminLogin() {
        String username = adminUsernameField.getText().trim();
        String password = adminPasswordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            adminLoginStatus.setText("Please enter both username and password");
            adminLoginStatus.setVisible(true);
            return;
        }
        
        try {
            boolean isAuthenticated = Firebase.authenticateAdmin(username, password);
            
            if (isAuthenticated) {
                // Hide login form, show admin content
                adminLoginForm.setVisible(false);
                adminLoginForm.setManaged(false);
                adminTabPane.setVisible(true);
                adminTabPane.setManaged(true);
                adminLogoutContainer.setVisible(true);
                adminLogoutContainer.setManaged(true);
                
                // Clear login fields and error message
                adminUsernameField.clear();
                adminPasswordField.clear();
                adminLoginStatus.setVisible(false);
                
                // Load admin data
                loadAdmins();
            } else {
                adminLoginStatus.setText("Invalid username or password");
                adminLoginStatus.setVisible(true);
            }
        } catch (Exception e) {
            adminLoginStatus.setText("Authentication error: " + e.getMessage());
            adminLoginStatus.setVisible(true);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleAdminLogout() {
        // Show login form, hide admin content
        adminLoginForm.setVisible(true);
        adminLoginForm.setManaged(true);
        adminTabPane.setVisible(false);
        adminTabPane.setManaged(false);
        adminLogoutContainer.setVisible(false);
        adminLogoutContainer.setManaged(false);
    }
    
    @FXML
    private void handleAddAdmin() {
        String username = newAdminUsernameField.getText().trim();
        String password = newAdminPasswordField.getText();
        String confirmPassword = confirmAdminPasswordField.getText();
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill all fields");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match");
            return;
        }
        
        try {
            // Check if username already exists
            boolean exists = Firebase.checkAdminExists(username);
            if (exists) {
                showAlert("Error", "Username already exists");
                return;
            }
            
            // Add new admin
            Firebase.addAdmin(username, password);
            
            // Clear fields
            newAdminUsernameField.clear();
            newAdminPasswordField.clear();
            confirmAdminPasswordField.clear();
            
            // Refresh admin list
            loadAdmins();
            
            showAlert("Success", "Admin added successfully!");
        } catch (Exception e) {
            showAlert("Error", "Error adding admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadAdmins() {
        try {
            Map<String, Admin> admins = Firebase.getAllAdmins();
            adminsList.clear();
            adminsList.addAll(admins.values());
        } catch (Exception e) {
            showAlert("Error", "Error loading admins: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
