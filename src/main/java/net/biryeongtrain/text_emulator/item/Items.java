package net.biryeongtrain.text_emulator.item;

import net.biryeongtrain.text_emulator.item.component.ItemComponents;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class Items {
    public static Item AIR = Registry.register(Registries.ITEM, Identifier.ofDefault("air"), new Item(new Item.Settings()));
    public static Item TEST_ITEM = Registry.register(Registries.ITEM, Identifier.ofDefault("test_item"), new Item(new Item.Settings().setMaxCount(33).component(ItemComponents.MAX_STACK_SIZE, 32)));
}
