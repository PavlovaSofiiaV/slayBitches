package task2;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
//Factory Method
// 1. Абстрактний продукт - Квиток
abstract class Ticket {
    private final String movieTitle;
    private final String seat;
    private final double price;

    public Ticket(String movieTitle, String seat, double price) {
        this.movieTitle = movieTitle;
        this.seat = seat;
        this.price = price;
    }

    public String getMovieTitle() { return movieTitle; }
    public String getSeat() { return seat; }
    public double getPrice() { return price; }

    public abstract String printDetails();
}

// Конкретні продукти
class PaperTicket extends Ticket {
    public PaperTicket(String movieTitle, String seat, double price) {
        super(movieTitle, seat, price);
    }

    @Override
    public String printDetails() {
        return String.format("[Паперовий квиток (Каса)] %s | Місце: %s | Ціна: %.2f грн | [Друк на бланку зі штрих-кодом]",
                getMovieTitle(), getSeat(), getPrice());
    }
}

class PdfTicket extends Ticket {
    public PdfTicket(String movieTitle, String seat, double price) {
        super(movieTitle, seat, price);
    }

    @Override
    public String printDetails() {
        return String.format("[PDF-квиток (Сайт)] %s | Місце: %s | Ціна: %.2f грн | [Сформовано файл з QR-кодом]",
                getMovieTitle(), getSeat(), getPrice());
    }
}

class WalletTicket extends Ticket {
    public WalletTicket(String movieTitle, String seat, double price) {
        super(movieTitle, seat, price);
    }

    @Override
    public String printDetails() {
        return String.format("[Apple/Google Wallet (Застосунок)] %s | Місце: %s | Ціна: %.2f грн | [Додано в цифровий гаманець]",
                getMovieTitle(), getSeat(), getPrice());
    }
}

// Новий продукт для розширення
class SelfServiceKioskTicket extends Ticket {
    public SelfServiceKioskTicket(String movieTitle, String seat, double price) {
        super(movieTitle, seat, price);
    }

    @Override
    public String printDetails() {
        return String.format("[Квиток терміналу] %s | Місце: %s | Ціна: %.2f грн | [Видано чековою стрічкою терміналу]",
                getMovieTitle(), getSeat(), getPrice());
    }
}

// 2. Абстрактний Творець (Creator)
abstract class SalesChannel {
    private final String channelName;

    public SalesChannel(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() { return channelName; }

    // ФАБРИЧНИЙ МЕТОД (Factory Method)
    public abstract Ticket createTicket(String movieTitle, String seat, double price);

    // Загальний бізнес-процес продажу (без if/else)
    public Ticket sellTicket(String movieTitle, String seat, double price) {
        log(String.format("Крок 1: Резервування місця '%s' на фільм '%s'...", seat, movieTitle));
        log(String.format("Крок 2: Прийняття оплати в розмірі %.2f грн...", price));

        // Виклик фабричного методу
        Ticket ticket = createTicket(movieTitle, seat, price);

        log(String.format("Крок 3: Видача квитка через канал '%s'...", channelName));
        log("Крок 4: Запис продажу в журнал системи.");
        return ticket;
    }

    protected void log(String message) {
        String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.printf("[%s] [%s] %s%n", timeStr, channelName, message);
    }
}

// Конкретні Творці (Concrete Creators)
class BoxOfficeSalesChannel extends SalesChannel {
    public BoxOfficeSalesChannel() { super("Каса у фоє"); }

    @Override
    public Ticket createTicket(String movieTitle, String seat, double price) {
        return new PaperTicket(movieTitle, seat, price);
    }
}

class WebsiteSalesChannel extends SalesChannel {
    public WebsiteSalesChannel() { super("Веб-сайт"); }

    @Override
    public Ticket createTicket(String movieTitle, String seat, double price) {
        return new PdfTicket(movieTitle, seat, price);
    }
}

class MobileAppSalesChannel extends SalesChannel {
    public MobileAppSalesChannel() { super("Мобільний застосунок"); }

    @Override
    public Ticket createTicket(String movieTitle, String seat, double price) {
        return new WalletTicket(movieTitle, seat, price);
    }
}

// Новий Творець без зміни існуючих класів
class SelfServiceKioskSalesChannel extends SalesChannel {
    public SelfServiceKioskSalesChannel() { super("Термінал самообслуговування"); }

    @Override
    public Ticket createTicket(String movieTitle, String seat, double price) {
        return new SelfServiceKioskTicket(movieTitle, seat, price);
    }
}

// 3. Точка входу (MainRunner)
public class task2 {
    public static void main(String[] args) {
        System.out.println("=== Кінотеатр «Кадр»: Демонстрація Завдання 2 (Factory Method) ===\n");

        SalesChannel boxOffice = new BoxOfficeSalesChannel();
        SalesChannel website = new WebsiteSalesChannel();
        SalesChannel mobileApp = new MobileAppSalesChannel();

        // 1. Продати квитки через 3 базові канали
        System.out.println("--- Продаж через існуючі канали ---");
        Ticket t1 = boxOffice.sellTicket("Дюна 2", "Ряд 4, Місце 12", 180.0);
        System.out.println("Результат: " + t1.printDetails() + "\n");

        Ticket t2 = website.sellTicket("Аватар 2", "Ряд 8, Місце 5", 220.0);
        System.out.println("Результат: " + t2.printDetails() + "\n");

        Ticket t3 = mobileApp.sellTicket("Оппенгеймер", "Ряд 6, Місце 10", 200.0);
        System.out.println("Результат: " + t3.printDetails() + "\n");

        // 2. Підключити термінал самообслуговування
        System.out.println("--- Підключення нового каналу (Термінал самообслуговування) ---");
        SalesChannel kiosk = new SelfServiceKioskSalesChannel();
        Ticket t4 = kiosk.sellTicket("Бетмен", "Ряд 3, Місце 1", 160.0);
        System.out.println("Результат: " + t4.printDetails() + "\n");
    }
}