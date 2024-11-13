package building.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewRenterDetails extends JFrame 
{
    private DefaultTableModel model;
    private JTable table;

    public ViewRenterDetails() 
    {
        setTitle("Renter Details");
        setLayout(new BorderLayout());

        // Create a table to display the renter details
        String[] columnNames = {"ID", "Name", "Contact", "Permanent Address", "NID Number", "Nominee Name", "Nominee Contact", "Assigned Flat"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton updateButton = new JButton("Update");
        JButton searchButton = new JButton("Search");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back"); // Back button

        buttonPanel.add(updateButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton); // Add the Back button to the panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data from the database
        loadRenterDetails();

        // Set the size and visibility of the frame
        setSize(800, 400);
        setLocationRelativeTo(null); // Center the frame on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this frame but not the application
        setVisible(true);

        // Action listeners for the buttons
        updateButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                updateRenter(); // Call method to update renter
            }
        });

        searchButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                searchRenter(); // Call method to search renter
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                deleteRenter(); // Call method to delete renter
            }
        });

        // Back button action listener
        backButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new UnitDetailsMenu(); // Open the Unit Details Menu
                dispose(); // Close the View Renter Details window
            }
        });
    }

    private void loadRenterDetails() 
    {
        String query = "SELECT * FROM renters"; // Adjust the query based on your needs
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) 
        {

            model.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                int id = rs.getInt("id"); // Adjust according to your actual database column name
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                String address = rs.getString("permanent_address");
                String nidNumber = rs.getString("nid_number");
                String nomineeName = rs.getString("nominee_name");
                String nomineeContact = rs.getString("nominee_contact");
                String assignedFlat = rs.getString("assigned_flat");

                // Add the data to the table model
                model.addRow(new Object[]{id, name, contact, address, nidNumber, nomineeName, nomineeContact, assignedFlat});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching renter details: " + e.getMessage());
        }
    }

    private void updateRenter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a renter to update.");
            return;
        }

        // Get the ID of the selected renter
        int id = (int) model.getValueAt(selectedRow, 0);

        // Gather new data (for simplicity, we will use input dialogs)
        String name = JOptionPane.showInputDialog(this, "Enter new name:", model.getValueAt(selectedRow, 1));
        String contact = JOptionPane.showInputDialog(this, "Enter new contact:", model.getValueAt(selectedRow, 2));
        String address = JOptionPane.showInputDialog(this, "Enter new address:", model.getValueAt(selectedRow, 3));
        String nidNumber = JOptionPane.showInputDialog(this, "Enter new NID Number:", model.getValueAt(selectedRow, 4));
        String nomineeName = JOptionPane.showInputDialog(this, "Enter new Nominee Name:", model.getValueAt(selectedRow, 5));
        String nomineeContact = JOptionPane.showInputDialog(this, "Enter new Nominee Contact:", model.getValueAt(selectedRow, 6));
        String assignedFlat = JOptionPane.showInputDialog(this, "Enter new Assigned Flat:", model.getValueAt(selectedRow, 7));

        // Update the database
        String updateQuery = "UPDATE renters SET name=?, contact=?, permanent_address=?, nid_number=?, nominee_name=?, nominee_contact=?, assigned_flat=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, name);
            pstmt.setString(2, contact);
            pstmt.setString(3, address);
            pstmt.setString(4, nidNumber);
            pstmt.setString(5, nomineeName);
            pstmt.setString(6, nomineeContact);
            pstmt.setString(7, assignedFlat);
            pstmt.setInt(8, id);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Renter updated successfully!");

            // Refresh the table
            loadRenterDetails();
        } catch (SQLException e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating renter: " + e.getMessage());
        }
    }

    private void searchRenter() 
    {
        String searchTerm = JOptionPane.showInputDialog(this, "Enter name or contact to search:");
        if (searchTerm == null || searchTerm.trim().isEmpty()) 
        {
            return; // If the input is empty, do nothing
        }

        String query = "SELECT * FROM renters WHERE name LIKE ? OR contact LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            model.setRowCount(0); // Clear existing rows
            while (rs.next()) 
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                String address = rs.getString("permanent_address");
                String nidNumber = rs.getString("nid_number");
                String nomineeName = rs.getString("nominee_name");
                String nomineeContact = rs.getString("nominee_contact");
                String assignedFlat = rs.getString("assigned_flat");

                // Add the found data to the table model
                model.addRow(new Object[]{id, name, contact, address, nidNumber, nomineeName, nomineeContact, assignedFlat});
            }
            if (model.getRowCount() == 0) 
            {
                JOptionPane.showMessageDialog(this, "No renters found matching: " + searchTerm);
            }
        } catch (SQLException e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching renters: " + e.getMessage());
        }
    }

    private void deleteRenter() 
    {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a renter to delete.");
            return;
        }

        // Get the ID of the selected renter
        int id = (int) model.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this renter?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) 
        {
            String deleteQuery = "DELETE FROM renters WHERE id=?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) 
            {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Renter deleted successfully!");

                // Refresh the table
                loadRenterDetails();
            } catch (SQLException e) 
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting renter: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) 
    {
        new ViewRenterDetails();
    }
}
