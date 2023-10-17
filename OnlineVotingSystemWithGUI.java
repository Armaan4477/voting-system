import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OnlineVotingSystemWithGUI {
    private static String[] candidateNames = {"Mith", "Mahek", "Jaspreet"};
    private static int[] candidateVotes = new int[candidateNames.length];
    private static Set<String> votedNames = new HashSet<>();
    private static Map<String, String> voteMapping = new HashMap<>();
    private static Map<String, Candidate> candidateInfo = new HashMap<>();

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
        JFrame frame = new JFrame("Online Voting System");
        frame.setSize(570, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Create JButton to show candidate profiles
        JButton viewProfilesButton = new JButton("View Candidate Profiles");
        viewProfilesButton.setBounds(10, 50, 180, 25);
        frame.add(viewProfilesButton);

        // Create JButton for voting
        JButton voteButton = new JButton("Vote");
        voteButton.setBounds(200, 50, 80, 25);
        frame.add(voteButton);

        // Create JButton to show final vote counts
        JButton showResultsButton = new JButton("Show Results");
        showResultsButton.setBounds(290, 50, 120, 25);
        frame.add(showResultsButton);

        // Create JButton to show votees
        JButton showVoteesButton = new JButton("Show Votees");
        showVoteesButton.setBounds(420, 50, 120, 25);
        frame.add(showVoteesButton);

        // Create JButton for admin interface
        JButton adminButton = new JButton("Admin");
        adminButton.setBounds(10, 10, 80, 25);
        frame.add(adminButton);

        // ActionListener for the showResultsButton
        showResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display the final vote count with candidate names
                StringBuilder results = new StringBuilder("Final Vote Counts:\n");
                for (int i = 0; i < candidateVotes.length; i++) {
                    results.append(candidateNames[i]).append(": ").append(candidateVotes[i]).append(" votes\n");
                }
                JOptionPane.showMessageDialog(frame, results.toString());
            }
        });

        // ActionListener for the voteButton
        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get user input for name
                String userName = JOptionPane.showInputDialog(frame, "Please enter your name:");

                // Check if the user clicks "Cancel"
                if (userName == null) {
                    return;
                }

                // Check if the user has already voted
                if (votedNames.contains(userName)) {
                    JOptionPane.showMessageDialog(frame, "Sorry, " + userName + ", you have already voted.");
                    return;
                }

                // Get user input for age
                String ageInput = JOptionPane.showInputDialog(frame, "Please enter your age:");

                // Check if the user clicks "Cancel"
                if (ageInput == null) {
                    return;
                }

                // Attempt to parse age input as an integer
                try {
                    int userAge = Integer.parseInt(ageInput);

                    // Check eligibility
                    if (userAge >= 18) {
                        // Display candidates and get user selection
                        int userSelection = JOptionPane.showOptionDialog
                                (frame,
                                        "Please select a candidate to vote for:",
                                        "Candidate Selection",
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        candidateNames,
                                        candidateNames[0]);

                        // Check if the user clicks "Cancel"
                        if (userSelection == JOptionPane.CLOSED_OPTION) {
                            return;
                        }

                        // Increment the vote count for the selected candidate
                        if (userSelection >= 0 && userSelection < candidateVotes.length) {
                            candidateVotes[userSelection]++;
                            votedNames.add(userName);
                            voteMapping.put(userName, candidateNames[userSelection]);
                            JOptionPane.showMessageDialog(frame, "Thank you for voting, " + userName + "!");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid candidate selection.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "I'm sorry, " + userName + ", you are not eligible to vote.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid age input. Please enter a valid number.");
                }
            }
        });

        // ActionListener for the showVoteesButton
        showVoteesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder voteesList = new StringBuilder("Votees List:\n");
                for (Map.Entry<String, String> entry : voteMapping.entrySet()) {
                    voteesList.append(entry.getKey()).append(": Voted for ").append(entry.getValue()).append("\n");
                }
                JOptionPane.showMessageDialog(frame, voteesList.toString());
            }
        });

        // ActionListener for the viewProfilesButton
        viewProfilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder profiles = new StringBuilder("Candidate Profiles:\n");
                for (String candidateName : candidateNames) {
                    Candidate candidate = candidateInfo.get(candidateName);
                    profiles.append("\nName: ").append(candidate.getName())
                            .append("\nBackground: ").append(candidate.getBackground())
                            .append("\nExperience: ").append(candidate.getExperience())
                            .append("\nPolicies: ").append(candidate.getPolicies()).append("\n\n");
                }
                JOptionPane.showMessageDialog(frame, profiles.toString());
            }
        });

        // ActionListener for the adminButton
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame adminFrame = new JFrame("Admin Interface");
                adminFrame.setSize(570, 300);
                adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                adminFrame.setLayout(null);
                adminFrame.setLocationRelativeTo(null);

                // Create JLabels and JTextFields for candidate information
                JLabel nameLabel = new JLabel("Name:");
                nameLabel.setBounds(10, 10, 80, 25);
                adminFrame.add(nameLabel);

                JTextField nameField = new JTextField();
                nameField.setBounds(100, 10, 200, 25);
                adminFrame.add(nameField);

                JLabel backgroundLabel = new JLabel("Background:");
                backgroundLabel.setBounds(10, 40, 80, 25);
                adminFrame.add(backgroundLabel);

                JTextField backgroundField = new JTextField();
                backgroundField.setBounds(100, 40, 200, 25);
                adminFrame.add(backgroundField);

                JLabel experienceLabel = new JLabel("Experience:");
                experienceLabel.setBounds(10, 70, 80, 25);
                adminFrame.add(experienceLabel);

                JTextField experienceField = new JTextField();
                experienceField.setBounds(100, 70, 200, 25);
                adminFrame.add(experienceField);

                JLabel policiesLabel = new JLabel("Policies:");
                policiesLabel.setBounds(10, 100, 80, 25);
                adminFrame.add(policiesLabel);

                JTextField policiesField = new JTextField();
                policiesField.setBounds(100, 100, 200, 25);
                adminFrame.add(policiesField);

                JLabel additionalInfoLabel = new JLabel("Additional Info:");
                additionalInfoLabel.setBounds(10, 130, 80, 25);
                adminFrame.add(additionalInfoLabel);

                JTextField additionalInfoField = new JTextField();
                additionalInfoField.setBounds(100, 130, 200, 25);
                adminFrame.add(additionalInfoField);

                // Create JComboBox for selecting candidate to edit
                JLabel selectCandidateLabel = new JLabel("Select Candidate:");
                selectCandidateLabel.setBounds(10, 170, 120, 25);
                adminFrame.add(selectCandidateLabel);

                JComboBox<String> candidateComboBox = new JComboBox<>(candidateNames);
                candidateComboBox.setBounds(130, 170, 150, 25);
                adminFrame.add(candidateComboBox);

                // Create JButton to save changes
                JButton saveButton = new JButton("Save Changes");
                saveButton.setBounds(100, 220, 120, 25);
                adminFrame.add(saveButton);

                // ActionListener for the candidateComboBox
candidateComboBox.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Update the text fields with the selected candidate's information
        String selectedCandidate = (String) candidateComboBox.getSelectedItem();
        Candidate candidate = candidateInfo.get(selectedCandidate);
        nameField.setText(candidate.getName());
        backgroundField.setText(candidate.getBackground());
        experienceField.setText(candidate.getExperience());
        policiesField.setText(candidate.getPolicies());
        additionalInfoField.setText(candidate.getAdditionalInfo());
    }
});

// ActionListener for the saveButton
saveButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the selected candidate and update their information in the candidateInfo map
        String selectedCandidate = (String) candidateComboBox.getSelectedItem();
        Candidate candidate = candidateInfo.get(selectedCandidate);
        candidate.setName(nameField.getText());
        candidate.setBackground(backgroundField.getText());
        candidate.setExperience(experienceField.getText());
        candidate.setPolicies(policiesField.getText());
        candidate.setAdditionalInfo(additionalInfoField.getText());
        JOptionPane.showMessageDialog(adminFrame, "Candidate information updated.");
    }
});

                // Make the admin frame visible
                adminFrame.setVisible(true);
            }
        });

        // Make the main frame visible
        frame.setVisible(true);
    }
}