package net.biryeongtrain.text_emulator.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.entity.Player;
import net.biryeongtrain.text_emulator.io.loader.*;
import net.biryeongtrain.text_emulator.scenario.ScenarioMeta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class LoadManager {
    public static final Path SAVE_PATH = Paths.get("save.json");
    public static final Path SCENARIO_ROOT = Paths.get("scenarios");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Lock lock = new ReentrantLock();
    private final Set<ContentsLoader<?>> LOADERS = new ObjectArraySet<>();
    private final Set<Pair<ScenarioMeta, Path>> notSortedScenarios = new HashSet<>();
    private final Set<Pair<ScenarioMeta, Path>> sortedScenarios = new LinkedHashSet<>();
    private final Map<String, Pair<ScenarioMeta, Path>> string2Meta = new Object2ObjectOpenHashMap<>();
    private final List<String> requiredDependencies = new ArrayList<>();

    public LoadManager() {
        LOADERS.add(new ItemLoader());
        LOADERS.add(new EntityTagLoader());
        LOADERS.add(new EntityTypeLoader());
        LOADERS.add(new SceneLoader());
    }

    public Set<ScenarioMeta> getScenarioMetas() {
        return this.sortedScenarios.stream().map(Pair::first).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * 로드 순서는 아이템 -> 엔티티 태그 -> 엔티티 -> 씬 순으로 진행되어야 합니다.
     */

    public void load() {
        synchronized (lock) {
            try {
                loadAndValidateAllScenarioMeta();
                loadSaveMeta();
                if (!hasAllRequirements()) {
                    throw new IllegalStateException("Missing required dependencies");
                }
                sortScenarioMeta();

                for (Pair<ScenarioMeta, Path> pair : sortedScenarios) {
                    boolean bl1 = true;
                    Main.LOGGER.info("- {}", pair.first().id());
                    for (ContentsLoader<?> loader : this.LOADERS) {
                        // skip if load fails for any reason
                        if (!bl1 || !loader.loadData(pair.second())) {
                            Main.LOGGER.error("Failed to load data for {}", pair.first().id());
                            bl1 = false;
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Implement error handling
                Main.LOGGER.error("Failed to load data", e);
                Main.LOGGER.error("Can not start game. Exiting...");
                System.exit(1);
            }
        }
    }

    private boolean hasAllRequirements() {
        return this.requiredDependencies.stream().allMatch(this.string2Meta::containsKey);
    }

    private void loadSaveMeta() {
        try {
            if (!Files.exists(SAVE_PATH)) {
                Main.LOGGER.info("Can't find save instance file. creating new save meta... (this is not an error!");
                var meta = new SaveMeta(List.of(), new Player(), null, List.of());
                GameManager.getInstance().setSaveMeta(meta);
                return;
            }
            String saveFile = Files.readString(SAVE_PATH);
            JsonElement jsonElement = JsonParser.parseString(saveFile);
            var result = SaveMeta.CODEC.decode(JsonOps.INSTANCE, jsonElement);
            result.ifSuccess(pair -> {
                SaveMeta meta = pair.getFirst();
                Main.LOGGER.info("Loaded save meta");
                this.requiredDependencies.addAll(meta.scenarioIds());
                GameManager.getInstance().setSaveMeta(meta);
            });

            result.ifError(e -> {
                Main.LOGGER.error("Failed to load save meta");
            });
        } catch (Exception e) {
            Main.LOGGER.error("Failed to load scenario meta");
        }
    }

    public synchronized void loadAndValidateAllScenarioMeta() throws IOException {
        if (Files.exists(SCENARIO_ROOT)) {
            if (Files.isDirectory(SCENARIO_ROOT)) {
                Files.list(SCENARIO_ROOT).forEach(path -> {
                    if (!Files.isDirectory(path)) {
                        return;
                    }
                    var scenarioFile = path.resolve("scenario.json");
                    if (!Files.exists(scenarioFile)) {
                        return;
                    }
                    Main.LOGGER.info("Loading scenario : {}", path.getFileName());
                    try {
                        String string = Files.readString(scenarioFile);
                        JsonElement json = JsonParser.parseString(string);
                        var result = ScenarioMeta.CODEC.decode(JsonOps.INSTANCE, json);

                        if (result.isError()) {
                            Main.LOGGER.error("Can not decode scenario data file {}. skipping...", scenarioFile.getFileName());
                            return;
                        }

                        var pair = result.getOrThrow();
                        ScenarioMeta meta = pair.getFirst();
                        if (meta != null) {
                            Pair<ScenarioMeta, Path> pair1 = Pair.of(meta, path);
                            this.notSortedScenarios.add(pair1);
                            this.string2Meta.put(meta.id(), pair1);
                        }
                    } catch (IOException e) {
                        Main.LOGGER.error("Can not read scenario data file {}. skipping... {}", path, e.getMessage());
                    }
                });

            } else {
                throw new IllegalStateException("Scenario root must be a directory");
            }

            Main.LOGGER.info("Loaded {} scenarios", this.notSortedScenarios.size());
            this.notSortedScenarios.forEach(pair -> {
                ScenarioMeta meta = pair.first();
                Main.LOGGER.info("Scenario : {}-{} by {}", meta.id(), meta.version(), meta.author());
            });
        } else {
            try {
                Files.createDirectory(SCENARIO_ROOT);
            } catch (Exception e) {
                Main.LOGGER.error(e.getMessage());
            }
        }
    }

    public void saveAll(SaveMeta meta) {
        synchronized (lock) {
            DataResult<JsonElement> result = SaveMeta.CODEC.encodeStart(JsonOps.INSTANCE, meta);

            result.ifError(e -> {
                Main.LOGGER.error("Can not encode save meta : {}", e.message());
            });

            result.ifSuccess(jsonElement -> {
                try {
                    Files.writeString(Paths.get("save.json"), GSON.toJson(jsonElement));
                } catch (IOException e) {
                    Main.LOGGER.error("Can not save save meta : {}", e.getMessage());
                }
            });
        }
    }

    public void sortScenarioMeta() {
        this.notSortedScenarios.forEach(this::loadMeta);
    }

    private boolean loadMeta(Pair<ScenarioMeta, Path> pair) {
        var meta = pair.first();
        for (String dependency : meta.dependencies()) {
            if (this.string2Meta.containsKey(dependency)) {
                var dependencyMeta = this.string2Meta.get(dependency);
                if (this.sortedScenarios.contains(dependencyMeta)) {
                    continue;
                }
                var result = loadMeta(dependencyMeta);
                if (!result) {
                    Main.LOGGER.warn("Can not load dependency {} for {}", dependency, meta.id());
                    Main.LOGGER.warn("Skipping... {}", meta.id());
                    return false;
                }
            } else {
                Main.LOGGER.warn("Can not find dependency {} for {}", dependency, meta.id());
                Main.LOGGER.warn("Skipping... {}", meta.id());
                return false;
            }
        }
        this.sortedScenarios.add(pair);
        return true;
    }
}
