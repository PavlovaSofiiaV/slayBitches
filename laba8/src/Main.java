import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Запускаємо Завдання 3 (GRASP Controller & Pure Fabrication) ===");
        runTask3();
    }

    public static void runTask3() {
        // 1. Створюємо технічний логер (Pure Fabrication)
        AuditLogger auditLogger = new FileAuditLogger();

        // 2. Створюємо контролер (Controller)
        BookIssuanceController controller = new BookIssuanceController(auditLogger);

        // 3. Зчитуємо дані з консолі (Thin CLI)
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть ім'я читача: ");
        String name = scanner.nextLine();
        System.out.print("Введіть категорію книги (Standard/Rare/Academic): ");
        String category = scanner.nextLine();

        IssueBookRequest request = new IssueBookRequest(name, category);

        try {
            // Передаємо виконання в контролер
            LoanRecord loan = controller.issueBook(request);

            // Друкуємо квитанцію, яку згенерував сам доменний об'єкт LoanRecord (Information Expert)
            System.out.println("\n" + loan.generateReceipt());
        } catch (Exception e) {
            System.out.println("\n[Помилка видачі]: " + e.getMessage());
        }
    }
}