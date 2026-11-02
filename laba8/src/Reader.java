
public class Reader {
    private static final int MAX_ALLOWED_LOANS = 5; // Бізнес-ліміт видач

    private final int id;
    private final String name;
    private final String ticketNumber;
    private int activeLoansCount;
    private final String email;

    // Creator: Конструктор гарантує створення валідного об'єкта
    public Reader(int id, String name, String ticketNumber, int activeLoansCount, String email) {
        if (id <= 0) throw new IllegalArgumentException("ID читача має бути більше 0.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Ім'я читача не може бути порожнім.");
        if (activeLoansCount < 0) throw new IllegalArgumentException("Кількість активних видач не може бути від'ємною.");

        this.id = id;
        this.name = name;
        this.ticketNumber = ticketNumber;
        this.activeLoansCount = activeLoansCount;
        this.email = email;
    }

    // Information Expert: Читач сам знає, чи може він взяти ще одну книгу
    public boolean canTakeMoreBooks() {
        return this.activeLoansCount < MAX_ALLOWED_LOANS;
    }

    // Бізнес-метод зміни стану замість публічного сеттера
    public void incrementActiveLoans() {
        if (!canTakeMoreBooks()) {
            throw new IllegalStateException("Перевищено ліміт активних видач книг (" + MAX_ALLOWED_LOANS + ")!");
        }
        this.activeLoansCount++;
    }

    // Геттери (без сеттерів)
    public int getId() { return id; }
    public String getName() { return name; }
    public String getTicketNumber() { return ticketNumber; }
    public int getActiveLoansCount() { return activeLoansCount; }
    public String getEmail() { return email; }
}
