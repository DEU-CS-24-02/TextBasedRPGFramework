package net.biryeongtrain.text_emulator.level;

import com.mojang.datafixers.util.Pair;
import net.biryeongtrain.text_emulator.level.scene.*;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.List;

public class EndScene extends Scene {
    public EndScene(Identifier id, SceneSelector selector, List<Pair<String, String>> conversations, List<SceneDecision> decision) {
        super(id, selector, conversations, decision);
    }

    public static EndScene create() {
        return new EndScene(Identifier.ofDefault("end_scene"),
                new SceneSelector(Condition.NEVER, Operator.NONE, "", Unit.EMPTY, 0),
                Scene.conversations(List.of("이야기가 모두 끝났습니다! 아쉽네요!", "진짜 이게 다에요!")),
                List.of(new SceneDecision("이게 다라고?", "",
                        List.of(
                                new SceneAction(ActionType.PRINT, Unit.EMPTY, "네! 이게 진짜 끝이에요!"),
                                new SceneAction(ActionType.PRINT, Unit.EMPTY, "처음부터 다시 시작하세요!"),
                                new SceneAction(ActionType.PRINT, Unit.EMPTY, "당신은 막대한 데미지를 입었습니다."),
                                new SceneAction(ActionType.TAKE, Unit.HEALTH, "10000")

                        )
                ))
        );
    }
}
