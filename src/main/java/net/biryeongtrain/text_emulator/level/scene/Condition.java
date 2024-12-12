package net.biryeongtrain.text_emulator.level.scene;

import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;

public enum Condition implements StringIdentifiable {
    IF {
        @Override
        public boolean check(Operator operator, Unit unit, String value) {
            return operator.compare(unit, value);
        }
    }, NEVER {
        @Override
        public boolean check(Operator operator, Unit unit, String value) {
            return false;
        }
    }, ALWAYS {
        @Override
        public boolean check(Operator operator, Unit unit, String value) {
            return true;
        }
    };

    public static EnumCodec<Condition> CODEC = StringIdentifiable.createCodec(Condition::values);
    public abstract boolean check(Operator operator, Unit unit, String value);
    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
