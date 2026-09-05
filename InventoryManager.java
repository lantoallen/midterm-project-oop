import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Holds the list of items and does all the actual add/update/remove/search/
// sort/display work. Main.java handles user input, this class handles the data.
public class InventoryManager {

    private final List<Item> items;
    private static final int LOW_STOCK_THRESHOLD = 5;

    public InventoryManager() {
        items = new ArrayList<>();
    }

    // Looks up an item by ID, ignoring case. Returns null if not found.
    private Item findById(String id) {
        if (id == null) {
            return null;
        }

        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(id.trim())) {
                return item;
            }
        }
        return null;
    }

    public boolean idExists(String id) {
        return findById(id) != null;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Item getItemById(String id) {
        return findById(id);
    }

    public boolean addItem(Category category, String id, String name, int quantity, double price) {
        if (category == null || idExists(id)) {
            return false;
        }

        Item newItem;
        switch (category) {
            case CLOTHING:
                newItem = new Clothing(id, name, quantity, price);
                break;
            case ELECTRONICS:
                newItem = new Electronics(id, name, quantity, price);
                break;
            case ENTERTAINMENT:
                newItem = new Entertainment(id, name, quantity, price);
                break;
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }

        items.add(newItem);
        return true;
    }

    public boolean updateQuantity(String id, int newQuantity) {
        Item item = getItemById(id);
        if (item == null) {
            return false;
        }
        item.setQuantity(newQuantity);
        return true;
    }

    public boolean updatePrice(String id, double newPrice) {
        Item item = getItemById(id);
        if (item == null) {
            return false;
        }
        item.setPrice(newPrice);
        return true;
    }

    public Item removeItem(String id) {
        Item item = getItemById(id);
        if (item == null) {
            return null;
        }
        items.remove(item);
        return item;
    }

    private void printFullHeader() {
        System.out.printf("%-10s %-20s %-10s %-12s %-15s%n",
                "ID", "Name", "Quantity", "Price", "Category");
        System.out.println("--------------------------------------------------------------------");
    }

    private void printCategoryHeader() {
        System.out.printf("%-10s %-20s %-10s %-12s%n",
                "ID", "Name", "Quantity", "Price");
        System.out.println("--------------------------------------------------------------");
    }

    public void displayAll() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty. No items to display.");
            return;
        }

        printFullHeader();
        for (Item item : items) {
            System.out.println(item.toTableRow());
        }
    }

    public void displayByCategory(Category category) {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty. No items to display.");
            return;
        }

        List<Item> filtered = new ArrayList<>();
        for (Item item : items) {
            if (item.getCategory() == category) {
                filtered.add(item);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No items found under category " + category + ".");
            return;
        }

        printCategoryHeader();
        for (Item item : filtered) {
            System.out.println(item.toCategoryRow());
        }
    }

    public Item searchItem(String id) {
        return getItemById(id);
    }

    // Sorts a copy of the list so the original order in "items" doesn't change.
    public void sortItems(String sortBy, boolean ascending) {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty. No items to sort.");
            return;
        }

        if (!sortBy.equalsIgnoreCase("quantity") && !sortBy.equalsIgnoreCase("price")) {
            System.out.println("Invalid sort field.");
            return;
        }

        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(new ItemComparator(sortBy, ascending));

        printFullHeader();
        for (Item item : sorted) {
            System.out.println(item.toTableRow());
        }
    }

    // Tells sort() how to order two items, by quantity or price, asc or desc.
    private static class ItemComparator implements Comparator<Item> {

        private final String sortBy;
        private final boolean ascending;

        public ItemComparator(String sortBy, boolean ascending) {
            this.sortBy = sortBy;
            this.ascending = ascending;
        }

        @Override
        public int compare(Item first, Item second) {
            int result;
            if (sortBy.equalsIgnoreCase("quantity")) {
                result = Integer.compare(first.getQuantity(), second.getQuantity());
            } else {
                result = Double.compare(first.getPrice(), second.getPrice());
            }

            if (ascending) {
                return result;
            } else {
                return -result;
            }
        }
    }

    // Shows items with quantity 5 or below.
    public void displayLowStock() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty. No low stock items to display.");
            return;
        }

        List<Item> lowStock = new ArrayList<>();
        for (Item item : items) {
            if (item.getQuantity() <= LOW_STOCK_THRESHOLD) {
                lowStock.add(item);
            }
        }

        if (lowStock.isEmpty()) {
            System.out.println("No low stock items. All items have quantity above " + LOW_STOCK_THRESHOLD + ".");
            return;
        }

        printFullHeader();
        for (Item item : lowStock) {
            System.out.println(item.toTableRow());
        }
    }
}