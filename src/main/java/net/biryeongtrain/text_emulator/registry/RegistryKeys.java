package net.biryeongtrain.text_emulator.registry;

import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class RegistryKeys {
    public static final Identifier ROOT = Identifier.of("root");
    public static final RegistryKey<Registry<Item>> ITEM = RegistryKey.ofRegistry(Identifier.of("item"));
}
