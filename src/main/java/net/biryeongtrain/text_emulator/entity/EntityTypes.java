package net.biryeongtrain.text_emulator.entity;

import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.List;

public class EntityTypes {
    public static EntityType PLAYER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.ofDefault("player"),
            new EntityType(Identifier.ofDefault("player"), 10, 1, 1, new LootTableManager(List.of(LootTableInstance.EMPTY)), List.of())
    );

    public static void register() {
        // NO-OP
    }
}
