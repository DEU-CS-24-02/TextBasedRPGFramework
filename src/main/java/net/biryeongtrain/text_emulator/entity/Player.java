package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.GameManager;

public class Player extends Entity {
    public static Codec<Player> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("health").forGetter(Player::getHealth),
                    Codec.FLOAT.fieldOf("armor").forGetter(Player::getArmor),
                    Codec.FLOAT.fieldOf("damage").forGetter(Player::getDamage),
                    PlayerInventory.CODEC.fieldOf("inventory").forGetter(Player::getInventory)
            ).apply(instance, Player::new)
    ));

    private final PlayerInventory inventory;
    public Player() {
        super(EntityTypes.PLAYER);
        this.inventory = new PlayerInventory();
    }

    private Player(float health, float armor, float damage, PlayerInventory inventory) {
        super(EntityTypes.PLAYER, health, armor, damage);
        this.inventory = inventory;
    }


    public void addGold(int value) {
        this.inventory.addGold(value);
        GameManager.getInstance().shout(String.format("골드 %d개를 획득했습니다!", value));
    }

    public PlayerInventory getInventory() {
        return inventory;
    }
}
