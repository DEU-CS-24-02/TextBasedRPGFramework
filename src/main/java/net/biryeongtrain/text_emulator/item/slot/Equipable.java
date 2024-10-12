package net.biryeongtrain.text_emulator.item.slot;

import net.biryeongtrain.text_emulator.entity.Slot;

public interface Equipable {
    Slot getSlot();
    void onEquip();
    double getArmorPoint();
}