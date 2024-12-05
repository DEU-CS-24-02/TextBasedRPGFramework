package net.biryeongtrain.text_emulator.item.component;

import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.item.component.type.Consumable;
import net.biryeongtrain.text_emulator.item.component.type.Rarity;
import net.biryeongtrain.text_emulator.item.component.type.SlotInstance;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 레지스트리에 등록할 컴포넌트를 열거한 클래스입니다.
 * 컴포넌트도 동적으로 할수는 있는데 안하는 이유는 게임 시스템이랑 연관있어서 그렇습니다.
 * 시스템까지 동적으로 하면 개발 시간이 부족해요.
 */
@SuppressWarnings({"unchecked", "unused"})
public class ItemComponents {
    public static ItemComponent<Consumable> CONSUMABLE = register(Identifier.of("consumable"), (builder) -> builder.codec(Consumable.CODEC));
    public static ItemComponent<List<SlotInstance>> SLOT_INSTANCE = register(Identifier.of("slot_instance"), (builder) -> builder.codec(SlotInstance.CODEC.listOf()));
    public static ItemComponent<Integer> MAX_STACK_SIZE = register(Identifier.of("max_stack_size"), (builder) -> builder.codec(Codec.INT));
    public static ItemComponent<List<String>> DESCRIPTION = register(Identifier.of("description"), (builder)->builder.codec(Codec.list(Codec.STRING)));
    public static ItemComponent<Float> DAMAGE = register(Identifier.of("damage"), (builder) -> builder.codec(Codec.FLOAT));
    public static ItemComponent<Float> ARMOR = register(Identifier.of("armor"), (builder) -> builder.codec(Codec.FLOAT));
    public static ItemComponent<Rarity> RARITY = register(Identifier.of("rarity"), (builder) -> builder.codec(Rarity.CODEC));
    public static ItemComponent<Integer> DURABILITY = register(Identifier.of("durability"), (builder) -> builder.codec(Codec.INT));
    public static ItemComponent<Integer> UPGRADE_LEVEL = register(Identifier.of("upgrade_level"), (builder) -> builder.codec(Codec.INT));
    public static ItemComponent<Integer> PRICE = register(Identifier.of("price"),(builder) -> builder.codec(Codec.INT)); // 판매 가격 기본값
    public static ItemComponent<Float> HEAL_AMOUNT = register(Identifier.of("heal_amount"),(builder) -> builder.codec(Codec.FLOAT)); // heal_amount가 설정되어 있는 아이템은 특정 값만큼 체력 회복
    // 아이템이 마지막으로 사용된 턴, 아이템 쿨타임 지정 값 추가
    public static ItemComponent<Integer> LAST_USED_TURN = register(Identifier.of("last_used_turn"),(builder) -> builder.codec(Codec.INT));
    public static ItemComponent<Integer> COOLDOWN = register(Identifier.of("cooldown"), (builder) -> builder.codec(Codec.INT));

    public static ComponentMap DEFAULT_ITEM_COMPONENTS = ComponentMap.builder()
            .add(MAX_STACK_SIZE, 100)
            .add(RARITY, Rarity.COMMON)
            .add(COOLDOWN, 0) // 기본 쿨타임은 0으로 지정
            .add(LAST_USED_TURN, -1) // 기본 사용 턴은 -1로 설정
            .add(UPGRADE_LEVEL, 0) // 초기 강화 레벨
            .add(PRICE, 10) // 기본 판매 가격
            .build();

    private static <T> ItemComponent<T> register(Identifier id, UnaryOperator<ItemComponent.Builder<T>> operator) {
        return (ItemComponent<T>) Registry.register(Registries.ITEM_COMPONENTS, id, operator.apply((ItemComponent.getBuilder())).build());
    }
}
