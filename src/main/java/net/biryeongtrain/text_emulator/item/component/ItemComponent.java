package net.biryeongtrain.text_emulator.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * 아이템에 적용될 컴포넌트의 추상부입니다.
 * @param <T>
 */
public interface ItemComponent<T> {
    Codec<ItemComponent<?>> CODEC = Codec.lazyInitialized(Registries.ITEM_COMPONENTS::getCodec);
    Codec<ItemComponent<?>> PERSISTENT_CODEC = CODEC.validate(componentType -> componentType.shouldSkipSerialization() ? DataResult.error(() -> "Encountered transient component " + Registries.ITEM_COMPONENTS.getId(componentType)) : DataResult.success(componentType));

    Codec<Map<ItemComponent<?>, Object>> TYPE_TO_VALUE_MAP_CODEC = Codec.dispatchedMap(PERSISTENT_CODEC, ItemComponent::getCodecOrThrow);

    static <T> Builder<T> getBuilder() {
        return new Builder<>();
    }
    Codec<T> getCodec();
    class Builder<T> {
        @Nullable
        private Codec<T> codec;

        public Builder() {}

        public Builder<T> codec(@Nullable Codec<T> codec) {
            this.codec = codec;
            return this;
        }

        public ItemComponent<T> build() {
            return new SimpleItemComponent<>(this.codec);
        }
    }

    default boolean shouldSkipSerialization() {
        return this.getCodec() == null;
    }

    default Codec<T> getCodecOrThrow() {
        Codec<T> codec = this.getCodec();
        if (codec == null) {
            throw new IllegalStateException(this + " is not a persistent component");
        }
        return codec;
    }

    class SimpleItemComponent<T> implements ItemComponent<T> {
        @Nullable
        private final Codec<T> codec;

        SimpleItemComponent(@Nullable Codec<T> codec) {
            this.codec = codec;
        }

        public @Nullable Codec<T> getCodec() {
            return this.codec;
        }

        public String toString() {
            return Util.registryValueToString(Registries.ITEM_COMPONENTS, this);
        }
    }
}
