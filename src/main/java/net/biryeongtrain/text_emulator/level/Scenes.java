package net.biryeongtrain.text_emulator.level;

import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class Scenes {
    public static Scene START_SCENE = Registry.register(Registries.SCENE, Identifier.ofDefault("start_scene"), StartScene.create());

    public static void register() {
        // NO-OP
    }
}
