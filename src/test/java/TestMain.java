import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.entity.Slot;
import net.biryeongtrain.text_emulator.item.ItemStack;
import net.biryeongtrain.text_emulator.item.Items;
import net.biryeongtrain.text_emulator.item.component.ComponentMap;
import net.biryeongtrain.text_emulator.item.component.ComponentMapImpl;
import net.biryeongtrain.text_emulator.item.component.type.Consumable;
import net.biryeongtrain.text_emulator.item.component.type.ItemComponents;
import net.biryeongtrain.text_emulator.item.component.type.Rarity;
import net.biryeongtrain.text_emulator.item.component.type.SlotInstance;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.biryeongtrain.text_emulator.Main.LOGGER;

public class TestMain {
    private ComponentMap map;
    private ComponentMapImpl components;
    @BeforeEach
    public void beforeTest() {
        map = ComponentMap.builder()
                .add(ItemComponents.MAX_STACK_SIZE, 100)
                .add(ItemComponents.CONSUMABLE, new Consumable(10))
                .build();

        components = new ComponentMapImpl(map);
        components.set(ItemComponents.MAX_STACK_SIZE, 1);
    }

    @Test
    @DisplayName("TestMain")
    public void testMain() {
        LOGGER.info("Start Identifier Test");
        Identifier correct = Identifier.of("you-can-do-like-this", "or_this_1_2");
        Identifier.ofDefault("test");
        // namespace : path
        Identifier incorrect = Identifier.of("you-can-do-like-this", "or_this_1_2");
        if (correct.equals(incorrect)) {
            LOGGER.info("Correct, {} {}", correct, incorrect);
        } else {
            LOGGER.info("Incorrect");
            Assertions.fail();
        }
        LOGGER.info("Identifier Test Passed");
    }

    @Test
    @DisplayName("Identifier Encode/Decode Test")
    public void IdentifierIOTest() {
        Identifier identifier = Identifier.of("you-can-do-like-this", "or_this_1_2");
        var encoded = Identifier.CODEC.encodeStart(JsonOps.INSTANCE, identifier);

        encoded.ifError(e -> {
            LOGGER.error("Error: {}", e.message());
            Assertions.fail();
        });

        if (encoded.result().isEmpty()) {
            LOGGER.error("Empty");
            Assertions.fail();
        } else {
            LOGGER.info("Encode Success");
            LOGGER.info(String.valueOf(encoded.result().get()));
        }

        var decoded = Identifier.CODEC.decode(JsonOps.INSTANCE, encoded.result().get());

        if (decoded.error().isPresent()) {
            LOGGER.error("Decode Error: {}", decoded.error().get());
            Assertions.fail();
        }

        if (decoded.result().isPresent()) {
            LOGGER.info("Decode Success");
            LOGGER.info(decoded.result().get().getFirst().toString());
        } else {
            LOGGER.error("Decode Error: Empty");
            Assertions.fail();
        }

        LOGGER.info("Identifier I/O Test Passed");
    }

    @Test
    @DisplayName("ComponentMapTest")
    public void testComponent() {
        LOGGER.info("Start Component I/O Test");
        var encoded = ComponentMap.CODEC.encodeStart(JsonOps.INSTANCE, map);
        encoded.ifError(e -> {
            LOGGER.error("Error: {}", e.message());
            Assertions.fail();
        });

        if (encoded.result().isEmpty()) {
            LOGGER.error("Empty");
            Assertions.fail();
        } else {
            LOGGER.info("Encode Success");
            LOGGER.info(String.valueOf(encoded.result().get()));
        }

        var decoded = ComponentMap.CODEC.decode(JsonOps.INSTANCE, encoded.result().get());

        if (decoded.error().isPresent()) {
            LOGGER.error("Decode Error: {}", decoded.error().get());
            Assertions.fail();
        }

        if (decoded.result().isPresent()) {
            LOGGER.info("Decode Success");

            LOGGER.info(decoded.result().get().getFirst().toString());
        } else {
            LOGGER.error("Decode Error : Empty");
            Assertions.fail();
        }

        LOGGER.info("Component I/O Test Passed");
    }

    @Test
    @DisplayName("ComponentMapImplTest")
    public void testComponentImpl() {
        LOGGER.info("Start ComponentImpl I/O Test");
        var encoded = ComponentMap.CODEC.encodeStart(JsonOps.INSTANCE, components);

        encoded.ifError(e -> {
            LOGGER.error("Encode Error: {}", e.message());
            Assertions.fail();
        });

        if (encoded.result().isEmpty()) {
            LOGGER.error("Encode Error : Empty");
            Assertions.fail();
        } else {
            LOGGER.info("Encode Success");
            LOGGER.info(encoded.result().get().toString());
        }

        var decoded = ComponentMap.CODEC.decode(JsonOps.INSTANCE, encoded.result().get());

        if (decoded.error().isPresent()) {
            LOGGER.error("Encode Error: {}", decoded.error().get());
            Assertions.fail();
        }

        if (decoded.result().isPresent()) {
            LOGGER.info("Decode Success");
            LOGGER.info(decoded.result().get().getFirst().toString());
        } else {
            LOGGER.error("DECODE ERROR: Empty");
            Assertions.fail();
        }

        int maxUsage = components.get(ItemComponents.MAX_STACK_SIZE);

        LOGGER.info("Max Usage: {}", maxUsage);
        components.set(ItemComponents.MAX_STACK_SIZE, 100);

        LOGGER.info("Max Usage: {}", components.get(ItemComponents.MAX_STACK_SIZE));
        LOGGER.info("ComponentImpl I/O Test Passed");

        // 아이템 Item.class : 종류 삽, 검 이런 개념
        // ItemStack.class : 삽, 검을 실체화 한 것 - 검의 내구도가 닳았다 하면 ItemStack의 내구도 데이터가 변경되는거
    }

    @Test
    @DisplayName("CODEC Test")
    public void codecTest() {
        TestClass testClass = new TestClass(10);

        var encoded = TestClass.CODEC.encodeStart(JsonOps.INSTANCE, testClass);
        if (encoded.isSuccess()) {
            JsonElement jsonElement = encoded.result().get();
            System.out.println(jsonElement);

            var decoded = TestClass.CODEC.decode(JsonOps.INSTANCE, jsonElement);

            if (decoded.isSuccess()) {
                Optional<Pair<TestClass, JsonElement>> result = decoded.result();
                TestClass testClass2 = result.get().getFirst();
                System.out.printf(testClass2.toString());
            }
        }
    }

    public static class TestClass {
        public static Codec<TestClass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("asdf").forGetter(TestClass::getI)
        ).apply(instance, (i) -> new TestClass(i)));


        private final int i;

        public TestClass(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }
    }

    @Test
    @DisplayName("ItemStack Codec Check")
    public void testItemStackCodec() {
        ItemStack stack = new ItemStack(Items.TEST_ITEM);

        stack.setCount(10);
        stack.set(ItemComponents.RARITY, Rarity.RARE);
        List<SlotInstance> list = new ArrayList<>();
        list.add(new SlotInstance(Slot.HEAD, 10F));
        stack.set(ItemComponents.SLOT_INSTANCE, list);

        var result = ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack);
        result.ifError(e -> Assertions.fail(e.message()));

        var json = result.result().orElseThrow(() -> new RuntimeException("ItemStack is Empty. Maybe can not decode stack?"));

        LOGGER.info("stack decoded : {}", json);

        var decodeResult = ItemStack.CODEC.decode(JsonOps.INSTANCE, json);
        decodeResult.ifError(e -> Assertions.fail(e.message()));

        var pair = decodeResult.result().orElseThrow(() -> new RuntimeException("ItemStack is Empty. Maybe can not decode stack?"));
        var decodedStack = pair.getFirst();

        Assertions.assertTrue(stack.isSameStack(decodedStack));
    }
}
