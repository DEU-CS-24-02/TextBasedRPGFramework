package net.biryeongtrain.text_emulator.item;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.io.Serializable;

public class Item implements Serializable<Item> {

    @Override
    public Codec<Item> getCodec() {
        return null;
    }

    @Override
    public Item serialize(JsonElement element) {
        return null;
    }

    public class Settings {

        public Settings setMaxCount(int maxCount) {
            return this;
        }
    }
}
