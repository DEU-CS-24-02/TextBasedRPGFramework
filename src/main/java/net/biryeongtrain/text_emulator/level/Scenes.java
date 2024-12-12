package net.biryeongtrain.text_emulator.level;

import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class Scenes {
    public static Scene START_SCENE = Registry.register(Registries.SCENE, Identifier.ofDefault("start_scene"), StartScene.create());
    public static Scene END_SCENE = Registry.register(Registries.SCENE, Identifier.ofDefault("end_scene"), EndScene.create());
    public static Scene DEAD_SCENE = Registry.register(Registries.SCENE, Identifier.ofDefault("dead_scene"), DeadScene.create());
    public static void register() {
        // NO-OP
    }
}
