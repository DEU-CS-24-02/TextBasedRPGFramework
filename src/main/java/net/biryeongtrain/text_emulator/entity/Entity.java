package net.biryeongtrain.text_emulator.entity;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.io.Serializable;

public interface Entity extends Serializable<Entity> {
    int getHealth();
    int getAttackPower();
    int getDefense();
    int getLevel();
    // 상태이상 관련 메서드 정의
    boolean hasStatusEffect(String effectType);

    @Override
    default Codec<Entity> getCodec() {
        return null;
    }

    @Override
    default Entity serialize(JsonElement element) {
        return null;
    }
}
