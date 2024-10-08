package net.biryeongtrain.text_emulator.item.component;

import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.function.UnaryOperator;

public class ItemComponents {
    public static DataComponent<Consumable> CONSUMABLE = register(Identifier.of("consumable"), (builder) -> builder.codec(Consumable.CODEC).cache());

    private static <T> DataComponent<T> register(Identifier id, UnaryOperator<DataComponent.Builder<T>> operator) {
        return (DataComponent<T>) Registry.register(Registries.ITEM_COMPONENTS, id, operator.apply((DataComponent.getBuilder())).build());
    }
}
