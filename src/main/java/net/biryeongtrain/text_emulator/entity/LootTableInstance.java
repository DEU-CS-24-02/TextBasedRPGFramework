package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.item.ItemStack;

import java.util.List;

public record LootTableInstance(int weight, List<ItemStack> items) {
    public static LootTableInstance EMPTY = new LootTableInstance(0, List.of(ItemStack.EMPTY));
    public static Codec<LootTableInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("weight").forGetter(LootTableInstance::weight),
            ItemStack.CODEC.listOf().fieldOf("items").forGetter(LootTableInstance::items)
    ).apply(instance, LootTableInstance::new));
}
