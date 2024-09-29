package net.biryeongtrain.text_emulator.registry;

import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class Registries {
    private static final Registry<Registry<?>> ROOT = Registries.create(RegistryKey.ofRegistry(RegistryKeys.ROOT), new SimpleRegistry<>(RegistryKey.ofRegistry(RegistryKeys.ROOT)));
    public static final Registry<Item> ITEM = Registries.create(RegistryKeys.ITEM, new SimpleRegistry<>(RegistryKeys.ITEM));
    public static final Registry<? extends Registry<?>> REGISTRIES = ROOT;


    private static <T, R extends Registry<T>> R create(RegistryKey<? extends Registry<T>> key, R registry) {
        // TODO:  Check Synario BootStrap Logic
        Identifier id = key.getValue();
        ROOT.add((RegistryKey<Registry<?>>) key, registry);
        return registry;
    }
}
