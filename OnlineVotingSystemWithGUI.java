//import important packages
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class OnlineVotingSystemWithGUI //main class
{
    private static String[] candidateNames = {"Mith", "Mahek", "Jaspreet"};//array of candidate names
    private static int[] candidateVotes = new int[candidateNames.length];//array of candidate votes
    private static Set<String> votedNames = new HashSet<>();//set of voted names
    private static Map<String, String> voteMapping = new HashMap<>();//map of vote mapping
    private static Map<String, Candidate> candidateInfo = new HashMap<>();//map of candidate information

    public static void main(String[] args)//main method 
    {
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
        JFrame frame = new JFrame("Online Voting System");//frame title
        frame.setSize(570, 300);//frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//frame close operation
        frame.setLayout(null);//frame layout
        //center of screen
        frame.setLocationRelativeTo(null);

        // Create JButton to show candidate profiles
JButton viewProfilesButton = new JButton("View Candidate Profiles");//button name
viewProfilesButton.setBounds(10, 50, 180, 25);//button size
frame.add(viewProfilesButton);//add button to frame

// Create JButton for voting
JButton voteButton = new JButton("Vote");//button name
voteButton.setBounds(200, 50, 80, 25);//button size
frame.add(voteButton);//add button to frame

// Create JButton to show final vote counts
JButton showResultsButton = new JButton("Show Results");//button name
showResultsButton.setBounds(290, 50, 120, 25);//button size
frame.add(showResultsButton);//add button to frame

// Create JButton to show votees
JButton showVoteesButton = new JButton("Show Votees");//button name
showVoteesButton.setBounds(420, 50, 120, 25);// button size
frame.add(showVoteesButton);//add button to frame


// ActionListener for the showResultsButton
showResultsButton.addActionListener(new ActionListener() //action listener for show results button
{
    @Override
    public void actionPerformed(ActionEvent e) //action performed method
    {
        // Display the final vote count with candidate names
        StringBuilder results = new StringBuilder("Final Vote Counts:\n");
        for (int i = 0; i < candidateVotes.length; i++) {//for loop
            results.append(candidateNames[i]).append(": ").append(candidateVotes[i]).append(" votes\n");//append method
        }
        // Display results in a JOptionPane
        JOptionPane.showMessageDialog(frame, results.toString());//show message dialog
    }
});



       // ActionListener for the voteButton
// ActionListener for the voteButton
voteButton.addActionListener(new ActionListener() //action listener for vote button
{
    @Override
    public void actionPerformed(ActionEvent e) //action performed method
    {
        // Get user input for name
        String userName = JOptionPane.showInputDialog(frame, "Please enter your name:");//show input dialog

        // Check if the user clicks "Cancel"
        if (userName == null) //if statement
        {
            return; // Exit the method if the user cancels the name input
        }

        // Check if the user has already voted
        if (votedNames.contains(userName)) 
        {
            JOptionPane.showMessageDialog(frame, "Sorry, " + userName + ", you have already voted.");
            return; // Exit the method if the user has already voted
        }

        // Get user input for age
        String ageInput = JOptionPane.showInputDialog(frame, "Please enter your age:");

        // Check if the user clicks "Cancel"
        if (ageInput == null) {
            return; // Exit the method if the user cancels the age input
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
                        "Candidate Selection",//option dialog title
                        JOptionPane.YES_NO_CANCEL_OPTION,//option dialog type
                        JOptionPane.QUESTION_MESSAGE,//option dialog message type
                        null,//icon
                        candidateNames,//options
                        candidateNames[0]);//default option

                // Check if the user clicks "Cancel"
                if (userSelection == JOptionPane.CLOSED_OPTION) {
                    return; // Exit the method if the user cancels the candidate selection
                }

                // Increment the vote count for the selected candidate
                if (userSelection >= 0 && userSelection < candidateVotes.length) {
                    candidateVotes[userSelection]++;//increment vote count
                    votedNames.add(userName); // Add the user to the set of voted names
                    voteMapping.put(userName, candidateNames[userSelection]);//put method
                    // Display a confirmation message to the user
                    JOptionPane.showMessageDialog(frame, "Thank you for voting, " + userName + "!");//show message dialog
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid candidate selection.");//show message dialog
                }
            } else {
                // Display a message to the user if they are not eligible to vote
                JOptionPane.showMessageDialog(frame, "I'm sorry, " + userName + ", you are not eligible to vote.");
            }
        } catch (NumberFormatException ex) {
            // Handle the case where the input for age is not a valid integer
            JOptionPane.showMessageDialog(frame, "Invalid age input. Please enter a valid number.");
        }
    }
});


        // ActionListener for the showVoteesButton
        showVoteesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//action performed method
                StringBuilder voteesList = new StringBuilder("Votees List:\n");//string builder
                for (Map.Entry<String, String> entry : voteMapping.entrySet()) {//for loop
                    voteesList.append(entry.getKey()).append(": Voted for ").append(entry.getValue()).append("\n");//append method
                }
                // Display the votees list in a JOptionPane
                JOptionPane.showMessageDialog(frame, voteesList.toString());//show message dialog
            }
        });

        // ActionListener for the viewProfilesButton
        viewProfilesButton.addActionListener(new ActionListener() {//action listener for view profiles button
            @Override
            public void actionPerformed(ActionEvent e) {//action performed method
                StringBuilder profiles = new StringBuilder("Candidate Profiles:\n");//string builder
                for (String candidateName : candidateNames) {//for loop
                    Candidate candidate = candidateInfo.get(candidateName);//get method
                    profiles.append("\nName: ").append(candidate.getName())//append method
                            .append("\nBackground: ").append(candidate.getBackground())
                            .append("\nExperience: ").append(candidate.getExperience())
                            .append("\nPolicies: ").append(candidate.getPolicies()).append("\n\n");
                }
                // Display candidate profiles in a JOptionPane
                JOptionPane.showMessageDialog(frame, profiles.toString());
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}
