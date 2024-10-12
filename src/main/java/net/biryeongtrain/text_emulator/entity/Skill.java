package net.biryeongtrain.text_emulator.entity;

public class Skill {
    private final String name;
    private final int levelRequired;
    private final String type; // 예: 공격, 버프 등

    public Skill(String name, int levelRequired, String type) {
        this.name = name;
        this.levelRequired = levelRequired;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public String getType() {
        return type;
    }

    // 추가적인 메서드 (예: 스킬 효과 적용 등)를 여기에 정의할 수 있습니다.
}
