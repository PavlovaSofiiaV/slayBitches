


public class LoanRecord {
    private final int loanId;
    private final Reader reader;
    private final Book book;
    private final int daysCount;
    private final double depositFee;
    private final double serviceFee;
    private String status;

    // Creator & Information Expert: Об'єкт конструюється у валідному стані й сам контролює суми
    public LoanRecord(int loanId, Reader reader, Book book, int daysCount, double depositFee, double serviceFee) {
        if (reader == null) throw new IllegalArgumentException("Читач не може бути null.");
        if (book == null) throw new IllegalArgumentException("Книга не може бути null.");
        if (!reader.canTakeMoreBooks()) {
            throw new IllegalStateException("Неможливо видати книгу: читач досяг ліміту видач!");
        }

        this.loanId = loanId;
        this.reader = reader;
        this.book = book;
        this.daysCount = daysCount;
        this.depositFee = depositFee;
        this.serviceFee = serviceFee;
        this.status = "Issued";

        // Оновлюємо стан читача при успішному оформленні
        this.reader.incrementActiveLoans();
    }

    // Information Expert: Сутність сама знає підсумкову суму до сплати
    public double getTotalPayable() {
        return this.depositFee + this.serviceFee;
    }

    // Information Expert: Формування тексту квитанції безпосередньо у сутності
    public String generateReceipt() {
        return "=== Квитанція видачі #" + loanId + " ===" +
                "\nЧитач: " + reader.getName() + " (" + reader.getTicketNumber() + ")" +
                "\nКнига: " + book.getTitle() + " [" + book.getCategory() + "]" +
                "\nТермін: " + daysCount + " днів" +
                "\nЗастава: " + depositFee + " грн" +
                "\nСервісний збір: " + serviceFee + " грн" +
                "\nРазом до сплати: " + getTotalPayable() + " грн" +
                "\nСтатус: " + status;
    }

    // Геттери
    public int getLoanId() { return loanId; }
    public Reader getReader() { return reader; }
    public Book getBook() { return book; }
    public double getDepositFee() { return depositFee; }
    public double getServiceFee() { return serviceFee; }
    public String getStatus() { return status; }
}
