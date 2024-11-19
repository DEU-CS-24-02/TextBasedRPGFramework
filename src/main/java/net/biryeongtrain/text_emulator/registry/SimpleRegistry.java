package net.biryeongtrain.text_emulator.registry;

import it.unimi.dsi.fastutil.objects.*;
import net.biryeongtrain.text_emulator.utils.Util;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimpleRegistry<T> implements Registry<T>{
    private boolean frozen = false;
    final RegistryKey<? extends Registry<T>> key;
    // we use IdentityMap because it uses == instead of equals. all registry value must be singleton
    private final ObjectList<T> entries = new ObjectArrayList<>();
    private final Reference2IntMap<T> entryToRawId = Util.make(new Reference2IntOpenHashMap<>(), map -> map.defaultReturnValue(-1));
    private final Map<RegistryKey<T>, T> idToEntry = new IdentityHashMap<>();
    private final Map<T, RegistryKey<T>> entryToId = new IdentityHashMap<>();

    public SimpleRegistry(RegistryKey<? extends Registry<T>> key) {
        this.key = key;
    }

    @Override
    public RegistryKey<? extends Registry<T>> getKey() {
        return this.key;
    }

    @Override
    public @Nullable Identifier getId(T value) {
        return this.entryToId.get(value).getValue();
    }

    @Override
    public Optional<RegistryKey<T>> getKey(T value) {
        return Optional.empty();
    }

    @Override
    public T get(@Nullable RegistryKey<T> value) {
        if (value == null) {
            return null;
        }
        return this.idToEntry.get(value);
    }

    @Override
    public T get(Identifier value) {
        return this.get(RegistryKey.of(this.key, value));
    }

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public void freeze() {
        this.frozen = true;
    }

    @Override
    public void clear() {
        this.frozen = false;
        this.entryToId.clear();
        this.idToEntry.clear();
    }

    private void assertNotFrozen(RegistryKey<T> key) {
        if (this.frozen) {
            throw new IllegalStateException("Registry is already frozen (trying to add key " + key + ")");
        }
    }

    @Override
    public T add(RegistryKey<T> key, T value) {
        this.assertNotFrozen(key);
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        if (this.idToEntry.containsKey(key)) {
            throw new IllegalStateException("Duplicate entry " + key);
        }

        if (this.entryToId.containsKey(value)) {
            throw new IllegalStateException("Duplicate entry " + value);
        }

        this.entries.add(value);
        this.idToEntry.put(key, value);
        this.entryToId.put(value, key);
        return value;
    }

    @Override
    public int getRawId(T var1) {
        return this.entryToRawId.getInt(var1);
    }

    @Override
    public @Nullable T get(int var1) {
        return this.entries.get(var1);
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.entries.iterator();
    }

}
