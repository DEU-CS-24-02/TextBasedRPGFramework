package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.level.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTableManager {
    public static Codec<LootTableManager> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
            LootTableInstance.CODEC.listOf().fieldOf("lootTables").forGetter(LootTableManager::getLootTables)
    ).apply(instance, LootTableManager::new)));
    private final List<LootTableInstance> lootTables = new ArrayList<>();
    private int totalWeight;
    private LootTableManager(List<LootTableInstance> lootTables) {
        this.lootTables.addAll(lootTables);
        totalWeight = lootTables.stream().mapToInt(LootTableInstance::weight).sum();
    }

    public List<LootTableInstance> getLootTables() {
        return lootTables;
    }

    public LootTableInstance getLootTable(Scene scene) {
        Random random = scene.getRandom();
        int weight = random.nextInt(totalWeight);
        for (LootTableInstance lootTable : lootTables) {
            weight -= lootTable.weight();
            if (weight < 0) {
                return lootTable;
            }
        }
        return LootTableInstance.EMPTY;
    }
}
