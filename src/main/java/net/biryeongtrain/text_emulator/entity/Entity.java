package net.biryeongtrain.text_emulator.entity;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.entity.damage.DamageType;
import net.biryeongtrain.text_emulator.io.Serializable;
import net.biryeongtrain.text_emulator.registry.Registries;

public class Entity implements Serializable<Entity> {
    public static Codec<Entity> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(instance -> instance.group(
                    Registries.ENTITY_TYPE.getCodec().fieldOf("type").forGetter(Entity::getType),
                    Codec.FLOAT.fieldOf("health").forGetter(Entity::getHealth),
                    Codec.FLOAT.fieldOf("armor").forGetter(Entity::getArmor),
                    Codec.FLOAT.fieldOf("damage").forGetter(Entity::getDamage)
            ).apply(instance, Entity::new))
    );

    private final EntityType type;
    private float health;
    private float armor;
    private float damage;

    public Entity(EntityType type) {
        this.type = type;
        this.health = type.getDefaultHealth();
        this.armor = type.getDefaultArmor();
        this.damage = type.getDefaultDamage();
    }
    private Entity(EntityType type, float health, float armor, float damage) {
        this.type = type;
        this.health = health;
        this.armor = armor;
        this.damage = damage;
    }

    public EntityType getType() {
        return type;
    }

    public float getHealth() {
        return health;
    }

    public float getArmor() {
        return armor;
    }

    public float getDamage() {
        return damage;
    }

    public void damage(float amount, DamageType type) {
        // TODO: 2024-11-21 Implement damage calculation
    }

    public void die() {
        // TODO: 2024-11-21 Implement death
    }

    public void heal(float amount) {

    }

    @Override
    public Codec<Entity> getCodec() {
        return null;
    }

    @Override
    public Entity serialize(JsonElement element) {
        return null;
    }
}
