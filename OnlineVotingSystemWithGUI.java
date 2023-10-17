//import important packages
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OnlineVotingSystemWithGUI {// Create a Candidate class to store candidate information
    private static String[] candidateNames = {"Mith", "Mahek", "Jaspreet"};// Array of candidate names
    private static int[] candidateVotes = new int[candidateNames.length];// Array of candidate votes
    private static Set<String> votedNames = new HashSet<>();// Set of names of people who have voted
    private static Map<String, String> voteMapping = new HashMap<>();// Map of names of people who have voted to their candidate selection
    private static Map<String, Candidate> candidateInfo = new HashMap<>();// Map of candidate names to their Candidate objects
    private static final String ADMIN_PASSCODE = "yourmom"; // Admin passcode

    public static void main(String[] args) {
        // Populate candidate information in the format of (name, background, experience, policies, additional info)
        candidateInfo.put("Mith", new Candidate("Mith Utekar",
                "Mith Utekar is a 19 year old male. Strong visions and work ethic.",
                "10+ years experience of working in ground -level service to the people.",
                " 1) Reduce gas and grocery prices\n" + //
                        "2) Focus more on effecient implementation of already existing policies \n" + //
                        "3) Animal welfare and protection\n" ,
                "Mith Utekar prioritizes community engagement, accountability, and a brighter future for our town. Your vote for Mith is a vote for progress and prosperity."));

        candidateInfo.put("Mahek", new Candidate("Mahek Pandey",
                "Mahek Pandey, a dynamic community member, has called our town home for decades and is dedicated to its well-being.",
                "Mahek has a proven record of local engagement, contributing to community initiatives and understanding the unique needs of our town.",
                "1. Small Business Support\n" + //
                        "2. Quality Education\n" + //
                        "3. Healthcare Accessibility\n" + //
                        "4. Neighborhood Safety\n" + //
                        "5. Environmental Conservation",
                "Mahek Pandey is committed to community unity, transparent governance, and a sustainable future. Your vote for Mahek is a vote for a thriving, harmonious town."));

        candidateInfo.put("Jaspreet", new Candidate("Jaspreet",
                "Jaspreet is a lifelong resident deeply connected to our diverse community, with a background in social work.",
                "Active in local initiatives, Jaspreet brings a deep understanding of community challenges and practical solutions.",
                "1. Diversity and Inclusion\n" + //
                        "2. Affordable Housing\n" + //
                        "3. Healthcare Access\n" + //
                        "4. Youth Empowerment\n" + //
                        "5. Environmental Sustainability",
                "Jaspreet is committed to equality, community engagement, and transparent representation. Your vote for Jaspreet is a vote for a better, more inclusive future."));

        // Create and configure JFrame
        JFrame frame = new JFrame("Online Voting System");// Create a JFrame with the title "Online Voting System"
        frame.setSize(570, 300);// Set the size of the JFrame to 570x300
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Set the default close operation to exit the program
        frame.setLayout(null);// Set the layout of the JFrame to null
        frame.setLocationRelativeTo(null);// Set the location of the JFrame to the center of the screen

        // Create JButton to show candidate profiles
        JButton viewProfilesButton = new JButton("View Candidate Profiles");// Create a JButton with the text "View Candidate Profiles"
        viewProfilesButton.setBounds(10, 50, 180, 25);// Set the bounds of the JButton to (10, 50, 180, 25)
        frame.add(viewProfilesButton);// Add the JButton to the JFrame

        // Create JButton for voting
        JButton voteButton = new JButton("Vote");// Create a JButton with the text "Vote"
        voteButton.setBounds(200, 50, 80, 25);// Set the bounds of the JButton to (200, 50, 80, 25)
        frame.add(voteButton);// Add the JButton to the JFrame

        // Create JButton to show final vote counts
        JButton showResultsButton = new JButton("Show Results");// Create a JButton with the text "Show Results"
        showResultsButton.setBounds(290, 50, 120, 25);// Set the bounds of the JButton to (290, 50, 120, 25)
        frame.add(showResultsButton);// Add the JButton to the JFrame

        // Create JButton to show votees
        JButton showVoteesButton = new JButton("Show Votees");// Create a JButton with the text "Show Votees"
        showVoteesButton.setBounds(420, 50, 120, 25);// Set the bounds of the JButton to (420, 50, 120, 25)
        frame.add(showVoteesButton);// Add the JButton to the JFrame

        // Create JButton for admin interface
        JButton adminButton = new JButton("Admin");// Create a JButton with the text "Admin"
        adminButton.setBounds(10, 10, 80, 25);// Set the bounds of the JButton to (10, 10, 80, 25)
        frame.add(adminButton);// Add the JButton to the JFrame

        // ActionListener for the showResultsButton
        showResultsButton.addActionListener(new ActionListener() {// Add an ActionListener to the JButton
            @Override
            public void actionPerformed(ActionEvent e) {// Override the actionPerformed method
                // Display the final vote count with candidate names
                StringBuilder results = new StringBuilder("Final Vote Counts:\n");// Create a StringBuilder with the title "Final Vote Counts:"
                for (int i = 0; i < candidateVotes.length; i++) {// Iterate through the candidateVotes array
                    results.append(candidateNames[i]).append(": ").append(candidateVotes[i]).append(" votes\n");
                }
                JOptionPane.showMessageDialog(frame, results.toString());// Display the final vote count
            }
        });

        // ActionListener for the voteButton
        voteButton.addActionListener(new ActionListener() {// Add an ActionListener to the JButton
            @Override
            public void actionPerformed(ActionEvent e) {// Override the actionPerformed method
                // Get user input for name
                String userName = JOptionPane.showInputDialog(frame, "Please enter your name:");// Create a JOptionPane to prompt the user for their name

                // Check if the user clicks "Cancel"
                if (userName == null) {
                    return;
                }

                // Check if the user has already voted
                if (votedNames.contains(userName)) {// Check if the votedNames set contains the user's name
                    JOptionPane.showMessageDialog(frame, "Sorry, " + userName + ", you have already voted.");// Display a message to the user
                    return;
                }

                // Get user input for age
                String ageInput = JOptionPane.showInputDialog(frame, "Please enter your age:");// Create a JOptionPane to prompt the user for their age

                // Check if the user clicks "Cancel"
                if (ageInput == null) {
                    return;
                }

                // Attempt to parse age input as an integer
                try {
                    int userAge = Integer.parseInt(ageInput);// Parse the age input as an integer

                    // Check eligibility
                    if (userAge >= 18) {// Check if the user is eligible to vote
                        // Display candidates and get user selection
                        int userSelection = JOptionPane.showOptionDialog// Create a JOptionPane to prompt the user to select a candidate
                                (frame,
                                        "Please select a candidate to vote for:",
                                        "Candidate Selection",
                                        JOptionPane.YES_NO_CANCEL_OPTION,// Set the option type to YES_NO_CANCEL_OPTION
                                        JOptionPane.QUESTION_MESSAGE,// Set the message type to QUESTION_MESSAGE
                                        null,
                                        candidateNames,// Set the options to the candidate names
                                        candidateNames[0]);

                        // Check if the user clicks "Cancel"
                        if (userSelection == JOptionPane.CLOSED_OPTION) {// Check if the user clicks "Cancel"
                            return;
                        }

                        // Increment the vote count for the selected candidate
                        if (userSelection >= 0 && userSelection < candidateVotes.length) {// Check if the user selection is valid
                            candidateVotes[userSelection]++;// Increment the vote count for the selected candidate
                            votedNames.add(userName);// Add the user's name to the votedNames set
                            voteMapping.put(userName, candidateNames[userSelection]);// Add the user's name and candidate selection to the voteMapping map
                            JOptionPane.showMessageDialog(frame, "Thank you for voting, " + userName + "!");// Display a message to the user
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid candidate selection.");// Display a message to the user
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "I'm sorry, " + userName + ", you are not eligible to vote.");// Display a message to the user
                    }
                } catch (NumberFormatException ex) {// Catch the NumberFormatException
                    JOptionPane.showMessageDialog(frame, "Invalid age input. Please enter a valid number.");// Display a message to the user
                }
            }
        });

        // ActionListener for the showVoteesButton
        showVoteesButton.addActionListener(new ActionListener() {// Add an ActionListener to the JButton
            @Override
            public void actionPerformed(ActionEvent e) {// Override the actionPerformed method
                StringBuilder voteesList = new StringBuilder("Votees List:\n");// Create a StringBuilder with the title "Votees List:"
                for (Map.Entry<String, String> entry : voteMapping.entrySet()) {// Iterate through the voteMapping map
                    voteesList.append(entry.getKey()).append(": Voted for ").append(entry.getValue()).append("\n");// Append the user's name and candidate selection to the StringBuilder
                }
                JOptionPane.showMessageDialog(frame, voteesList.toString());// Display the votees list
            }
        });

        // ActionListener for the viewProfilesButton
        viewProfilesButton.addActionListener(new ActionListener() {// Add an ActionListener to the JButton
            @Override
            public void actionPerformed(ActionEvent e) {// Override the actionPerformed method
                StringBuilder profiles = new StringBuilder("Candidate Profiles:\n");// Create a StringBuilder with the title "Candidate Profiles:"
                for (String candidateName : candidateNames) {// Iterate through the candidateNames array
                    Candidate candidate = candidateInfo.get(candidateName);// Get the Candidate object for the current candidate name
                    profiles.append("\nName: ").append(candidate.getName())// Append the candidate information to the StringBuilder
                            .append("\nBackground: ").append(candidate.getBackground())
                            .append("\nExperience: ").append(candidate.getExperience())
                            .append("\nPolicies: ").append(candidate.getPolicies()).append("\n\n");
                }
                JOptionPane.showMessageDialog(frame, profiles.toString());// Display the candidate profiles
            }
        });

        // ActionListener for the adminButton
        adminButton.addActionListener(new ActionListener() {// Add an ActionListener to the JButton
            @Override
            public void actionPerformed(ActionEvent e) {// Override the actionPerformed method
                // Prompt the user for a passcode
                String passcode = JOptionPane.showInputDialog(frame, "Please enter the admin passcode:");

                // Check if the user clicks "Cancel"
                if (passcode == null) {
                    return;
                }

                // Check if the passcode is correct
                if (passcode.equals(ADMIN_PASSCODE)) {
                    JFrame adminFrame = new JFrame("Admin Interface");// Create a JFrame with the title "Admin Interface"
                    adminFrame.setSize(570, 300);// Set the size of the JFrame to 570x300
                    adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// Set the default close operation to dispose the JFrame
                    adminFrame.setLayout(null); // Set the layout of the JFrame to null
                    adminFrame.setLocationRelativeTo(null);// Set the location of the JFrame to the center of the screen

                    // Create JLabels and JTextFields for candidate information
                    JLabel nameLabel = new JLabel("Name:");// Create a JLabel with the text "Name:"
                    nameLabel.setBounds(10, 10, 80, 25);// Set the bounds of the JLabel to (10, 10, 80, 25)
                    adminFrame.add(nameLabel);// Add the JLabel to the JFrame

                    JTextField nameField = new JTextField();// Create a JTextField
                    nameField.setBounds(100, 10, 200, 25);// Set the bounds of the JTextField to (100, 10, 200, 25)
                    adminFrame.add(nameField);// Add the JTextField to the JFrame

                    JLabel backgroundLabel = new JLabel("Background:");// Create a JLabel with the text "Background:"
                    backgroundLabel.setBounds(10, 40, 80, 25);// Set the bounds of the JLabel to (10, 40, 80, 25)
                    adminFrame.add(backgroundLabel);// Add the JLabel to the JFrame

                    JTextField backgroundField = new JTextField();// Create a JTextField
                    backgroundField.setBounds(100, 40, 200, 25);// Set the bounds of the JTextField to (100, 40, 200, 25)
                    adminFrame.add(backgroundField);// Add the JTextField to the JFrame

                    JLabel experienceLabel = new JLabel("Experience:");// Create a JLabel with the text "Experience:"
                    experienceLabel.setBounds(10, 70, 80, 25);// Set the bounds of the JLabel to (10, 70, 80, 25)
                    adminFrame.add(experienceLabel);// Add the JLabel to the JFrame

                    JTextField experienceField = new JTextField();// Create a JTextField
                    experienceField.setBounds(100, 70, 200, 25);// Set the bounds of the JTextField to (100, 70, 200, 25)
                    adminFrame.add(experienceField);// Add the JTextField to the JFrame

                    JLabel policiesLabel = new JLabel("Policies:");// Create a JLabel with the text "Policies:"
                    policiesLabel.setBounds(10, 100, 80, 25);// Set the bounds of the JLabel to (10, 100, 80, 25)
                    adminFrame.add(policiesLabel);// Add the JLabel to the JFrame

                    JTextField policiesField = new JTextField();// Create a JTextField
                    policiesField.setBounds(100, 100, 200, 25);// Set the bounds of the JTextField to (100, 100, 200, 25)
                    adminFrame.add(policiesField);// Add the JTextField to the JFrame

                    JLabel additionalInfoLabel = new JLabel("Additional Info:");// Create a JLabel with the text "Additional Info:"
                    additionalInfoLabel.setBounds(10, 130, 80, 25);// Set the bounds of the JLabel to (10, 130, 80, 25)
                    adminFrame.add(additionalInfoLabel);// Add the JLabel to the JFrame

                    JTextField additionalInfoField = new JTextField();// Create a JTextField
                    additionalInfoField.setBounds(100, 130, 200, 25);// Set the bounds of the JTextField to (100, 130, 200, 25)
                    adminFrame.add(additionalInfoField);// Add the JTextField to the JFrame

                    // Create JComboBox for selecting candidate to edit
                    JLabel selectCandidateLabel = new JLabel("Select Candidate:");// Create a JLabel with the text "Select Candidate:"
                    selectCandidateLabel.setBounds(10, 170, 120, 25);// Set the bounds of the JLabel to (10, 170, 120, 25)
                    adminFrame.add(selectCandidateLabel);// Add the JLabel to the JFrame

                    JComboBox<String> candidateComboBox = new JComboBox<>(candidateNames);// Create a JComboBox with the candidate names
                    candidateComboBox.setBounds(130, 170, 150, 25);// Set the bounds of the JComboBox to (130, 170, 150, 25)
                    adminFrame.add(candidateComboBox);// Add the JComboBox to the JFrame

                    // Create JButton to save changes
                    JButton saveButton = new JButton("Save Changes");// Create a JButton with the text "Save Changes"
                    saveButton.setBounds(100, 220, 120, 25);// Set the bounds of the JButton to (100, 220, 120, 25)
                    adminFrame.add(saveButton);// Add the JButton to the JFrame

                    // ActionListener for the candidateComboBox
                    candidateComboBox.addActionListener(new ActionListener() {// Add an ActionListener to the JComboBox
                        @Override
                        public void actionPerformed(ActionEvent e) {// Override the actionPerformed method
                            // Update the text fields with the selected candidate's information
                            String selectedCandidate = (String) candidateComboBox.getSelectedItem();// Get the selected candidate
                            Candidate candidate = candidateInfo.get(selectedCandidate);// Get the Candidate object for the selected candidate
                            nameField.setText(candidate.getName());// Update the text fields with the selected candidate's information
                            backgroundField.setText(candidate.getBackground());// Update the text fields with the selected candidate's information
                            experienceField.setText(candidate.getExperience());// Update the text fields with the selected candidate's information
                            policiesField.setText(candidate.getPolicies());// Update the text fields with the selected candidate's information
                            additionalInfoField.setText(candidate.getAdditionalInfo());// Update the text fields with the selected candidate's information
                        }
                    });

                    // ActionListener for the saveButton
                    saveButton.addActionListener(new ActionListener() {// Add an ActionListener to the JButton
                        @Override
                        public void actionPerformed(ActionEvent e) {// Override the actionPerformed method
                            // Get the selected candidate and update their information in the candidateInfo map
                            String selectedCandidate = (String) candidateComboBox.getSelectedItem();// Get the selected candidate
                            Candidate candidate = candidateInfo.get(selectedCandidate);// Get the Candidate object for the selected candidate
                            candidate.setName(nameField.getText());// Update the candidate information in the candidateInfo map
                            candidate.setBackground(backgroundField.getText());// Update the candidate information in the candidateInfo map
                            candidate.setExperience(experienceField.getText());// Update the candidate information in the candidateInfo map
                            candidate.setPolicies(policiesField.getText());// Update the candidate information in the candidateInfo map
                            candidate.setAdditionalInfo(additionalInfoField.getText());// Update the candidate information in the candidateInfo map
                            JOptionPane.showMessageDialog(adminFrame, "Candidate information updated.");// Display a message to the user
                        }
                    });

                    // Make the admin frame visible
                    adminFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Incorrect passcode. Access denied.");// Display a message to the user
                }
            }
        });

        // Make the main frame visible
        frame.setVisible(true);
    }
}

class Candidate {// Candidate class to store information about a candidate
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
