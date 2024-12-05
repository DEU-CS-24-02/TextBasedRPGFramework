package net.biryeongtrain.text_emulator.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.level.scene.SceneDecision;
import net.biryeongtrain.text_emulator.level.scene.SceneSelector;

import java.util.List;

public record Scene(SceneSelector selector, List<String> conversations, SceneDecision decision) {
    public static Codec<Scene> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SceneSelector.CODEC.fieldOf("selector").forGetter(Scene::selector),
            Codec.STRING.listOf().fieldOf("conversations").forGetter(Scene::conversations),
            SceneDecision.CODEC.fieldOf("decision").forGetter(Scene::decision)
    ).apply(instance, Scene::new));
}
