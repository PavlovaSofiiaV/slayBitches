package task3;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// ---------------------------------------------------------
// Допоміжний клас для форматованого виведення журналу подій
// ---------------------------------------------------------
class Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void log(String source, String event) {
        String time = LocalTime.now().format(FORMATTER);
        System.out.printf("[%s] [%s] %s%n", time, source, event);
    }
}

// ---------------------------------------------------------
// Абстрактні продукти
// ---------------------------------------------------------
interface MovieCopy {
    String getDescription();
}

interface ProjectorProfile {
    String getDescription();
}

interface AudioProfile {
    String getDescription();
}

// ---------------------------------------------------------
// Абстрактна фабрика
// ---------------------------------------------------------
interface CinemaFactory {
    String getFormatName();
    MovieCopy createMovieCopy();
    ProjectorProfile createProjectorProfile();
    AudioProfile createAudioProfile();
}

// =========================================================
// Конкретні реалізації для формату 2D
// =========================================================
class MovieCopy2D implements MovieCopy {
    public String getDescription() { return "Копія фільму: 2D Standard Digital Copy"; }
}
class ProjectorProfile2D implements ProjectorProfile {
    public String getDescription() { return "Профіль проєктора: 2D Flat/Scope (Стандартна яскравість)"; }
}
class AudioProfile2D implements AudioProfile {
    public String getDescription() { return "Звуковий профіль: 5.1 Surround Sound"; }
}
class CinemaFactory2D implements CinemaFactory {
    public String getFormatName() { return "2D"; }
    public MovieCopy createMovieCopy() { return new MovieCopy2D(); }
    public ProjectorProfile createProjectorProfile() { return new ProjectorProfile2D(); }
    public AudioProfile createAudioProfile() { return new AudioProfile2D(); }
}

// =========================================================
// Конкретні реалізації для формату 3D
// =========================================================
class MovieCopy3D implements MovieCopy {
    public String getDescription() { return "Копія фільму: 3D Stereoscopic Dual-Stream Copy"; }
}
class ProjectorProfile3D implements ProjectorProfile {
    public String getDescription() { return "Профіль проєктора: 3D Dual-Projector High-Gain"; }
}
class AudioProfile3D implements AudioProfile {
    public String getDescription() { return "Звуковий профіль: 7.1 Enhanced Spatial Audio"; }
}
class CinemaFactory3D implements CinemaFactory {
    public String getFormatName() { return "3D"; }
    public MovieCopy createMovieCopy() { return new MovieCopy3D(); }
    public ProjectorProfile createProjectorProfile() { return new ProjectorProfile3D(); }
    public AudioProfile createAudioProfile() { return new AudioProfile3D(); }
}

// =========================================================
// Конкретні реалізації для формату Dolby Atmos
// =========================================================
class MovieCopyAtmos implements MovieCopy {
    public String getDescription() { return "Копія фільму: 4K UltraHD Dolby Atmos Master Copy"; }
}
class ProjectorProfileAtmos implements ProjectorProfile {
    public String getDescription() { return "Профіль проєктора: Dolby Vision HDR Laser Projector Profile"; }
}
class AudioProfileAtmos implements AudioProfile {
    public String getDescription() { return "Звуковий профіль: Dolby Atmos Immersive Object-Based Audio"; }
}
class CinemaFactoryAtmos implements CinemaFactory {
    public String getFormatName() { return "Dolby Atmos"; }
    public MovieCopy createMovieCopy() { return new MovieCopyAtmos(); }
    public ProjectorProfile createProjectorProfile() { return new ProjectorProfileAtmos(); }
    public AudioProfile createAudioProfile() { return new AudioProfileAtmos(); }
}

// =========================================================
// Конкретні реалізації для нового формату IMAX (Розширюваність)
// =========================================================
class MovieCopyIMAX implements MovieCopy {
    public String getDescription() { return "Копія фільму: IMAX Remastered 1.43:1 Aspect Ratio Copy"; }
}
class ProjectorProfileIMAX implements ProjectorProfile {
    public String getDescription() { return "Профіль проєктора: IMAX Dual 4K Laser Projection System"; }
}
class AudioProfileIMAX implements AudioProfile {
    public String getDescription() { return "Звуковий профіль: IMAX 12-Channel Immersive Sound"; }
}
class CinemaFactoryIMAX implements CinemaFactory {
    public String getFormatName() { return "IMAX"; }
    public MovieCopy createMovieCopy() { return new MovieCopyIMAX(); }
    public ProjectorProfile createProjectorProfile() { return new ProjectorProfileIMAX(); }
    public AudioProfile createAudioProfile() { return new AudioProfileIMAX(); }
}

// ---------------------------------------------------------
// Клієнтський код (Код запуску сеансу не знає конкретних класів)
// ---------------------------------------------------------
class ScreeningLauncher {
    public void launchScreening(CinemaFactory factory) {
        Logger.log("HallSetup", "Початок налаштування залу для показу у форматі: " + factory.getFormatName());

        // Отримання узгодженого набору елементів через абстракції
        MovieCopy movie = factory.createMovieCopy();
        ProjectorProfile projector = factory.createProjectorProfile();
        AudioProfile audio = factory.createAudioProfile();

        Logger.log("HallSetup", "Успішно сформовано єдиний пакет налаштувань для формату [" + factory.getFormatName() + "]");

        // Виведення складу набору в читабельному вигляді
        System.out.println("   ┌────────────────────────────────────────────────────────┐");
        System.out.println("   │ Склад набору сеансу [" + factory.getFormatName() + "]:");
        System.out.println("   │  • " + movie.getDescription());
        System.out.println("   │  • " + projector.getDescription());
        System.out.println("   │  • " + audio.getDescription());
        System.out.println("   └────────────────────────────────────────────────────────┘");

        Logger.log("CinemaHall", "Сеанс у форматі " + factory.getFormatName() + " успішно запущено.");
    }
}

// ---------------------------------------------------------
// Клас-раннер (Main)
// ---------------------------------------------------------
public class Task3 {
    public static void main(String[] args) {
        Logger.log("System", "Старт виконання демонстраційного сценарію налаштування залу.");

        ScreeningLauncher launcher = new ScreeningLauncher();

        // 1. Запуск 2D сеансу
        CinemaFactory factory2D = new CinemaFactory2D();
        launcher.launchScreening(factory2D);

        System.out.println();

        // 2. Запуск 3D сеансу
        CinemaFactory factory3D = new CinemaFactory3D();
        launcher.launchScreening(factory3D);

        System.out.println();

        // 3. Запуск Dolby Atmos сеансу
        CinemaFactory factoryAtmos = new CinemaFactoryAtmos();
        launcher.launchScreening(factoryAtmos);

        System.out.println();

        // 4. Демонстрація додавання нового формату IMAX без змін у коді запуску (launcher)
        Logger.log("System", "Інтеграція нового формату у систему кінотеатру...");
        CinemaFactory factoryIMAX = new CinemaFactoryIMAX();
        launcher.launchScreening(factoryIMAX);

        Logger.log("System", "Демонстраційний сценарій успішно завершено.");
    }
}
