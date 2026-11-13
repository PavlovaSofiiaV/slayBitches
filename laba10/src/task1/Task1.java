package task1;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//Builder
// 1. Складний об'єкт (Product)
class MovieOrder {
    private final String session;
    private final List<String> seats;
    private final int popcornCount;
    private final int drinksCount;
    private final int glasses3DCount;
    private final String promoCode;
    private final boolean isVipService;

    MovieOrder(String session, List<String> seats, int popcornCount, int drinksCount,
               int glasses3DCount, String promoCode, boolean isVipService) {
        this.session = session;
        this.seats = new ArrayList<>(seats);
        this.popcornCount = popcornCount;
        this.drinksCount = drinksCount;
        this.glasses3DCount = glasses3DCount;
        this.promoCode = promoCode;
        this.isVipService = isVipService;
    }

    @Override
    public String toString() {
        String seatsStr = String.join(", ", seats);
        List<String> extras = new ArrayList<>();
        if (popcornCount > 0) extras.add("Попкорн: " + popcornCount);
        if (drinksCount > 0) extras.add("Напої: " + drinksCount);
        if (glasses3DCount > 0) extras.add("3D-окуляри: " + glasses3DCount);
        if (promoCode != null && !promoCode.isEmpty()) extras.add("Промокод: " + promoCode);
        if (isVipService) extras.add("VIP-обслуговування");

        String extrasStr = extras.isEmpty() ? "немає" : String.join("; ", extras);
        return String.format("[Замовлення] Сеанс: %s | Місця: [%s] | Додатки: %s", session, seatsStr, extrasStr);
    }
}

// 2. Будівельник (Builder)
class MovieOrderBuilder {
    private String session;
    private final List<String> seats = new ArrayList<>();
    private int popcornCount = 0;
    private int drinksCount = 0;
    private int glasses3DCount = 0;
    private String promoCode;
    private boolean isVipService = false;

    public MovieOrderBuilder setSession(String session) {
        this.session = session;
        return this;
    }

    public MovieOrderBuilder addSeat(String seat) {
        this.seats.add(seat);
        return this;
    }

    public MovieOrderBuilder addPopcorn(int count) {
        this.popcornCount += count;
        return this;
    }

    public MovieOrderBuilder addDrinks(int count) {
        this.drinksCount += count;
        return this;
    }

    public MovieOrderBuilder add3DGlasses(int count) {
        this.glasses3DCount += count;
        return this;
    }

    public MovieOrderBuilder setPromoCode(String promoCode) {
        this.promoCode = promoCode;
        return this;
    }

    public MovieOrderBuilder enableVipService() {
        this.isVipService = true;
        return this;
    }

    public MovieOrder build() {
        if (session == null || session.trim().isEmpty()) {
            throw new IllegalStateException("Неможливо сформувати замовлення: не вказано сеанс.");
        }
        if (seats.isEmpty()) {
            throw new IllegalStateException("Неможливо сформувати замовлення: не обрано жодного місця.");
        }
        return new MovieOrder(session, seats, popcornCount, drinksCount, glasses3DCount, promoCode, isVipService);
    }
}

// 3. Директор (Director)
class OrderDirector {
    public MovieOrder buildComboForTwo(MovieOrderBuilder builder, String session, String seat1, String seat2) {
        return builder
                .setSession(session)
                .addSeat(seat1)
                .addSeat(seat2)
                .addPopcorn(2)
                .addDrinks(2)
                .build();
    }
}

// 4. Точка входу (MainRunner)
public class Task1 {
    private static void log(String source, String message) {
        String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.printf("[%s] [%s] %s%n", timeStr, source, message);
    }

    public static void main(String[] args) {
        System.out.println("=== Кінотеатр «Кадр»: Демонстрація Завдання 1 (Builder) ===\n");

        // 1. Сформувати повне замовлення
        log("OrderService", "Розпочато формування повного замовлення...");
        MovieOrderBuilder builder1 = new MovieOrderBuilder();
        MovieOrder order1 = builder1
                .setSession("Аватар 2 (19:00)")
                .addSeat("Ряд 5, Місце 10")
                .addSeat("Ряд 5, Місце 11")
                .addPopcorn(2)
                .addDrinks(2)
                .setPromoCode("DISCOUNT2026")
                .build();

        log("OrderService", "Замовлення успішно сформовано!");
        log("ConsoleOutput", order1.toString());
        System.out.println();

        // 2. Сформувати типове замовлення "Комбо на двох"
        log("OrderDirector", "Формування типового замовлення 'Комбо на двох'...");
        OrderDirector director = new OrderDirector();
        MovieOrderBuilder builder2 = new MovieOrderBuilder();
        MovieOrder comboOrder = director.buildComboForTwo(builder2, "Дюна 2 (21:30)", "Ряд 8, Місце 14", "Ряд 8, Місце 15");

        log("OrderDirector", "Типове замовлення успішно сформовано!");
        log("ConsoleOutput", comboOrder.toString());
        System.out.println();

        // 3. Спробувати завершити формування без місць
        log("OrderService", "Спроба формування некоректного замовлення (без місць)...");
        try {
            MovieOrderBuilder invalidBuilder = new MovieOrderBuilder();
            MovieOrder invalidOrder = invalidBuilder
                    .setSession("Інтерстеллар (15:00)")
                    .addPopcorn(1)
                    .build();
        } catch (IllegalStateException ex) {
            log("OrderService", ex.getMessage());
        }
    }
}