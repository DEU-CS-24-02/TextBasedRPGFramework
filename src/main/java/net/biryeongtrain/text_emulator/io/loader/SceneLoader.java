package net.biryeongtrain.text_emulator.io.loader;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.io.storage.ScenarioPath;
import net.biryeongtrain.text_emulator.level.Scene;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

public class SceneLoader implements ContentsLoader<Scene>{

    @Override
    public boolean loadData(Path path) {
        var typePath = ScenarioPath.getPath(path, ScenarioPath.SCENE);
        if (Files.exists(typePath)) {
            try {
                Files.list(typePath).filter(childPath -> childPath.toString().endsWith(".json")).forEach(
                        sceneFile -> {
                            try {
                                var dataString = Files.readString(sceneFile);
                                var pair = Scene.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(dataString)).getOrThrow();
                                var scene = pair.getFirst();
                                Registry.register(getRegistry(), scene.id(), scene);

                                Main.LOGGER.info("          - Loaded entity type: {}", scene.id());
                            } catch (Exception e) {
                                Main.LOGGER.error("Failed to load entity type: {}", sceneFile);
                                Main.LOGGER.error(e.toString());
                            }
                        }
                );
            } catch (Exception e) {
                Main.LOGGER.error("Failed to load path: {}", typePath);
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull Registry<Scene> getRegistry() {
        return Registries.SCENE;
    }
}
