package net.biryeongtrain.text_emulator.utils;

import java.util.function.Consumer;

public class Util {
    public static <T> T make(T object, Consumer<? super T> initializer) {
        initializer.accept(object);
        return object;
    }
}
