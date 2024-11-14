package net.biryeongtrain.text_emulator.registry;

import net.biryeongtrain.text_emulator.entity.Entity;
import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.item.component.ItemComponent;
import net.biryeongtrain.text_emulator.level.Scene;
import net.biryeongtrain.text_emulator.scenario.Scenario;
import net.biryeongtrain.text_emulator.scenario.ScenarioMeta;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class RegistryKeys {
    public static final Identifier ROOT = Identifier.of("root");
    public static final RegistryKey<Registry<Item>> ITEM = RegistryKey.ofRegistry(Identifier.of("item"));
    public static final RegistryKey<Registry<Scenario>> SCENARIO = RegistryKey.ofRegistry(Identifier.of("scenario"));
    public static final RegistryKey<Registry<Entity>> ENTITY = RegistryKey.ofRegistry(Identifier.of("entity"));
    public static final RegistryKey<Registry<Scene>> SCENE = RegistryKey.ofRegistry(Identifier.of("scene"));
    public static final RegistryKey<Registry<ItemComponent<?>>> ITEM_COMPONENTS = RegistryKey.ofRegistry(Identifier.of("item_components"));
    public static final RegistryKey<Registry<ScenarioMeta>> SCENARIO_META = RegistryKey.ofRegistry(Identifier.of("scenario_meta"));
}
