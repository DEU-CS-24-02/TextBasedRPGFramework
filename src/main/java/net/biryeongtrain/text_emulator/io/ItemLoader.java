package net.biryeongtrain.text_emulator.io;

import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.Util;

public class ItemLoader {
    public static void loadItems() {
        if (Registries.ITEM.isFrozen()) {
            throw new IllegalStateException("Cannot load items into a frozen registry");
        }
        Util.IOExecutor.execute(() -> {

        });
    }
}
