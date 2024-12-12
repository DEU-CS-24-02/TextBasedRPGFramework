package net.biryeongtrain.text_emulator.level.scene;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.swing.TextAreaPanel;

import java.util.ArrayList;
import java.util.List;

public record SceneDecision(String text,
                            String description,
                            List<SceneAction> actions) {

    public static Codec<SceneDecision> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("text").forGetter(SceneDecision::text),
            Codec.STRING.fieldOf("description").forGetter(SceneDecision::description),
            SceneAction.CODEC.listOf().fieldOf("actions").forGetter(SceneDecision::actions)
    ).apply(instance, SceneDecision::new));


    public static class Builder {
        String display;
        List<SceneAction> actions = new ArrayList<>();

        private Builder() {}

        public static Builder create() {
            return new Builder();
        }

        public Builder display(String text) {
            this.display = text;
            return this;
        }

        public Builder actions(SceneAction... actions) {
            this.actions.addAll(List.of(actions));
            return this;
        }

        public SceneDecision build() {
            return new SceneDecision(display, "", actions);
        }
    }


}
