import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
//Prototype
// Допоміжний клас 1: Цінова категорія (вкладений об'єкт)
class PriceCategory implements Cloneable {
    private String name;
    private double price;

    public PriceCategory(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public PriceCategory clone() {
        try {
            return (PriceCategory) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Помилка клонування цінової категорії", e);
        }
    }

    @Override
    public String toString() {
        return name + ": " + price + " грн";
    }
}

// Допоміжний клас 2: Схема залу (вкладений об'єкт)
class HallLayout implements Cloneable {
    private String hallName;
    private int totalRows;
    private int seatsPerRow;

    public HallLayout(String hallName, int totalRows, int seatsPerRow) {
        this.hallName = hallName;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
    }

    public String getHallName() { return hallName; }
    public void setHallName(String hallName) { this.hallName = hallName; }

    @Override
    public HallLayout clone() {
        try {
            return (HallLayout) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Помилка клонування схеми залу", e);
        }
    }

    @Override
    public String toString() {
        return String.format("Зал '%s' (%d рядів по %d місць)", hallName, totalRows, seatsPerRow);
    }
}

// Прототип: Клас сеансу фільму зі складним внутрішнім станом
class MovieSession implements Cloneable {
    private String movieTitle;
    private String time;
    private HallLayout hallLayout;
    private List<PriceCategory> priceCategories;

    public MovieSession(String movieTitle, String time, HallLayout hallLayout, List<PriceCategory> priceCategories) {
        this.movieTitle = movieTitle;
        this.time = time;
        this.hallLayout = hallLayout;
        this.priceCategories = priceCategories;
        log("Створено базовий сеанс-шаблон через new");
    }

    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public void setTime(String time) { this.time = time; }
    public HallLayout getHallLayout() { return hallLayout; }
    public List<PriceCategory> getPriceCategories() { return priceCategories; }

    // РЕАЛІЗАЦІЯ ГЛИБОКОГО КОПІЮВАННЯ (Deep Copy)
    @Override
    public MovieSession clone() {
        try {
            // 1. Поверхневе клонування примітивів та посилань
            MovieSession clonedSession = (MovieSession) super.clone();

            // 2. Глибоке клонування схеми залу (створюється окремий об'єкт в пам'яті)
            clonedSession.hallLayout = this.hallLayout.clone();

            // 3. Глибоке клонування списку цінових категорій та кожного елемента всередині
            clonedSession.priceCategories = new ArrayList<>();
            for (PriceCategory category : this.priceCategories) {
                clonedSession.priceCategories.add(category.clone());
            }

            clonedSession.log("Швидко згенеровано новий сеанс шляхом клонування (Prototype)");
            return clonedSession;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Помилка клонування сеансу", e);
        }
    }

    private void log(String action) {
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.printf("[%s] [Session] %s: '%s' о %s (%s)%n",
                currentTime, action, movieTitle, time, hallLayout.getHallName());
    }

    public void printSessionDetails() {
        System.out.printf("  • Фільм: %s | Час: %s | %s%n", movieTitle, time, hallLayout);
        System.out.print("    Ціни: ");
        for (PriceCategory pc : priceCategories) {
            System.out.print("[" + pc + "] ");
        }
        System.out.println("\n");
    }
}

// Головний клас-раннер Завдання 2
public class Task2 {
    public static void main(String[] args) {
        System.out.println("=== ДЕМОНСТРАЦІЯ ЗАВДАННЯ 2: PROTOTYPE (DEEP COPY) ===\n");

        // 1. Створення базової конфігурації
        List<PriceCategory> basePrices = new ArrayList<>();
        basePrices.add(new PriceCategory("VIP", 250.0));
        basePrices.add(new PriceCategory("Standard", 150.0));

        HallLayout redHall = new HallLayout("Червоний Зал", 10, 15);

        // 2. Створення базового прототипу
        MovieSession basePrototype = new MovieSession("Шаблонний Фільм", "00:00", redHall, basePrices);
        System.out.println("\n[Базовий сеанс-прототип]:");
        basePrototype.printSessionDetails();

        // 3. Клонування сеансу для ранкового показу
        MovieSession morningSession = basePrototype.clone();
        morningSession.setMovieTitle("Дюна: Частина Друга");
        morningSession.setTime("10:00");

        // 4. Клонування сеансу для вечірнього показу + зміна ціни у клоні
        MovieSession eveningSession = basePrototype.clone();
        eveningSession.setMovieTitle("Дюна: Частина Друга");
        eveningSession.setTime("19:30");
        eveningSession.getPriceCategories().get(1).setPrice(180.0); // Піднімаємо ціну Standard для вечора

        // 5. Демонстрація незалежності клонованих об'єктів (доказ Deep Copy)
        System.out.println("[Ранковий сеанс]:");
        morningSession.printSessionDetails();

        System.out.println("[Вечірній сеанс (із піднятою ціною Standard)]:");
        eveningSession.printSessionDetails();

        System.out.println("[Перевірка базового прототипу (його ціни НЕ змінилися)]:");
        basePrototype.printSessionDetails();
    }
}