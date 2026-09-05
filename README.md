# Inventory Management System
A Java program for managing a store's inventory. Made for my
midterm project in OOP.

## What it does
The program shows a menu with these options:
1. Add Item
2. Update Item (Quantity or Price)
3. Remove Item
4. Display Items by Category
5. Display All Items
6. Search Item
7. Sort Items (by Quantity or Price, Ascending or Descending)
8. Display Low Stock Items (quantity 5 or below)
9. Exit

Every item has an ID, name, quantity, price, and category (Clothing,
Electronics, or Entertainment). The program checks for invalid input at
every step (empty fields, negative numbers, duplicate IDs, unknown
categories, etc.) and asks again instead of crashing.

## OOP Concepts Used
- Encapsulation- `Item`'s fields (id, name, quantity, price) are
  private. They can only be read or changed through getters/setters.
- Abstraction - `Item` is an abstract class with an abstract method
  `getCategory()`. `Clothing`, `Electronics`, and `Entertainment` each
  extend `Item` and implement that method their own way.

## Files
- `Main.java` - shows the menu and handles user input
- `InventoryManager.java` - the actual logic (add, update, remove, search, sort, display)
- `Item.java` - abstract base class for an item
- `Category.java` - enum for the 3 categories
- `Clothing.java`, `Electronics.java`, `Entertainment.java` - subclasses of `Item`

## How to Run
```bash
javac *.java
java Main
```