package net.biryeongtrain.text_emulator.item.component.type;

import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.item.component.ComponentMap;
import net.biryeongtrain.text_emulator.item.component.DataComponent;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.function.UnaryOperator;

public class ItemComponents {
    // TODO : add DESCRIPTION, DAMAGE, ARMOR, RARITY, DURABILITY
    public static DataComponent<Consumable> CONSUMABLE = register(Identifier.of("consumable"), (builder) -> builder.codec(Consumable.CODEC));
    public static DataComponent<Equipable> EQUIPABLE = register(Identifier.of("equipable"), (builder) -> builder.codec(Equipable.CODEC));
    public static DataComponent<Integer> MAX_STACK_SIZE = register(Identifier.of("max_stack_size"), (builder) -> builder.codec(Codec.INT));
    public static ComponentMap DEFAULT_ITEM_COMPONENTS = ComponentMap.builder().add(MAX_STACK_SIZE, 100).build();

    private static <T> DataComponent<T> register(Identifier id, UnaryOperator<DataComponent.Builder<T>> operator) {
        return (DataComponent<T>) Registry.register(Registries.ITEM_COMPONENTS, id, operator.apply((DataComponent.getBuilder())).build());
    }
}
