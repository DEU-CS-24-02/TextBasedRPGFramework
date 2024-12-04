package net.biryeongtrain.text_emulator.io.loader;

import net.biryeongtrain.text_emulator.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface ContentsLoader<T> {
    default void load(Path path) {
        this.throwIfFrozen();
        this.loadData(path);
    }

    void loadData(Path path);
    @NotNull
    Registry<T> getRegistry();

    default void throwIfFrozen() {
        if (getRegistry().isFrozen()) {
            throw new IllegalStateException("Registry is frozen. maybe loaded already? : %s ".formatted(getRegistry().getKey()));
        }
    }
}
