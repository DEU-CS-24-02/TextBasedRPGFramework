package net.biryeongtrain.text_emulator.item;

import net.biryeongtrain.text_emulator.item.component.ItemComponents;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

/**
 * "꼭" 존재해야하는 아이템만 열거된 아이템 클래스입니다.
 * 다른 아이템들은 동적으로 로드 되기 떄문에 따로 등록하지 않아도 됩니다.
 */
public class Items {
    public static Item AIR = Registry.register(Registries.ITEM, Identifier.ofDefault("air"), new Item(new Item.Settings()));
    public static Item TEST_ITEM = Registry.register(Registries.ITEM, Identifier.ofDefault("test_item"), new Item(new Item.Settings().setMaxCount(33).component(ItemComponents.MAX_STACK_SIZE, 32)));
}
