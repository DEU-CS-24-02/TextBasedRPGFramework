package net.biryeongtrain.text_emulator.io.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.entity.EntityTag;
import net.biryeongtrain.text_emulator.io.storage.ScenarioPath;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EntityTagLoader implements ContentsLoader<EntityTag> {
    @Override
    public boolean loadData(Path path) {
        var entityTagRoot = ScenarioPath.getPath(path, ScenarioPath.ENTITY_TAG);
        if (Files.exists(entityTagRoot)) {
            try {
                Files.list(entityTagRoot).filter(childPath -> childPath.toString().endsWith(".json")).forEach(
                        entityTagFile -> {
                            try {
                                var dataString = Files.readString(entityTagFile);
                                JsonObject json = JsonParser.parseString(dataString).getAsJsonObject();
                                var pair = EntityTag.IO_CODEC.decode(JsonOps.INSTANCE, json).getOrThrow();
                                var tag = pair.getFirst();
                                Registry.register(getRegistry(), tag.id, tag);
                                Main.LOGGER.info("  - Loaded entity tag: {}", tag.id);
                            } catch (Exception e) {
                                Main.LOGGER.error("Failed to load entity tag: {}", entityTagFile);
                                Main.LOGGER.error(e.toString());
                            }
                        }

                );
            } catch (IOException e) {
                Main.LOGGER.error("Failed to load entity tag path: {}", entityTagRoot);
                Main.LOGGER.error(e.getMessage());
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull Registry<EntityTag> getRegistry() {
        return Registries.ENTITY_TAG;
    }
}
