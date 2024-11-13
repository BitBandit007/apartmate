package building.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Login extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/apartment_system";
    private static final String USER = "mahin"; // Your MySQL username
    private static final String PASS = "123"; // Your MySQL password

    public Login() {
        setTitle("Apartment System");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/logo.png"));
        Image i2 = i1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel label = new JLabel(i3);

        // Set layout constraints for logo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        add(label, gbc);

        // Headline label
        JLabel headline = new JLabel("Apartmate");
        headline.setFont(new Font("Times New Roman", Font.BOLD, 50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(headline, gbc);

        getContentPane().setBackground(Color.orange);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Reset to single column
        add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15); // 15 columns
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15); // 15 columns
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.orange);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check database connection
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showThemedMessage("Database connection failed!", Color.BLACK, Color.RED, Color.WHITE);
                    return; // Exit if connection fails
                }

                // Validate username and password
                if (username.equals("admin") && password.equals("admin")) {
                    showThemedMessage("Login Successful!", Color.ORANGE, Color.BLACK, Color.DARK_GRAY);
                    // Open main menu after successful login
                    new MainMenu();
                    dispose(); // Close the login window
                } else {
                    showThemedMessage("Invalid Username or Password", Color.BLACK, Color.RED, Color.WHITE);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Button spans both columns
        add(loginButton, gbc);

        getRootPane().setDefaultButton(loginButton);

        setSize(800, 480);
        setVisible(true);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Custom themed message dialog with button color customization
private void showThemedMessage(String message, Color bgColor, Color textColor, Color buttonColor) {
    // Create a JDialog with custom panel for color control
    JDialog dialog = new JDialog(this, "Message", true);
    dialog.setLayout(new BorderLayout());

    // Message panel with centered text
    JPanel messagePanel = new JPanel(new GridBagLayout());
    messagePanel.setBackground(bgColor); // Background color
    JLabel label = new JLabel(message);
    label.setForeground(textColor); // Text color
    label.setFont(new Font("Arial", Font.BOLD, 14));
    messagePanel.add(label);

    // Button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(bgColor);
    JButton okButton = new JButton("OK");
    okButton.setBackground(buttonColor); // Button background color
    okButton.setForeground(Color.ORANGE); // Button text color
    okButton.setFocusPainted(false);
    okButton.setOpaque(true); // Ensures the background color is fully applied
    okButton.addActionListener(e -> dialog.dispose());
    buttonPanel.add(okButton);

    // Set OK button as default for Enter key
    dialog.getRootPane().setDefaultButton(okButton);

    dialog.add(messagePanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    dialog.setSize(300, 150);
    dialog.setLocationRelativeTo(this); // Center dialog on parent frame
    dialog.setVisible(true);
}


    public static void main(String args[]) {
        new Login();
    }
}
