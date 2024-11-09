package net.biryeongtrain.text_emulator.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.io.Serializable;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.SerializationUtils.deserialize;

public interface Entity extends Serializable<Entity> {
    Identifier getIdentifier(); // 식별자 가져오기
    int getCommentId(); // 시스템 ID
    String getCommentName(); // 사용자 이름
    double getHealth(); // 체력
    double getAttackPower(); // 공격력
    double getDefense(); // 방어력
    String getDescription(); // 설명
    String getCommentType(); // 타입
    String getType(); // 엔티티 타입
    String getTags(); // 태그

    @Override
    default Codec<Entity> getCodec() {
        return Codec.unit(this);
    }

    @Override
    default Entity serialize(JsonElement element) {
        if (!element.isJsonObject()) {
            throw new IllegalArgumentException("Invalid JSON element for Entity serialization");
        }

        JsonObject jsonObject = element.getAsJsonObject();

        /**
         * Json 요소 로직 구현 중,각 속성을 추출하여 새로운 Entity 생성
         * @param slot
//         * @param inventory
//         */
        return deserialize(jsonObject);
    }
    //JSON 요소에서 Entity를 생성하는 메소드를 만들어 주고
    Entity deserialize(JsonObject jsonObject);

    // Entity를 JSON으로 변환하는 메소드
    JsonElement serializeToJson();

    void equip(Slot slot, Inventory inventory); // 장착
    void unequip(Slot slot); // 해제
    Inventory getEquippedItem(Slot slot); // 장착된 아이템 가져오기
    Map<Slot, Inventory> getEquippedItems(); // 장착된 아이템 목록 가져오기
}
