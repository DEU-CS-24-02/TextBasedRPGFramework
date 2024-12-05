package net.biryeongtrain.text_emulator.level.scene;

import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;

public enum Condition implements StringIdentifiable {
    IF, NEVER, ALWAYS;

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
