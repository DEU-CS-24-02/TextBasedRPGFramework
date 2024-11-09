package net.biryeongtrain.text_emulator.item;

import net.biryeongtrain.text_emulator.entity.Slot;

public interface Equipable {
    Slot getSlot();
    void onEquip();
    double getArmorPoint();

}