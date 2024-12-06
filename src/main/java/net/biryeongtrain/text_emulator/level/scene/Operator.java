package net.biryeongtrain.text_emulator.level.scene;

import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;

public enum Operator implements StringIdentifiable {
    BELOW, MORE, LOWER, OVER, EQUALS, HAS;

    public static EnumCodec<Operator> CODEC = StringIdentifiable.createCodec(Operator::values);

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
