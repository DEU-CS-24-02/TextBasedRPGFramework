package net.biryeongtrain.text_emulator.level.scene;

import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;

public enum ActionType implements StringIdentifiable {
    GIVE {
        @Override
        public void execute(Unit unit, String value) {

        }
    }, TAKE {
        @Override
        public void execute(Unit unit, String value) {

        }
    }, PRINT {
        @Override
        public void execute(Unit unit, String value) {

        }
    }, GOTO {
        @Override
        public void execute(Unit unit, String value) {

        }
    };

    public abstract void execute(Unit unit, String value);

    public static EnumCodec<ActionType> CODEC = StringIdentifiable.createCodec(ActionType::values);

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
