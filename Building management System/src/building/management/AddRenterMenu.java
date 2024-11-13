package building.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddRenterMenu extends JFrame {
    private JTextField nameField, contactField, addressField, nidField, nomineeNameField, nomineeContactField;
    private JComboBox<String> flatChoice;

    public AddRenterMenu() {
        setTitle("Add Renter Details");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set the size of the window
        setSize(400, 400);
        setLocation(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set background color to orange
        getContentPane().setBackground(Color.orange);

        // Input fields for renter details
        nameField = new JTextField(20);
        contactField = new JTextField(20);
        addressField = new JTextField(20);
        nidField = new JTextField(20);
        nomineeNameField = new JTextField(20);
        nomineeContactField = new JTextField(20);
        String[] flats = generateFlats();
        flatChoice = new JComboBox<>(flats);

        // Add button to submit details
        JButton addButton = createStyledButton("Add Renter");
        JButton cancelButton = createStyledButton("Cancel"); // Cancel button

        // Action listener for the Add Renter button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRenterToDatabase(); // Call the method to add renter to the database
            }
        });

        // Add action listener for the cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UnitDetailsMenu(); // Go back to Unit Details Menu
                dispose(); // Close the Add Renter Menu
            }
        });

        // Add components to the layout using gbc
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Renter Name:"), gbc);
        gbc.gridx = 1; add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Contact Number:"), gbc);
        gbc.gridx = 1; add(contactField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Permanent Address:"), gbc);
        gbc.gridx = 1; add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("NID Number:"), gbc);
        gbc.gridx = 1; add(nidField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Nominee Name:"), gbc);
        gbc.gridx = 1; add(nomineeNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Nominee Contact:"), gbc);
        gbc.gridx = 1; add(nomineeContactField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Assign Flat:"), gbc);
        gbc.gridx = 1; add(flatChoice, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Create a panel for buttons to align them properly
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.orange);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        setVisible(true);
    }

    private String[] generateFlats() {
        String[] flats = new String[30];
        for (int i = 0; i < 30; i++) {
            int floor = i / 3 + 1;
            char flat = (char) ('a' + (i % 3));
            flats[i] = floor + "" + flat;
        }
        return flats;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(Color.BLACK); // Button background color
        button.setForeground(Color.orange); // Button text color
        button.setFocusPainted(false); // Removes focus border on button
        return button;
    }

    private void addRenterToDatabase() {
        String name = nameField.getText();
        String contact = contactField.getText();
        String address = addressField.getText();
        String nid = nidField.getText();
        String nomineeName = nomineeNameField.getText();
        String nomineeContact = nomineeContactField.getText();
        String flat = (String) flatChoice.getSelectedItem();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO renters (name, contact, permanent_address, nid_number, nominee_name, nominee_contact, assigned_flat) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, contact);
                pstmt.setString(3, address);
                pstmt.setString(4, nid);
                pstmt.setString(5, nomineeName);
                pstmt.setString(6, nomineeContact);
                pstmt.setString(7, flat);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Renter added successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding renter: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddRenterMenu();
    }
}
