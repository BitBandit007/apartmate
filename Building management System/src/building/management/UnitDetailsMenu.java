package building.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnitDetailsMenu extends JFrame {

    public UnitDetailsMenu() {
        setTitle("Unit Details Menu");
        setLayout(new GridBagLayout());

        // Set background color to orange
        getContentPane().setBackground(Color.orange);

        // Set the size and location of the window
        setSize(400, 400);
        setLocation(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons with black background and orange text
        JButton addDetailsButton = createStyledButton("Add Details");
        JButton viewAllButton = createStyledButton("View All Details"); // View All Details button
        JButton backButton = createStyledButton("Back"); // Back button

        // Button alignment in the center
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Column
        gbc.gridy = 0; // Row
        gbc.insets = new Insets(10, 0, 10, 0); // Padding around buttons
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment

        // Add buttons to the frame
        add(addDetailsButton, gbc);
        gbc.gridy++;
        add(viewAllButton, gbc); // Add the "View All Details" button
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 10, 0); // Larger padding for the back button
        add(backButton, gbc);

        // Action listeners for each button
        addDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddRenterMenu(); // Open the Add Renter Menu
                dispose(); // Close the Unit Details Menu
            }
        });

        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllDetails(); // Call method to view all details
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu(); // Go back to MainMenu
                dispose(); // Close the Unit Details Menu
            }
        });

        setVisible(true);
    }

    // Helper method to create styled buttons with black background and orange text
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(Color.BLACK); // Button background color
        button.setForeground(Color.orange); // Button text color
        button.setFocusPainted(false); // Removes focus border on button
        return button;
    }

    // Method to handle viewing all details
    private void viewAllDetails() {
        new ViewRenterDetails(); // Open the View Renter Details window
        dispose(); // Close the Unit Details Menu
    }

    public static void main(String[] args) {
        new UnitDetailsMenu();
    }
}
