import com.mojang.serialization.JsonOps;
import net.biryeongtrain.text_emulator.item.component.ComponentMap;
import net.biryeongtrain.text_emulator.item.component.ComponentMapImpl;
import net.biryeongtrain.text_emulator.item.component.type.Consumable;
import net.biryeongtrain.text_emulator.item.component.type.ItemComponents;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        LOGGER.info("ComponentImpl I/O Test Passed");
    }
}
