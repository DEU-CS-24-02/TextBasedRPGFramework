package net.biryeongtrain.text_emulator;

import net.biryeongtrain.text_emulator.entity.EntityTypes;
import net.biryeongtrain.text_emulator.item.Items;
import net.biryeongtrain.text_emulator.level.Scenes;
import net.biryeongtrain.text_emulator.utils.LogUtils;
import org.slf4j.Logger;

public class Main {
    public static Logger LOGGER = LogUtils.getLogger();

    public static void main(String[] args) {
        Items.register();
        EntityTypes.register();
        Scenes.register();
        GameManager.getInstance().load();

        Main.LOGGER.info("Done!");
    }
}
