package net.biryeongtrain.text_emulator.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


public interface DataComponent<T> {
    Codec<DataComponent<?>> CODEC = Codec.lazyInitialized(Registries.ITEM_COMPONENTS::getCodec);
    Codec<DataComponent<?>> PERSISTENT_CODEC = CODEC.validate(componentType -> componentType.shouldSkipSerialization() ? DataResult.error(() -> "Encountered transient component " + Registries.ITEM_COMPONENTS.getId(componentType)) : DataResult.success(componentType));

    Codec<Map<DataComponent<?>, Object>> TYPE_TO_VALUE_MAP_CODEC = Codec.dispatchedMap(PERSISTENT_CODEC, DataComponent::getCodecOrThrow);

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

        public DataComponent<T> build() {
            return new SimpleDataComponent<>(this.codec);
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

    class SimpleDataComponent<T> implements DataComponent<T> {
        @Nullable
        private final Codec<T> codec;

        SimpleDataComponent(@Nullable Codec<T> codec) {
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
