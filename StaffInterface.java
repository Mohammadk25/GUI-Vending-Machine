// Import necessary Swing classes for the graphical user interface.
import javax.swing.*;
// Import the DefaultTableModel class for managing table data in Swing.
import javax.swing.table.DefaultTableModel;
// Import necessary AWT classes for graphical components and layouts.
import java.awt.*;
// Import necessary IO classes for reading from and writing to files.
import java.io.*;
// Import necessary utility classes for data structures and operations.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Define a class named "StaffInterface" that extends (or inherits from) JFrame, which represents a window in a GUI.
public class StaffInterface extends JFrame {
    // Declare instance variables for the class.
    private ArrayList<StaffAccount> staffAccounts;   // List to store staff account data.
    private JTextField usernameField;                // Text field for entering username.
    private JPasswordField passwordField;            // Password field for entering password.
    private JTextArea staffListArea;                 // Text area for displaying list of staff.
    private DefaultTableModel drinksTableModel;      // Table model for managing drink data.
    private JButton viewOrdersButton;                // Button to view all orders.
    private JButton generateReportButton;            // Button to generate reports.
    private static final String STAFF_FILE = "staff.txt";  // Constant for the filename where staff data is stored.

    // Constructor for the StaffInterface class.
    public StaffInterface() {
        staffAccounts = loadStaffAccounts();  // Load staff account data from the file.
        if (!presentUserChoice()) {  // Present a dialog to the user for login or account creation. If unsuccessful, exit.
            return;
        }

        initializeUI();  // Initialize the user interface components.

        // Create a button for viewing all orders and add an action listener to it.
        viewOrdersButton = new JButton("View All Orders");
        viewOrdersButton.addActionListener(e -> viewOrders());

        // Create a button for generating reports and add an action listener to it.
        generateReportButton = new JButton("Generate Reports");
        generateReportButton.addActionListener(e -> generateReports());

        // Create a panel for reports buttons.
        JPanel reportsPanel = new JPanel();
        reportsPanel.add(viewOrdersButton);
        reportsPanel.add(generateReportButton);

        // Create a back button and add an action listener to it.
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.dispose();  // Close the current window.
            VendingMachine vendingMachine = new VendingMachine();  // Create an instance of the VendingMachine class.
            vendingMachine.setVisible(true);  // Make the vending machine window visible.
        });

        // Add the reports panel to the top of the window.
        add(reportsPanel, BorderLayout.NORTH);
    }

    // Method to present the user with the choice to login or create a new account.
    private boolean presentUserChoice() {
        // Define options for the user.
        Object[] options = {"Login", "Create New Account"};
        // Show a dialog with the options and store the user's choice.
        int choice = JOptionPane.showOptionDialog(null,
                "Please choose an action:",
                "Staff Interface",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        // Handle the user's choice.
        if (choice == JOptionPane.YES_OPTION) {  // If "Login" is chosen.
            boolean loggedIn = showLoginDialog();  // Show the login dialog and store the result.
            if (!loggedIn) {  // If login is unsuccessful.
                VendingMachine.main(new String[]{});  // Start the main method of the VendingMachine class.
                dispose();  // Close the current window.
                return false;  // Return false indicating unsuccessful login.
            } else {
                return true;  // Return true indicating successful login.
            }
        } else if (choice == JOptionPane.NO_OPTION) {  // If "Create New Account" is chosen.
            createNewAccount();  // Call the method to create a new account.
            VendingMachine.main(new String[]{});  // Start the main method of the VendingMachine class.
            dispose();  // Close the current window.
            return false;  // Return false indicating that a new account was created.
        } else {  // If the dialog is closed or cancelled.
            VendingMachine.main(new String[]{});  // Start the main method of the VendingMachine class.
            dispose();  // Close the current window.
            return false;  // Return false indicating that no action was taken.
        }
    }

    // Method to initialize the graphical user interface components.
    private void initializeUI() {
        setTitle("Staff Interface");  // Set the title of the window.
        setSize(450, 350);  // Set the size of the window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Set the default close operation for the window.
        setLayout(new BorderLayout(10, 10));  // Set the layout manager for the window.

        // Define background and button colors.
        Color backgroundColor = new Color(240, 248, 255);
        Color buttonColor = new Color(100, 149, 237);

        // Create a scroll pane and a bottom panel.
        JScrollPane scrollPane = createScrollPane(backgroundColor);
        JPanel bottomPanel = createBottomPanel(backgroundColor, buttonColor);

        // Add the scroll pane and bottom panel to the window.
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        displayStaffAccounts();  // Display the list of staff accounts.
        loadAndDisplayDrinks();  // Load and display the list of drinks.
    }

    // Method to create the top panel for creating new accounts.
    private JPanel createTopPanel(Color backgroundColor, Color buttonColor) {
        JPanel topPanel = new JPanel();  // Create a new panel.
        topPanel.setBorder(BorderFactory.createTitledBorder("Create New Account"));  // Set a border with a title for the panel.
        topPanel.setLayout(new GridLayout(3, 2, 5, 5));  // Set the layout manager for the panel.
        topPanel.setBackground(backgroundColor);  // Set the background color of the panel.

        // Create text fields for entering username and password.
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton createNewAccountButton = new JButton("Create New Account");  // Create a button for creating new accounts.
        createNewAccountButton.addActionListener(e -> createNewAccount());  // Add an action listener to the button.

        // Add components to the panel.
        topPanel.add(new JLabel("Username:"));
        topPanel.add(usernameField);
        topPanel.add(new JLabel("Password:"));
        topPanel.add(passwordField);
        topPanel.add(createNewAccountButton);

        return topPanel;  // Return the created panel.
    }

    // Method to create a scroll pane for the staff list area.
    private JScrollPane createScrollPane(Color backgroundColor) {
        staffListArea = new JTextArea(10, 30);  // Create a text area with specified rows and columns.
        staffListArea.setEditable(false);  // Set the text area to be non-editable.
        JScrollPane scrollPane = new JScrollPane(staffListArea);  // Create a scroll pane and add the text area to it.
        scrollPane.setBackground(backgroundColor);  // Set the background color of the scroll pane.
        return scrollPane;  // Return the created scroll pane.
    }

    // Method to create the bottom panel with buttons.
    private JPanel createBottomPanel(Color backgroundColor, Color buttonColor) {
        // Create buttons for different functionalities.
        JButton drinkManagementButton = new JButton("Manage Drinks");
        drinkManagementButton.setForeground(Color.BLACK);  // Set the text color of the button.
        drinkManagementButton.addActionListener(e -> openDrinkManagementInterface());  // Add an action listener to the button.

        JButton changeUsernameButton = new JButton("Change Username");  // Create a button for changing the username.
        changeUsernameButton.addActionListener(e -> changeUsername());  // Add an action listener to the button.

        JButton backButton = new JButton("Back");  // Create a back button.
        backButton.addActionListener(e -> {  // Add an action listener to the back button.
            this.dispose();  // Close the current window.
            VendingMachine vendingMachine = new VendingMachine();  // Create an instance of the VendingMachine class.
            vendingMachine.setVisible(true);  // Make the vending machine window visible.
        });

        // Create the bottom panel and set its layout manager and background color.
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.setBackground(backgroundColor);

        // Add the buttons to the bottom panel.
        bottomPanel.add(changeUsernameButton);
        bottomPanel.add(drinkManagementButton);
        bottomPanel.add(backButton);

        return bottomPanel;  // Return the created bottom panel.
    }

    // Method to change the username of a staff account.
    private void changeUsername() {
        String oldUsername = JOptionPane.showInputDialog("Enter current username:");  // Prompt the user to enter the current username.
        if (staffAccounts.stream().anyMatch(account -> account.getUsername().equals(oldUsername))) {  // Check if the entered username exists.
            String newUsername = JOptionPane.showInputDialog("Enter new username:");  // Prompt the user to enter the new username.
            staffAccounts.stream()  // Stream the list of staff accounts.
                    .filter(account -> account.getUsername().equals(oldUsername))  // Filter accounts with the entered old username.
                    .forEach(account -> account.setUsername(newUsername));  // Set the new username for the filtered accounts.
            saveStaffAccounts();  // Save the updated list of staff accounts.
            displayStaffAccounts();  // Display the updated list of staff accounts.
            JOptionPane.showMessageDialog(this, "Username changed successfully!");  // Show a success message.
        } else {
            JOptionPane.showMessageDialog(this, "Username not found.");  // Show an error message if the entered username is not found.
        }
    }

    // Method to load and display the list of drinks.
    private void loadAndDisplayDrinks() {
        loadDrinks();  // Load the list of drinks from the file.
        String[] columnNames = {"Drink Name", "Price", "Quantity"};  // Define column names for the drinks table.
        drinksTableModel = new DefaultTableModel(columnNames, 0);  // Create a table model with the defined column names.
        new JTable(drinksTableModel);  // Create a new table with the created table model.
        populateDrinksTable();  // Populate the table with drink data.
    }

    // Method to load the list of staff accounts from the file.
    private ArrayList<StaffAccount> loadStaffAccounts() {
        ArrayList<StaffAccount> accounts = new ArrayList<>();  // Create an empty list for storing staff accounts.
        try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_FILE))) {  // Create a BufferedReader to read from the staff file.
            String line;
            while ((line = reader.readLine()) != null) {  // Read each line from the file.
                String[] parts = line.split(",");  // Split the line by comma to get the username and password.
                if (parts.length == 2) {  // Check if the line has both username and password.
                    String username = parts[0];  // Get the username from the first part.
                    String password = parts[1];  // Get the password from the second part.
                    accounts.add(new StaffAccount(username, password));  // Create a new staff account and add it to the list.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Print the stack trace if an exception occurs.
        }
        return accounts;  // Return the loaded list of staff accounts.
    }

    // Method to show the login dialog.
    private boolean showLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2));  // Create a panel with a grid layout.
        JTextField loginUsernameField = new JTextField();  // Create a text field for entering the username.
        JPasswordField loginPasswordField = new JPasswordField();  // Create a password field for entering the password.
        panel.add(new JLabel("Email (Username):"));  // Add a label to the panel.
        panel.add(loginUsernameField);  // Add the username field to the panel.
        panel.add(new JLabel("Password:"));  // Add a label to the panel.
        panel.add(loginPasswordField);  // Add the password field to the panel.

        // Show a confirm dialog with the created panel.
        int result = JOptionPane.showConfirmDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {  // If the user clicks the OK button.
            String enteredUsername = loginUsernameField.getText();  // Get the entered username.
            String enteredPassword = new String(loginPasswordField.getPassword());  // Get the entered password.
            
            // Check if the entered username or password is empty.
            if (enteredUsername.trim().isEmpty() || enteredPassword.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields are required!");  // Show an error message.
                return false;  // Return false indicating unsuccessful login.
            }

            // Check if the entered username and password are valid.
            if (!isValidLogin(enteredUsername, enteredPassword)) {
                JOptionPane.showMessageDialog(this, "Incorrect details!");  // Show an error message.
                return false;  // Return false indicating unsuccessful login.
            }

            return true;  // Return true indicating successful login.
        }
        return false;  // Return false indicating that the login dialog was cancelled or closed.
    }

    // Method to check if the entered username and password are valid.
    private boolean isValidLogin(String username, String password) {
        for (StaffAccount account : staffAccounts) {  // Loop through each staff account.
            // Check if the account's username and password match the entered username and password.
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return true;  // Return true indicating that the entered details are valid.
            }
        }
        return false;  // Return false indicating that the entered details are invalid.
    }

    // Method to create a new staff account.
    private void createNewAccount() {
        String newUsername = JOptionPane.showInputDialog("Enter new username:");  // Prompt the user to enter a new username.
        if (newUsername == null || newUsername.trim().isEmpty()) {  // Check if the entered username is empty or null.
            JOptionPane.showMessageDialog(this, "Username cannot be empty.");  // Show an error message.
            return;  // Exit the method.
        }

        String newPassword = JOptionPane.showInputDialog("Enter new password:");  // Prompt the user to enter a new password.
        if (newPassword == null || newPassword.trim().isEmpty()) {  // Check if the entered password is empty or null.
            JOptionPane.showMessageDialog(this, "Password cannot be empty.");  // Show an error message.
            return;  // Exit the method.
        }

        // Create a new staff account with the entered username and password and add it to the list.
        staffAccounts.add(new StaffAccount(newUsername, newPassword));
        saveStaffAccounts();  // Save the updated list of staff accounts to the file.
        JOptionPane.showMessageDialog(this, "New account created successfully!");  // Show a success message.
    }

    // Empty method to populate the drinks table. This needs to be implemented for actual functionality.
    private void populateDrinksTable() {

    }

    // Method to load the list of drinks from the file.
    private ArrayList<Drink> loadDrinks() {
        ArrayList<Drink> drinks = new ArrayList<>();  // Create an empty list for storing drinks.
        return drinks;  // Return the loaded list of drinks.
    }

    // Method to save the list of staff accounts to the file.
    private void saveStaffAccounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STAFF_FILE))) {  // Create a BufferedWriter to write to the staff file.
            for (StaffAccount account : staffAccounts) {  // Loop through each staff account.
                // Write the account's username and password to the file.
                writer.write(account.getUsername() + "," + account.getPassword());
                writer.newLine();  // Move to the next line.
            }
        } catch (IOException e) {
            e.printStackTrace();  // Print the stack trace if an exception occurs.
        }
    }

    // Method to display the list of staff accounts in the staff list area.
    private void displayStaffAccounts() {
        staffListArea.setText("Staff Accounts:\n");  // Set the initial text of the staff list area.
        for (StaffAccount account : staffAccounts) {  // Loop through each staff account.
            staffListArea.append(account.getUsername() + "\n");  // Append the account's username to the staff list area.
        }
    }

    // Method to open the drink management interface.
    private void openDrinkManagementInterface() {
        DrinkManagement drinkManagement = new DrinkManagement();  // Create an instance of the DrinkManagement class.
        drinkManagement.setVisible(true);  // Make the drink management window visible.
    }

    // Method to view all orders.
    private void viewOrders() {
        StringBuilder ordersText = new StringBuilder("All Orders:\n");  // Create a StringBuilder for storing order text.
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {  // Create a BufferedReader to read from the orders file.
            String line;
            while ((line = reader.readLine()) != null) {  // Read each line from the file.
                ordersText.append(line).append("\n");  // Append the line to the orders text.
            }
        } catch (IOException e) {
            e.printStackTrace();  // Print the stack trace if an exception occurs.
        }
        JOptionPane.showMessageDialog(this, ordersText.toString());  // Show a dialog with the orders text.
    }

    // Method to generate reports.
    private void generateReports() {
        Map<String, Integer> drinkCounts = new HashMap<>();  // Create a map for storing the count of each drink.
        double highestSale = 0;  // Variable to store the highest sale amount.

        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {  // Create a BufferedReader to read from the orders file.
            String line;
            while ((line = reader.readLine()) != null) {  // Read each line from the file.
                if (line.contains(" x ")) {  // Check if the line contains drink order data.
                    String[] parts = line.split(" x ");  // Split the line by " x " to get the drink name and quantity.
                    String drinkName = parts[0].trim();  // Get the drink name.
                    int quantity = Integer.parseInt(parts[1].split(" = ")[0].trim());  // Get the drink quantity.
                    // Update the drink count in the map.
                    drinkCounts.put(drinkName, drinkCounts.getOrDefault(drinkName, 0) + quantity);
                } else if (line.contains("Total: ")) {  // Check if the line contains total sale data.
                    double totalSale = Double.parseDouble(line.split(": ")[1].trim());  // Get the total sale amount.
                    highestSale = Math.max(highestSale, totalSale);  // Update the highest sale amount if necessary.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Print the stack trace if an exception occurs.
        }

        // Find the most popular drink (the drink with the highest count).
        String mostPopularDrink = drinkCounts.entrySet().stream()
                                             .max(Map.Entry.comparingByValue())
                                             .get().getKey();

        // Create a report string.
        String report = "Most Popular Drink: " + mostPopularDrink + "\n";
        report += "Highest Amount Per Sales: " + highestSale + "\n";

        JOptionPane.showMessageDialog(this, report);  // Show a dialog with the report.
    }

    // The main method of the StaffInterface class.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {  // Use the SwingUtilities.invokeLater method to ensure thread safety.
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  // Set the look and feel of the GUI to match the system's look and feel.
            } catch (Exception e) {
                e.printStackTrace();  // Print the stack trace if an exception occurs.
            }
            new StaffInterface().setVisible(true);  // Create an instance of the StaffInterface class and make its window visible.
        });
    }
}

class StaffAccount {
    private String username;
    private String password;

    public StaffAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

	public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
class Drink {
    private String name;
    private double price;
    private int quantity;
    private String imagePath;

    public Drink(String name, double price, int quantity, String imagePath) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static ArrayList<Drink> loadDrinks() {
        ArrayList<Drink> drinks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("drinks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    String imagePath = parts[3];
                    drinks.add(new Drink(name, price, quantity, imagePath));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drinks;
    }

    public static void saveDrinks(ArrayList<Drink> drinksList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("drinks.txt"))) {
            for (Drink drink : drinksList) {
                String line = drink.getName() + "," + drink.getPrice() + "," + drink.getQuantity() + "," + drink.getImagePath();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
