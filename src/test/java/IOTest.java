import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.entity.EntityTag;
import net.biryeongtrain.text_emulator.entity.EntityType;
import net.biryeongtrain.text_emulator.entity.LootTableInstance;
import net.biryeongtrain.text_emulator.entity.LootTableManager;
import net.biryeongtrain.text_emulator.io.LoadManager;
import net.biryeongtrain.text_emulator.io.storage.ScenarioPath;
import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.item.ItemStack;
import net.biryeongtrain.text_emulator.item.Items;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.scenario.ScenarioMeta;
import net.biryeongtrain.text_emulator.utils.Util;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class IOTest {
    @Test
    @DisplayName("PathTest")
    public void pathTest() throws ExecutionException, InterruptedException {
        String strPath = "./test";
        Path path = Path.of(strPath);

        ItemStack stack = new ItemStack(Items.AIR);
        Future<Integer> result = Util.IOExecutor.submit(() -> {
            try {
                Files.createDirectories(path);

                var json = ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).result().get();
                Files.writeString(path.resolve("test.json"), json.toString(), StandardCharsets.UTF_8);
            } catch (Throwable t) {
                Main.LOGGER.error(t.getMessage(), t);
            }
            return 1;
        });

        if (result.get() == 1) {
            Main.LOGGER.info("DONE!");
        }

        Util.shutdownExecutors();
    }

    @Test
    @DisplayName("PathTest2")
    public void pathTest2() throws ExecutionException, InterruptedException {
        Path path = Path.of("./scenarios/bbb/scenario.json");
        Path path2 = Path.of("./scenarios/ccc/scenario.json");
        Path path3 = Path.of("./scenarios/aaa/scenario.json");
        Path entityTagPath = path.getParent().resolve("entity_tag/test_tag.json");
        Path entityTypePath = path.getParent().resolve("entity/test_entity.json");
        ScenarioMeta meta = new ScenarioMeta("테스트 시나리오", "test_scenario", "1.0.0", "테스트 시나리오입니다.", List.of("biryeongtrain"), List.of("test_scenario2"));
        ScenarioMeta meta2 = new ScenarioMeta("테스트 시나리오2", "test_scenario2", "1.0.0", "테스트 시나리오입니다.", List.of("biryeongtrain"), List.of());
        ScenarioMeta meta3 = new ScenarioMeta(
                "테스트 시나리오3",
                "test_scenario3",
                "1.0.0",
                "테스트 시나리오입니다.",
                List.of("biryeongtrain"),
                List.of("test_scenario2", "test_scenario")
        );
        JsonElement json = ScenarioMeta.CODEC.encodeStart(JsonOps.INSTANCE, meta).result().get();
        JsonElement json2 = ScenarioMeta.CODEC.encodeStart(JsonOps.INSTANCE, meta2).result().get();
        JsonElement json3 = ScenarioMeta.CODEC.encodeStart(JsonOps.INSTANCE, meta3).result().get();

        var manager = new LoadManager();
        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, json.toString(), StandardCharsets.UTF_8);
            }
            if (!Files.exists(path2)) {
                Files.createDirectories(path2.getParent());
                Files.writeString(path2, json2.toString(), StandardCharsets.UTF_8);
            }
            if (!Files.exists(path3)) {
                Files.createDirectories(path3.getParent());
                Files.writeString(path3, json3.toString(), StandardCharsets.UTF_8);
            }
            var itemPath = ScenarioPath.getPath(path.getParent(), ScenarioPath.ITEM);
            if (!Files.exists(itemPath)) {
                Files.createDirectories(itemPath);
                Item item = new Item(new Item.Settings());
                var itemJson = Item.CODEC.encodeStart(JsonOps.INSTANCE, item).result().get();
                itemJson.getAsJsonObject().addProperty("id", Identifier.ofDefault("test_item").toString());
                Files.writeString(itemPath.resolve("test_item.json"), itemJson.toString(), StandardCharsets.UTF_8);
            }

            EntityTag tag = new EntityTag("TestTag", Identifier.ofDefault("test_tag"), EntityTag.Type.ENTITY);
            if (!Files.exists(entityTagPath)) {
                Files.createDirectories(entityTagPath.getParent());
                var tagJson = EntityTag.IO_CODEC.encodeStart(JsonOps.INSTANCE, tag).getOrThrow();
                Files.writeString(entityTagPath, tagJson.toString());
            }

            CompletableFuture<Void> task = CompletableFuture.runAsync(manager::load, Util.IOExecutor);
            task.get();

            // 해당 부분은 레지스트리에 파일이 등록되어야만 직렬화가 가능한 파일들입니다.
            if (!Files.exists(entityTypePath)) {
                Files.createDirectories(entityTypePath.getParent());
                var entityType = new EntityType(Identifier.ofDefault("test_entity"), 20f, 20f, 20f, new LootTableManager(List.of(LootTableInstance.EMPTY)), List.of(Registries.ENTITY_TAG.get(tag.id)));
                var entityTypeJson = EntityType.CODEC.encodeStart(JsonOps.INSTANCE, entityType).getOrThrow();
                Files.writeString(entityTypePath, entityTypeJson.toString());
            }
            Main.LOGGER.info("DONE!");
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }
}
