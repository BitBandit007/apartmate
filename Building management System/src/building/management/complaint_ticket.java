package building.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class complaint_ticket extends JFrame {
    private JComboBox<String> unitChoice;
    private JTextField contactField;
    private JTextArea complaintArea;
    private String renterName; // To store the renter's name based on unit selection

    public complaint_ticket() {
        setTitle("Submit a Complaint/Ticket");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        setSize(500, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.ORANGE);

        // Initialize fields
        unitChoice = new JComboBox<>(generateFlats());
        contactField = new JTextField(20);
        complaintArea = new JTextArea(5, 20);
        complaintArea.setLineWrap(true);
        complaintArea.setWrapStyleWord(true);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Unit selection
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Select Unit:"), gbc);
        gbc.gridx = 1;
        add(unitChoice, gbc);

        // Contact Number
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Contact Number:"), gbc);
        gbc.gridx = 1;
        add(contactField, gbc);

        // Complaint Area
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Complaint Details:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(new JScrollPane(complaintArea), gbc);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.ORANGE);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitComplaint();
            }
        });

        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.ORANGE);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.ORANGE);
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Add an action listener to get the renter name when a unit is selected
        unitChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchRenterName();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private String[] generateFlats() {
        String[] flats = new String[30];
        for (int i = 0; i < 30; i++) {
            int floor = i / 3 + 1;
            char flat = (char) ('A' + (i % 3));
            flats[i] = floor + "" + flat;
        }
        return flats;
    }

    private void fetchRenterName() {
        String selectedUnit = (String) unitChoice.getSelectedItem();

        // Fetch the renter's name based on the selected unit (assuming you have a renters table with an "assigned_flat" column)
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT name FROM renters WHERE assigned_flat = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, selectedUnit);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    renterName = rs.getString("name"); // Store renter's name
                } else {
                    renterName = "Unknown"; // In case the unit has no renter
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching renter's name: " + ex.getMessage());
        }
    }

    private void submitComplaint() {
        String contact = contactField.getText();
        String complaint = complaintArea.getText();
        String unit = (String) unitChoice.getSelectedItem();

        // Ensure that renterName is not null before submitting the complaint
        if (renterName == null || renterName.equals("Unknown")) {
            JOptionPane.showMessageDialog(this, "No renter found for the selected unit.");
            return;
        }

        // Save to database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO complaints (unit, renter_name, contact, complaint_details) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, unit);
                pstmt.setString(2, renterName); // Using the renter's name we fetched earlier
                pstmt.setString(3, contact);
                pstmt.setString(4, complaint);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Complaint submitted successfully!");
                dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting complaint: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new complaint_ticket();
    }
}
