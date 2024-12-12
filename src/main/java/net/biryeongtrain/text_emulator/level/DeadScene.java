package net.biryeongtrain.text_emulator.level;

import com.mojang.datafixers.util.Pair;
import net.biryeongtrain.text_emulator.level.scene.*;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DeadScene extends Scene {
    public DeadScene(Identifier id, SceneSelector selector, List<Pair<String, String>> conversations, List<SceneDecision> decision) {
        super(id, selector, conversations, decision);
    }

    public static DeadScene create() {
        return new DeadScene(Identifier.ofDefault("dead_scene"),
                new SceneSelector(Condition.NEVER, Operator.NONE, "", Unit.EMPTY, 0),
                Scene.conversations(List.of("당신은 사망했습니다.")),
                new ArrayList<>()
        );
    }

}
