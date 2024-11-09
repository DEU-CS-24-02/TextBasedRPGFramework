package net.biryeongtrain.text_emulator.entity;

import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.item.ItemStack;
import net.biryeongtrain.text_emulator.utils.Clearable;

import java.util.Set;
import java.util.function.Predicate;

/**
 * 인벤토리 인터페이스입니다.
 * 상황에 따라 필요한 인벤토리가 존재하면 해당 클래스를 상속받아 사용하세요.
 */
public interface Inventory extends Clearable {
    int size();
    boolean isEmpty();
    ItemStack getStack(int slot);
    ItemStack removeStack(int slot);
    ItemStack removeStack(int slot, int amount);
    void setStack(int slot, ItemStack stack);
    default int getMaxCountPerStack() {
        return 99;
    }
    default int getMaxCount(ItemStack stack) {
        return Math.min(this.getMaxCountPerStack(), stack.getCount());
    }
    default boolean isValid(int slot, ItemStack stack) {
        return true;
    }

    default int count(Item item) {
        int i = 0;

        for (int j = 0; j < size(); ++j) {
            ItemStack stack = this.getStack(j);
            if (stack.getItem().equals(item)) {
                i += stack.getCount();
            }
        }

        return i;
    }

    default boolean containsAny(Set<ItemStack> items) {
        return this.containsAny((stack) -> !stack.isEmpty() && items.contains(stack));
    }

    default boolean containsAny(Predicate<ItemStack> predicate) {
        for (int i = 0; i < size(); ++i) {
            if (predicate.test(getStack(i))) {
                return true;
            }
        }

        return false;
    }
}