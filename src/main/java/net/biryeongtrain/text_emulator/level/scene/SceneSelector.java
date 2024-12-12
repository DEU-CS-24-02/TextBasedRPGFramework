package net.biryeongtrain.text_emulator.level.scene;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.entity.Player;

public record SceneSelector(Condition condition,
                            Operator operator,
                            String value,
                            Unit unit,
                            int weight) {

    public static Codec<SceneSelector> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Condition.CODEC.fieldOf("condition").forGetter(SceneSelector::condition),
            Operator.CODEC.fieldOf("operator").forGetter(SceneSelector::operator),
            Codec.STRING.fieldOf("value").forGetter(SceneSelector::value),
            Unit.CODEC.fieldOf("unit").forGetter(SceneSelector::unit),
            Codec.INT.fieldOf("weight").forGetter(SceneSelector::weight)
    ).apply(instance, SceneSelector::new));


    public boolean checkCondition(Player player) {
        return this.weight != 0 && this.condition.check(this.operator, this.unit, this.value);
    }

    public static class Builder {
        Condition condition;
        Operator operator;
        String value;
        Unit unit;
        int weight = 0;

        private Builder() {}

        public static Builder create() {
            return new Builder();
        }

        public Builder condition(Condition condition) {
            this.condition = condition;
            return this;
        }


        public Builder operator(Operator operator) {
            this.operator = operator;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder unit(Unit unit) {
            this.unit = unit;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public SceneSelector build() {
            if (this.condition == null) {
                throw new IllegalArgumentException("Condition must be set");
            }
            if (condition == Condition.ALWAYS || condition == Condition.NEVER) {
                operator = Operator.NONE;
                unit = Unit.EMPTY;
                value = "";
                if (condition == Condition.NEVER) {
                    weight = 0;
                }
            } else {
                if (operator == null || operator == Operator.NONE) {
                    throw new IllegalArgumentException("Operator must be set");
                }
                if (unit == null || unit == Unit.EMPTY) {
                    throw new IllegalArgumentException("Unit must be set");
                }
            }
            return new SceneSelector(condition, operator, value, unit, weight);
        }
    }
}
