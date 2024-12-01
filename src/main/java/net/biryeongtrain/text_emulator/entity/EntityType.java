package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.level.Scene;
import net.biryeongtrain.text_emulator.registry.RegistryKey;
import net.biryeongtrain.text_emulator.registry.RegistryKeys;

public class EntityType {
    public static final Codec<EntityType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryKey.createCodec(RegistryKeys.ENTITY_TYPE).fieldOf("key").forGetter(EntityType::getKey),
            Codec.FLOAT.fieldOf("defaultHealth").forGetter(EntityType::getDefaultHealth),
            Codec.FLOAT.fieldOf("defaultDamage").forGetter(EntityType::getDefaultDamage),
            Codec.FLOAT.fieldOf("defaultArmor").forGetter(EntityType::getDefaultArmor),
            LootTableManager.CODEC.fieldOf("lootTableManager").forGetter(EntityType::getLootTableManager)
    ).apply(instance, EntityType::new));

    private final RegistryKey<EntityType> key;

    private final float defaultHealth;
    private final float defaultDamage;
    private final float defaultArmor;
    private final LootTableManager lootTableManager;

    public LootTableManager getLootTableManager() {
        return lootTableManager;
    }

    public float getDefaultArmor() {
        return defaultArmor;
    }

    public float getDefaultDamage() {
        return defaultDamage;
    }

    public float getDefaultHealth() {
        return defaultHealth;
    }

    public RegistryKey<EntityType> getKey() {
        return key;
    }

    private EntityType(RegistryKey<EntityType> key, float defaultHealth, float defaultDamage, float defaultArmor, LootTableManager lootTableManager) {
        this.key = key;
        this.defaultHealth = defaultHealth;
        this.defaultDamage = defaultDamage;
        this.defaultArmor = defaultArmor;
        this.lootTableManager = lootTableManager;
    }

    public Entity createEntity() {
        return null;
    }

    @FunctionalInterface
    public interface EntityFactory {
        Entity create(EntityType type, Scene scene);
    }
}
