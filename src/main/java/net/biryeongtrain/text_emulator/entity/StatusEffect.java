package net.biryeongtrain.text_emulator.entity;
public class StatusEffect {
    private String name;
    private int duration; // 상태 효과 지속 시간

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

    public void decrementDuration() {
        if (duration > 0) {
            duration--;
        }
    }

    public boolean isExpired() {
        return duration <= 0;
    }
}