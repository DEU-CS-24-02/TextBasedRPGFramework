package net.biryeongtrain.text_emulator.io.loader;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.entity.EntityType;
import net.biryeongtrain.text_emulator.io.storage.ScenarioPath;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

public class EntityTypeLoader implements ContentsLoader<EntityType> {

    @Override
    public boolean loadData(Path path) {
        var typePath = ScenarioPath.getPath(path, ScenarioPath.ENTITY);
        if (Files.exists(typePath)) {
            try {
                Files.list(typePath).filter(childPath -> childPath.toString().endsWith(".json")).forEach(
                        entityTypeFile -> {
                            try {
                                var dataString = Files.readString(entityTypeFile);
                                var pair = EntityType.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(dataString)).getOrThrow();
                                var type = pair.getFirst();
                                Registry.register(getRegistry(), type.getKey(), type);

                                Main.LOGGER.info("      - Loaded entity type: {}", type.getKey());
                            } catch (Exception e) {
                                Main.LOGGER.error("Failed to load entity type: {}", entityTypeFile);
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
    public @NotNull Registry<EntityType> getRegistry() {
        return Registries.ENTITY_TYPE;
    }
}
