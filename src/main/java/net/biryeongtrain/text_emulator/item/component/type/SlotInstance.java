package net.biryeongtrain.text_emulator.item.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.entity.Slot;

public record SlotInstance(Slot slot, float value) {
    public static Codec<SlotInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Slot.CODEC.fieldOf("slot").forGetter(SlotInstance::slot),
                    Codec.FLOAT.fieldOf("value").forGetter(SlotInstance::value)
            ).apply(instance, SlotInstance::new)
    );
}
