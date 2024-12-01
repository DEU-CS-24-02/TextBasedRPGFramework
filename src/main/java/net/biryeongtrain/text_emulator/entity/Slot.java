package net.biryeongtrain.text_emulator.entity;

import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;

public enum Slot implements StringIdentifiable {
    HEAD,
    CHEST_PLATE,
    LEGGINGS,
    BOOTS,
    LEFT_HAND,
    RIGHT_HAND;

    public static final StringIdentifiable.EnumCodec<Slot> CODEC = StringIdentifiable.createCodec(Slot::values);

    @Override
    public String asString() {
        return this.name().toLowerCase().replace("_"," ");
    }
}
