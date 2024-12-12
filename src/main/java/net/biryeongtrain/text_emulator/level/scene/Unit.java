package net.biryeongtrain.text_emulator.level.scene;

import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;

public enum Unit implements StringIdentifiable {

    KARMA {
        @Override
        String getPlayerValue() {
            return String.valueOf(instance.getPlayer().getInventory().getReputation());
        }
    }, GOLD {
        @Override
        String getPlayerValue() {
            return String.valueOf(instance.getPlayer().getInventory().getGold());
        }
    }, HEALTH {
        @Override
        String getPlayerValue() {
            return String.valueOf(instance.getPlayer().getHealth());
        }
    }, ITEM {
        @Override
        String getPlayerValue() {
            return null;
        }
    }, EMPTY {
        @Override
        String getPlayerValue() {
            return null;
        }
    }
    ;

    public static EnumCodec<Unit> CODEC = StringIdentifiable.createCodec(Unit::values);
    private static GameManager instance = GameManager.getInstance();
    abstract String getPlayerValue();
    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
