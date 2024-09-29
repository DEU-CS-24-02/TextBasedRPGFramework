package net.biryeongtrain.text_emulator.registry;

import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Registry<T> {
    RegistryKey<? extends Registry<T>> getKey();
    @Nullable
    Identifier getId(T value);
    Optional<RegistryKey<T>> getKey(T value);
    T get(@Nullable RegistryKey<T> value);
    T get(Identifier value);

    void freeze();
    void clear();

    T add(RegistryKey<T> key, T value);
}
