// Import the Component class from the java.awt package, which provides graphical components.
import java.awt.Component;

// Import the ImageIcon class from the javax.swing package, which provides support for icons in Swing components.
import javax.swing.ImageIcon;

// Import the JTable class from the javax.swing package, which provides support for creating and displaying tables in Swing.
import javax.swing.JTable;

// Import the DefaultTableCellRenderer class from the javax.swing.table package, which provides a default cell renderer for a JTable.
import javax.swing.table.DefaultTableCellRenderer;

// Import the JLabel class from the javax.swing package, which provides support for displaying text, images, or both in a lightweight component.
import javax.swing.JLabel;

// Define a new class "ImageTextCellRenderer" that extends (inherits from) the DefaultTableCellRenderer class.
class ImageTextCellRenderer extends DefaultTableCellRenderer {

    // Override the getTableCellRendererComponent method from the DefaultTableCellRenderer class.
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        // Call the superclass's implementation of the method to get a JLabel.
        // This provides default rendering behavior for the cell.
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Check if the value passed to the renderer is an instance of the Drink class.
        if (value instanceof Drink) {
            // Cast the value to a Drink object.
            Drink drink = (Drink) value;

            // Set the text of the label to the name of the drink.
            label.setText(drink.getName());
            
            // Create a new ImageIcon using the image path of the drink and set it as the icon of the label.
            label.setIcon(new ImageIcon(drink.getImagePath()));
        }

        // Return the modified JLabel to be used as the renderer for the cell.
        return label;
    }
}
