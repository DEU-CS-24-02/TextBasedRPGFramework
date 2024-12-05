package net.biryeongtrain.text_emulator.item.component;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 컴포넌트 변경점 여러개를 {@link ComponentMapImpl}에 한번에 넣기위해 사용합니다.
 * 일반적으로는 {@link ComponentChanges.Builder}를 이용하여 해당 클래스를 생성하는 것이 바람직합니다.
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class ComponentChanges {
    public static final ComponentChanges EMPTY = new ComponentChanges(Reference2ObjectMaps.emptyMap());
    public static final Codec<ComponentChanges> CODEC = Codec.dispatchedMap(Type.CODEC, Type::getValueCodec).xmap(changes -> {
                if (changes.isEmpty()) {
                    return EMPTY;
                }

                Reference2ObjectArrayMap<ItemComponent<?>, Optional<?>> reference2ObjectArrayMap = new Reference2ObjectArrayMap<>(changes.size());
                for (Map.Entry<Type, ?> entry : changes.entrySet()) {
                    Type type = entry.getKey();
                    if (type.removed) {
                        reference2ObjectArrayMap.put(type.type, Optional.empty());
                        continue;
                    }
                    reference2ObjectArrayMap.put(type.type, Optional.of(entry.getValue()));
                }

                return new ComponentChanges(reference2ObjectArrayMap);
            }, changes -> {
                Reference2ObjectArrayMap<Type, Object> reference2ObjectArrayMap = new Reference2ObjectArrayMap<>(changes.changedComponents.size());
                for (Map.Entry<ItemComponent<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(changes.changedComponents)) {
                    ItemComponent<?> component = entry.getKey();
                    if (component.shouldSkipSerialization()) {
                        continue;
                    }
                    Optional<?> optional = entry.getValue();
                    if (optional.isPresent()) {
                        reference2ObjectArrayMap.put(new Type(component, false), optional.get());
                        continue;
                    }
                    reference2ObjectArrayMap.put(new Type(component, true), Unit.INSTANCE);
                }
                return (Map) reference2ObjectArrayMap;
            }
    );
    private static final String REMOVE_PREFIX = "!";

    final Reference2ObjectMap<ItemComponent<?>, Optional<?>> changedComponents;

    public ComponentChanges(Reference2ObjectMap<ItemComponent<?>, Optional<?>> changedComponents) {
        this.changedComponents = changedComponents;
    }

    @Nullable
    public <T> Optional<? extends T> get(ItemComponent<? extends T> type) {
        return (Optional) this.changedComponents.get(type);
    }

    public Set<Map.Entry<ItemComponent<?>, Optional<?>>> entrySet() {
        return this.changedComponents.entrySet();
    }

    public int size() {
        return this.changedComponents.size();
    }

    public ComponentChanges withRemovedIf(Predicate<ItemComponent<?>> removedTypePredicate) {
        if (this.isEmpty()) {
            return EMPTY;
        }

        Reference2ObjectArrayMap<ItemComponent<?>, Optional<?>> reference2ObjectArrayMap = new Reference2ObjectArrayMap<>(this.changedComponents.size());
        reference2ObjectArrayMap.keySet().removeIf(removedTypePredicate);
        if (reference2ObjectArrayMap.isEmpty()) {
            return EMPTY;
        }
        return new ComponentChanges(reference2ObjectArrayMap);
    }

    public boolean isEmpty() {
        return this.changedComponents.isEmpty();
    }

    public AddedRemovedPair toAddedRemovedPair() {
        if (this.isEmpty()) {
            return AddedRemovedPair.EMPTY;
        }
        ComponentMap.Builder builder = ComponentMap.builder();
        Set<ItemComponent<?>> set = Sets.newIdentityHashSet();
        this.changedComponents.forEach((type, value) -> {
            if (value.isPresent()) {
                builder.put(type, value.get());
            } else {
                set.add(type);
            }
        });
        return new AddedRemovedPair(builder.build(), set);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentChanges that = (ComponentChanges) o;
        return Objects.equal(changedComponents, that.changedComponents);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(changedComponents);
    }

    public String toString() {
        return toString(this.changedComponents);
    }

    static String toString(Reference2ObjectMap<ItemComponent<?>, Optional<?>> changes) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        boolean bl = true;

        for (Map.Entry<ItemComponent<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(changes)) {
            if (bl) {
                bl = false;
            } else {
                stringBuilder.append(", ");
            }

            Optional<?> optional = entry.getValue();
            if (optional.isPresent()) {
                stringBuilder.append(entry.getKey()).append("=>").append(optional.get());
                continue;
            }

            stringBuilder.append(REMOVE_PREFIX).append(entry.getKey());
        }

        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public static class Builder {
        private final Reference2ObjectMap<ItemComponent<?>, Optional<?>> changes = new Reference2ObjectArrayMap<>();

        public <T> Builder add(ItemComponent<T> type, T value) {
            this.changes.put(type, Optional.of(value));
            return this;
        }

        public <T> Builder remove(ItemComponent<T> type) {
            this.changes.put(type, Optional.empty());
            return this;
        }

        public <T> Builder add(Component<T> component) {
            return this.add(component.type(), component.value());
        }

        public ComponentChanges build() {
            if (this.changes.isEmpty()) {
                return EMPTY;
            }
            return new ComponentChanges(this.changes);
        }
    }

    public record AddedRemovedPair(ComponentMap added, Set<ItemComponent<?>> removed) {
        public static final AddedRemovedPair EMPTY = new AddedRemovedPair(ComponentMap.EMPTY, Set.of());
    }

    record Type(ItemComponent<?> type, boolean removed) {
        public static final Codec<Type> CODEC = Codec.STRING.flatXmap(id -> {
                    Identifier identifier;
                    ItemComponent<?> componentType;
                    boolean bl = id.startsWith(REMOVE_PREFIX);

                    if (bl) {
                        id = id.substring(REMOVE_PREFIX.length());
                    }
                    if ((componentType = Registries.ITEM_COMPONENTS.get(identifier = Identifier.tryParse(id))) == null) {
                        return DataResult.error(() -> "No component type " + identifier);
                    }
                    if (componentType.shouldSkipSerialization()) {
                        return DataResult.error(() -> "Component type " + identifier + " is not serializable");
                    }
                    return DataResult.success(new Type(componentType, bl));
                }, type -> {
                    ItemComponent<?> component = type.type;
                    Identifier identifier = Registries.ITEM_COMPONENTS.getId(component);
                    if (identifier == null) {
                        return DataResult.error(() -> "Unknown component type: " + component);
                    }

                    return DataResult.success(type.removed ? REMOVE_PREFIX + identifier : identifier.toString());
                }
        );

        public Codec<?> getValueCodec() {
            return this.removed ? Codec.EMPTY.codec() : this.type.getCodec();
        }
    }
}
