import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourierApp {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel topNavPanel;
    private JPanel contentPanel;
    private JButton createShipmentButton;
    private JButton trackShipmentButton;
    private JButton infobutton;

    // Define a list to store shipment details
    private List<Shipment> shipments = new ArrayList<>();

    public CourierApp() {
        // Load shipment details from the file
        loadShipmentDetails();
    }

    public void createAndShowGUI() {
        frame = new JFrame("Courier App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));

        mainPanel = new JPanel(new BorderLayout());

        createTopNavigation();
        createContentPanel();

        mainPanel.add(topNavPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    // ...
    private void createTopNavigation() {
        topNavPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topNavPanel.setBackground(new Color(30, 144, 255)); // Green background
        topNavPanel.setPreferredSize(new Dimension(frame.getWidth(), 80));

        createShipmentButton = createButton("Create Shipment");
        trackShipmentButton = createButton("Track Shipment");
        infobutton = new JButton("Info"); // Create "Info" button

        // Apply the same styling as the "Track Shipment" button
        infobutton.setBackground(new Color(46, 139, 87)); // Dark green background
        infobutton.setForeground(Color.WHITE);
        infobutton.setBorderPainted(true);
        infobutton.setFocusPainted(false);
        infobutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        infobutton.setFont(new Font("Arial", Font.PLAIN, 18));

        JTextArea infoTextArea = new JTextArea(10, 30);
        infoTextArea.setText("89 - Niharika Bhagwat\r\n" + //
                "\r\n" + //
                "90 - Drishti Waikar \r\n" + //
                "\r\n" + //
                "91 - Soham Vaity\r\n" + //
                "");
        infoTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        infoTextArea.setBackground(new Color(240, 240, 240));
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setLineWrap(true);
        infoTextArea.setEditable(false);
        infoTextArea.setBorder(BorderFactory.createEmptyBorder());

        infobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.removeAll();
                contentPanel.add(infoTextArea);
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });

        topNavPanel.add(createShipmentButton);
        topNavPanel.add(trackShipmentButton);
        topNavPanel.add(infobutton);
    }
    // ...

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(46, 139, 87)); // Dark green background
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.PLAIN, 18));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleButtonClick(text);
            }
        });

        return button;
    }

    private void createContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
    }

    private void handleButtonClick(String buttonText) {
        contentPanel.removeAll();

        if (buttonText.equals("Create Shipment")) {
            createShipmentForm();
        } else if (buttonText.equals("Track Shipment")) {
            openTrackShipment();
        } else if (buttonText.equals("Manage Clients")) {
            // Invoke this method to display the ManageClients panel
        } else if (buttonText.equals("Info")) {
            // Add code for the Reports functionality here
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void createShipmentForm() {
        // Create a Create Shipment form

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));

        String[] labels = { "Tracking ID:", "Name:", "Weight (kg):", "Destination:" };
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            fields[i] = new JTextField();

            formPanel.add(label);
            formPanel.add(fields[i]);
        }

        JButton submitButton = new JButton("Submit");
        formPanel.add(new JLabel());
        formPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] details = new String[labels.length];
                for (int i = 0; i < labels.length; i++) {
                    details[i] = labels[i] + " " + fields[i].getText();
                }

                saveShipmentDetails(details);

                JOptionPane.showMessageDialog(frame, "Shipment created successfully!");

                clearFormFields(fields);
            }
        });

        contentPanel.add(formPanel);
    }

    private void saveShipmentDetails(String[] details) {
        String filePath = "shipments.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (String detail : details) {
                writer.write(detail + "\n");
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFormFields(JTextField[] fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void openTrackShipment() {
        SwingUtilities.invokeLater(() -> {
            new TrackShipment(shipments).createAndShowGUI();
        });
    }

    private void loadShipmentDetails() {
        String filePath = "shipments.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String trackingID = null;
            String name = null;
            double weight = 0.0;
            String destination = null;

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Tracking ID: ")) {
                    trackingID = line.substring("Tracking ID: ".length());
                } else if (line.startsWith("Name: ")) {
                    name = line.substring("Name: ".length());
                } else if (line.startsWith("Weight (kg): ")) {
                    weight = Double.parseDouble(line.substring("Weight (kg): ".length()));
                } else if (line.startsWith("Destination: ")) {
                    destination = line.substring("Destination: ".length());
                } else if (line.isEmpty()) {
                    if (trackingID != null && name != null && destination != null) {
                        shipments.add(new Shipment(trackingID, name, weight, destination));
                    }

                    trackingID = null;
                    name = null;
                    weight = 0.0;
                    destination = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginApp();
        });
    }
}

class TrackShipment {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTextField trackingIDField;
    private JTextArea resultArea;
    private List<Shipment> shipments;

    TrackShipment(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public void createAndShowGUI() {
        frame = new JFrame("Track Shipment");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 300));

        mainPanel = new JPanel(new BorderLayout());

        createContentPanel();

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void createContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Track Shipment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel trackingIDLabel = new JLabel("Enter Tracking ID:");
        trackingIDField = new JTextField(15);

        JButton trackButton = new JButton("Track");

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        trackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String trackingID = trackingIDField.getText();
                trackShipment(trackingID);
            }
        });

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(trackingIDLabel);
        contentPanel.add(trackingIDField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(trackButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(scrollPane);
    }

    private void trackShipment(String trackingID) {
        resultArea.setText("");

        for (Shipment shipment : shipments) {
            if (shipment.getTrackingID().equals(trackingID)) {
                resultArea.append("Tracking ID: " + shipment.getTrackingID() + "\n");
                resultArea.append("Name: " + shipment.getName() + "\n");
                resultArea.append("Weight: " + shipment.getWeight() + " kg\n");
                resultArea.append("Destination: " + shipment.getDestination() + "\n");
                return;
            }
        }

        resultArea.append("Shipment with Tracking ID " + trackingID + " not found.");
    }
}

class Shipment {
    private String trackingID;
    private String name;
    private double weight;
    private String destination;

    public Shipment(String trackingID, String name, double weight, String destination) {
        this.trackingID = trackingID;
        this.name = name;
        this.weight = weight;
        this.destination = destination;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public String getDestination() {
        return destination;
    }
}
