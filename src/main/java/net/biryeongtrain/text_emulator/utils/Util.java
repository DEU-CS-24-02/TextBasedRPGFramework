package net.biryeongtrain.text_emulator.utils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class Util {
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
                object2IntMap.put((T)values.get(j), j);
            }

            return object2IntMap;
        }
    }
}
