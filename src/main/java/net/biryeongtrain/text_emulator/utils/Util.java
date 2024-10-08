package net.biryeongtrain.text_emulator.utils;

import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.function.Consumer;

public class Util {
    public static <T> T make(T object, Consumer<? super T> initializer) {
        initializer.accept(object);
        return object;
    }

    public static <T> String registryValueToString(Registry<T> registry, T value) {
        Identifier identifier = registry.getId(value);
        return identifier == null ? "[unregistered]" : identifier.toString();
    }

}
