import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Singleton - Єдина система продажу квитків кінотеатру "Кадр"
class SeatBookingSystem {
    private static volatile SeatBookingSystem instance;
    private final String systemId;

    // Стан зайнятих місць: "час_сеансу:ряд-місце" -> "хто забронював"
    private final Map<String, String> bookedSeats = new HashMap<>();

    // Приватний конструктор унеможливлює створення об'єкта через new ззовні
    private SeatBookingSystem() {
        this.systemId = UUID.randomUUID().toString().substring(0, 8);
        log("System", "Систему продажу ініціалізовано.");
    }

    // Глобальна точка доступу з підтримкою потокобезпечності (Double-Checked Locking)
    public static SeatBookingSystem getInstance() {
        if (instance == null) {
            synchronized (SeatBookingSystem.class) {
                if (instance == null) {
                    instance = new SeatBookingSystem();
                }
            }
        }
        return instance;
    }

    public String getSystemId() {
        return systemId;
    }

    // Метод продажу квитка
    public boolean bookSeat(String sessionTime, int row, int seat, String channelName) {
        String seatKey = sessionTime + ":" + row + "-" + seat;

        if (bookedSeats.containsKey(seatKey)) {
            String owner = bookedSeats.get(seatKey);
            log(channelName, String.format("намагається продати %d-%d на сеанс %s → відмова «Місце %d-%d уже продано (%s)»",
                    row, seat, sessionTime, row, seat, owner));
            return false;
        }

        bookedSeats.put(seatKey, channelName);
        log(channelName, String.format("продано місце %d-%d на сеанс %s", row, seat, sessionTime));
        return true;
    }

    // Форматований консольний лог: [час] [джерело] подія
    private void log(String source, String message) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.printf("[%s] [%s] %s%n", time, source, message);
    }
}

// Канал 1: Каса у фоє
class BoxOffice {
    public SeatBookingSystem getSystem() {
        return SeatBookingSystem.getInstance();
    }
    public void sellTicket(String time, int row, int seat) {
        getSystem().bookSeat(time, row, seat, "каса");
    }
}

// Канал 2: Мобільний застосунок
class MobileApp {
    public SeatBookingSystem getSystem() {
        return SeatBookingSystem.getInstance();
    }
    public void sellTicket(String time, int row, int seat) {
        getSystem().bookSeat(time, row, seat, "застосунок");
    }
}

// Канал 3: Вебсайт
class Website {
    public SeatBookingSystem getSystem() {
        return SeatBookingSystem.getInstance();
    }
    public void sellTicket(String time, int row, int seat) {
        getSystem().bookSeat(time, row, seat, "сайт");
    }
}

// Клас-раннер для відтворення демонстраційного сценарію
public class Task1 {
    public static void main(String[] args) {
        System.out.println("=== ДЕМОНСТРАЦІЯ ЗАВДАННЯ 1: SINGLETON ===\n");

        BoxOffice boxOffice = new BoxOffice();
        MobileApp mobileApp = new MobileApp();
        Website website = new Website();

        // 1. Каса продає місце 5-7 на сеанс 19:00
        boxOffice.sellTicket("19:00", 5, 7);

        // 2. Мобільний застосунок намагається продати те саме місце
        mobileApp.sellTicket("19:00", 5, 7);

        // 3. Перевірка ідентифікатора системи з усіх трьох каналів
        System.out.println();
        System.out.println("[INFO] ID системи з Каси:       " + boxOffice.getSystem().getSystemId());
        System.out.println("[INFO] ID системи з Застосунку: " + mobileApp.getSystem().getSystemId());
        System.out.println("[INFO] ID системи з Сайту:      " + website.getSystem().getSystemId());

        boolean isSame = (boxOffice.getSystem() == mobileApp.getSystem()) &&
                (mobileApp.getSystem() == website.getSystem());
        System.out.println("[INFO] Усі канали працюють з одним і тим самим екземпляром: " + isSame);
    }
}