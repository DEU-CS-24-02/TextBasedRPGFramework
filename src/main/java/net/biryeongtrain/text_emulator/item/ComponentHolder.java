package net.biryeongtrain.text_emulator.item;

import net.biryeongtrain.text_emulator.item.component.ComponentMap;
import net.biryeongtrain.text_emulator.item.component.ItemComponent;
import org.jetbrains.annotations.Nullable;

public interface ComponentHolder {
    ComponentMap getComponents();

    /**
     * 주어진 {@code componentType}의 값을 반환합니다. 만약 해당 값이 존재하지 않는다면 {@code null}
     * 을 반환합니다.
     *
     * <p>반환된 값은 불변형 이여야 합니다.
     */
    @Nullable
    default <T> T get(ItemComponent<? extends T> componentType) {
        return this.getComponents().get(componentType);
    }

    /**
     * 주어진 {@code componentType}의 값을 반환합니다. 만약 해당 값이 존재하지 않는다면
     * {@code fallback}을 반환합니다.
     *
     *
     * <p>메소드에서 주어진 {@code fallback}은 컴포넌트에 영향을 끼치지 않습니다.
     * 반환된 값은 불변형 이여야 합니다.
     */
   default <T> T getOrDefault(ItemComponent<? extends T> componentType, T fallback) {
        return this.getComponents().getOrDefault(componentType, fallback);
   }

    /**
     * 주어진 {@code componentType}이 존재하는지 확인합니다. <p>
     *
     * @return {@code true} 를 반환하면 해당 컴포넌트가 존재합니다.
     */
   default boolean contains(ItemComponent<?> type) {
         return this.getComponents().contains(type);
   }
}
