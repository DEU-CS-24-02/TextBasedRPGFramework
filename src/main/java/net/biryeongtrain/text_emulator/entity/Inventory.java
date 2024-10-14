package net.biryeongtrain.text_emulator.entity;

import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.utils.collections.DefaultedList;

public class Inventory {
    public final DefaultedList<Item> entries;

    public Inventory(int size) {
        this.entries = DefaultedList.ofSize(size, Item.EMPTY);
    }
}
