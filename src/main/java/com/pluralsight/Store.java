
package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file
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

    // load inventory method that creates products and adds it to the inventory list
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
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

    // display products that shows the user all of the products
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {

        System.out.println("All available Products: ");
        for (Product product : inventory) {
            System.out.println(product); // Prints out all available products
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

    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {

        double totalPrice = 0;
        System.out.println("Cart: ");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        } else {
            for (Product product : cart) {
                System.out.println(product); // Prints out all products in cart
                totalPrice += product.getPrice();
            }
            System.out.printf("Total Price: $%.2f%n", totalPrice);

            System.out.println("Check Out(C) | Remove(R) | Exit(X): "); // Asks user for product id to add to cart or if they want to exit this screen
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("C")) {
                checkOut(cart, totalPrice, scanner);
            }

            if (userInput.equalsIgnoreCase("R")){
                removeItem(cart, totalPrice, scanner);
            }

            if (userInput.equalsIgnoreCase("X")) { // If the user clicks "X" then they will exit the cart
                return;
            }
        }

    }

    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {

        System.out.println("Press 'Y' if you want to check out all items in your cart(X to exit): ");
        String userInput = scanner.nextLine();

        if (userInput.equalsIgnoreCase("Y")){
            System.out.println("Total Amount Due: $" + String.format("%.2f", totalAmount));
            System.out.println("Enter Payment Amount: ");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine();

            if (paymentAmount >= totalAmount) {
                System.out.println("Payment Successful!");
                double changeDue = paymentAmount - totalAmount;

                System.out.println("\n===================== RECEIPT =======================");
                System.out.printf("%-8s %-35s %8s%n", "ID", "Name", "Price");
                System.out.println("-----------------------------------------------------");
                for (Product product : cart) {
                    System.out.printf("%-8s %-35s $%7.2f%n",
                            product.getId(), product.getName(), product.getPrice());
                }
                System.out.println("-----------------------------------------------------");
                System.out.printf("%-44s $%7.2f%n", "TOTAL:", totalAmount);
                System.out.printf("%-44s $%7.2f%n", "PAID:", paymentAmount);
                System.out.printf("%-44s $%7.2f%n", "CHANGE DUE:", changeDue);
                System.out.println("-----------------------------------------------------");
                System.out.println("           Thank you for your purchase!              ");
                System.out.println("=====================================================\n");

                // writes and saves receipt to a new file in receipts folder
                try {
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
                    PrintWriter writer = new PrintWriter("Receipts/" + timestamp + ".txt");

                    writer.println("\n===================== RECEIPT =======================");
                    writer.printf("%-8s %-35s %8s%n", "ID", "Name", "Price");
                    writer.println("-----------------------------------------------------");
                    for (Product product : cart) {
                        writer.printf("%-8s %-35s $%7.2f%n",
                                product.getId(), product.getName(), product.getPrice());
                    }
                    writer.println("-----------------------------------------------------");
                    writer.printf("%-44s $%7.2f%n", "TOTAL:", totalAmount);
                    writer.printf("%-44s $%7.2f%n", "PAID:", paymentAmount);
                    writer.printf("%-44s $%7.2f%n", "CHANGE DUE:", changeDue);
                    writer.println("-----------------------------------------------------");
                    writer.println("           Thank you for your purchase!              ");
                    writer.println("=====================================================\n");
                    writer.close();

                    System.out.println("Your receipt has been emailed to you!");


                } catch (Exception e) {
                    System.err.println("Something went wrong emailing you the receipt. Please try again.");
                }

                cart.clear(); // clears cart after payment is complete

                return;
            } else {
                System.out.println("Insufficient Amount. Please enter at least $" + totalAmount);
            }
        }

        if (userInput.equalsIgnoreCase("X")) { // If the user clicks "X" then they will exit the cart
            return;
        }


    }

    // remove item method that gives the user the option to remove an item from the cart
    public static void removeItem(ArrayList<Product> cart, double totalAmount,
                                  Scanner scanner) {
        System.out.println("Enter Product Id to add to cart(X to exit): ");
        String userInput = scanner.nextLine();

        Product foundProduct = findProductById(userInput, cart);
        if (foundProduct.equals(foundProduct.getId())) {
            System.out.println("This item does not exist or isn't in the cart.");
        } else {
            cart.remove(foundProduct);
            System.out.println("Removed " + foundProduct.getName() + " from the cart.");
        }

    }

    public static Product findProductById(String id, ArrayList<Product> inventory) {
        for (Product product : inventory) {
            if (product.getId().equalsIgnoreCase(id)) {
                return product;
            }
        }

        return null;
    }
}

 