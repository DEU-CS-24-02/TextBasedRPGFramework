package net.biryeongtrain.text_emulator;

import net.biryeongtrain.text_emulator.io.LoadManager;
import net.biryeongtrain.text_emulator.swing.SimpleTextRPG;
import net.biryeongtrain.text_emulator.utils.LogUtils;
import net.biryeongtrain.text_emulator.utils.Util;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static Logger LOGGER = LogUtils.getLogger();

    public static void main(String[] args) {
        LoadManager manager = new LoadManager();
        CompletableFuture<Void> future = CompletableFuture.runAsync(manager::load, Util.IOExecutor);
        int i = 0;
        while(!future.isDone()) {
            // TODO : Add loading animation
        }

        new SimpleTextRPG();

        Util.shutdownExecutors();
        Main.LOGGER.info("Done!");
    }
}
