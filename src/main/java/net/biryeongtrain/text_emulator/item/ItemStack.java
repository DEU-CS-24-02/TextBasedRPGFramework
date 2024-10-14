package net.biryeongtrain.text_emulator.item;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.io.Serializable;

public class ItemStack implements Serializable<ItemStack> {
    @Override
    public Codec<ItemStack> getCodec() {
        return null;
    }

    @Override
    public ItemStack serialize(JsonElement element) {
        return null;
    }
}
