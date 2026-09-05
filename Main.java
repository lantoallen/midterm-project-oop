import java.util.*;

// Runs the menu and reads/validates all user input, then calls
// InventoryManager to actually do the work.
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final InventoryManager manager = new InventoryManager();

    public static void main(String[] args) {
        boolean isProgramRunning = true;

        while (isProgramRunning) {
            printMenu();
            int choice = readMenuChoice();
            System.out.println();

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    updateItem();
                    break;
                case 3:
                    removeItem();
                    break;
                case 4:
                    displayByCategory();
                    break;
                case 5:
                    manager.displayAll();
                    break;
                case 6:
                    searchItem();
                    break;
                case 7:
                    sortItems();
                    break;
                case 8:
                    manager.displayLowStock();
                    break;
                case 9:
                    System.out.println("Thank you for using the Inventory Management System. Goodbye!");
                    isProgramRunning = false;
                    break;
                default:
                    break;
            }

            if (choice != 9) {
                System.out.println();
            }
        }

        sc.close();
    }

    private static void printMenu() {
        System.out.println("========================================");
        System.out.println("      INVENTORY MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println("Menu");
        System.out.println("1 - Add Item");
        System.out.println("2 - Update Item");
        System.out.println("3 - Remove Item");
        System.out.println("4 - Display Items by Category");
        System.out.println("5 - Display All Items");
        System.out.println("6 - Search Item");
        System.out.println("7 - Sort Items");
        System.out.println("8 - Display Low Stock Items");
        System.out.println("9 - Exit");
        System.out.print("Enter your choice: ");
    }

    // =====================================================================
    // Input validation helpers
    // =====================================================================

    // True if every character is a digit and the string isn't empty.
    private static boolean isAllDigits(String text) {
        if (text.isEmpty()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // True for something like "19.99" or "5". "NaN"/"Infinity" fail this
    // since they contain letters, which is what we want.
    private static boolean isValidDecimalFormat(String text) {
        if (text.isEmpty()) {
            return false;
        }
        int dotCount = 0;
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (currentChar == '.') {
                dotCount++;
            } else if (!Character.isDigit(currentChar)) {
                return false;
            }
        }
        if (dotCount > 1) {
            return false;
        }
        if (text.charAt(0) == '.' || text.charAt(text.length() - 1) == '.') {
            return false;
        }
        return true;
    }

    // True for "05" or "007", but not "0" by itself.
    private static boolean hasLeadingZero(String text) {
        return text.length() >= 2 && text.charAt(0) == '0';
    }

    // Same idea, but "0.50" is a normal price and shouldn't count - only
    // something like "05.50" should.
    private static boolean hasLeadingZeroDecimal(String text) {
        return text.length() >= 2 && text.charAt(0) == '0' && Character.isDigit(text.charAt(1));
    }

    private static int readMenuChoice() {
        boolean isChoiceValid = false;
        int choice = -1;

        while (!isChoiceValid) {
            if (!sc.hasNextLine()) {
                System.out.println("\nThank you for using the Inventory Management System. Goodbye!!");
                System.exit(0);
            }

            String input = sc.nextLine().trim();
            boolean isSingleDigit = input.length() == 1 && Character.isDigit(input.charAt(0));
            boolean isZero = isSingleDigit && input.charAt(0) == '0';
            if (!isSingleDigit || isZero) {
                System.out.print("Invalid choice. Please enter a number between 1 and 9 (no leading zeros): ");
            } else {
                choice = Integer.parseInt(input);
                isChoiceValid = true;
            }
        }
        return choice;
    }

    private static String readNonEmptyString(String prompt) {
        boolean isTextValid = false;
        String input = "";

        while (!isTextValid) {
            System.out.print(prompt);
            if (!sc.hasNextLine()) {
                System.out.println("\nThank you for using the Inventory Management System. Goodbye!!");
                System.exit(0);
            }

            input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            } else {
                isTextValid = true;
            }
        }
        return input;
    }

    private static int readNonNegativeInt(String prompt) {
        boolean isIntValid = false;
        int value = 0;

        while (!isIntValid) {
            System.out.print(prompt);
            if (!sc.hasNextLine()) {
                System.out.println("\nThank you for using the Inventory Management System. Goodbye!!");
                System.exit(0);
            }

            String input = sc.nextLine().trim();

            if (!isAllDigits(input)) {
                System.out.println("Invalid input. Please enter a valid non-negative whole number.");
            } else if (hasLeadingZero(input)) {
                System.out.println("Leading zeros are not allowed. Please enter a valid number (e.g., 5 instead of 05).");
            } else {
                try {
                    value = Integer.parseInt(input);
                    isIntValid = true;
                } catch (NumberFormatException e) {
                    System.out.println("Number is too large. Please enter a smaller whole number.");
                }
            }
        }
        return value;
    }

    private static double readNonNegativeDouble(String prompt) {
        boolean isDoubleValid = false;
        double value = 0.0;

        while (!isDoubleValid) {
            System.out.print(prompt);
            if (!sc.hasNextLine()) {
                System.out.println("\nThank you for using the Inventory Management System. Goodbye!!");
                System.exit(0);
            }

            String input = sc.nextLine().trim();

            if (!isValidDecimalFormat(input)) {
                System.out.println("Invalid input. Please enter a valid decimal number (e.g., 19.99).");
            } else if (hasLeadingZeroDecimal(input)) {
                System.out.println("Leading zeros are not allowed. Please enter a valid price (e.g., 5.50 instead of 05.50).");
            } else {
                try {
                    value = Double.parseDouble(input);
                    isDoubleValid = true;
                } catch (NumberFormatException e) {
                    System.out.println("Number is too large. Please enter a valid price.");
                }
            }
        }
        return value;
    }

    private static Category readCategoryChoice(String prompt) {
        String input = readNonEmptyString(prompt);
        Category category = Category.fromString(input);
        if (category == null) {
            System.out.println("Category " + input + " does not exist!");
            return null;
        }
        return category;
    }

    // =====================================================================
    // Menu actions
    // =====================================================================

    private static void addItem() {
        Category category = readCategoryChoice("Enter Category (Clothing/Electronics/Entertainment): ");
        if (category == null) {
            return;
        }

        String id = readNonEmptyString("Enter Item ID: ");
        if (manager.idExists(id)) {
            System.out.println("Item ID " + id + " already exists! Please use a unique ID.");
            return;
        }

        String name = readNonEmptyString("Enter Item Name: ");
        int quantity = readNonNegativeInt("Enter Quantity: ");
        double price = readNonNegativeDouble("Enter Price: ");

        boolean isAdded = manager.addItem(category, id, name, quantity, price);
        if (isAdded) {
            System.out.println("Item added successfully!");
        } else {
            System.out.println("Item could not be added.");
        }
    }

    private static void updateItem() {
        String id = readNonEmptyString("Enter Item ID: ");
        Item item = manager.getItemById(id);
        if (item == null) {
            System.out.println("Item not found!");
            return;
        }

        String option = "";
        boolean isOptionValid = false;
        while (!isOptionValid) {
            System.out.print("Update (1) Quantity or (2) Price? Enter 1 or 2: ");
            option = sc.nextLine().trim();
            if (option.equals("1") || option.equalsIgnoreCase("quantity")
                    || option.equals("2") || option.equalsIgnoreCase("price")) {
                isOptionValid = true;
            } else {
                System.out.println("Invalid option. Please enter 1 (Quantity) or 2 (Price).");
            }
        }

        if (option.equals("1") || option.equalsIgnoreCase("quantity")) {
            int newQuantity = readNonNegativeInt("Enter new Quantity: ");
            int oldQuantity = item.getQuantity();
            manager.updateQuantity(id, newQuantity);
            System.out.println("Quantity of Item " + item.getName() + " is updated from "
                    + oldQuantity + " to " + newQuantity);
        } else {
            double newPrice = readNonNegativeDouble("Enter new Price: ");
            double oldPrice = item.getPrice();
            manager.updatePrice(id, newPrice);
            System.out.printf("Price of Item %s is updated from %.2f to %.2f%n",
                    item.getName(), oldPrice, newPrice);
        }
    }

    private static void removeItem() {
        String id = readNonEmptyString("Enter Item ID: ");
        Item removed = manager.removeItem(id);
        if (removed == null) {
            System.out.println("Item not found!");
        } else {
            System.out.println("Item " + removed.getName() + " has been removed from the inventory");
        }
    }

    private static void displayByCategory() {
        Category category = readCategoryChoice("Enter Category (Clothing/Electronics/Entertainment): ");
        if (category == null) {
            return;
        }
        manager.displayByCategory(category);
    }

    private static void searchItem() {
        String id = readNonEmptyString("Enter Item ID: ");
        Item item = manager.searchItem(id);
        if (item == null) {
            System.out.println("Item not found!");
            return;
        }

        System.out.println("Item Found:");
        System.out.println("ID: " + item.getId());
        System.out.println("Name: " + item.getName());
        System.out.println("Quantity: " + item.getQuantity());
        System.out.printf("Price: %.2f%n", item.getPrice());
        System.out.println("Category: " + item.getCategory());
    }

    private static void sortItems() {
        if (manager.isEmpty()) {
            System.out.println("Inventory is empty. No items to sort.");
            return;
        }

        String sortBy = "";
        boolean isSortFieldValid = false;
        while (!isSortFieldValid) {
            System.out.print("Sort by (Quantity/Price): ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("quantity") || input.equalsIgnoreCase("price")
                    || input.equals("1") || input.equals("2")) {
                sortBy = input.equals("1") ? "quantity" : input.equals("2") ? "price" : input;
                isSortFieldValid = true;
            } else {
                System.out.println("Invalid input. Please enter 'Quantity' or 'Price'.");
            }
        }

        boolean isOrderValid = false;
        boolean isAscending = true;
        while (!isOrderValid) {
            System.out.print("Order (Ascending/Descending): ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("ascending") || input.equalsIgnoreCase("asc")) {
                isAscending = true;
                isOrderValid = true;
            } else if (input.equalsIgnoreCase("descending") || input.equalsIgnoreCase("desc")) {
                isAscending = false;
                isOrderValid = true;
            } else {
                System.out.println("Invalid input. Please enter 'Ascending' or 'Descending'.");
            }
        }

        manager.sortItems(sortBy, isAscending);
    }
}