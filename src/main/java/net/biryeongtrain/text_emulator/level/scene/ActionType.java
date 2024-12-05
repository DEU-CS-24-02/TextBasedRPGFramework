package net.biryeongtrain.text_emulator.level.scene;

import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;

public enum ActionType implements StringIdentifiable {
    GIVE, TAKE, PRINT, GOTO;

    public static EnumCodec<ActionType> CODEC = StringIdentifiable.createCodec(ActionType::values);
    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
