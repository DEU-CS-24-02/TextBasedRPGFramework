package net.biryeongtrain.text_emulator.entity;
import net.biryeongtrain.text_emulator.item.Equipable;

import net.biryeongtrain.text_emulator.item.Item;

public class Equipment implements Item<Equipment>, Equipable {
    private final String name;
    private final int attackPower;
    private final int defense;
    private final Slot slot; // Slot 사용
    private final int usageCount;

    public Equipment(String name, int attackPower, int defense, Slot slot, int usageCount) {
        this.name = name;
        this.attackPower = attackPower;
        this.defense = defense;
        this.slot = slot;
        this.usageCount = usageCount;
    }

    @Override
    public int getAttackPower() {
        return attackPower;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public Slot getSlot() {
        return slot;
    }

    @Override
    public int getUsageCount() {
        return usageCount;
    }

    @Override
    public void onEquip() {
        System.out.println(name + " has been equipped.");
    }

    @Override
    public double getArmorPoint() {
        return defense;
    }
}
