package net.biryeongtrain.text_emulator.item.slot;

import net.biryeongtrain.text_emulator.entity.Slot;
import net.biryeongtrain.text_emulator.item.Item;

public class Equipment implements Item<Equipment>, Equipable {
    private final String name;
    private final int attackPower;
    private final int defense;
    private final Slot slot;
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
        return defense; // 또는 다른 방어력 계산 로직을 사용할 수 있습니다.
    }

    // 추가적인 메서드 (예: 장비의 효과 적용 등)를 여기에 정의할 수 있습니다.
}