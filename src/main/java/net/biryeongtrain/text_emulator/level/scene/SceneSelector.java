package net.biryeongtrain.text_emulator.level.scene;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SceneSelector(Condition condition,
                            Operator operator,
                            String value,
                            Unit unit,
                            float weight) {

    public static Codec<SceneSelector> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Condition.CODEC.fieldOf("condition").forGetter(SceneSelector::condition),
            Operator.CODEC.fieldOf("operator").forGetter(SceneSelector::operator),
            Codec.STRING.fieldOf("value").forGetter(SceneSelector::value),
            Unit.CODEC.fieldOf("unit").forGetter(SceneSelector::unit),
            Codec.FLOAT.fieldOf("weight").forGetter(SceneSelector::weight)
    ).apply(instance, SceneSelector::new));


    public boolean checkCondition() {
        return true;
    }
}
