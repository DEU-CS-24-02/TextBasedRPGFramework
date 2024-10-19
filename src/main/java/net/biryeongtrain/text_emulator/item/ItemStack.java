package net.biryeongtrain.text_emulator.item;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.io.Serializable;
import net.biryeongtrain.text_emulator.item.component.ComponentChanges;
import net.biryeongtrain.text_emulator.item.component.ComponentMap;
import net.biryeongtrain.text_emulator.item.component.ComponentMapImpl;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.Codecs;
import org.jetbrains.annotations.Nullable;

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

    private final Item base;
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
        return null;
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

    public boolean isOf(Item item) {
        return this.base == item;
    }


    @Override
    public ComponentMap getComponents() {
        return (!this.isEmpty() ? this.components : ComponentMap.EMPTY);
    }
}
