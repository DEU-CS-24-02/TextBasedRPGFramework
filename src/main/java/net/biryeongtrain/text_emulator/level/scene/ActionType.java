package net.biryeongtrain.text_emulator.level.scene;

import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.entity.Player;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public enum ActionType implements StringIdentifiable {
    GIVE {
        @Override
        public void execute(GameManager instance, Unit unit, String value) {
            Player player = instance.getPlayer();
            switch (unit) {
                case HEALTH -> player.addHealth(Integer.parseInt(value));
                case GOLD -> player.addGold(Integer.parseInt(value));
                case ITEM -> {
                    if (!ActionType.validateIdentifier(value)) {
                        return;
                    }
                    var item = Registries.ITEM.get(Identifier.of(value));
                    if (item == null) {
                        Main.LOGGER.error("Item not found: " + value);
                        return;
                    }
                    player.addItem(item);
                }
                case KARMA -> player.addReputation(Integer.parseInt(value));
            }
        }
    }, TAKE {
        @Override
        public void execute(GameManager instance, Unit unit, String value) {
            Player player = instance.getPlayer();
            switch (unit) {
                case HEALTH -> player.addHealth(-Integer.parseInt(value));
                case GOLD -> player.addGold(-Integer.parseInt(value));
                case ITEM -> {
                    if (!ActionType.validateIdentifier(value)) {
                        return;
                    }
                    var item = Registries.ITEM.get(Identifier.of(value));
                    if (item == null) {
                        Main.LOGGER.error("Item not found: " + value);
                        return;
                    }
                    player.removeItem(item);
                }
                case KARMA -> player.addReputation(-Integer.parseInt(value));
            }
        }
    }, PRINT {
        @Override
        public void execute(GameManager instance, Unit unit, String value) {
            instance.shout(value);
        }
    }, GOTO {
        @Override
        public void execute(GameManager instance, Unit unit, String value) {
            if (!ActionType.validateIdentifier(value)) {
                return;
            }
            var sceneId = Identifier.of(value);
            var scene = Registries.SCENE.get(sceneId);
            if (scene == null) {
                Main.LOGGER.error("Scene not found: " + value);
                return;
            }
            instance.goToScene(sceneId);
        }
    };

    public abstract void execute(GameManager instance, Unit unit, String value);

    public static EnumCodec<ActionType> CODEC = StringIdentifiable.createCodec(ActionType::values);
    private static boolean validateIdentifier(String value) {
        if (Identifier.validate(value).isError()) {
            Main.LOGGER.error("Invalid Identifier: " + value);
            return false;
        }
        return true;
    }
    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
