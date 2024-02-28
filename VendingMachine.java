// Importing necessary packages for GUI components and event handling
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// Importing necessary packages for file operations
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Importing the ArrayList class for potential list operations
import java.util.ArrayList;

// Defining a new class named 'VendingMachine' that extends JFrame (making it a window)
public class VendingMachine extends JFrame {

    // Declaring private member variables (buttons) for the interface
    private JButton customerButton;
    private JButton staffButton;

    // Defining a constant for the background color of the interface
    private static final Color BACKGROUND_COLOR = Color.BLACK;

    // Default constructor for the VendingMachine class
    public VendingMachine() {

        // Trying to set the look and feel of the GUI to match the system's
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();  // Print any exceptions that might occur
        }

        // Setting the title of the window
        setTitle("Vending Machine");
        // Setting the size of the window
        setSize(600, 400);
        // Setting the default close operation (closes the application when the window is closed)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setting the layout of the main window to BorderLayout
        setLayout(new BorderLayout());
        // Setting the background color of the content pane
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Creating an ImageIcon object from an image file to represent the vending machine
        ImageIcon machineIcon = new ImageIcon("machine.png");
        // Creating a JLabel to hold and display the image icon
        JLabel machineLabel = new JLabel(machineIcon);

        // Creating the main panel with a BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);  // Setting its background color

        // Creating a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));  // Grid layout for buttons
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));  // Setting its border
        buttonPanel.setBackground(BACKGROUND_COLOR);  // Setting its background color

        // Creating styled buttons and assigning their action behaviors
        customerButton = createStyledButton("Customer", e -> openCustomerInterface());
        staffButton = createStyledButton("Staff", e -> openStaffInterface());
        JButton viewOrdersButton = createStyledButton("View Orders", e -> viewAllOrders());

        // Adding the buttons to the button panel
        buttonPanel.add(customerButton);
        buttonPanel.add(staffButton);
        buttonPanel.add(viewOrdersButton);

        // Adding the button panel and the image label to the main panel
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(machineLabel, BorderLayout.EAST);

        // Creating and adding an exit button
        JButton exitButton = createStyledButton("Exit", e -> System.exit(0));
        buttonPanel.add(exitButton);

        // Adding the main panel to the window's content pane
        add(mainPanel, BorderLayout.CENTER);
    }

    // Method to create a button with a specific style and action
    private JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);  // Creating a new button with the given text
        button.setFont(new Font("Arial", Font.BOLD, 16));  // Setting the font of the button
        button.addActionListener(actionListener);  // Adding the action listener to the button
        button.setBackground(new Color(100, 149, 237));  // Setting the background color of the button
        button.setForeground(Color.BLACK);  // Setting the foreground (text) color of the button
        return button;  // Returning the styled button
    }

    // Method to open the customer interface
    private void openCustomerInterface() {
        CustomerInterface customerInterface = new CustomerInterface();  // Creating a new customer interface
        customerInterface.setVisible(true);  // Making it visible
        this.setVisible(false);  // Hiding the current window
    }

    // Method to view all orders from a file
    private void viewAllOrders() {
        StringBuilder ordersText = new StringBuilder("All Orders:\n");  // Initializing a StringBuilder for order text
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {  // Trying to read from a file
            String line;
            while ((line = reader.readLine()) != null) {  // Reading each line until the end
                ordersText.append(line).append("\n");  // Appending each line to the StringBuilder
            }
        } catch (IOException e) {
            e.printStackTrace();  // Print any IO exceptions that might occur
        }
        // Displaying a message dialog with the orders text
        JOptionPane.showMessageDialog(this, ordersText.toString());
    }

    // Method to open the staff interface
    private void openStaffInterface() {
        StaffInterface staffInterface = new StaffInterface();  // Creating a new staff interface
        staffInterface.setVisible(true);  // Making it visible
        this.setVisible(false);  // Hiding the current window
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {  // Using the Swing thread to ensure thread safety
            VendingMachine vendingMachine = new VendingMachine();  // Creating a new VendingMachine object
            vendingMachine.setVisible(true);  // Making it visible
        });
    }
}
