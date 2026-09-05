// Item subclass for the Clothing category.
public class Clothing extends Item {

    public Clothing(String id, String name, int quantity, double price) {
        super(id, name, quantity, price);
    }

    @Override
    public Category getCategory() {
        return Category.CLOTHING;
    }
}