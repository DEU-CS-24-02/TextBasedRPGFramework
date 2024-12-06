package net.biryeongtrain.text_emulator.level.scene;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record SceneDecision(String text,
                            String description,
                            List<SceneAction> actions) {

    public static Codec<SceneDecision> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("text").forGetter(SceneDecision::text),
            Codec.STRING.fieldOf("description").forGetter(SceneDecision::description),
            SceneAction.CODEC.listOf().fieldOf("actions").forGetter(SceneDecision::actions)
    ).apply(instance, SceneDecision::new));

    public void createButton() {
    }
}
