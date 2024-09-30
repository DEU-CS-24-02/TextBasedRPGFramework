package net.biryeongtrain.text_emulator;

import net.biryeongtrain.text_emulator.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

public class Main {
    public static Logger LOGGER = LoggerFactory.getLogger("TextEmulator");
    public static void main(String[] args) {
        LOGGER.info(MarkerFactory.getMarker("TextEmulator"), "Hello, world!");
        var item = Registries.ITEM;
    }
}