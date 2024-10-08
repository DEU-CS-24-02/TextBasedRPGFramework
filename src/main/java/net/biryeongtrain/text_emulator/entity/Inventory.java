package net.biryeongtrain.text_emulator.entity;

public class Inventory {
    private String name;
    private int effect; // 예: 능력치 증가 효과

    public Inventory(String name, int effect) {
        this.name = name;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public int getEffect() {
        return effect;
    }
}
