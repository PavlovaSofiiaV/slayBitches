
public class Book {
    private final int id;
    private final String title;
    private final String category; // "Standard", "Rare", "Academic"
    private final double valueAmount;

    // Creator / Static Factory Method
    public Book(int id, String title, String category, double valueAmount) {
        if (id <= 0) throw new IllegalArgumentException("ID книги має бути більше 0.");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Назва книги не може бути порожньою.");
        if (valueAmount < 0) throw new IllegalArgumentException("Оціночна вартість не може бути від'ємною.");

        this.id = id;
        this.title = title;
        this.category = category;
        this.valueAmount = valueAmount;
    }

    // Геттери (інкапсуляція збережена)
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public double getValueAmount() { return valueAmount; }
}
