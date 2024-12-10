package net.biryeongtrain.text_emulator.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.entity.Player;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.List;

public record SaveMeta(List<String> scenarioIds, Player player, Identifier currentSceneId, List<Identifier> completedScenes) {
    public static Codec<SaveMeta> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("scenarioIds").forGetter(SaveMeta::scenarioIds),
            Player.CODEC.fieldOf("player").forGetter(SaveMeta::player),
            Identifier.CODEC.fieldOf("currentSceneId").forGetter(SaveMeta::currentSceneId),
            Identifier.CODEC.listOf().fieldOf("completedScenes").forGetter(SaveMeta::completedScenes)
    ).apply(instance, SaveMeta::new));
}
