package net.biryeongtrain.text_emulator.io.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.io.storage.ScenarioPath;
import net.biryeongtrain.text_emulator.item.Item;
import net.biryeongtrain.text_emulator.item.component.ItemComponents;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ItemLoader implements ContentsLoader<Item> {

    @Override
    public boolean loadData(Path path) {
        ItemComponents.load(); // TODO Relocate
        var itemRootPath = ScenarioPath.getPath(path, ScenarioPath.ITEM);
        if (Files.exists(itemRootPath)) {
            try {
                Files.list(itemRootPath)
                        .filter(itemPath -> itemPath.toString().endsWith(".json"))
                        .forEach(itemFile -> {
                    try {
                        var dataString = Files.readString(itemFile);
                        JsonObject json = JsonParser.parseString(dataString).getAsJsonObject();
                        var dataResult = Item.CODEC.decode(JsonOps.INSTANCE, json);
                        var itemPair = dataResult.getOrThrow();
                        Identifier id = Identifier.of(json.get("id").getAsString());
                        Registry.register(getRegistry(), id, itemPair.getFirst());

                        Main.LOGGER.info("  - Loaded item: {}", id);

                    } catch (IOException e) {
                        Main.LOGGER.error("Failed to load item: {}", itemFile);
                    }
                });
            } catch (Exception e) {
                Main.LOGGER.error("Failed to load item path: {}, {}", itemRootPath, e.getMessage());
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull Registry<Item> getRegistry() {
        return Registries.ITEM;
    }
}
