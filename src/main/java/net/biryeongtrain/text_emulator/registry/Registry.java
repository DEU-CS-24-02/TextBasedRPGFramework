package net.biryeongtrain.text_emulator.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import net.biryeongtrain.text_emulator.utils.collections.IndexedIterable;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Registry<T> extends IndexedIterable<T> {
    RegistryKey<? extends Registry<T>> getKey();
    @Nullable
    Identifier getId(T value);
    Optional<RegistryKey<T>> getKey(T value);
    T get(@Nullable RegistryKey<T> value);
    T get(Identifier value);

    boolean isFrozen();
    void freeze();
    void clear();
    default Codec<T> getCodec() {
        return Identifier.CODEC.xmap(this::get, this::getId);
    }

    T add(RegistryKey<T> key, T value);

    static <R> R register(Registry<R> registry, Identifier id, R value) {
        registry.add(RegistryKey.of(registry.getKey(), id), value);
        return value;
    }
}
