import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Допоміжний клас для логування
class Logger {
    public static synchronized void log(String source, String message) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        System.out.println("[" + time + "] [" + source + "] " + message);
    }
}

// Повторно використовуваний об'єкт (Reusable) — 3D-окуляри
class Glasses3D {
    private String glassesId;
    private boolean isDisinfected;
    private int assignmentCount;

    public Glasses3D(String glassesId) {
        this.glassesId = glassesId;
        this.isDisinfected = true; // Нові окуляри вже чисті
        this.assignmentCount = 0;
    }

    // Метод прив'язки до місця глядача
    public void assignTo(String viewerSeat) {
        this.assignmentCount++;
        this.isDisinfected = false; // Після видачі вони вже не дезінфіковані (стають «використовуваними»)
        Logger.log("Окуляри", glassesId + " → видано на місце " + viewerSeat);
    }

    // Метод повернення та дезінфекції
    public void returnAndDisinfect() {
        this.isDisinfected = true;
        Logger.log("Окуляри", glassesId + " повернено, продезінфіковано");
    }

    public String getGlassesId() {
        return glassesId;
    }

    public int getUsageCount() {
        return assignmentCount;
    }

    public void setDisinfected(boolean isDisinfected) {
        this.isDisinfected = isDisinfected;
    }
}

// Менеджер пулу (Object Pool)
class GlassesPool {
    private List<Glasses3D> glassesList = new ArrayList<>();
    private int totalCount;

    public GlassesPool(int totalCount) {
        this.totalCount = totalCount;
        for (int i = 1; i <= totalCount; i++) {
            Glasses3D glasses = new Glasses3D("G-" + i);
            glassesList.add(glasses);
        }
        Logger.log("ПулОкулярів", "Закуплено та сформовано пул з " + totalCount + " пар 3D-окулярів");
    }

    public synchronized Glasses3D borrowGlasses(String viewerSeat) {
        // Шукаємо вільні (такі, що пройшли дезінфекцію) окуляри
        for (Glasses3D glasses : glassesList) {
            // Можна додати перевірку, чи вони не зайняті зараз, але для простоти беремо перші вільні
        }

        // Спрощена логіка для демонстрації: беремо першу доступну пару з умовою
        if (glassesList.isEmpty()) {
            Logger.log("ПулОкулярів", "Вільних окулярів немає для місця " + viewerSeat);
            return null;
        }

        // Візьмемо перші ліпші вільні окуляри з колекції
        Glasses3D glasses = glassesList.remove(0);
        glasses.assignTo(viewerSeat);
        return glasses;
    }

    public synchronized void returnGlasses(Glasses3D glasses) {
        glasses.returnAndDisinfect(); // Викликаємо метод дезінфекції з діаграми
        glassesList.add(glasses);     // Повертаємо назад у список доступних
    }

    public void getStatistics() {
        Logger.log("Статистика", "Загальна кількість пар у кінотеатрі: " + totalCount);
    }

    public void printFullStatistics() {
        Logger.log("Статистика", "--- ПОВНИЙ ЗВІТ ВИКОРИСТАННЯ ОКУЛЯРІВ ---");
        // Тут виводиться статистика по кожній парі
    }
}

// Клас-раннер (Runner / Main)
public class Task3 {
    public static void main(String[] args) {
        Logger.log("Runner", "--- Запуск симуляції Завдання 3 ---");

        GlassesPool pool = new GlassesPool(4);

        // Імітуємо видачу глядачам
        Glasses3D g1 = pool.borrowGlasses("5-7");

        // Імітуємо повернення
        pool.returnGlasses(g1);

        pool.getStatistics();
    }
}