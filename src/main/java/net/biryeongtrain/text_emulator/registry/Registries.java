package net.biryeongtrain.text_emulator.registry;

import net.biryeongtrain.text_emulator.entity.Entity;
import net.biryeongtrain.text_emulator.entity.EntityType;
import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.item.component.ItemComponent;
import net.biryeongtrain.text_emulator.level.Scene;
import net.biryeongtrain.text_emulator.scenario.Scenario;
import net.biryeongtrain.text_emulator.scenario.ScenarioMeta;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class Registries {
    private static final Registry<Registry<?>> ROOT = new SimpleRegistry<>(RegistryKey.ofRegistry(RegistryKeys.ROOT));
    public static final Registry<Scenario> SCENARIO = Registries.create(RegistryKeys.SCENARIO, new SimpleRegistry<>(RegistryKeys.SCENARIO));
    public static final Registry<Item> ITEM = Registries.create(RegistryKeys.ITEM, new SimpleRegistry<>(RegistryKeys.ITEM));
    public static final Registry<Entity> ENTITY = Registries.create(RegistryKeys.ENTITY, new SimpleRegistry<>(RegistryKeys.ENTITY));
    public static final Registry<Scene> SCENE = Registries.create(RegistryKeys.SCENE, new SimpleRegistry<>(RegistryKeys.SCENE));
    public static final Registry<ItemComponent<?>> ITEM_COMPONENTS = Registries.create(RegistryKeys.ITEM_COMPONENTS, new SimpleRegistry<>(RegistryKeys.ITEM_COMPONENTS));
    public static final Registry<ScenarioMeta> SCENARIO_META = Registries.create(RegistryKeys.SCENARIO_META, new SimpleRegistry<>(RegistryKeys.SCENARIO_META));
    public static final Registry<EntityType> ENTITY_TYPE = Registries.create(RegistryKeys.ENTITY_TYPE, new SimpleRegistry<>(RegistryKeys.ENTITY_TYPE));

    public static final Registry<? extends Registry<?>> REGISTRIES = ROOT;

    private static <T extends Registry<?>> T create(RegistryKey<T> key, T registry) {
        // TODO:  Check Scenario BootStrap Logic
        Identifier id = key.getValue();
        ROOT.add((RegistryKey<Registry<?>>) key, registry);
        return registry;
    }

    public static void freeze() {
        for(var registry : REGISTRIES) {
            registry.freeze();
        }
    }
}
