package net.biryeongtrain.text_emulator;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.biryeongtrain.text_emulator.entity.EntityType;
import net.biryeongtrain.text_emulator.entity.Player;
import net.biryeongtrain.text_emulator.entity.damage.DamageType;
import net.biryeongtrain.text_emulator.io.LoadManager;
import net.biryeongtrain.text_emulator.io.SaveMeta;
import net.biryeongtrain.text_emulator.level.Scene;
import net.biryeongtrain.text_emulator.level.Scenes;
import net.biryeongtrain.text_emulator.level.scene.SceneAction;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.scenario.ScenarioMeta;
import net.biryeongtrain.text_emulator.swing.SimpleTextRPG;
import net.biryeongtrain.text_emulator.utils.Util;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;

/**
 * GameManager는 플레이어가 게임을 플레이 할 때 게임의 전반적인 플레이를 관리하는 클래스입니다.
 * GameManager의 인스턴스는 매 게임마다 하나여야하며, 중복될 수 없습니다.
 * GameManager는 프로그램에 실행되는게 아니라 플레이어가 게임을 플레이 할 떄 생성됩니다.
 */
public class GameManager {
    private static GameManager instance;
    public static SimpleTextRPG UI;

    private Player player;
    private volatile boolean isLoaded = false;
    private LoadManager loadManager;
    private Scene currentScene;
    private Scene nextRandomScene;
    private final RandomGenerator rndGenerator = RandomGeneratorFactory.of("L64X128MixRandom").create(ThreadLocalRandom.current().nextLong());
    private final List<Identifier> completedScenes = new ObjectArrayList<>();

    private final List<Scene> selectableScene = new ObjectArrayList<>(Registries.SCENE.size());


    private GameManager() {
        this.loadManager = new LoadManager();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public Player getPlayer() {
        return player;
    }

    public void start() {
        if (this.currentScene == null && !this.completedScenes.contains(Scenes.START_SCENE.id())) {
            this.currentScene = Scenes.START_SCENE;
        }
    }

    public void searchNextScene() {
        this.selectableScene.clear();

        Registries.SCENE.forEach(scene -> {
            boolean canSelected = scene.canSelected(this.player);
            if (canSelected) {
                instance.selectableScene.add(scene);
            }
        });
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public boolean isCompleted(Identifier id) {
        return completedScenes.contains(id);
    }

    public void selectNextRandomScene() {
        var stream = this.selectableScene
                .stream()
                .filter(Scene::isAlways);
        var optionalScene = stream.findAny();
        if (optionalScene.isPresent()) {
            this.nextRandomScene = optionalScene.get();
            return;
        }
        if (selectableScene.isEmpty()) {
            this.nextRandomScene = Scenes.END_SCENE;
            return;
        }
        this.nextRandomScene = selectableScene.get(rndGenerator.nextInt(selectableScene.size()));
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void load() {
        Util.IOExecutor.execute(() -> {
            this.loadManager.load();
            this.isLoaded = true;
        });
    }

    public void setSaveMeta(SaveMeta meta) {
        this.player = meta.player();
        this.currentScene = Registries.SCENE.get(meta.currentSceneId());
    }

    public void save() {
        if (!isLoaded) {
            throw new IllegalStateException("Game not loaded");
        }

        CompletableFuture.runAsync(() -> {
            Set<ScenarioMeta> scenarioMetaSet = loadManager.getScenarioMetas();
            Set<String> scenarioIdSet = scenarioMetaSet
                    .stream()
                    .map(ScenarioMeta::id)
                    .collect(Collectors.toUnmodifiableSet());

            SaveMeta meta = new SaveMeta(new ArrayList<>(scenarioIdSet), player, this.currentScene.id(), this.completedScenes);
            this.loadManager.saveAll(meta);
        }, Util.IOExecutor);
    }

    public void goNextScene() {
        this.currentScene = this.nextRandomScene;
        this.nextRandomScene = null;
        UI.getTextAreaPanel().goNextScene();
    }

    public void setSceneAsCompleted() {
        this.completedScenes.add(currentScene.id());
    }

    public void executeDecision(int index) {

        // TODO : give player or gameManager arg
        this.currentScene.decision().get(index).actions().forEach(SceneAction::execute);
    }

    public void shout(String... s) {
        UI.addTextSerializations(s);
    }

    public void goToScene(Identifier id) {
        this.completedScenes.add(id);
        this.nextRandomScene = Registries.SCENE.get(id);
        this.goNextScene();
        UI.getTextAreaPanel().goNextScene();
    }

    public void startCombat(EntityType type) {
        float playerDamage = this.player.getDamage() - type.getDefaultArmor();
        var entityKillChance = 100 - (type.getDefaultHealth() - playerDamage) * 10;
        float rnd = rndGenerator.nextFloat(100);
        if (rnd < entityKillChance) {
            this.shout("적을 성공적으로 처치했습니다!");
            this.player.addReputation(10);
            return;
        }
        this.shout("적을 처치하지 못했습니다.");
        player.damage(type.getDefaultDamage(), DamageType.ENTITY);
    }


    public RandomGenerator getRandom() {
        return this.rndGenerator;
    }

    public String[] getSceneTexts() {
        if (this.currentScene == null) {
            return new String[0];
        }
        return this.currentScene.getConversations().toArray(new String[0]);
    }
}
