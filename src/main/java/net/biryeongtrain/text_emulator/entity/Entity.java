package net.biryeongtrain.text_emulator.entity;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.io.Serializable;

public interface Entity extends Serializable<Entity> {
    @Override
    default Codec<Entity> getCodec() {
        return null;
    }

    @Override
    default Entity serialize(JsonElement element) {
        return null;
    }
}
