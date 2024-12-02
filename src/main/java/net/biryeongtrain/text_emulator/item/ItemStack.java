package net.biryeongtrain.text_emulator.item;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.entity.Slot;
import net.biryeongtrain.text_emulator.io.Serializable;
import net.biryeongtrain.text_emulator.item.component.ComponentChanges;
import net.biryeongtrain.text_emulator.item.component.ComponentMap;
import net.biryeongtrain.text_emulator.item.component.ComponentMapImpl;
import net.biryeongtrain.text_emulator.item.component.ItemComponent;
import net.biryeongtrain.text_emulator.item.component.ItemComponents;
import net.biryeongtrain.text_emulator.item.component.type.SlotInstance;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 아이템의 실제 인스턴스인 ItemStack 입니다.
 * ItemStack은 아이템의 종류와 그 정보를 가지고고 있습니다.
 * ItemStack은 아이템의 갯수, 성질, 특성을 가지며, 수정이 이루어지는 객체입니다.
 * 플레이어가 주어진 아이템은 모두 Item 클래스가 아닌 ItemStack의 인스턴스를 반환해야합니다.
 */
public class ItemStack implements Serializable<ItemStack>, ComponentHolder {
    public static final Codec<ItemStack> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create((instance) -> instance.group(
                    Registries.ITEM.getCodec().fieldOf("id").forGetter(ItemStack::getItem),
                    Codecs.rangedInt(0, 99).fieldOf("count").forGetter(ItemStack::getCount),
                    ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(stack -> stack.components.getChanges())
            ).apply(instance, ItemStack::new)
            )
    );

    public static final ItemStack EMPTY = new ItemStack((Void) null);

    private Item base;
    final ComponentMapImpl components;
    private int count;

    private int upgradeLavel; //현재 강화 레벨
    private int maxupgradeLevel = 5; // 최대 강화 레벨

    public ItemStack(Item item) {
        this(item, 1);
    }

    public ItemStack(Item item, int count) {
        this(item, count, new ComponentMapImpl(item.getComponents()));
    }

    private ItemStack(Item item, int count, ComponentMapImpl components) {
        this.base = item;
        this.count = count;
        this.components = components;
        this.upgradeLavel = 0; // 초기 강화 레벨은 0

        this.set(ItemComponents.DAMAGE, 10.0f); // 기본 공격력
        this.set(ItemComponents.ARMOR, 5.0f); // 기본 방어력
    }

    public boolean canUpgrade() {
        return this.upgradeLavel < this.maxupgradeLevel; // 현재 레벨이 최대 강화 레벨보다 낮아야 강화 가능
    }

    private ItemStack(Item item, int count, ComponentChanges changes) {
        this(item, count, ComponentMapImpl.create(item.getComponents(), changes));
    }

    private ItemStack(@Nullable Void v) {
        this.base = null;
        this.components = new ComponentMapImpl(ComponentMapImpl.EMPTY);
    }

    @Override
    public Codec<ItemStack> getCodec() {
        return CODEC;
    }

    @Override
    public ItemStack serialize(JsonElement element) {
        var test = CODEC.decode(JsonOps.INSTANCE, element);
        return test.result().get().getFirst();
    }

    public boolean isEmpty() {
        return this == EMPTY || this.count <= 0;
    }

    public Item getItem() {
        return this.base;
    }

    public int getCount() {
        return this.count;
    }

    public int getUpgradeLavel() { return this.upgradeLavel; }

    /**
     * 해당 ItemStack 이 Item 인스턴스와 같은지 확인합니다.
     * @param item 비교하려는 아이템
     * @return 같은 아이템이면 {@code true}
     */
    public boolean isOf(Item item) {
        return this.base == item;
    }

    public <T> void set(ItemComponent<T> type, T value) {
        this.components.set(type, value);
    }

    @Override
    public ComponentMap getComponents() {
        return (!this.isEmpty() ? this.components : ComponentMap.EMPTY);
    }


    public void shrink() {
        this.count--;
        if (this.count <= 0) {
            this.base = Items.AIR;
        }
    }

    public void setCount(int i) {
        this.count = Math.min(this.getMaxCount(), i);

        if (this.count <= 0) {
            this.base = Items.AIR;
        }
    }

    public int getMaxCount() {
        return this.contains(ItemComponents.MAX_STACK_SIZE) ? this.get(ItemComponents.MAX_STACK_SIZE) : ItemComponents.DEFAULT_ITEM_COMPONENTS.get(ItemComponents.MAX_STACK_SIZE);
    }

    public boolean isSameStack(ItemStack other) {
        boolean bl1 = other.components.equals(this.components);
        boolean bl2 = other.base == this.base; // must == check. all item class must be Singleton.
        boolean bl3 = other.count == this.count;
        return bl1 && bl2 && bl3;
    }

    public boolean isEquipable() {
        return this.contains(ItemComponents.SLOT_INSTANCE);
    }

    public float getEquipmentValue(Slot slot) {
        var instance = this.get(ItemComponents.SLOT_INSTANCE);
        if (instance == null) {
            return 0.0F;
        }

        var optional = instance.stream()
                .filter(slotInstance -> slotInstance.slot() == slot)
                .findFirst();

        return optional.map(SlotInstance::value).orElse(0.0F);

    }

    public void equip(Slot slot) {
        // TODO : EQUIP LOGIC
    }

    // 아이템 쿨타임 관련 메서드
    public boolean canUse(int currentTurn) {
        int lastUsedTurn = this.getOrDefault(ItemComponents.LAST_USED_TURN, -1);
        int cooldown = this.getOrDefault(ItemComponents.COOLDOWN, 0);
        return currentTurn > lastUsedTurn + cooldown;
    }

    public boolean useWithCooldown(int currentTurn) {
        if (!canUse(currentTurn)) {
            return false; // 쿨타임이 끝나지 않았으면 false를 반환 합니다.
        }
        this.set(ItemComponents.LAST_USED_TURN, currentTurn);
        return true;
    }

    // 아이템 강화 관련 메서드
    private void updateStats() {

        float damageBase = this.getOrDefault(ItemComponents.DAMAGE, 0.0f);
        float armorBase = this.getOrDefault(ItemComponents.ARMOR, 0.0f);

        this.set(ItemComponents.DAMAGE, damageBase + (this.upgradeLavel * 2.0f)); // 공격력 증가량
        this.set(ItemComponents.ARMOR, armorBase + (this.upgradeLavel * 1.0f)); // 방어력 증가량
    }

    public boolean upgrade(float success) {
        // 현재 강화 레벨이 최대 강화 레벨보다 높을 경우
        if (!canUpgrade()) {
            System.out.println("이미 최대 강화 레벨입니다.");
            return false;
        }

        // 0.0 ~ 1.0 사이의 난수 생성
        float random = (float) Math.random();
        if (random < success) {
            this.upgradeLavel++; // 현재 강화레벨 + 1
            updateStats(); // 강화 성공시 스탯 갱신
            System.out.println("강화 성공! 현재 강화 레벨 : " + this.upgradeLavel);
            return true;
        }
        else {
            System.out.println("강화 실패...");
            return false;
        }
    }
}
