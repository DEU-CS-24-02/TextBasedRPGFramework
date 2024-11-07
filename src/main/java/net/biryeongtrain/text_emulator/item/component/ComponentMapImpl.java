package net.biryeongtrain.text_emulator.item.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ComponentMapImpl implements ComponentMap {
    private final ComponentMap baseComponents;
    private Reference2ObjectMap<ItemComponent<?>, Optional<?>> changedComponents;
    private boolean copyOnWrite;

    public ComponentMapImpl(ComponentMap baseComponents) {
        this(baseComponents, Reference2ObjectMaps.emptyMap(), true);
    }

    public ComponentMapImpl(ComponentMap baseComponents, Reference2ObjectMap<ItemComponent<?>, Optional<?>> changedComponents, boolean copyOnWrite) {
        this.baseComponents = baseComponents;
        this.changedComponents = changedComponents;
        this.copyOnWrite = copyOnWrite;
    }

    public static ComponentMapImpl create(ComponentMap baseComponents, ComponentChanges changes) {
        if (ComponentMapImpl.shouldReuseChangesMap(baseComponents, changes.changedComponents)) {
            return new ComponentMapImpl(baseComponents, changes.changedComponents, true);
        }
        ComponentMapImpl componentMapImpl = new ComponentMapImpl(baseComponents);
        componentMapImpl.setChanges(changes);
        return componentMapImpl;
    }

    private static boolean shouldReuseChangesMap(ComponentMap baseComponents, Reference2ObjectMap<ItemComponent<?>, Optional<?>> changedComponents) {
        for (Map.Entry<ItemComponent<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(changedComponents)) {
            Object object = baseComponents.get(entry.getKey());
            Optional<?> optional = entry.getValue();
            if (optional.isPresent() && !optional.get().equals(object)) {
                return false;
            }

            if (object != null) {
                continue;
            }
            return false;
        }

        return true;
    }

    @Override
    public <T> T get(ItemComponent<? extends T> key) {
        Optional<T> optional = (Optional<T>) this.changedComponents.get(key);

        if (optional != null) {
            return optional.orElseGet(() -> this.baseComponents.get(key));
        }

        return this.baseComponents.get(key);
    }

    @Nullable
    public <T> T set(ItemComponent<? super T> type, @Nullable T value) {
        this.onWrite();
        T object = (T) this.baseComponents.get(type);
        Optional<Object> optional = Objects.equals(value, object) ? (Optional<Object>) this.changedComponents.remove(type) : (Optional<Object>) this.changedComponents.put(type, Optional.ofNullable(value));
        if (optional != null) {
            return (T) optional.orElse(null);
        }

        return object;
    }

    @Nullable
    public <T> T remove(ItemComponent<? extends T> type) {
        this.onWrite();
        T object = this.baseComponents.get(type);
        Optional<Object> optional = object != null ? (Optional<Object>) this.changedComponents.put(type, Optional.empty()) : (Optional<Object>) this.changedComponents.remove(type);

        if (optional != null) {
            return (T) optional.orElse(null);
        }

        return object;
    }

    public void applyChanges(ComponentChanges changes) {
        this.onWrite();
        for (Map.Entry<ItemComponent<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(changes.changedComponents)) {
            this.applyChange(entry.getKey(), entry.getValue());
        }
    }

    private void applyChange(ItemComponent<?> type, Optional<?> value) {
        Object object = this.baseComponents.get(type);
        if (value.isPresent()) {
            if (value.get().equals(object)) {
                this.changedComponents.remove(type);
            } else {
                this.changedComponents.put(type, value);
            }
        } else if (object != null) {
            this.changedComponents.put(type, Optional.empty());
        } else {
            this.changedComponents.remove(type);
        }
    }

    public void setChanges(ComponentChanges changes) {
        this.onWrite();
        this.changedComponents.clear();
        this.changedComponents.putAll(changes.changedComponents);
    }

    public void setAll(ComponentMap components) {
        for (Component<?> type : components) {
            type.apply(this);
        }
    }


    @Override
    public Set<ItemComponent<?>> getTypes() {
        if (this.changedComponents.isEmpty()) {
            return this.baseComponents.getTypes();
        }
        ReferenceArraySet<ItemComponent<?>> set = new ReferenceArraySet<>(this.baseComponents.getTypes());
        for (Reference2ObjectMap.Entry<ItemComponent<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(this.changedComponents)) {
            Optional<?> optional = entry.getValue();
            if (optional.isPresent()) {
                set.add(entry.getKey());
                continue;
            }
            set.remove(entry.getKey());
        }

        return set;
    }

    private void onWrite() {
        if (this.copyOnWrite) {
            this.changedComponents = new Reference2ObjectArrayMap<>(this.changedComponents);
            this.copyOnWrite = false;
        }
    }

    @Override
    public @NotNull Iterator<Component<?>> iterator() {
        if (this.changedComponents.isEmpty()) {
            return this.baseComponents.iterator();
        }

        ArrayList<Component<?>> list = new ArrayList<>(this.changedComponents.size() + this.baseComponents.size());
        for (Reference2ObjectMap.Entry<ItemComponent<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(this.changedComponents)) {
            if (entry.getValue().isEmpty()) continue;
            list.add(Component.of((ItemComponent)entry.getKey(), ((Optional)entry.getValue()).get()));
        }
        for (Component<?> component : this.baseComponents) {
            if (this.changedComponents.containsKey(component.type())) continue;
            list.add(component);
        }

        return list.iterator();
    }

    @Override
    public int size() {
        int i = baseComponents.size();
        for (Reference2ObjectMap.Entry<ItemComponent<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(this.changedComponents)) {
            boolean bl = entry.getValue().isPresent();
            if (bl && !baseComponents.contains(entry.getKey())) {
                continue;
            }
            i += bl ? 1 : -1;
        }

        return i;
    }

    public ComponentChanges getChanges() {
        if (this.changedComponents.isEmpty()) {
            return ComponentChanges.EMPTY;
        }

        this.copyOnWrite = true;
        return new ComponentChanges(this.changedComponents);
    }

    public ComponentMapImpl copy() {
        this.copyOnWrite = true;
        return new ComponentMapImpl(this.baseComponents, this.changedComponents, true);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentMapImpl)) return false;
        ComponentMapImpl componentMapImpl = (ComponentMapImpl)o;
        if (!this.baseComponents.equals(componentMapImpl.baseComponents)) return false;
        if (!this.changedComponents.equals(componentMapImpl.changedComponents)) return false;
        return true;
    }

    @Override
    public int hashCode() {
       return this.baseComponents.hashCode() + this.changedComponents.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "{" + this.stream().map(Component::toString).collect(Collectors.joining(", ")) + "}";
    }
}
