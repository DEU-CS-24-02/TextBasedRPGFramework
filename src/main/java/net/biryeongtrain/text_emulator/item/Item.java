package net.biryeongtrain.text_emulator.item;

import net.biryeongtrain.text_emulator.io.Serializable;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public interface Item<T extends Item<T>> extends Serializable<T> {
    /**
     * Returns Max Usage of Item.
     * if this -1, it means it not consumable.
     * @return Max Usage of Item.
     */
    default int getUsage() {
        return -1;
    }

    /**
     * returns the damage that increase when the item is held.
     *
     * @return the damage item
     */
    default float getDamage() {
        return 0.0F;
    }
    
    Identifier getId();
}
