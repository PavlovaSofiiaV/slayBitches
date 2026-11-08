//task3,
// DTO для передачі запиту з консолі в контролер
class IssueBookRequest {
    private final String readerName;
    private final String category;

    public IssueBookRequest(String readerName, String category) {
        this.readerName = readerName;
        this.category = category;
    }

    public String getReaderName() { return readerName; }
    public String getCategory() { return category; }
}

// GRASP Controller: керує сценарієм використання, не містить System.out чи Scanner
public class BookIssuanceController {
    private final AuditLogger auditLogger;

    public BookIssuanceController(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    public LoanRecord issueBook(IssueBookRequest request) {
        // Завантажуємо/створюємо доменні об'єкти з Завдання 2
        Reader reader = new Reader(7, request.getReaderName(), "T-8841", 2, "reader@univ.edu");
        Book book = new Book(104, "Алгоритми та структури даних", request.getCategory(), 1500.0);

        double deposit = 0.0;
        double fee = 50.0;

        if ("Rare".equalsIgnoreCase(book.getCategory())) {
            deposit = book.getValueAmount() * 0.5;
            fee += 200.0;
        } else if ("Academic".equalsIgnoreCase(book.getCategory())) {
            deposit = book.getValueAmount() * 0.2;
            fee += 50.0;
        }

        // Створюємо запис видачі (Creator + Information Expert)
        LoanRecord loan = new LoanRecord(8801, reader, book, 14, deposit, fee);

        // Викликаємо штучний сервіс логування (Pure Fabrication)
        auditLogger.logIssuance(book.getId(), reader.getId());

        return loan;
    }
}
