package net.biryeongtrain.text_emulator.entity;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.entity.damage.DamageType;
import net.biryeongtrain.text_emulator.io.Serializable;
import net.biryeongtrain.text_emulator.registry.Registries;

public class Entity implements Serializable<Entity> {
    public static Codec<Entity> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(instance -> instance.group(
                    Registries.ENTITY_TYPE.getCodec().fieldOf("type").forGetter(Entity::getType),
                    Codec.FLOAT.fieldOf("health").forGetter(Entity::getHealth),
                    Codec.FLOAT.fieldOf("armor").forGetter(Entity::getArmor),
                    Codec.FLOAT.fieldOf("damage").forGetter(Entity::getDamage),
                    Codec.FLOAT.fieldOf("max_health").forGetter(Entity::getMaxHealth)
            ).apply(instance, Entity::new))
    );

    private final EntityType type;
    private float health;
    private float armor;
    private float damage;
    private float maxHealth;
    public boolean isAlive = true;

    public Entity(EntityType type) {
        this.type = type;
        this.health = type.getDefaultHealth();
        this.armor = type.getDefaultArmor();
        this.damage = type.getDefaultDamage();
        this.maxHealth = type.getDefaultHealth();
    }
    Entity(EntityType type, float health, float armor, float damage, float maxHealth) {
        this.type = type;
        this.health = health;
        this.armor = armor;
        this.damage = damage;
        this.maxHealth = maxHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }


    public EntityType getType() {
        return type;
    }

    public float getHealth() {
        return health;
    }

    public void addHealth(float value) {
        this.health = Math.clamp(health + value, 0, maxHealth);
        if (health == 0) {
            this.die();
        }
    }

    public float getArmor() {
        return armor;
    }

    public float getDamage() {
        return damage;
    }

    public void heal(float value) {
        GameManager.getInstance().shout(String.format("%s가 %s 만큼 회복했습니다!", this.type.getKey().getPath(), value));
        this.health = Math.max(health + value, maxHealth);
    }

    public void damage(float amount, DamageType type) {
        GameManager.getInstance().shout(type.damageDisplayFormat.formatted(amount));
        this.addHealth(-amount);
    }

    public void die() {
        this.isAlive = false;
        this.dropToPlayer();
    }

    @Override
    public Codec<Entity> getCodec() {
        return null;
    }

    @Override
    public Entity serialize(JsonElement element) {
        return null;
    }

    public void dropToPlayer() {
        Player player = GameManager.getInstance().getPlayer();
        LootTableInstance instance = this.type.getLootTableManager().getLootTable();
        instance.items().forEach(item -> {player.getInventory().addStack(item);});
    }
}
