import java.util.Objects;

// --- Доменні моделі з інкапсуляцією та перевірками (Domain Layer) ---
public class Task5{
    public class Reader {
        private final int id;
        private final String name;
        private final String ticketNumber;
        private final int activeLoansCount;
        private final String email;

        public Reader(int id, String name, String ticketNumber, int activeLoansCount, String email) {
            if (activeLoansCount < 0) throw new IllegalArgumentException("Кількість активних видач не може бути від'ємною");
            this.id = id;
            this.name = Objects.requireNonNull(name, "Ім'я не може бути null");
            this.ticketNumber = Objects.requireNonNull(ticketNumber, "Номер квитка не може бути null");
            this.activeLoansCount = activeLoansCount;
            this.email = Objects.requireNonNull(email, "Email не може бути null");
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getTicketNumber() { return ticketNumber; }
        public int getActiveLoansCount() { return activeLoansCount; }
        public String getEmail() { return email; }
    }

    public class Book {
        private final int id;
        private final String title;
        private final String category;
        private final double valueAmount;

        public Book(int id, String title, String category, double valueAmount) {
            if (valueAmount < 0) throw new IllegalArgumentException("Вартість книги не може бути від'ємною");
            this.id = id;
            this.title = Objects.requireNonNull(title, "Назва не може бути null");
            this.category = Objects.requireNonNull(category, "Категорія не може бути null");
            this.valueAmount = valueAmount;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getCategory() { return category; }
        public double getValueAmount() { return valueAmount; }
    }

    public class LoanRecord {
        private final int loanId;
        private final Reader reader;
        private final Book book;
        private final int daysCount;
        private final double depositFee;
        private final double serviceFee;
        private final double totalPayable;
        private final String status;

        public LoanRecord(int loanId, Reader reader, Book book, int daysCount, double depositFee, double serviceFee, String status) {
            this.loanId = loanId;
            this.reader = Objects.requireNonNull(reader);
            this.book = Objects.requireNonNull(book);
            this.daysCount = daysCount;
            this.depositFee = depositFee;
            this.serviceFee = serviceFee;
            this.totalPayable = depositFee + serviceFee;
            this.status = Objects.requireNonNull(status);
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

// --- Сервісний шар та бізнес-логіка (Service Layer) ---

    // 1. Розрахунок застави та зборів
    public class FeeCalculator {
        public static class FeeResult {
            public final double deposit;
            public final double serviceFee;

            public FeeResult(double deposit, double serviceFee) {
                this.deposit = deposit;
                this.serviceFee = serviceFee;
            }
        }

        public FeeResult calculateFees(Book book) {
            double deposit = 0.0;
            double fee = 50.0;

            if ("Rare".equalsIgnoreCase(book.getCategory())) {
                deposit = book.getValueAmount() * 0.5;
                fee += 200.0;
            } else if ("Academic".equalsIgnoreCase(book.getCategory())) {
                deposit = book.getValueAmount() * 0.2;
                fee += 50.0;
            }

            return new FeeResult(deposit, fee);
        }
    }

    // 2. Форматування квитанцій
    public class ReceiptFormatter {
        public String formatReceipt(LoanRecord loan) {
            return "Квитанція видачі #" + loan.getLoanId() +
                    "\nЧитач: " + loan.getReader().getName() + " (" + loan.getReader().getTicketNumber() + ")" +
                    "\nКнига: " + loan.getBook().getTitle() +
                    "\nЗастава: " + loan.getDepositFee() + " грн" +
                    "\nСервісний збір: " + loan.getServiceFee() + " грн" +
                    "\nРазом: " + loan.getTotalPayable() + " грн";
        }
    }

    // 3. Інфраструктурні сервіси (Логування та Повідомлення)
    public class AuditLogger {
        public void logLoan(LoanRecord loan) {
            System.out.println("[Журнал логів] Видачу книги #" + loan.getBook().getId() +
                    " читачу #" + loan.getReader().getId() + " записано в library_audit.log.");
        }
    }

    public class EmailNotifier {
        public void sendReminder(Reader reader) {
            System.out.println("[Email] Нагадування надіслано на " + reader.getEmail());
        }
    }

    // 4. Головний оркестратор бізнес-процесу
    public class LoanService {
        private final FeeCalculator feeCalculator;
        private final ReceiptFormatter receiptFormatter;
        private final AuditLogger auditLogger;
        private final EmailNotifier emailNotifier;

        public LoanService(FeeCalculator feeCalculator, ReceiptFormatter receiptFormatter,
                           AuditLogger auditLogger, EmailNotifier emailNotifier) {
            this.feeCalculator = feeCalculator;
            this.receiptFormatter = receiptFormatter;
            this.auditLogger = auditLogger;
            this.emailNotifier = emailNotifier;
        }

        public LoanRecord issueBook(Reader reader, Book book, int daysCount) {
            if (reader.getActiveLoansCount() >= 5) {
                throw new IllegalStateException("Перевищено ліміт книг для читача");
            }

            FeeCalculator.FeeResult feeResult = feeCalculator.calculateFees(book);

            LoanRecord loan = new LoanRecord(
                    8801,
                    reader,
                    book,
                    daysCount,
                    feeResult.deposit,
                    feeResult.serviceFee,
                    "Issued"
            );

            String receipt = receiptFormatter.formatReceipt(loan);
            System.out.println(receipt);

            auditLogger.logLoan(loan);
            emailNotifier.sendReminder(reader);

            return loan;
        }
    }
}

