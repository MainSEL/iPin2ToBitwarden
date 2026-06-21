package net.mainsel.ipintobitwarden;

import net.mainsel.ipintobitwarden.objects.LogType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogHandler {
    public void logInfo(String message) {
        logLine(message, LogType.INFO);
    }

    public void logWarn(String message) {
        logLine(message, LogType.WARN);
    }

    public void logError(String message) {
        logLine(message, LogType.ERROR);
    }

    private void logLine(String message, LogType logType) {
        var date = new Date();
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        var dateAsString = dateFormat.format(date);
        var logTypeName = getLogTypeName(logType);

        var logLine = dateAsString + "\t" + logTypeName + "\t" + message;

        System.out.println(logLine);

        try {
            var currentDirectory = System.getProperty("user.dir");
            var logFilePath = Paths.get(currentDirectory, "iPinToBitwarden.log");
            Files.writeString(logFilePath, logLine + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private String getLogTypeName(LogType logType) {
        switch (logType) {
            case ERROR -> {
                return "FEHLER";
            }
            case WARN -> {
                return "WARNUNG";
            }
            default -> {
                return "INFO";
            }
        }
    }
}
