package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;

import java.util.Map;

public class Monster implements Entity {
    private final String name;
    private int health;
    private int attackPower;
    private int defense;
    private int level;
    private Map<String, Boolean> statusEffects;

    public Monster(String name, int health, int attackPower, int defense, int level, Map<String, Boolean> statusEffects) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.defense = defense;
        this.level = level;
        this.statusEffects = statusEffects;
    }

    @Override
    public int getHealth() {
        return health;
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
    public int getLevel() {
        return level;
    }

    @Override
    public boolean hasStatusEffect(String effectType) {
        return statusEffects.getOrDefault(effectType, false);
    }
}
