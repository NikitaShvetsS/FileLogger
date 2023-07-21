package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger {
    private FileLoggerConfiguration config;

    public FileLogger(FileLoggerConfiguration config) {
        this.config = config;
    }
    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
    private String getLogPrefix(LoggingLevel level) {
        return "[" + getCurrentTime() + "][" + level.name() + "] ";
    }
    private void checkFileSize() throws FileMaxSizeReachedException {
        File logFile = new File(config.getFilePath());
        if (logFile.exists() && logFile.length() >= config.getMaxFileSize()) {
            throw new FileMaxSizeReachedException("File size reached maximum limit. Current size: "
                    + logFile.length() + " bytes. Maximum size: " + config.getMaxFileSize() + " bytes.");
        }
    }
    private void writeLogToFile(String log) {
        try (FileWriter writer = new FileWriter(config.getFilePath(), true)) {
            writer.write(log + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public void debug(String message) throws FileMaxSizeReachedException {
        if (config.getLoggingLevel() == LoggingLevel.DEBUG || config.getLoggingLevel() == LoggingLevel.INFO) {
            checkFileSize();
            String log = getLogPrefix(LoggingLevel.DEBUG) + "Message: " + message;
            writeLogToFile(log);
        }
    }

    public void info(String message) throws FileMaxSizeReachedException {
        if (config.getLoggingLevel() == LoggingLevel.INFO) {
            checkFileSize();
            String log = getLogPrefix(LoggingLevel.INFO) + "Message: " + message;
            writeLogToFile(log);
        }
    }
}
