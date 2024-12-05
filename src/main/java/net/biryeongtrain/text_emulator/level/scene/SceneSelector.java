package net.biryeongtrain.text_emulator.level.scene;

public class SceneSelector {
    Condition condition;
    Operator operator;
    String value;
    Unit unit;
    float weight;

    public boolean checkCondition() {
        return true;
    }
}
