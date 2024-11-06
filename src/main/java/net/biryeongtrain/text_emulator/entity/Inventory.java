package net.biryeongtrain.text_emulator.entity;

import net.biryeongtrain.text_emulator.item.ItemStack;
import net.biryeongtrain.text_emulator.utils.collections.DefaultedList;

public class Inventory {
    public final DefaultedList<ItemStack> entries;

    public Inventory(int size) {
        this.entries = DefaultedList.ofSize(size, ItemStack.EMPTY);
    }

    public void get() {

    }
}
