package net.biryeongtrain.text_emulator.entity;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.entity.damage.DamageType;
import net.biryeongtrain.text_emulator.io.Serializable;
import net.biryeongtrain.text_emulator.level.Scene;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.RegistryKey;
import net.biryeongtrain.text_emulator.registry.RegistryKeys;
import net.biryeongtrain.text_emulator.item.ItemStack;

import java.util.List;
import java.util.Objects;

public class Entity implements Serializable<Entity> {
    public static final Codec<Entity> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(instance -> instance.group(
                    RegistryKey.createCodec(RegistryKeys.ENTITY_TYPE).fieldOf("type").forGetter(entity -> entity.type.getKey()),
                    Codec.FLOAT.fieldOf("health").forGetter(Entity::getHealth),
                    Codec.FLOAT.fieldOf("armor").forGetter(Entity::getArmor),
                    Codec.FLOAT.fieldOf("damage").forGetter(Entity::getDamage)
            ).apply(instance, Entity::createFromCodec)) // Codec에서 사용할 메서드로 변경
    );

    private final EntityType type;
    private float health;
    private float armor;
    private float damage;
    private boolean isAlive;
    private LootTableManager lootTableManager; // LootTableManager 추가
    private Scene scene; // 현재 씬


    public Entity(EntityType type) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.health = type.getDefaultHealth();
        this.armor = type.getDefaultArmor();
        this.damage = type.getDefaultDamage();
        this.isAlive = true; // 초기 생존 상태
    }

    public Entity(EntityType type, LootTableManager lootTableManager, Scene scene) {
        this(type); // 기본 생성자 호출
        this.lootTableManager = lootTableManager; // LootTableManager 초기화
        this.scene = scene; // 씬 초기화
    }

    // Codec에서 사용할 메서드
    private static Entity createFromCodec(RegistryKey<EntityType> typeKey, float health, float armor, float damage) {
        /**TODO : Registries 클래스에 ENTITY_TYPE이 선언되어 있지 않아 수정하지 않음.
         * FIXME : Modify after confirmation
         *
         *
         */
        EntityType type = Objects.requireNonNull(Registries.ENTITY_TYPE.get(typeKey), "Invalid entity type key");
        Entity entity = new Entity(type);
        entity.health = health;
        entity.armor = armor;
        entity.damage = damage;
        return entity;
    }

    // 나머지 메서드들
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
        float effectiveDamage = Math.max(1, amount - armor);
        health -= effectiveDamage;

        System.out.printf("%s에게 %.2f의 피해를 입혔습니다. 현재 체력: %.2f%n", this.type.getKey(), effectiveDamage, health);

        if (health <= 0) {
            die();
        }
    }

    public void die() {
        if (isAlive) {
            isAlive = false; // 사망 상태로 변경
            System.out.printf("%s가 사망했습니다.%n", getType().getKey());
        }
    }


    @Override
    public Codec<Entity> getCodec() {
        return CODEC;
    }

    @Override
    public Entity serialize(JsonElement element) {
        return null; // 이 부분은 필요에 따라 구현
    }
}
