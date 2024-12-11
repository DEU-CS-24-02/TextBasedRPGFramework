package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.level.Scene;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.List;

/**
 * EntityType은 Entity 클래스를 생성하기 위한 정보를 가지고 있습니다.
 * EntityType은 직접 생성되지 않으며, LoaderManager가 인식한 시나리오의 엔티티 정보 파일을 통해 등록됩니다.
 *
 * @see net.biryeongtrain.text_emulator.io.LoadManager
 */
public class EntityType {
    public static Codec<EntityType> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(EntityType::getKey),
            Codec.FLOAT.fieldOf("defaultHealth").forGetter(EntityType::getDefaultHealth),
            Codec.FLOAT.fieldOf("defaultDamage").forGetter(EntityType::getDefaultDamage),
            Codec.FLOAT.fieldOf("defaultArmor").forGetter(EntityType::getDefaultArmor),
            LootTableManager.CODEC.fieldOf("lootTableManager").forGetter(EntityType::getLootTableManager),
            Registries.ENTITY_TAG.getCodec().listOf().fieldOf("tags").forGetter(EntityType::getTags)
    ).apply(instance, EntityType::new)));

    private final Identifier key;
    private final float defaultHealth;
    private final float defaultDamage;
    private final float defaultArmor;
    private final List<EntityTag> tags;
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

    public List<EntityTag> getTags() {
        return this.tags;
    }

    public Identifier getKey() {
        return key;
    }

    public EntityType(Identifier key, float defaultHealth, float defaultDamage, float defaultArmor, LootTableManager lootTableManager, List<EntityTag> tags) {
        this.key = key;
        this.defaultHealth = defaultHealth;
        this.defaultDamage = defaultDamage;
        this.defaultArmor = defaultArmor;
        this.tags = tags;
        this.lootTableManager = lootTableManager;
    }

    public Entity createEntity() {
        return new Entity(this);
    }

    @FunctionalInterface
    public interface EntityFactory {
        Entity create(EntityType type, Scene scene);
    }
}
