package net.biryeongtrain.text_emulator.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Map;

public record Component<T> (ItemComponent<T> type, T value) {
    static Component<?> of (Map.Entry<ItemComponent<?>, Object> entry) {
        return Component.of((ItemComponent<Object>)entry.getKey(), entry.getValue());
    }

    public static <T> Component<T> of(ItemComponent<T> type, T value) {
        return new Component<>(type, value);
    }

    public void apply(ComponentMapImpl components) {
        components.set(this.type, this.value);
    }

    public <R> DataResult<R> encode(DynamicOps<R> ops) {
        Codec<R> codec = (Codec<R>) this.type.getCodec();
        if (codec == null) {
            return DataResult.error(() -> "Component of type " + this.type + " is not encodable");
        }
        return codec.encodeStart(ops, (R) this.value);
    }

    @Override
    public String toString() {
        return this.type + "=>" + this.value;
    }
}
