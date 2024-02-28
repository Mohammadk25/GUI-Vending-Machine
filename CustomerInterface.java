// Importing necessary packages for GUI components, layouts, and event handling
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

// Importing necessary packages for file operations
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Importing necessary packages for list and map data structures
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// Defining a new class named 'CustomerInterface' that extends JFrame (making it a window)
public class CustomerInterface extends JFrame {

    // Private members for the application logic
    private ArrayList<Drink> availableDrinks;  // List of drinks available
    private Map<Drink, Integer> cart;  // Cart to store selected drinks and their quantities

    // GUI components
    private JTable drinksTable;
    private DefaultTableModel drinksTableModel;
    private JButton addToCartButton;
    private JButton checkoutButton;
    private Color backgroundColor = new Color(240, 248, 255);
    private Color buttonColor = new Color(100, 149, 237);
    private Font tableFont = new Font("Arial", Font.PLAIN, 12);
    private Font buttonFont = new Font("Arial", Font.BOLD, 14);

    // Default constructor for the CustomerInterface class
    public CustomerInterface() {

        // Setting up the main window properties
        setTitle("Customer Interface");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close the window without terminating the application
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);

        // Initializing the drinks table
        String[] columnNames = {"Drink Name", "Price", "Quantity"};
        drinksTableModel = new DefaultTableModel(columnNames, 0);
        drinksTable = new JTable(drinksTableModel);
        // Setting a custom cell renderer for the first column (potentially to display images alongside text)
        drinksTable.getColumnModel().getColumn(0).setCellRenderer(new ImageTextCellRenderer());
        JScrollPane scrollPane = new JScrollPane(drinksTable);  // Scroll pane to allow scrolling of the table

        // Initializing the 'Add to Cart' button and its action
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> addToCart());

        // Initializing the 'Checkout' button and its action
        checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> checkout());

        // Panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addToCartButton);
        buttonPanel.add(checkoutButton);

        // Adding components to the main window
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Styling the table and buttons
        drinksTable.setFont(tableFont);
        drinksTable.setGridColor(new Color(200, 200, 200));
        drinksTable.setSelectionBackground(new Color(173, 216, 230));
        drinksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        addToCartButton.setFont(buttonFont);
        addToCartButton.setBackground(buttonColor);
        checkoutButton.setFont(buttonFont);
        checkoutButton.setBackground(buttonColor);

        // Loading drinks from a file and populating the table
        availableDrinks = loadAvailableDrinks();
        populateDrinksTable();

        // Initializing the cart as a HashMap
        cart = new HashMap<>();

        // Setting a border for the content pane
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Determining the row height for the table based on the tallest image
        int maxImageHeight = 0;
        for (Drink drink : availableDrinks) {
            ImageIcon icon = new ImageIcon(drink.getImagePath());
            if (icon.getIconHeight() > maxImageHeight) {
                maxImageHeight = icon.getIconHeight();
            }
        }
        drinksTable.setRowHeight(maxImageHeight);
    }

    // Method to load drinks from a file and return them as a list
    private ArrayList<Drink> loadAvailableDrinks() {
        ArrayList<Drink> drinks = new ArrayList<>();
        // Reading from a file
        try (BufferedReader reader = new BufferedReader(new FileReader("drinks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {  // Checking if the data format is correct
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    String image = parts[3];
                    drinks.add(new Drink(name, price, quantity, image));  // Creating a new Drink object and adding it to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handling exceptions
        }
        return drinks;  // Returning the list of drinks
    }

    // Method to populate the table with drinks from the list
    private void populateDrinksTable() {
        drinksTableModel.setRowCount(0);  // Clearing the table
        for (Drink drink : availableDrinks) {
            Object[] row = {drink, drink.getPrice(), drink.getQuantity()};
            drinksTableModel.addRow(row);  // Adding a row for each drink
            // Setting preferred widths for columns
            drinksTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            drinksTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            drinksTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        }
    }

    // Method to add selected drinks to the cart
    private void addToCart() {
        int selectedRow = drinksTable.getSelectedRow();  // Getting the selected row index
        if (selectedRow != -1) {  // If a row is selected
            Drink selectedDrink = availableDrinks.get(selectedRow);
            int quantityRequested = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Quantity:"));  // Asking the user for the quantity
            if (selectedDrink.getQuantity() < quantityRequested) {  // Checking stock
                JOptionPane.showMessageDialog(this, selectedDrink.getName() + " is out of stock.");
            } else {
                cart.put(selectedDrink, quantityRequested);  // Adding the drink to the cart
                JOptionPane.showMessageDialog(this, quantityRequested + " x " + selectedDrink.getName() + " added to cart.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a drink first.");  // If no row is selected
        }
    }

    // Method to save the updated drinks list to the file
    private void saveDrinks() {
        // Writing to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("drinks.txt"))) {
            for (Drink drink : availableDrinks) {
                String line = drink.getName() + "," + drink.getPrice() + "," + drink.getQuantity() + "," + drink.getImagePath();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handling exceptions
        }
    }

    // Method for the checkout process
    private void checkout() {
        if (cart.isEmpty()) {  // If the cart is empty
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
            return;
        }

        // Building the order summary
        StringBuilder orderSummary = new StringBuilder("Order Summary:\n");
        double total = 0;
        for (Map.Entry<Drink, Integer> entry : cart.entrySet()) {
            double cost = entry.getKey().getPrice() * entry.getValue();
            total += cost;
            orderSummary.append(entry.getKey().getName()).append(" x ").append(entry.getValue())
                        .append(" = ").append(cost).append("\n");
        }
        orderSummary.append("\nTotal: ").append(total);

        // Asking the user for confirmation
        int choice = JOptionPane.showConfirmDialog(this, orderSummary.toString(), "Confirm Order", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Deducting the quantities of the drinks in the cart
            for (Map.Entry<Drink, Integer> entry : cart.entrySet()) {
                Drink drink = entry.getKey();
                int quantityBought = entry.getValue();
                drink.setQuantity(drink.getQuantity() - quantityBought);
            }
            saveDrinks();  // Saving the updated list to the file
            saveOrder(orderSummary.toString());  // Saving the order
            cart.clear();  // Clearing the cart
            JOptionPane.showMessageDialog(this, "Order placed successfully!");

            // Refreshing the table and returning to the main interface
            populateDrinksTable();
            this.dispose();  // Closing the customer interface
            VendingMachine vendingMachine = new VendingMachine();  // Opening the main interface
            vendingMachine.setVisible(true);
        }
    }

    // Method to save the order to a file
    private void saveOrder(String orderSummary) {
        // Appending the order to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            writer.write(orderSummary);
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();  // Handling exceptions
        }
    }

}
