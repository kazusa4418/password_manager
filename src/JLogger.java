package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@SuppressWarnings("unused")
public class JLogger {
    private static final LoggerList loggers = new LoggerList();

    static {
        createLogFolder();
    }

    private static Logger getLogger() {
        String className = ThreadUtils.getClassNameCalledThis(2);

        if (!loggers.existsLogger(className)) {
            loggers.createNewLogger(className);
        }
        return loggers.getLogger(className);
    }

    private static Logger getErrorLogger() {
        String className = ThreadUtils.getClassNameCalledThis(2);

        if (!loggers.existsErrorLogger(className)) {
            loggers.createNewErrorLogger(className);
        }
        return loggers.getErrorLogger(className);
    }

    public static void log(Level level, String logMsg, Throwable throwable) {
        if (level == Level.SEVERE) {
            getErrorLogger().log(level, logMsg, throwable);
        }
        else {
            getLogger().log(level, logMsg, throwable);
        }
    }

    public static void finest(String logMsg) {
        getLogger().finest(logMsg);
    }

    public static void finer(String logMsg) {
        getLogger().finer(logMsg);
    }

    public static void fine(String logMsg) {
        getLogger().fine(logMsg);
    }

    public static void config(String logMsg) {
        getLogger().config(logMsg);
    }

    public static void info(String logMsg) {
        getLogger().info(logMsg);
    }

    public static void warning(String logMsg) {
        getLogger().warning(logMsg);
    }

    public static void severe(String logMsg) {
        getErrorLogger().severe(logMsg);
    }

    public static void close() {
        Consumer<Handler[]> closeFunction = handlers -> {
            for (Handler handler : handlers) {
                handler.close();
            }
        };
        loggers.getLoggerNames().stream().map(Logger::getLogger).map(Logger::getHandlers).forEach(closeFunction);
        loggers.getErrorLoggerNames().stream().map(Logger::getLogger).map(Logger::getHandlers).forEach(closeFunction);
    }

    private static void createLogFolder() {
        File folder = new File("./log");
        if (!folder.exists()) {
            if (folder.mkdir()) {
                info("create new log folder.");
            }
            else {
                severe("failed to create new log folder.");
            }
        }
    }
}

class LoggerList {
    private final List<String> loggers = new ArrayList<>();
    private final List<String> errLoggers = new ArrayList<>();

    Logger getLogger(String loggerName) {
        return Logger.getLogger(loggerName);
    }

    Logger getErrorLogger(String loggerName) {
        return Logger.getLogger(loggerName + ".err");
    }

    boolean existsLogger(String loggerName) {
        return exists(loggerName, loggers);
    }

    boolean existsErrorLogger(String loggerName) {
        return exists(loggerName + ".err", errLoggers);
    }

    private boolean exists(String loggerName, List<String> loggers) {
        for (String logger : loggers) {
            if (logger.equals(loggerName)) {
                return true;
            }
        }
        return false;
    }

    void createNewLogger(String loggerName) {
        Logger newLogger = create(loggerName);
        loggers.add(newLogger.getName());
    }

    void createNewErrorLogger(String loggerName) {
        Logger newLogger = create(loggerName + ".err");
        errLoggers.add(newLogger.getName());
    }

    private Logger create(String loggerName) {
        Logger logger = Logger.getLogger(loggerName);

        Handler handler = null;
        try {
            handler = new FileHandler("./log/" + loggerName + ".log");
        }
        catch (IOException err) {
            // ワカラン
            err.printStackTrace();
            System.exit(1);
        }

        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        return logger;
    }

    List<String> getLoggerNames() {
        return loggers;
    }

    List<String> getErrorLoggerNames() {
        return errLoggers;
    }
}
