package net.biryeongtrain.text_emulator.io.loader;

import net.biryeongtrain.text_emulator.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface ContentsLoader<T> {
    void load();
    @NotNull
    Registry<T> getRegistry();

    default void throwIfFrozen() {
        if (getRegistry().isFrozen()) {
            throw new IllegalStateException("Registry is frozen. maybe loaded already? : %s ".formatted(getRegistry().getKey()));
        }
    }
}
