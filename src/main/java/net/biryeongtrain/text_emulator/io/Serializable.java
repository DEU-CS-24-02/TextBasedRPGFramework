package net.biryeongtrain.text_emulator.io;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;

public interface Serializable<T extends Serializable<T>> {
    Codec<T> getCodec();
    T serialize(JsonElement element);
}
