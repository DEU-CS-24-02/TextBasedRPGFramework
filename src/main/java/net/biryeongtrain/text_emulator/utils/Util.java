package net.biryeongtrain.text_emulator.utils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class Util {
    private static final Logger LOGGER = LoggerFactory.getLogger("Utils");
    public static final ExecutorService IOExecutor = createIoWorker("IO-Worker-", false);

    public static <T> T make(T object, Consumer<? super T> initializer) {
        initializer.accept(object);
        return object;
    }

    public static <T> String registryValueToString(Registry<T> registry, T value) {
        Identifier identifier = registry.getId(value);
        return identifier == null ? "[unregistered]" : identifier.toString();
    }

    public static <T> ToIntFunction<T> lastIndexGetter(List<T> values) {
        int i = values.size();
        if (i < 8) {
            return values::indexOf;
        } else {
            Object2IntMap<T> object2IntMap = new Object2IntOpenHashMap<>(i);
            object2IntMap.defaultReturnValue(-1);

            for (int j = 0; j < i; j++) {
                object2IntMap.put(values.get(j), j);
            }

            return object2IntMap;
        }
    }

    public static ExecutorService createIoWorker(String namedPrefix, boolean daemon) {
        AtomicInteger atomicInteger = new AtomicInteger(1);

        return Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);

            thread.setDaemon(daemon);
            thread.setName(namedPrefix + atomicInteger.getAndIncrement());
            thread.setUncaughtExceptionHandler(Util::uncaughtExceptionHandler);

            return thread;
        });
    }

    private static void uncaughtExceptionHandler(Thread thread, Throwable throwable) {
        if (throwable instanceof CompletionException) {
            throwable = throwable.getCause();
        }

        LOGGER.error(String.format("Caught exception in thread %s", thread), throwable);
    }

    public static void shutdownExecutors() {
        Main.LOGGER.info("Shutting down executors"  );
        IOExecutor.shutdownNow();
    }
}
