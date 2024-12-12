package net.biryeongtrain.text_emulator.utils;

public class MathHelper {
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
