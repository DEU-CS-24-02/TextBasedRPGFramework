import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.entity.*;
import net.biryeongtrain.text_emulator.io.LoadManager;
import net.biryeongtrain.text_emulator.io.storage.ScenarioPath;
import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.item.ItemStack;
import net.biryeongtrain.text_emulator.item.Items;
import net.biryeongtrain.text_emulator.item.component.ItemComponents;
import net.biryeongtrain.text_emulator.item.component.type.Rarity;
import net.biryeongtrain.text_emulator.level.Scene;
import net.biryeongtrain.text_emulator.level.scene.*;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.scenario.ScenarioMeta;
import net.biryeongtrain.text_emulator.utils.LogUtils;
import net.biryeongtrain.text_emulator.utils.Util;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class IOTest {
    public static Logger LOGGER = LogUtils.getLogger();

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
        Path scenePath = path.getParent().resolve("scene/test_scene.json");
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
                Item item = new Item(new Item.Settings(), "테스트");
                var itemJson = Item.CODEC.encodeStart(JsonOps.INSTANCE, item).result().get();
                itemJson.getAsJsonObject().addProperty("id", Identifier.ofDefault("test_item_datagen").toString());
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

            if (!Files.exists(scenePath)) {
                Files.createDirectories(scenePath.getParent());
                var selector = new SceneSelector(Condition.IF, Operator.BELOW, "50", Unit.GOLD, 100);
                var decision1 = new SceneDecision("테스트1", "fancy", List.of(new SceneAction(ActionType.PRINT, Unit.EMPTY, "테스트1")));
                var scene = new Scene(Identifier.ofDefault("test_scene"), selector, Scene.conversations(List.of("테스트1", "테스트2", "테스트3")), List.of(decision1));
                var sceneResult = Scene.CODEC.encodeStart(JsonOps.INSTANCE, scene);
                var sceneJson = sceneResult.result().get();
                Files.writeString(scenePath, sceneJson.toString());
            }

            Main.LOGGER.info("DONE!");
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void TestInventory() {
        Items.register();
        EntityTypes.register();
        var inventory = new PlayerInventory();
        var codec = PlayerInventory.CODEC;
        var json = codec.encodeStart(JsonOps.INSTANCE, inventory).result().get();
        var result = codec.decode(JsonOps.INSTANCE, json).result().get().getFirst();
        LOGGER.info(json.toString());
    }

    @Test
    public void generateScenario() {
        var path = LoadManager.SCENARIO_ROOT;
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        var scenarioRootPath = path.resolve("first_test_scenario");
        var scenarioPath = scenarioRootPath.resolve("test_scenario.json");
        var itemPath = scenarioRootPath.resolve("item").resolve("magic_potion.json");
        var item2Path = scenarioRootPath.resolve("item").resolve("strange_key.json");
        var entityPath = scenarioRootPath.resolve("entity").resolve("grunt.json");
        var sceneRootPath = scenarioRootPath.resolve("scene");
        var scenePath = sceneRootPath.resolve("potion_giveaway_scene.json");
        var scene2Path = sceneRootPath.resolve("get_strange_key.json");
        ScenarioMeta meta = new ScenarioMeta(
                "시연용 시나리오",
                "showcase_scenario",
                "1.0.0",
                "시연용 시나리오입니다.",
                List.of("biryeongtrain"),
                List.of()
        );
        Item item = new Item(new Item.Settings()
                .setMaxCount(99)
                .component(ItemComponents.HEAL_AMOUNT, 100f)
                .component(ItemComponents.RARITY, Rarity.ADVANCED)
                , "마법의 포션");
        Item item2 = new Item(new Item.Settings()
                .setMaxCount(1)
                .component(ItemComponents.RARITY, Rarity.COMMON)
                , "이상한 열쇠"
        );
        Registry.register(Registries.ITEM, Identifier.of("showcase_scenario", "magic_potion"), item);
        Registry.register(Registries.ITEM, Identifier.of("showcase_scenario", "strange_key"), item2);

        EntityType entityType = new EntityType(
                Identifier.of("showcase_scenario", "grunt"),
                5f,
                10f,
                0f,
                new LootTableManager(List.of(new LootTableInstance(100, List.of(new ItemStack(item))))),
                List.of()
        );
        Registry.register(Registries.ENTITY_TYPE, entityType.getKey(), entityType);

        Scene giveawayScene = new Scene(
                Identifier.of("showcase_scenario", "potiongiveaway_scene"),
                new SceneSelector(Condition.IF, Operator.BELOW, "100", Unit.HEALTH, 100),
                Scene.conversations(List.of(
                        "\"이봐! 잠시 와보게나!\"",
                        "멀지 않은 곳에서 누군가가 당신을 부릅니다.",
                        "\"여행자로 보이는데, 이 포션을 받게나. 도움이 될거야.\"",
                        "그는 당신에게 마법의 포션을 건넵니다",
                        "\"포션을 받을까요?\""
                )),
                List.of(new SceneDecision("포션 받기", "fancy", List.of(
                                new SceneAction(ActionType.GIVE, Unit.ITEM, Registries.ITEM.getId(item).toString()),
                                new SceneAction(ActionType.PRINT, Unit.EMPTY, "포션을 받았습니다."),
                                new SceneAction(ActionType.GOTO, Unit.EMPTY, "random")
                        )),
                        new SceneDecision("포션 받지 않기", "fancy", List.of(
                                new SceneAction(ActionType.PRINT, Unit.EMPTY, "포션을 받지 않았습니다."),
                                new SceneAction(ActionType.GOTO, Unit.EMPTY, "random")
                        ))
                )
        );
        Scene scene = new Scene(
                Identifier.of("showcase_scenario", "get_strange_key"),
                new SceneSelector(Condition.ALWAYS, Operator.NONE, "0", Unit.EMPTY, 100),
                Scene.conversations(List.of(
                        "지루한 걸음이 계속되는 와중에 당신은 녹슨 열쇠를 찾았습니다.",
                        "귀중한 열쇠일까요? 녹슬은 것을 보아하니 그런 것 같지 않습니다."
                )),
                List.of(new SceneDecision("열쇠 줍기", "fancy", List.of(
                                new SceneAction(ActionType.PRINT, Unit.EMPTY, "당신은 어딘가 쓸모있겠지 라고 생각하며 열쇠를 가방에 넣었습니다."),
                                new SceneAction(ActionType.GIVE, Unit.ITEM, Registries.ITEM.getId(item2).toString()),
                                new SceneAction(ActionType.GOTO, Unit.EMPTY, "random")
                        )),
                        new SceneDecision("열쇠 버리기", "fancy", List.of(
                                new SceneAction(ActionType.PRINT, Unit.EMPTY, "굳이 쓸 모 없어보이는 녹슨 열쇠를 가방에 넣을 필요가 있을까요?"),
                                new SceneAction(ActionType.PRINT, Unit.EMPTY, "당신은 열쇠를 버렸습니다."),
                                new SceneAction(ActionType.GOTO, Unit.EMPTY, "random")
                        ))
                        )
        );

        Registry.register(Registries.SCENE, giveawayScene.id(), giveawayScene);
        Registry.register(Registries.SCENE, scene.id(), scene);
        if (!Files.exists(scenarioPath)) {
            try {
                Files.createDirectories(scenarioPath.getParent());
                var scenarioJson = ScenarioMeta.CODEC.encodeStart(JsonOps.INSTANCE, meta).result().get();
                Files.writeString(scenarioPath, scenarioJson.toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (!Files.exists(itemPath)) {
            try {
                Files.createDirectories(itemPath.getParent());
                var itemJson = Item.CODEC.encodeStart(JsonOps.INSTANCE, item).result().get();
                Files.writeString(itemPath, itemJson.toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (!Files.exists(item2Path)) {
            try {
                var itemJson = Item.CODEC.encodeStart(JsonOps.INSTANCE, item2).result().get();
                Files.writeString(item2Path, itemJson.toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (!Files.exists(entityPath)) {
            try {
                Files.createDirectories(entityPath.getParent());
                var entityJson = EntityType.CODEC.encodeStart(JsonOps.INSTANCE, entityType).result().get();
                Files.writeString(entityPath, entityJson.toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (!Files.exists(scenePath)) {
            try {
                Files.createDirectories(scenePath.getParent());
                var sceneJson = Scene.CODEC.encodeStart(JsonOps.INSTANCE, giveawayScene).result().get();
                Files.writeString(scenePath, sceneJson.toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        if (!Files.exists(scene2Path)) {
            try {
                var sceneJson = Scene.CODEC.encodeStart(JsonOps.INSTANCE, scene).result().get();
                Files.writeString(scene2Path, sceneJson.toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
