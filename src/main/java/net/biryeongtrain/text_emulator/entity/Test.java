package net.biryeongtrain.text_emulator.entity;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;

public class Test implements Entity{
    @Override
    public Codec<Test> getCodec() {
        return null;
    }

    @Override
    public Test serialize(JsonElement element) {
        return null;
    }
}
