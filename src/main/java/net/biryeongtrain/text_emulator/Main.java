package net.biryeongtrain.text_emulator;

import net.biryeongtrain.text_emulator.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import java.util.HashMap;
import java.util.Map;
public class Main {
    public static Logger LOGGER = LoggerFactory.getLogger("TextEmulator");
    public static void main(String[] args) {
        LOGGER.info(MarkerFactory.getMarker("TextEmulator"), "Hello, world!");
        var item = Registries.ITEM;
        // 플레이어 캐릭터 및 몬스터 생성 예시
        HashMap<String, Boolean> playerStatusEffects = new HashMap<>();
        playerStatusEffects.put("burn", false);
        playerStatusEffects.put("shock", false);
    }
}