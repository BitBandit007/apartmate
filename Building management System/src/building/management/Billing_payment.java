package building.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Billing_payment extends JFrame {
    private JTextField unitField;
    private JTextField billAmountField;
    private JTextField paymentField;
    private JButton payButton;
    private JButton viewHistoryButton;

    public Billing_payment() {
        setTitle("Billing and Payment");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        setSize(500, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.ORANGE);

        // Initialize fields
        unitField = new JTextField(20);
        billAmountField = new JTextField(20);
        paymentField = new JTextField(20);

        unitField.setEditable(false);
        billAmountField.setEditable(false);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Unit selection
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Unit:"), gbc);
        gbc.gridx = 1;
        add(unitField, gbc);

        // Bill Amount
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Amount Due:"), gbc);
        gbc.gridx = 1;
        add(billAmountField, gbc);

        // Payment amount
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Enter Payment:"), gbc);
        gbc.gridx = 1;
        add(paymentField, gbc);

        // Pay button
        payButton = new JButton("Pay");
        payButton.setBackground(Color.BLACK);
        payButton.setForeground(Color.ORANGE);
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makePayment();
            }
        });
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(payButton, gbc);

        // View History button
        viewHistoryButton = new JButton("View Payment History");
        viewHistoryButton.setBackground(Color.BLACK);
        viewHistoryButton.setForeground(Color.ORANGE);
        viewHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPaymentHistory();
            }
        });
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(viewHistoryButton, gbc);

        fetchBillDetails();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void fetchBillDetails() {
        // Fetch the bill details based on the unit (assuming you have a bills table with a "unit" column)
        String unit = "101A"; // Replace this with the selected unit
        unitField.setText(unit);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT bill_amount FROM bills WHERE unit = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, unit);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    billAmountField.setText(rs.getString("bill_amount"));
                } else {
                    JOptionPane.showMessageDialog(this, "No bills found for this unit.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill details: " + ex.getMessage());
        }
    }

    private void makePayment() {
        String unit = unitField.getText();
        double amountDue = Double.parseDouble(billAmountField.getText());
        double payment = Double.parseDouble(paymentField.getText());

        if (payment > amountDue) {
            JOptionPane.showMessageDialog(this, "Payment amount exceeds the bill amount.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE bills SET bill_amount = bill_amount - ? WHERE unit = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, payment);
                pstmt.setString(2, unit);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Payment successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error processing payment.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing payment: " + ex.getMessage());
        }
    }

    private void viewPaymentHistory() {
        // Display the payment history (assuming there is a payment history table)
        // This is a placeholder implementation, you can customize the history fetching as needed
        JOptionPane.showMessageDialog(this, "View Payment History feature coming soon!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Billing_payment());
    }
}
