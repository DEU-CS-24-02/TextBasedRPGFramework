package net.biryeongtrain.text_emulator.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Consumable(int usage) {
    public static Codec<Consumable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("usage").forGetter(Consumable::usage)
            ).apply(instance, Consumable::new)
    );

    public Consumable decrement() {
        return new Consumable(this.usage - 1);
    }
}
