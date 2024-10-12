package net.biryeongtrain.text_emulator.item;

import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.io.Serializable;
public interface Item<T extends Item<T>> extends Serializable<T> {
    /**
     * Returns the attack power of the item.
     * @return Attack power of the item.
     */
    int getAttackPower();
    /**
     * Returns the defense value of the item.
     * @return Defense value of the item.
     */
    int getDefense();

    /**
     * Returns the slot where the item can be equipped.
     * @return Slot type of the item.
     */
    Slot getSlot();

    /**
     * Returns the usage count of the item.
     * If this returns -1, it means the item is not consumable.
     * @return Usage count of the item.
     */
    int getUsageCount();

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
}
