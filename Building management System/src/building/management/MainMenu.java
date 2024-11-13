package building.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Main Menu - Apartment System");

        // Use GridBagLayout for responsive layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between buttons

        // Title label setup
        JLabel titleLabel = new JLabel("Welcome to Apartmate");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        getContentPane().setBackground(Color.orange);

        // Unit Details button setup
        JButton unitDetailsButton = new JButton("Unit Details");
        unitDetailsButton.setBackground(Color.BLACK);
        unitDetailsButton.setForeground(Color.ORANGE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        unitDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UnitDetailsMenu();
                dispose(); // Close main menu
            }
        });
        add(unitDetailsButton, gbc);

        // Billing and Payment button setup
        JButton billingPaymentButton = new JButton("Billing and Payment");
        billingPaymentButton.setBackground(Color.BLACK);
        billingPaymentButton.setForeground(Color.ORANGE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        billingPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Billing_payment(); // Opens the billing/payment window
            }
        });
        add(billingPaymentButton, gbc);

        // Complain/Ticket button setup
        JButton complainTicketButton = new JButton("Complain/Ticket");
        complainTicketButton.setBackground(Color.BLACK);
        complainTicketButton.setForeground(Color.ORANGE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        complainTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new complaint_ticket(); // Opens the complaint/ticket window
            }
        });
        add(complainTicketButton, gbc);

        // Logout button setup
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.BLACK);
        logoutButton.setForeground(Color.ORANGE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose(); // Close main menu
            }
        });
        add(logoutButton, gbc);

        setSize(800, 480);
        setVisible(true);
        setLocationRelativeTo(null); // Center window on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}
