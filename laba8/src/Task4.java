
import java.util.Objects;
import java.util.Scanner;

public class Task4 {

    // ==========================================
    // 1. ДОМЕННІ МОДЕЛІ ТА VALUE OBJECTS
    // ==========================================

    public static class Reader {
        private final int id;
        private final String name;
        private final String ticketNumber;
        private final int activeLoansCount;
        private final String email;

        public Reader(int id, String name, String ticketNumber, int activeLoansCount, String email) {
            if (id <= 0) throw new IllegalArgumentException("ID читача має бути більшим за 0.");
            if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Ім'я читача не може бути порожнім.");
            if (activeLoansCount < 0) throw new IllegalArgumentException("Кількість активних видач не може бути від'ємною.");
            if (email == null || !email.contains("@")) throw new IllegalArgumentException("Некоректний Email читача.");

            this.id = id;
            this.name = name;
            this.ticketNumber = Objects.requireNonNull(ticketNumber, "Номер квитка не може бути null");
            this.activeLoansCount = activeLoansCount;
            this.email = email;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getTicketNumber() { return ticketNumber; }
        public int getActiveLoansCount() { return activeLoansCount; }
        public String getEmail() { return email; }
    }

    public static class Book {
        private final int id;
        private final String title;
        private final String category; // Standard, Rare, Academic
        private final double valueAmount;

        public Book(int id, String title, String category, double valueAmount) {
            if (id <= 0) throw new IllegalArgumentException("ID книги має бути більшим за 0.");
            if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Назва книги не може бути порожньою.");
            if (valueAmount < 0) throw new IllegalArgumentException("Оціночна вартість книги не може бути від'ємною.");

            this.id = id;
            this.title = title;
            this.category = Objects.requireNonNull(category, "Категорія не може бути null");
            this.valueAmount = valueAmount;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getCategory() { return category; }
        public double getValueAmount() { return valueAmount; }
    }

    public static class LoanRecord {
        private final int loanId;
        private final Reader reader;
        private final Book book;
        private final int daysCount;
        private final double depositFee;
        private final double serviceFee;
        private final double totalPayable;
        private String status;

        public LoanRecord(int loanId, Reader reader, Book book, int daysCount, double depositFee, double serviceFee) {
            this.loanId = loanId;
            this.reader = Objects.requireNonNull(reader, "Читач не може бути null");
            this.book = Objects.requireNonNull(book, "Книга не може бути null");
            this.daysCount = daysCount;
            this.depositFee = depositFee;
            this.serviceFee = serviceFee;
            this.totalPayable = depositFee + serviceFee;
            this.status = "Issued";
        }

        public int getLoanId() { return loanId; }
        public Reader getReader() { return reader; }
        public Book getBook() { return book; }
        public int getDaysCount() { return daysCount; }
        public double getDepositFee() { return depositFee; }
        public double getServiceFee() { return serviceFee; }
        public double getTotalPayable() { return totalPayable; }
        public String getStatus() { return status; }
    }

    // ==========================================
    // 2. POLYMORPHISM: ІЄРАРХІЯ СТРАТЕГІЙ РОЗРАХУНКУ
    // ==========================================

    public interface BookCategoryPricingStrategy {
        double calculateDeposit(double bookValue);
        double calculateServiceFee(double baseFee);
    }

    public static class RareCategoryStrategy implements BookCategoryPricingStrategy {
        @Override
        public double calculateDeposit(double bookValue) {
            return bookValue * 0.5;
        }

        @Override
        public double calculateServiceFee(double baseFee) {
            return baseFee + 200.0;
        }
    }

    public static class AcademicCategoryStrategy implements BookCategoryPricingStrategy {
        @Override
        public double calculateDeposit(double bookValue) {
            return bookValue * 0.2;
        }

        @Override
        public double calculateServiceFee(double baseFee) {
            return baseFee + 50.0;
        }
    }

    public static class StandardCategoryStrategy implements BookCategoryPricingStrategy {
        @Override
        public double calculateDeposit(double bookValue) {
            return 0.0;
        }

        @Override
        public double calculateServiceFee(double baseFee) {
            return baseFee;
        }
    }

    // Фабрика для отримання відповідної стратегії
    public static class PricingStrategyFactory {
        public static BookCategoryPricingStrategy getStrategy(String category) {
            if ("Rare".equalsIgnoreCase(category)) {
                return new RareCategoryStrategy();
            } else if ("Academic".equalsIgnoreCase(category)) {
                return new AcademicCategoryStrategy();
            } else {
                return new StandardCategoryStrategy();
            }
        }
    }

    // ==========================================
    // 3. PROTECTED VARIATIONS: СТАБІЛЬНІ ІНТЕРФЕЙСИ
    // ==========================================

    // Контракт для логування аудіту
    public interface AuditLogger {
        void logLoanIssue(int bookId, int readerId);
    }

    public static class FileAuditLogger implements AuditLogger {
        @Override
        public void logLoanIssue(int bookId, int readerId) {
            System.out.println("[Журнал логів] Видачу книги #" + bookId + " читачу #" + readerId + " записано в library_audit.log.");
        }
    }

    // Контракт для надсилання сповіщень
    public interface NotificationService {
        void sendLoanReminder(String email, String details);
    }

    public static class EmailNotificationService implements NotificationService {
        @Override
        public void sendLoanReminder(String email, String details) {
            System.out.println("[Email] Нагадування надіслано на " + email + ": " + details);
        }
    }

    // Контракт для друку/формування квитанції
    public interface ReceiptPrinter {
        void printReceipt(LoanRecord loan);
    }

    public static class ConsoleReceiptPrinter implements ReceiptPrinter {
        @Override
        public void printReceipt(LoanRecord loan) {
            String ticket = "=== Квитанція видачі #" + loan.getLoanId() + " ===" +
                    "\nЧитач: " + loan.getReader().getName() + " (" + loan.getReader().getTicketNumber() + ")" +
                    "\nКнига: " + loan.getBook().getTitle() +
                    "\nЗастава: " + loan.getDepositFee() + " грн" +
                    "\nСервісний збір: " + loan.getServiceFee() + " грн" +
                    "\nРазом до сплати: " + loan.getTotalPayable() + " грн";
            System.out.println(ticket);
        }
    }

    // ==========================================
    // 4. СЕРВІС БІЗНЕС-ЛОГІКИ (Склеювання через DI)
    // ==========================================

    public static class LibraryLoanService {
        private final AuditLogger auditLogger;
        private final NotificationService notificationService;
        private final ReceiptPrinter receiptPrinter;

        // Впровадження залежностей через конструктор (Dependency Injection)
        public LibraryLoanService(AuditLogger auditLogger, NotificationService notificationService, ReceiptPrinter receiptPrinter) {
            this.auditLogger = Objects.requireNonNull(auditLogger);
            this.notificationService = Objects.requireNonNull(notificationService);
            this.receiptPrinter = Objects.requireNonNull(receiptPrinter);
        }

        public LoanRecord issueBook(Reader reader, Book book, int daysCount) {
            // Перевірка інваріантів бізнес-логіки
            if (reader.getActiveLoansCount() >= 5) {
                throw new IllegalStateException("Перевищено ліміт виданих книг для читача!");
            }

            // Отримання поліморфної стратегії розрахунку
            BookCategoryPricingStrategy strategy = PricingStrategyFactory.getStrategy(book.getCategory());

            double deposit = strategy.calculateDeposit(book.getValueAmount());
            double fee = strategy.calculateServiceFee(50.0); // 50.0 — базовий збір

            LoanRecord loan = new LoanRecord(8801, reader, book, daysCount, deposit, fee);

            // Виклик ізольованих точок варіативності
            receiptPrinter.printReceipt(loan);
            auditLogger.logLoanIssue(book.getId(), reader.getId());
            notificationService.sendLoanReminder(reader.getEmail(), "Книга: " + book.getTitle() + ", поверніть через " + daysCount + " днів.");

            return loan;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Реєстрація видачі книги ===");
        System.out.print("Введіть ім'я читача: ");
        String name = scanner.nextLine();
        System.out.print("Введіть категорію книги (Standard/Rare/Academic): ");
        String category = scanner.nextLine();

        Reader reader = new Reader(7, name, "T-8841", 2, "reader@univ.edu");
        Book book = new Book(104, "Алгоритми та структури даних", category, 1500.0);

        // Збирання сервісу з конкретними реалізаціями інфраструктури
        LibraryLoanService loanService = new LibraryLoanService(
                new FileAuditLogger(),
                new EmailNotificationService(),
                new ConsoleReceiptPrinter()
        );

        loanService.issueBook(reader, book, 14);
    }
}

