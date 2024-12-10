package net.biryeongtrain.text_emulator;

import net.biryeongtrain.text_emulator.entity.EntityTypes;
import net.biryeongtrain.text_emulator.io.LoadManager;
import net.biryeongtrain.text_emulator.item.Items;
import net.biryeongtrain.text_emulator.swing.SimpleTextRPG;
import net.biryeongtrain.text_emulator.utils.LogUtils;
import net.biryeongtrain.text_emulator.utils.Util;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static Logger LOGGER = LogUtils.getLogger();

    public static void main(String[] args) {

        int i = 0;

        new SimpleTextRPG();
        Items.register();
        EntityTypes.register();

        GameManager.getInstance().load();
        Util.shutdownExecutors();
        Main.LOGGER.info("Done!");
    }
}
