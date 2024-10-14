package net.biryeongtrain.text_emulator.item.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.entity.Slot;

import java.util.List;

public record Equipable(List<Slot> slots) {
    public static final Codec<Equipable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Slot.CODEC.listOf().fieldOf("slots").forGetter(Equipable::slots)
            ).apply(instance, Equipable::new)
    );
}
