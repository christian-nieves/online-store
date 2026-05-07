
package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starter code for the Online Store workshop.
 * Students will complete the TODO sections to make the program work.
 */
public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: id|name|price)
        loadInventory("products.csv", inventory);

        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();                     // clear newline

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    /**
     * Reads product data from a file and populates the inventory list.
     * File format (pipe-delimited):
     * id|name|price
     * <p>
     * Example line:
     * A17|Wireless Mouse|19.99
     */
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        // TODO: read each line, split on "|",
        //       create a Product object, and add it to the inventory list
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String id = parts[0];
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                inventory.add(new Product(id, name, price));
            }
            bufferedReader.close();

        } catch (Exception e){
            System.err.println("Something went wrong. Please try again!");
        }
    }

    /**
     * Displays all products and lets the user add one to the cart.
     * Typing X returns to the main menu.
     */
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {
        // TODO: show each product (id, name, price),
        //       prompt for an id, find that product, add to cart
        System.out.println("All available Products: ");
        for (Product product : inventory) {
            System.out.println("ID: " + product.getId() + " | Name: " + product.getName() + " | Price: " + product.getPrice()); // Prints out all available products
        }

        System.out.println("Enter Product Id to add to cart(X to exit): "); // Asks user for product id to add to cart or if they want to exit this screen
        String userInput = scanner.nextLine();

        if (userInput.equalsIgnoreCase("X")){ // If the user clicks "X" then they will be brought back to the home screen
            return;
        }

        Product foundProduct = findProductById(userInput, inventory);
        if (foundProduct == null) {
            System.out.println("Sorry the id doesn't exist.");
        } else {
            cart.add(foundProduct);
            System.out.println("Added " + foundProduct.getName() + " to cart!");
        }


    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        // TODO:
        //   • list each product in the cart
        //   • compute the total cost
        //   • ask the user whether to check out (C) or return (X)
        //   • if C, call checkOut(cart, totalAmount, scanner)

        double totalPrice = 0;
        System.out.println("Cart: ");
        for (Product product : cart) {
            System.out.println("ID: " + product.getId() + " | Name: " + product.getName() + " | Price: " + product.getPrice()); // Prints out all products in cart
            totalPrice += product.getPrice();
        }
        System.out.println("Total Price: " + totalPrice);

        System.out.println("Check Out(C) or Exit(X): "); // Asks user for product id to add to cart or if they want to exit this screen
        String userInput = scanner.nextLine();

        if (userInput.equalsIgnoreCase("C")){
            checkOut(cart, totalPrice, scanner);
        }

        if (userInput.equalsIgnoreCase("X")){ // If the user clicks "X" then they will exit the cart
            return;
        }

    }


    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        // TODO: implement steps listed above

        System.out.println("Press 'Y' if you want to check out all items in your cart(X to exit): ");
        String userInput = scanner.nextLine();

        if (userInput.equalsIgnoreCase("Y")){
            System.out.println("Total Amount Due: " + totalAmount);
            System.out.println("Enter Payment Amount: ");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine();

            if (paymentAmount > totalAmount) {
                System.out.println("Payment Successful!");
                double changeDue = paymentAmount - totalAmount;
                System.out.println("Change Due: " + changeDue);
                System.out.println(" ");
                System.out.println("Receipt");

            }
        }


    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // TODO: loop over the list and compare ids
        for (Product product : inventory) {
            if (product.getId().equalsIgnoreCase(id)) {
                return product;
            }
        }

        return null;
    }
}

 