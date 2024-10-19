package net.biryeongtrain.text_emulator.item.component.type;

import net.biryeongtrain.text_emulator.entity.Slot;
import net.biryeongtrain.text_emulator.utils.StringIdentifiable;

public enum Rarity implements StringIdentifiable {
    COMMON,
    ADVANCED,
    RARE,
    EPIC,
    LEGENDARY;

    public static final StringIdentifiable.EnumCodec<Rarity> CODEC = StringIdentifiable.createCodec(Rarity::values);

    @Override
    public String asString() {
        return this.toString().toLowerCase();
    }
}
