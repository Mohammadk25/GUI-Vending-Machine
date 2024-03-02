# Vending Machine System

This repository contains the code for a vending machine system consisting of a customer interface and a staff interface. The system allows customers to view available drinks, add them to the cart, and checkout. Staff members can manage accounts, view orders, and generate reports.

## Customer Interface

### Functionality
- Display available drinks with their names, prices, and quantities.
- Add selected drinks to the cart.
- Checkout to place an order.
- Automatically update drink quantities after checkout.

### Usage
1. Run the `CustomerInterface` class to open the customer interface.
2. View available drinks and add them to the cart.
3. Proceed to checkout to finalize the order.

## Staff Interface

### Functionality
- View all staff accounts.
- Create new staff accounts.
- View all orders made by customers.
- Generate reports for sales and inventory.

### Usage
1. Run the `StaffInterface` class to open the staff interface.
2. Log in with an existing staff account or create a new one.
3. View orders or generate reports as required.

## Additional Classes

- `Drink`: Represents a drink item with attributes such as name, price, quantity, and image path.
- `StaffAccount`: Represents a staff member's account with a username and password.

## File Handling
- Drink information is loaded from and saved to a file named `drinks.txt`.
- Staff account information is loaded from and saved to a file named `staff.txt`.

## Dependencies
- Java Swing: Used for building the graphical user interface.
- File I/O: Used for reading and writing data to files.

## Development
- Java version: JDK 8 or later
- Integrated Development Environment (IDE): Any Java-supported IDE such as IntelliJ IDEA, Eclipse, or NetBeans.

## Contributors
- Sardar Mohammad Khan


Happy Vending!
