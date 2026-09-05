// The 3 valid categories an item can belong to.
public enum Category {
    CLOTHING,
    ELECTRONICS,
    ENTERTAINMENT;

    // Matches user input to a category, ignoring case. Returns null if it doesn't match.
    public static Category fromString(String input) {
        if (input == null) {
            return null;
        }

        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        for (Category category : values()) {
            if (category.name().equalsIgnoreCase(trimmed)) {
                return category;
            }
        }

        return null;
    }

    // Makes it print as "Clothing" instead of "CLOTHING".
    @Override
    public String toString() {
        String lower = name().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}