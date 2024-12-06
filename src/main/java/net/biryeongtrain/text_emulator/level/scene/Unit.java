package net.biryeongtrain.text_emulator.level.scene;

import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;

public enum Unit implements StringIdentifiable {
    KARMA, GOLD, HEALTH, ITEM;

    public static EnumCodec<Unit> CODEC = StringIdentifiable.createCodec(Unit::values);

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
