package net.biryeongtrain.text_emulator.entity;

public class StatusEffect {
    private final String name;
    private final int duration; // 지속 시간

    public StatusEffect(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    // Codec 및 직렬화 로직 추가
}
