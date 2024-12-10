package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;

public class Player extends Entity {
    public static Codec<Player> CODEC;
    public Player() {
        super(EntityTypes.PLAYER);
    }

    public void heal(float value) {

    }

    public void addGold(int value) {

    }
}
