package net.biryeongtrain.text_emulator.item.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 실제 아이템 스택이 가지는 컴포넌트를 저장하는 Mutable 클래스입니다. <br></br>
 * {@link SimpleComponentMap}
 * 과 다른 점은 SimpleComponentMap은 불변입니다. (아이템 자체에 내장되어있는 기본값으로 들어가는 컴포넌트가 들어가있는 클래스입니다. ) <br>
 * {@code ComponentMapImpl}에는 {@code ComponentMap}(baseComponent), {@code ref2ObjectMap}(changedComponent), {@code copyOnWrite}필드가 존재합니다. <br>
 *
 * {@code baseComponent}은 해당 컴포넌트가 참조할 기본 컴포넌트입니다. 만약 요청받은 컴포넌트가 changedComponent에 존재하지 않을 때 기본값을 전달할 떄 사용됩니다. <br>
 * {@code changedComponent}는 변경된 컴포넌트를 보유합니다. 배열 맵으로 되어 있기 때문에, 값이 새로 생길 떄 마다 새로운 인스턴스를 생성하여 값을 복사합니다. <br>
 * {@code copyOnWrite} copy-on-write 기법입니다. 해당 bool 타입은 클래스의 {@code getChanges()} 를 실행시킬 떄 {@code true} 가 됩니다. <br>
 * 이는 {@code getChanges()} 를 실행하고 나서 Component의 내용이 바뀌는 것을 getChanges로 가져간 부분이 영향이 없게끔 하기 위함입니다. <br>
 * <a href="https://en.wikipedia.org/wiki/Copy-on-write">Copy-On-Write Wikipedia</a> <br>
 *
 * 자세한 로직은 {@link ComponentMapImpl#get(ItemComponent)} {@link ComponentMapImpl#set(ItemComponent, Object)}
 * {@link ComponentMapImpl#applyChanges(ComponentChanges)} 를 참조하세요.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
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

        if (optional.isPresent()) {
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
        if (!(o instanceof ComponentMapImpl componentMapImpl)) return false;
        if (!this.baseComponents.equals(componentMapImpl.baseComponents)) return false;
        return this.changedComponents.equals(componentMapImpl.changedComponents);
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
