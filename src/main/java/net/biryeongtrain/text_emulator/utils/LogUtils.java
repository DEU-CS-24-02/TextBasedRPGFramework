package net.biryeongtrain.text_emulator.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.spi.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;

import java.util.function.Supplier;

public class LogUtils {
    public static String FATAL_MARKER_ID = "FATAL";
    public static Marker FATAL_MARKER = MarkerFactory.getMarker(FATAL_MARKER_ID);
    private static final StackWalker WALKER;

    public static boolean isLoggerActive() {
        LoggerContext loggerContext = LogManager.getContext();
        if (loggerContext instanceof LifeCycle cycle) {
            return !cycle.isStopped();
        }

        return true;
    }

    public static void configureRootLoggingLevel(Level level) {
        org.apache.logging.log4j.core.LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(convertLevel(level));
        context.updateLoggers();
    }

    private static org.apache.logging.log4j.Level convertLevel(Level level) {
        return switch (level) {
            case TRACE -> org.apache.logging.log4j.Level.TRACE;
            case DEBUG -> org.apache.logging.log4j.Level.DEBUG;
            case INFO -> org.apache.logging.log4j.Level.INFO;
            case WARN -> org.apache.logging.log4j.Level.WARN;
            case ERROR -> org.apache.logging.log4j.Level.ERROR;
            default -> throw new IncompatibleClassChangeError();
        };
    }

    public static Object defer(final Supplier<Object> result) {
        class ToString {
            ToString() {
            }

            public String toString() {
                return result.get().toString();
            }
        }

        return new ToString();
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger(WALKER.getCallerClass());
    }

    static {
        WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    }
}

