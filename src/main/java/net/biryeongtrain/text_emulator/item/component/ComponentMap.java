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
        public <T> T get(DataComponent<? extends T> key) {
            return null;
        }

        @Override
        public Set<DataComponent<?>> getTypes() {
            return Set.of();
        }

        @NotNull
        @Override
        public Iterator<Component<?>> iterator() {
            return Collections.emptyIterator();
        }
    };

    Codec<ComponentMap> CODEC = ComponentMap.createCodecFromValueMap(DataComponent.TYPE_TO_VALUE_MAP_CODEC);

    static Codec<ComponentMap> createCodecFromValueMap(Codec<Map<DataComponent<?>, Object>> typeToValueMapCodec) {
        return typeToValueMapCodec.flatComapMap(Builder::build, componentMap -> {
            int i = componentMap.size();
            if (i == 0) {
                return DataResult.success(Reference2ObjectMaps.emptyMap());
            }

            Reference2ObjectArrayMap<DataComponent<?>, Object> reference2ObjectArrayMap = new Reference2ObjectArrayMap<>(i);
            for (Component<?> component : componentMap) {
                if (component.type().shouldSkipSerialization()) continue;
                reference2ObjectArrayMap.put(component.type(), component.value());
            }

            return DataResult.success((Map<DataComponent<?>, Object>)reference2ObjectArrayMap);
        });
    }

    <T> T get(DataComponent<? extends T> key);

    Set<DataComponent<?>> getTypes();

    default boolean contains(DataComponent<?> key) {
        return this.get(key) != null;
    }

    default int size() {
        return this.getTypes().size();
    }

    default boolean isEmpty() {
        return this.size() == 0;
    }

    @Nullable
    default <T> Component<T> copy(DataComponent<T> type) {
        T object = this.get(type);
        return object != null ? new Component<T>(type, object) : null;
    }

    @NotNull
    @Override
    default Iterator<Component<?>> iterator() {
        return Iterators.transform(this.getTypes().iterator(), type -> Objects.requireNonNull(this.copy((DataComponent<?>)type)));
    }

    default ComponentMap filtered(final Predicate<DataComponent<?>> predicate) {
        return new ComponentMap() {

            @Override
            public <T> T get(DataComponent<? extends T> key) {
                return null;
            }

            @Override
            public Set<DataComponent<?>> getTypes() {
                return Set.of();
            }
        };
    }

    default <T> T getOrDefault(DataComponent<? extends T> key, T defaultValue) {
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
        private final Reference2ObjectMap<DataComponent<?>, Object> map = new Reference2ObjectArrayMap<>();

        public <T> Builder add(DataComponent<T> type, @Nullable T value) {
            this.put(type, value);
            return this;
        }

        <T> void put(DataComponent<T> type, @Nullable Object value) {
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

        private static ComponentMap build(Map<DataComponent<?>, Object> map) {
            if (map.isEmpty()) {
                return EMPTY;
            }
            if (map.size() < 8) {
                return new SimpleComponentMap(new Reference2ObjectArrayMap<>(map));
            }
            return new SimpleComponentMap(new Reference2ObjectOpenHashMap<>(map));
        }
    }

    record SimpleComponentMap(Reference2ObjectMap<DataComponent<?>, Object> map) implements ComponentMap {
        @Override
        @Nullable
        public <T> T get(DataComponent<? extends T> type) {
            return (T)this.map.get(type);
        }

        @Override
        public boolean contains(DataComponent<?> type) {
            return this.map.containsKey(type);
        }

        @Override
        public Set<DataComponent<?>> getTypes() {
            return this.map.keySet();
        }

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
