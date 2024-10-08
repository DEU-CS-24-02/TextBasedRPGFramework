package net.biryeongtrain.text_emulator.entity;

import java.util.ArrayList;
import java.util.List;
import net.biryeongtrain.text_emulator.item.Item;

public class _character{
    private String name;
    private int health;
    private int money;
    private int defense_mental;
    private int experience; 
    private List<Inventory> inventory;
    private List<Ability> abilities; // 능력 리스트
    private List<StatusEffect> statusEffects; // 능력 리스트
    

    public _character(String name, int health, int money, int defense_mental){
        this.name = name;
        this.health = health;
        this.money = money;
        this.defense_mental = defense_mental;
        this.experience = 0;
        this.inventory = new ArrayList<>(); // 인벤토리 리스트 초기화 
        this.abilities = new ArrayList<>(); // 능력 리스트 초기화
        this.statusEffects = new ArrayList<>(); //상태 리스트 초기화
    }
// Getter 및 Setter
public String getName() {
    return name;
}

public int getHealth() {
    return health;
}

public void heal(int amount) {
    int MAX_Health = 3;
    this.health += amount;
    if (this.health > MAX_Health) {
        this.health = MAX_Health;
    }
    System.out.println("현재 체력: " + this.health);
}

public void addItem(Inventory item) {
    inventory.add(item);
    System.out.println(item.getName() + " 아이템을 얻었습니다.");
}

public void removeItem(Inventory item) {
    inventory.remove(item);
    System.out.println(item.getName() + " 아이템을 사용했습니다.");
}

public void listItems() {
    System.out.println("현재 보유한 아이템:");
    for (Inventory item : inventory) {
        System.out.println("- " + item.getName());
    }
}

public void addAbility(Ability ability) {
    abilities.add(ability);
    System.out.println(ability.getName() + " 능력을 얻었습니다.");
}

public void addStatusEffect(StatusEffect effect) {
    statusEffects.add(effect);
    System.out.println(effect.getName() + " 상태 효과가 적용되었습니다.");
}

public void updateStatusEffects() {
    for (int i = statusEffects.size() - 1; i >= 0; i--) {
        StatusEffect effect = statusEffects.get(i);
        effect.decrementDuration();
        if (effect.isExpired()) {
            statusEffects.remove(i);
            System.out.println(effect.getName() + " 상태 효과가 사라졌습니다.");
        }
    }
}

public boolean hasItem(String itemName) {
    for (Inventory g_item : inventory) {
        if (g_item.getName().equals(itemName)) {
            return true;
        }
    }
    return false;
}
}