import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginApp {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JToggleButton showPasswordToggle;

    public LoginApp() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 250));

        JPanel loginPanel = new JPanel(new GridLayout(5, 3, 10, 10));
        loginPanel.setBackground(new Color(30, 144, 255)); // Light gray background

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(new Color(255, 255, 255)); // Dodger blue color
        usernameField = new JTextField();
        usernameField.setBackground(new Color(255, 255, 255)); // White background

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(255, 255, 255)); // Dodger blue color
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(255, 255, 255)); // White background
        showPasswordToggle = new JToggleButton("Show Password");
        showPasswordToggle.setForeground(new Color(0, 0, 0)); // Black text
        showPasswordToggle.setBackground(new Color(240, 240, 240)); // Light gray background

        passwordField.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (showPasswordToggle.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });

        showPasswordToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordToggle.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        loginButton.setBackground(new Color(46, 139, 87)); // Dodger blue background
        loginButton.setForeground(new Color(255, 255, 255)); // White text
        registerButton.setBackground(new Color(46, 139, 87)); // Dodger blue background
        registerButton.setForeground(new Color(255, 255, 255)); // White text

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (authenticateUser(username, password)) {
                    frame.dispose(); // Close the login window
                    showCourierApp(); // Show the main application window
                } else {
                    JOptionPane.showMessageDialog(frame, "Login Failed. Invalid credentials.");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (registerUser(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Registration Successful!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Registration Failed. User already exists.");
                }
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel()); // Empty cell
        loginPanel.add(showPasswordToggle);
        loginPanel.add(new JLabel()); // Empty cell
        loginPanel.add(loginButton);
        loginPanel.add(new JLabel()); // Empty cell
        loginPanel.add(registerButton);

        frame.getContentPane().add(loginPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private boolean authenticateUser(String username, String password) {
        // Implement user authentication here (check against user credentials)
        try (BufferedReader reader = new BufferedReader(new FileReader("UserDetails.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: " + username)) {
                    String storedPassword = reader.readLine().substring(10); // Read the stored password
                    return storedPassword.equals(password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Authentication failed
    }

    private boolean registerUser(String username, String password) {
        // Implement user registration here (store user credentials in a file)
        // For simplicity, we'll assume user registration is successful if a username is
        // unique.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("UserDetails.txt", true))) {
            // Check if the username is already registered
            BufferedReader reader = new BufferedReader(new FileReader("UserDetails.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: " + username)) {
                    reader.close();
                    return false; // User already exists
                }
            }
            reader.close();

            // Register the new user
            writer.write("Username: " + username + "\n");
            writer.write("Password: " + password + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void showCourierApp() {
        SwingUtilities.invokeLater(() -> {
            new CourierApp().createAndShowGUI();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginApp();
        });

    }
}