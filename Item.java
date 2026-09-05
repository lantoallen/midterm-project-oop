// Base class for all inventory items. Fields are private so they can only
// be changed through the setters below, which also check the values are valid.
public abstract class Item {

    private String id;
    private String name;
    private int quantity;
    private double price;

    public Item(String id, String name, int quantity, double price) {
        setId(id);
        setName(name);
        setQuantity(quantity);
        setPrice(price);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be blank.");
        }
        this.id = id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be blank.");
        }

        this.name = name.trim();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0.0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    // Each subclass decides its own category (Clothing, Electronics, Entertainment).
    public abstract Category getCategory();

    // One row for the "Display All" / sort / low stock tables: ID, Name, Quantity, Price, Category.
    public String toTableRow() {
        return String.format("%-10s %-20s %-10d %-12.2f %-15s",
                id, name, quantity, price, getCategory());
    }

    // Same as above but without the Category column, for the "by category" table.
    public String toCategoryRow() {
        return String.format("%-10s %-20s %-10d %-12.2f",
                id, name, quantity, price);
    }
}