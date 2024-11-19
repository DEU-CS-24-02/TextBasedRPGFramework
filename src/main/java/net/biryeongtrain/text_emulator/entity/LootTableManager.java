package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;

public class LootTableManager {
    public static Codec<LootTableManager> CODEC;
    private final List<LootTableInstance> lootTables = new ArrayList<>();
}
