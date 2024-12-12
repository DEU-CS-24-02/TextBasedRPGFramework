package net.biryeongtrain.text_emulator.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.item.ItemStack;
import net.biryeongtrain.text_emulator.utils.Util;

import java.util.List;

public class PlayerInventory implements Inventory {
    public static Codec<PlayerInventory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(PlayerInventory::getStacks),
            Codec.LONG.fieldOf("gold").forGetter(PlayerInventory::getGold),
            Codec.LONG.fieldOf("reputation").forGetter(PlayerInventory::getReputation)
    ).apply(instance, PlayerInventory::new));
    private final List<ItemStack> stacks;
    private long gold = 0L;
    private long reputation = 0L;
    public PlayerInventory() {
        this.stacks = new ObjectArrayList<>(20);
    }

    public int getMaxSize() {
        return 20;
    }

    private PlayerInventory(List<ItemStack> stacks, long gold, long reputation) {
        this.stacks = Util.make(new ObjectArrayList<>(stacks), list -> list.size(20));
        this.gold = gold;
        this.reputation = reputation;
    }

    @Override
    public int size() {
        return this.stacks.size();
    }

    public ImmutableList<ItemStack> getStacks() {
        return ImmutableList.copyOf(this.stacks);
    }

    public long getGold() {
        return gold;
    }

    public void addGold(long value) {
        this.gold += value;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.stacks) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= 0 && slot < this.stacks.size() ? this.stacks.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        var itemStack = this.getStack(slot);
        if (!itemStack.isEmpty()) {
            this.stacks.set(slot, ItemStack.EMPTY);
            this.markDirty();
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        var stack = this.getStack(slot);
        if (!stack.isEmpty()) {
            if (stack.getCount() <= amount) {
                this.stacks.remove(slot);
                this.markDirty();
                return stack;
            } else {
                this.markDirty();
                return stack.split(amount);
            }
        }

        return ItemStack.EMPTY;
    }

    public boolean removeStack(Item item) {
        if (this.containsAny(stack -> stack.getItem() == item)) {
            var that = this.stacks.stream().filter(stack -> stack.getItem() == item).findFirst().get();
            that.decrement(1);
            this.markDirty();
            return true;
        }
        return false;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.stacks.set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
        this.markDirty();
    }

    @Override
    public void clear() {
        this.stacks.clear();
        this.markDirty();
    }

    @Override
    public boolean addStack(ItemStack stack) {
        if (this.containsAny(stack::isSameStack)) {
            var that = this.stacks.stream().filter(stack::isSameStack).findFirst().get();
            that.increment(stack.getCount());
            return true;
        }

        if (this.size() >= this.getMaxSize()) {
            return false;
        }
        this.stacks.add(stack);
        this.markDirty();
        return true;
    }

    public void markDirty() {
        GameManager.UI.getPlayerInventoryPanel().UpdateData();
    }

    public long getReputation() {
        return reputation;
    }

    public void addReputation(long value) {
        this.reputation += value;
    }
}
