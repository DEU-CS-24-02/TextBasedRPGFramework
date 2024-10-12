package net.biryeongtrain.text_emulator.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerCharacter implements Entity {
    private final String name;
    private int health;
    private int attackPower;
    private int defense;
    private int level;
    private int gold;
    private Map<String, Boolean> statusEffects; // 상태이상 여부
    private List<Skill> skills; // 보유 스킬

    public PlayerCharacter(String name, int health, int attackPower, int defense, int level, int gold, Map<String, Boolean> statusEffects, List<Skill> skills) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.defense = defense;
        this.level = level;
        this.gold = gold;
        this.statusEffects = statusEffects;
        this.skills = skills;
    }

    public String getName() {
        return name;
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

    // 추가적인 메서드 (예: 스킬 추가, 상태이상 관리 등)을 여기에 정의할 수 있습니다.
}