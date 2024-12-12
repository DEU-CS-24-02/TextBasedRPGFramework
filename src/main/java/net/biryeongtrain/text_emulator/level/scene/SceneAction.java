package net.biryeongtrain.text_emulator.level.scene;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.GameManager;

public record SceneAction(ActionType type,
                          Unit unit,
                          String value) {

    public static Codec<SceneAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ActionType.CODEC.fieldOf("type").forGetter(SceneAction::type),
            Unit.CODEC.fieldOf("unit").forGetter(SceneAction::unit),
            Codec.STRING.fieldOf("value").forGetter(SceneAction::value)
    ).apply(instance, SceneAction::new));

    public void execute() {
        this.type.execute(GameManager.getInstance(), this.unit, this.value);
    }

    public static class Builder {
        ActionType type;
        Unit unit;
        String value;

        private Builder() {}

        public static Builder create() {
            return new Builder();
        }

        public Builder type(ActionType type) {
            this.type = type;
            return this;
        }

        public Builder unit(Unit unit) {
            this.unit = unit;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public SceneAction build() {
            if (type == null) {
                throw new IllegalArgumentException("type cannot be null");
            }
            if (type == ActionType.TAKE || type == ActionType.GIVE) {
                unit = Unit.EMPTY;
            }

            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException("value cannot be empty");
            }
            return new SceneAction(type, unit, value);
        }
    }
}
