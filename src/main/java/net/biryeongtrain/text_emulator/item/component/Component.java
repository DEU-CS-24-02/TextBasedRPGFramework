package net.biryeongtrain.text_emulator.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Map;

public record Component<T> (DataComponent<T> type, T value) {
    static Component<?> of (Map.Entry<DataComponent<?>, Object> entry) {
        return Component.of((DataComponent<Object>)entry.getKey(), entry.getValue());
    }

    public static <T> Component<T> of(DataComponent<T> type, T value) {
        return new Component<>(type, value);
    }

    public void apply(ComponentMapImpl components) {
        components.set(this.type, this.value);
    }

    public <T> DataResult<T> encode(DynamicOps<T> ops) {
        Codec<T> codec = (Codec<T>) this.type.getCodec();
        if (codec == null) {
            return DataResult.error(() -> "Component of type " + String.valueOf(this.type) + " is not encodable");
        }
        return codec.encodeStart(ops, (T) this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.type) + "=>" + String.valueOf(this.value);
    }
}
