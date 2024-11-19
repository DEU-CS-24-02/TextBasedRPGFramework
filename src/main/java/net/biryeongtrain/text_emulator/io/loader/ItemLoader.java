package net.biryeongtrain.text_emulator.io.loader;

import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class ItemLoader implements ContentsLoader<Item> {


    @Override
    public void loadData() {

    }

    @Override
    public @NotNull Registry<Item> getRegistry() {
        return Registries.ITEM;
    }
}
