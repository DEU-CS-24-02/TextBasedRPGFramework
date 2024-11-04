package net.biryeongtrain.text_emulator.item.component.type;

import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.item.component.ComponentMap;
import net.biryeongtrain.text_emulator.item.component.DataComponent;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.List;
import java.util.function.UnaryOperator;

@SuppressWarnings({"unchecked", "unused"})
public class ItemComponents {
    // TODO : add DESCRIPTION, DAMAGE, ARMOR, RARITY, DURABILITY
    public static DataComponent<Consumable> CONSUMABLE = register(Identifier.of("consumable"), (builder) -> builder.codec(Consumable.CODEC));
    public static DataComponent<List<SlotInstance>> SLOT_INSTANCE = register(Identifier.of("slot_instance"), (builder) -> builder.codec(SlotInstance.CODEC.listOf()));
    public static DataComponent<Integer> MAX_STACK_SIZE = register(Identifier.of("max_stack_size"), (builder) -> builder.codec(Codec.INT));
    public static DataComponent<List<String>> DESCRIPTION = register(Identifier.of("description"), (builder)->builder.codec(Codec.list(Codec.STRING)));
    public static DataComponent<Float> DAMAGE = register(Identifier.of("damage"), (builder) -> builder.codec(Codec.FLOAT));
    public static DataComponent<Float> ARMOR = register(Identifier.of("armor"), (builder) -> builder.codec(Codec.FLOAT));
    public static DataComponent<Rarity> RARITY = register(Identifier.of("rarity"), (builder) -> builder.codec(Rarity.CODEC));
    public static DataComponent<Integer> DURABILITY = register(Identifier.of("durability"), (builder) -> builder.codec(Codec.INT));

    public static ComponentMap DEFAULT_ITEM_COMPONENTS = ComponentMap.builder()
            .add(MAX_STACK_SIZE, 100)
            .add(RARITY, Rarity.COMMON)
            .build();

    private static <T> DataComponent<T> register(Identifier id, UnaryOperator<DataComponent.Builder<T>> operator) {
        return (DataComponent<T>) Registry.register(Registries.ITEM_COMPONENTS, id, operator.apply((DataComponent.getBuilder())).build());
    }
}
