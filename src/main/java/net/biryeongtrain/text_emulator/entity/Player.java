package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.item.ItemStack;

public class Player extends Entity {
    public static Codec<Player> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("health").forGetter(Player::getHealth),
                    Codec.FLOAT.fieldOf("armor").forGetter(Player::getArmor),
                    Codec.FLOAT.fieldOf("damage").forGetter(Player::getDamage),
                    PlayerInventory.CODEC.fieldOf("inventory").forGetter(Player::getInventory),
                    Codec.FLOAT.optionalFieldOf("max_health", EntityTypes.PLAYER.getDefaultHealth()).forGetter(Player::getMaxHealth)
            ).apply(instance, Player::new)
    ));

    private final PlayerInventory inventory;
    public Player() {
        super(EntityTypes.PLAYER);
        this.inventory = new PlayerInventory();
    }

    private Player(float health, float armor, float damage, PlayerInventory inventory, float maxHealth) {
        super(EntityTypes.PLAYER, health, armor, damage, maxHealth);
        this.inventory = inventory;
    }



    public void addGold(int value) {
        this.inventory.addGold(value);
        GameManager.getInstance().shout(String.format("골드 %d개를 획득했습니다!", value));
    }

    public void addItem(Item item) {
        this.inventory.addStack(new ItemStack(item, 1));
        GameManager.getInstance().shout(String.format("%s을(를) 획득했습니다!", item.getName()));
    }

    public void addItem(ItemStack stack) {
        this.inventory.addStack(stack);
        GameManager.getInstance().shout(String.format("%s을(를) 획득했습니다!", stack.getItem().getName()));
    }

    public void removeItem(Item item) {
        var result = this.inventory.removeStack(item);
        if (result) {
            GameManager.getInstance().shout(String.format("%s을(를) 잃었습니다!", item.getName()));
        }
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public void addReputation(int value) {
        this.inventory.addReputation(value);
        GameManager.getInstance().shout(String.format("명성 %d을(를) 획득했습니다!", value));
    }

}
