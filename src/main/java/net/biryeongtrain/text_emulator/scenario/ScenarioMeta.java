package net.biryeongtrain.text_emulator.scenario;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.List;

public record ScenarioMeta(String name, String id, String version, String description,
                           List<String> author, List<String> dependencies) {
    public static final Codec<ScenarioMeta> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ScenarioMeta::name),
            Codec.STRING.fieldOf("id").forGetter(ScenarioMeta::id),
            Codec.STRING.fieldOf("version").forGetter(ScenarioMeta::version),
            Codec.STRING.fieldOf("description").forGetter(ScenarioMeta::description),
            Codec.list(Codec.STRING).fieldOf("author").forGetter(ScenarioMeta::author),
            Codec.list(Codec.STRING).fieldOf("dependencies").forGetter(ScenarioMeta::dependencies)
            ).apply(instance, ScenarioMeta::new)
    );


}
