package net.biryeongtrain.text_emulator.item;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.biryeongtrain.text_emulator.io.Serializable;
import net.biryeongtrain.text_emulator.item.component.ComponentMap;
import net.biryeongtrain.text_emulator.item.component.DataComponent;
import net.biryeongtrain.text_emulator.item.component.type.ItemComponents;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * 아이템과 아이템 스택은 구별됩니다.
 * 아이템 : 아이템의 종류와 그에 따른 기본적인 Component 를 가지고 있습니다. 이 데이터를 참조하여 ItemStack이 생성됩니다. <br></br>
 * 아이탬 스택 : 실질적으로 플레이어에게 보여지는 객체이며, 아이템을 기반으로 아이템의 종류, 성질을 가집니다. <br></br>
 * 아이템 스택은 실질적으로 아이템 갯수, 성질, 특성을 가지며, 수정이 이루어지는 객체입니다. <br></br>
 * 사용자는 아이템을 추가할 때, 이 아이템 클래스를 추가하는 것이지, 아이탬 스택을 추가하는게 아닌 것을 꼭 숙지 부탁드립니다. <br></br>
 * 다만, 세이브 등에서는 아이템이 아닌 아이템 스택을 저장합니다. 그 이유는 아이템 스택이 실질적인 아이템 요소이기 때문입니다.
 */

public class Item {
    private final ComponentMap components;

    public Item(Settings settings) {
        this.components = settings.getValidateComponents();
    }

    public static Identifier getId(Item item) {
        return Registries.ITEM.getId(item);
    }

    public ComponentMap getComponents() {
        return components;
    }

    public int getMaxCount() {
        return this.components.getOrDefault(ItemComponents.MAX_STACK_SIZE, 1);
    }

    /**
     * 해당 클래스는 아이템의 기본 컴포넌트를 쉽게 정의하기 위한 빌더 클래스입니다.
     * 이 빌더 클래스를 이용하여 아이템 클래스를 제작해야합니다.
     */
    public static class Settings {
        private static final Interner<ComponentMap> COMPONENT_MAP_INTERNER = Interners.newStrongInterner();
        @Nullable
        private ComponentMap.Builder components;

        public <T> Settings component(DataComponent<T> type, T value) {
            if (this.components == null) {
                this.components = ComponentMap.builder().addAll(ItemComponents.DEFAULT_ITEM_COMPONENTS);
            }
            this.components.add(type, value);
            return this;
        }

        public Settings setMaxCount(int maxCount) {
            this.component(ItemComponents.MAX_STACK_SIZE, maxCount);
            return this;
        }

        private ComponentMap getComponents() {
            if (this.components == null) {
                return ItemComponents.DEFAULT_ITEM_COMPONENTS;
            }
            return COMPONENT_MAP_INTERNER.intern(this.components.build());
        }

         ComponentMap getValidateComponents() {
            return this.getComponents();
        }

    }
}
