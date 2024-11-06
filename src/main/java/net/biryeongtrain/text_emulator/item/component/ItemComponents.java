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

@SuppressWarnings({"unchecked", "unused"})
public class ItemComponents {
    // TODO : add DESCRIPTION, DAMAGE, ARMOR, RARITY, DURABILITY
    public static ItemComponent<Consumable> CONSUMABLE = register(Identifier.of("consumable"), (builder) -> builder.codec(Consumable.CODEC));
    public static ItemComponent<List<SlotInstance>> SLOT_INSTANCE = register(Identifier.of("slot_instance"), (builder) -> builder.codec(SlotInstance.CODEC.listOf()));
    public static ItemComponent<Integer> MAX_STACK_SIZE = register(Identifier.of("max_stack_size"), (builder) -> builder.codec(Codec.INT));
    public static ItemComponent<List<String>> DESCRIPTION = register(Identifier.of("description"), (builder)->builder.codec(Codec.list(Codec.STRING)));
    public static ItemComponent<Float> DAMAGE = register(Identifier.of("damage"), (builder) -> builder.codec(Codec.FLOAT));
    public static ItemComponent<Float> ARMOR = register(Identifier.of("armor"), (builder) -> builder.codec(Codec.FLOAT));
    public static ItemComponent<Rarity> RARITY = register(Identifier.of("rarity"), (builder) -> builder.codec(Rarity.CODEC));
    public static ItemComponent<Integer> DURABILITY = register(Identifier.of("durability"), (builder) -> builder.codec(Codec.INT));

    public static ComponentMap DEFAULT_ITEM_COMPONENTS = ComponentMap.builder()
            .add(MAX_STACK_SIZE, 100)
            .add(RARITY, Rarity.COMMON)
            .build();

    private static <T> ItemComponent<T> register(Identifier id, UnaryOperator<ItemComponent.Builder<T>> operator) {
        return (ItemComponent<T>) Registry.register(Registries.ITEM_COMPONENTS, id, operator.apply((ItemComponent.getBuilder())).build());
    }
}