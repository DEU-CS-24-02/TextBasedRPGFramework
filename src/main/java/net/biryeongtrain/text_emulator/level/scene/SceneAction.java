package net.biryeongtrain.text_emulator.level.scene;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.checkerframework.checker.units.qual.C;

public record SceneAction(ActionType type,
                          Unit unit,
                          String value) {

    public static Codec<SceneAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ActionType.CODEC.fieldOf("type").forGetter(SceneAction::type),
            Unit.CODEC.fieldOf("unit").forGetter(SceneAction::unit),
            Codec.STRING.fieldOf("value").forGetter(SceneAction::value)
    ).apply(instance, SceneAction::new));

    public void execute() {
        this.type.execute(this.unit, this.value);
    }

}
