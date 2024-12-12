package net.biryeongtrain.text_emulator.level.scene;

import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public enum Operator implements StringIdentifiable {
    BELOW {
        @Override
        public <T> boolean compare(Unit unit, T value2) {
            switch (unit) {
                case KARMA -> {
                    return Long.parseLong(Unit.KARMA.getPlayerValue()) < Long.parseLong(value2.toString());
                }
                case GOLD -> {
                    return Long.parseLong(Unit.GOLD.getPlayerValue()) < Long.parseLong(value2.toString());
                }
                case HEALTH -> {
                    return Float.parseFloat(Unit.HEALTH.getPlayerValue()) < Float.parseFloat(value2.toString());
                }
            }
            return false;
        }
    }, MORE {
        @Override
        public <T> boolean compare(Unit unit, T value2) {
            switch (unit) {
                case KARMA -> {
                    return Long.parseLong(Unit.KARMA.getPlayerValue()) >= Long.parseLong(value2.toString());
                }
                case GOLD -> {
                    return Long.parseLong(Unit.GOLD.getPlayerValue()) >= Long.parseLong(value2.toString());
                }
                case HEALTH -> {
                    return Float.parseFloat(Unit.HEALTH.getPlayerValue()) >= Float.parseFloat(value2.toString());
                }
            }
            return false;
        }
    }, LOWER {
        @Override
        public <T> boolean compare(Unit unit, T value2) {
            switch (unit) {
                case KARMA -> {
                    return Long.parseLong(Unit.KARMA.getPlayerValue()) <= Long.parseLong(value2.toString());
                }
                case GOLD -> {
                    return Long.parseLong(Unit.GOLD.getPlayerValue()) <= Long.parseLong(value2.toString());
                }
                case HEALTH -> {
                    return Float.parseFloat(Unit.HEALTH.getPlayerValue()) <= Float.parseFloat(value2.toString());
                }
            }
            return false;
        }
    }, OVER {
        @Override
        public <T> boolean compare(Unit unit, T value2) {
            switch (unit) {
                case KARMA -> {
                    return Long.parseLong(Unit.KARMA.getPlayerValue()) > Long.parseLong(value2.toString());
                }
                case GOLD -> {
                    return Long.parseLong(Unit.GOLD.getPlayerValue()) > Long.parseLong(value2.toString());
                }
                case HEALTH -> {
                    return Float.parseFloat(Unit.HEALTH.getPlayerValue()) > Float.parseFloat(value2.toString());
                }
            }
            return false;
        }
    }, EQUALS {
        @Override
        public <T> boolean compare(Unit unit, T value2) {
            switch (unit) {
                case KARMA -> {
                    return Long.parseLong(value2.toString()) == Long.parseLong(Unit.KARMA.getPlayerValue());
                }
                case GOLD -> {
                    return Long.parseLong(value2.toString()) == Long.parseLong(Unit.GOLD.getPlayerValue());
                }
                case HEALTH -> {
                    return Float.parseFloat(value2.toString()) == Float.parseFloat(Unit.HEALTH.getPlayerValue());
                }
            }
            return false;
        }
    }, HAS {
        @Override
        public <T> boolean compare(Unit unit, T value2) {
            if (unit != Unit.ITEM) {
                return false;
            }
            if (!(value2 instanceof String v2)) {
                return false;
            }
            var dataResult = Identifier.validate(v2);
            if (dataResult.isError()) {
                Main.LOGGER.error("Invalid Identifier: {}", v2);
                return false;
            }
            var item = Registries.ITEM.get(dataResult.result().get());
            if (item == null) {
                Main.LOGGER.error("Item not found: {}", v2);
                return false;
            }
            var player = GameManager.getInstance().getPlayer();
            return player.getInventory().containsAny(itemStack -> itemStack.getItem() == item);
        }
    },
    NONE {
        @Override
        public <T> boolean compare(Unit unit, T value2) {
            return true;
        }
    }
    ;

    public static EnumCodec<Operator> CODEC = StringIdentifiable.createCodec(Operator::values);

    public abstract <T> boolean compare(Unit unit, T value2);

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}