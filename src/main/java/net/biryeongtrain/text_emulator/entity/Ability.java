package net.biryeongtrain.text_emulator.entity;

public class Ability {
    private String name;
    private int power;

    public Ability(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }
}

