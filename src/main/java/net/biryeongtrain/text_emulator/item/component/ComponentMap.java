package net.biryeongtrain.text_emulator.item.component;

import com.google.common.collect.Iterators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ComponentMap extends Iterable<Component<?>> {
    ComponentMap EMPTY = new ComponentMap() {
        @Override
        public <T> T get(ItemComponent<? extends T> key) {
            return null;
        }

        @Override
        public Set<ItemComponent<?>> getTypes() {
            return Set.of();
        }

        @NotNull
        @Override
        public Iterator<Component<?>> iterator() {
            return Collections.emptyIterator();
        }
    };

    Codec<ComponentMap> CODEC = ComponentMap.createCodecFromValueMap(ItemComponent.TYPE_TO_VALUE_MAP_CODEC);

    static Codec<ComponentMap> createCodecFromValueMap(Codec<Map<ItemComponent<?>, Object>> typeToValueMapCodec) {
        return typeToValueMapCodec.flatComapMap(Builder::build, componentMap -> {
            int i = componentMap.size();
            if (i == 0) {
                return DataResult.success(Reference2ObjectMaps.emptyMap());
            }

            Reference2ObjectArrayMap<ItemComponent<?>, Object> reference2ObjectArrayMap = new Reference2ObjectArrayMap<>(i);
            for (Component<?> component : componentMap) {
                if (component.type().shouldSkipSerialization()) continue;
                reference2ObjectArrayMap.put(component.type(), component.value());
            }

            return DataResult.success((Map<ItemComponent<?>, Object>)reference2ObjectArrayMap);
        });
    }

    <T> T get(ItemComponent<? extends T> key);

    Set<ItemComponent<?>> getTypes();

    default boolean contains(ItemComponent<?> key) {
        return this.get(key) != null;
    }

    default int size() {
        return this.getTypes().size();
    }

    default boolean isEmpty() {
        return this.size() == 0;
    }

    @Nullable
    default <T> Component<T> copy(ItemComponent<T> type) {
        T object = this.get(type);
        return object != null ? new Component<>(type, object) : null;
    }

    @NotNull
    @Override
    default Iterator<Component<?>> iterator() {
        return Iterators.transform(this.getTypes().iterator(), type -> Objects.requireNonNull(this.copy((ItemComponent<?>)type)));
    }

    default ComponentMap filtered(final Predicate<ItemComponent<?>> predicate) {
        return new ComponentMap() {

            @Override
            public <T> T get(ItemComponent<? extends T> key) {
                return null;
            }

            @Override
            public Set<ItemComponent<?>> getTypes() {
                return Set.of();
            }
        };
    }

    default <T> T getOrDefault(ItemComponent<? extends T> key, T defaultValue) {
        T value = this.get(key);
        return value != null ? value : defaultValue;
    }

    default Stream<Component<?>> stream() {
        return StreamSupport.stream(Spliterators.spliterator(this.iterator(), this.size(), Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.NONNULL | Spliterator.IMMUTABLE), false);
    }

    static Builder builder() {
        return new Builder();
    }
    // 컴포넌트 기본 설정 때 쓸 빌더
    class Builder {
        private final Reference2ObjectMap<ItemComponent<?>, Object> map = new Reference2ObjectArrayMap<>();

        public <T> Builder add(ItemComponent<T> type, @Nullable T value) {
            this.put(type, value);
            return this;
        }

        <T> void put(ItemComponent<T> type, @Nullable Object value) {
            if (value != null) {
                this.map.put(type, value);
            } else {
                this.map.remove(type);
            }
        }

        public Builder addAll(ComponentMap components) {
            components.getTypes().forEach(type -> this.put(type, components.get(type)));
            return this;
        }

        public ComponentMap build() {
           return Builder.build(this.map);
        }

        private static ComponentMap build(Map<ItemComponent<?>, Object> map) {
            if (map.isEmpty()) {
                return EMPTY;
            }
            if (map.size() < 8) {
                return new SimpleComponentMap(new Reference2ObjectArrayMap<>(map));
            }
            return new SimpleComponentMap(new Reference2ObjectOpenHashMap<>(map));
        }
    }

    record SimpleComponentMap(Reference2ObjectMap<ItemComponent<?>, Object> map) implements ComponentMap {
        @Override
        @Nullable
        public <T> T get(ItemComponent<? extends T> type) {
            return (T)this.map.get(type);
        }

        @Override
        public boolean contains(ItemComponent<?> type) {
            return this.map.containsKey(type);
        }

        @Override
        public Set<ItemComponent<?>> getTypes() {
            return this.map.keySet();
        }

        @NotNull
        @Override
        public Iterator<Component<?>> iterator() {
            return Iterators.transform(Reference2ObjectMaps.fastIterator(this.map), Component::of);
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        public String toString() {
            return this.map.toString();
        }
    }

}
