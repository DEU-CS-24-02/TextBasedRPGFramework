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

}