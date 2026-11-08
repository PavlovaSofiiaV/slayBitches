//task3
// Інтерфейс для логування (Indirection)
public interface AuditLogger {//3
    void logIssuance(int bookId, int readerId);
}

// Технічний клас (Pure Fabrication), який пише лог у файл
class FileAuditLogger implements AuditLogger {//2
    @Override
    public void logIssuance(int bookId, int readerId) {
        System.out.println("[Журнал логів] Видачу книги #" + bookId + " читачу #" + readerId + " записано в library_audit.log.");
    }
}
