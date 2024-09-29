package net.biryeongtrain.text_emulator.registry;

import com.google.common.collect.MapMaker;
import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public class RegistryKey<T> {
    private static final ConcurrentMap<RegistryIdPair, RegistryKey<?>> INSTANCES = new MapMaker().weakValues().makeMap();


    private final Identifier registry;
    private final Identifier value;

    public static <T> Codec<RegistryKey<T>> createCodec(RegistryKey<? extends Registry<T>> registry) {
        return Identifier.CODEC.xmap(id -> RegistryKey.of(registry, id), RegistryKey::getValue);
    }

    public static <T> RegistryKey<T> of(RegistryKey<? extends Registry<T>> registry, Identifier value) {
        return RegistryKey.of(registry.value, value);
    }

    private RegistryKey(Identifier registry, Identifier value) {
        this.registry = registry;
        this.value = value;
    }

    public static <T> RegistryKey<Registry<T>> ofRegistry(Identifier registry) {
        return RegistryKey.of(RegistryKeys.ROOT, registry);
    }

    private static <T> RegistryKey<T> of(Identifier registry, Identifier value) {
        return (RegistryKey<T>) INSTANCES.computeIfAbsent(new RegistryIdPair(registry, value), pair -> new RegistryKey<>(pair.registry, pair.id));
    }

    public String toString() {
        return "ResourceKey[" + String.valueOf(this.registry) + " / " + String.valueOf(this.value) + "]";
    }

    public boolean isOf(RegistryKey<? extends Registry<?>> registry) {
        return this.registry.equals(registry.getValue());
    }

    public Optional<RegistryKey<T>> tryCast(RegistryKey<? extends Registry<T>> registryRef) {
        return this.isOf(registryRef) ? Optional.of(this) : Optional.empty();
    }

    public RegistryKey<Registry<T>> getRegistryRef() {
        return RegistryKey.ofRegistry(this.registry);
    }

    public Identifier getRegistry() {
        return registry;
    }

    public Identifier getValue() {
        return value;
    }
    record RegistryIdPair(Identifier registry, Identifier id) {}
}
