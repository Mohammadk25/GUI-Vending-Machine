// These are imports from Java's standard library for GUI and image operations.
import java.awt.*;  // Provides classes for creating user interface components.
import java.awt.image.BufferedImage;  // Represents an image with an accessible buffer of image data.
import java.io.File;  // Represents file and directory pathnames.
import java.io.IOException;  // Signals that an I/O exception of some sort has occurred.
import java.util.ArrayList;  // A resizable-array implementation of the List interface.

// These are Java's extensions for image operations and GUI components.
import javax.imageio.ImageIO;  // A class containing static convenience methods for locating ImageReaders/Writers.
import javax.swing.*;  // Contains classes for creating user interfaces and for painting graphics and images.
import javax.swing.table.DefaultTableModel;  // Default table model.

// This is the main class for managing drinks in a vending machine. It extends JFrame, which means it's a window.
class DrinkManagement extends JFrame {

    // These are member variables for the class.
    private ArrayList<Drink> drinksList;  // List of drink objects.
    private JTextField nameField, priceField, quantityField;  // Text fields to enter drink details.
    private JLabel imageLabel;  // Label to display the drink image.
    private File selectedImage;  // File object representing the selected drink image.
    private JTable drinksTable;  // Table to display drinks.
    private DefaultTableModel drinksTableModel;  // Table model for drinksTable.

    // Constructor for the class.
    public DrinkManagement() {
        // Basic JFrame initialization.
        setTitle("Drink Management");  // Title of the window.
        setSize(500, 400);  // Window size.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close operation.
        setLayout(new BorderLayout(10, 10));  // Set layout of the frame to BorderLayout.

        drinksList = Drink.loadDrinks();  // Load drinks from storage (not provided in the code).

        // Creating a button and adding an action listener to it.
        JButton modifyDrinkButton = new JButton("Modify Drink");
        modifyDrinkButton.addActionListener(e -> modifyDrink());  // Lambda function to call modifyDrink() method.

        // Creating different panels and scroll panes.
        JPanel formPanel = createFormPanel(modifyDrinkButton);
        JScrollPane tableScrollPane = createTableScrollPane();
        JPanel bottomPanel = createBottomPanel();

        // Adding panels to the main frame.
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Additional button creations with actions to navigate.
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.dispose();
            StaffInterface staffInterface = new StaffInterface();
            staffInterface.setVisible(true);
        });

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            this.dispose();
            VendingMachine vendingMachine = new VendingMachine();
            vendingMachine.setVisible(true);
        });

        setLocationRelativeTo(null);  // Center the frame on the screen.
    }

    private JPanel createFormPanel(JButton modifyDrinkButton) {
        // Create a new JPanel with a grid layout of 5 rows and 2 columns.
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        
        // Define background and button colors using RGB values.
        Color backgroundColor = new Color(240, 248, 255);
        Color buttonColor = new Color(100, 149, 237);
        
        // Set the background color of the form panel.
        formPanel.setBackground(backgroundColor);

        // Initialize the text fields for name, price, and quantity.
        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();

        // Create a button for image selection and a label to show selected image status.
        JButton selectImageButton = new JButton("Select Image");
        imageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);

        // Button to add a new drink.
        JButton addDrinkButton = new JButton("Add Drink");

        // Attach action listeners to buttons to define what they do when clicked.
        selectImageButton.addActionListener(e -> chooseImage());
        addDrinkButton.addActionListener(e -> addDrink());

        // Add components to the form panel.
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(selectImageButton);
        formPanel.add(imageLabel);
        formPanel.add(addDrinkButton);
        formPanel.add(modifyDrinkButton);

        // Set the background color for buttons.
        selectImageButton.setBackground(buttonColor);
        addDrinkButton.setBackground(buttonColor);
        modifyDrinkButton.setBackground(buttonColor);

        // Return the fully constructed form panel.
        return formPanel;
    }

    private JScrollPane createTableScrollPane() {
        // Define column names for the drinks table.
        String[] columnNames = {"Drink Name", "Price", "Quantity", "Image"};

        // Create a table model with column names and no initial rows.
        // Override getColumnClass to set custom rendering for the Image column.
        drinksTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 3) return ImageIcon.class;
                return Object.class;
            }
        };

        // Create a table using the defined table model.
        drinksTable = new JTable(drinksTableModel);

        // Populate the table with drink data.
        populateDrinksTable();

        // Adjust column width for the Image column and set row height.
        drinksTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        drinksTable.setRowHeight(50);

        // Return a scrollable pane containing the drinks table.
        return new JScrollPane(drinksTable);
    }

    private JPanel createBottomPanel() {
        // Create a new JPanel.
        JPanel bottomPanel = new JPanel();
        
        // Set the layout for the panel as a grid of 1 row and 2 columns.
        bottomPanel.setLayout(new GridLayout(1, 2));

        // Define colors using RGB values.
        Color backgroundColor = new Color(240, 248, 255);
        Color buttonColor = new Color(100, 149, 237);

        // Create "Back" and "Home" buttons with attached action listeners.
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.dispose();
            StaffInterface staffInterface = new StaffInterface();
            staffInterface.setVisible(true);
        });

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            this.dispose();
            VendingMachine vendingMachine = new VendingMachine();
            vendingMachine.setVisible(true);
        });

        // Set the background color for buttons.
        backButton.setBackground(buttonColor);
        homeButton.setBackground(buttonColor);

        // Add buttons to the bottom panel.
        bottomPanel.add(backButton);
        bottomPanel.add(homeButton);

        // Return the constructed bottom panel.
        return bottomPanel;
    }
    private void modifyDrink() {
        // Get the index of the selected row from the drinks table.
        int selectedRow = drinksTable.getSelectedRow();

        // Check if no row is selected.
        if (selectedRow == -1) {
            // Show a dialog notifying the user to select a drink.
            JOptionPane.showMessageDialog(this, "Please select a drink to modify.");
            return;
        }

        // Define the modification options available to the user.
        String[] options = {"Name", "Price", "Quantity", "Image"};
        // Show a dialog for the user to select which attribute they want to modify.
        String choice = (String) JOptionPane.showInputDialog(
                this,
                "What do you want to modify?",
                "Modify Drink",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Exit the method if the user cancels the dialog.
        if (choice == null) return;

        // Retrieve the selected drink from the list based on the selected row.
        Drink selectedDrink = drinksList.get(selectedRow);

        // Based on the user's choice, prompt for the new value and update the drink.
        switch (choice) {
            case "Name":
                String newName = JOptionPane.showInputDialog(this, "Enter new name:");
                if (newName != null && !newName.isEmpty()) {
                    selectedDrink.setName(newName);
                }
                break;
            case "Price":
                try {
                    double newPrice = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter new price:"));
                    selectedDrink.setPrice(newPrice);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid price.");
                }
                break;
            case "Quantity":
                try {
                    int newQuantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter new quantity:"));
                    selectedDrink.setQuantity(newQuantity);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity.");
                }
                break;
            case "Image":
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File newImage = fileChooser.getSelectedFile();
                    selectedDrink.setImagePath(newImage.getPath());
                }
                break;
        }

        // Refresh the table to show the updated data.
        populateDrinksTable();
        // Save the updated list of drinks (method not provided, but presumably saves to a file or database).
        Drink.saveDrinks(drinksList);
    }

    private void populateDrinksTable() {
        // Clear all rows from the table model.
        drinksTableModel.setRowCount(0);

        // Loop through each drink in the drinks list.
        for (Drink drink : drinksList) {
            ImageIcon imageIcon = null;
            try {
                // Read the image from the drink's image path.
                BufferedImage image = ImageIO.read(new File(drink.getImagePath()));
                // Scale the image to fit the table and create an ImageIcon.
                imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Prepare row data for the drink.
            Object[] rowData = {
                drink.getName(),
                drink.getPrice(),
                drink.getQuantity(),
                imageIcon
            };
            // Add the row to the table model.
            drinksTableModel.addRow(rowData);
        }
    }

    private void chooseImage() {
        // Create a file chooser dialog.
        JFileChooser fileChooser = new JFileChooser();
        // Show the dialog and get the user's decision.
        int returnValue = fileChooser.showOpenDialog(this);

        // If the user selected a file.
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // Get the selected file.
            selectedImage = fileChooser.getSelectedFile();
            try {
                // Read the image from the selected file.
                BufferedImage image = ImageIO.read(selectedImage);
                // Scale the image to fit the label and create an ImageIcon.
                ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                // Set the ImageIcon to the image label.
                imageLabel.setIcon(imageIcon);
                // Clear the text of the image label.
                imageLabel.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addDrink() {
        // Retrieve the drink name from the name field.
        String name = nameField.getText();

        // Validate the name.
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid drink name.");
            return;
        }

        double price;
        try {
            // Parse the price from the price field.
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price.");
            return;
        }

        int quantity;
        try {
            // Parse the quantity from the quantity field.
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
            return;
        }

        // Validate the selected image.
        if (selectedImage == null) {
            JOptionPane.showMessageDialog(this, "Please select an image.");
            return;
        }

        // Create a new drink with the given details.
        Drink newDrink = new Drink(name, price, quantity, selectedImage.getPath());
        // Add the new drink to the drinks list.
        drinksList.add(newDrink);
        // Refresh the table to show the new drink.
        populateDrinksTable();
        // Save the updated list of drinks.
        Drink.saveDrinks(drinksList);

        // Notify the user that the drink has been added.
        JOptionPane.showMessageDialog(this, name + " has been added.");
        // Clear the form fields.
        clearFormContent();
    }

    private void clearFormContent() {
        // Clear the text fields.
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        // Clear the image label.
        imageLabel.setIcon(null);
        imageLabel.setText("No Image Selected");
        // Reset the selected image.
        selectedImage = null;
    }

    public static void main(String[] args) {
        // Start the GUI in the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            new DrinkManagement().setVisible(true);
        });
    }
}