package net.biryeongtrain.text_emulator.entity;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.io.Serializable;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.RegistryKey;
import net.biryeongtrain.text_emulator.registry.RegistryKeys;

import java.util.Objects;

public class Entity implements Serializable<Entity> {
    public static Codec<Entity> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(instance -> instance.group(
                    RegistryKey.createCodec(RegistryKeys.ENTITY_TYPE).fieldOf("type").forGetter(entity -> entity.type.getKey()),
                    Codec.FLOAT.fieldOf("health").forGetter(Entity::getHealth),
                    Codec.FLOAT.fieldOf("armor").forGetter(Entity::getArmor),
                    Codec.FLOAT.fieldOf("damage").forGetter(Entity::getDamage)
            ).apply(instance, Entity::new))
    );

    private final EntityType type;
    private float health;
    private float armor;
    private float damage;

    public Entity(RegistryKey<EntityType> type, float health, float armor, float damage) {
        this.type = Objects.requireNonNull(Registries.ENTITY_TYPE.get(type));
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


    @Override
    public Codec<Entity> getCodec() {
        return null;
    }

    @Override
    public Entity serialize(JsonElement element) {
        return null;
    }
}
