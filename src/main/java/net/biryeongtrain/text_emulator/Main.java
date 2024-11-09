package net.biryeongtrain.text_emulator;
import com.mojang.serialization.DataResult;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.registry.Registry;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

public class Main {
    public static Logger LOGGER = LoggerFactory.getLogger("TextEmulator");
    public static void main(String[] args) {
        LOGGER.info(MarkerFactory.getMarker("TextEmulator"), "Hello, world!");


    }
}