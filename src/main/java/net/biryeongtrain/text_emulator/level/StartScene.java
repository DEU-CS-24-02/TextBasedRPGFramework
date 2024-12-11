package net.biryeongtrain.text_emulator.level;

import com.mojang.datafixers.util.Pair;
import net.biryeongtrain.text_emulator.level.scene.*;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StartScene extends Scene {

    private StartScene(Identifier id, SceneSelector selector, List<Pair<String, String>> conversations, List<SceneDecision> decision) {
        super(id, selector, conversations, decision);
    }

    public static StartScene create() {
        SceneSelector selector = new SceneSelector(Condition.ALWAYS, null, null, null, 10000);
        List<Pair<String, String>> conversations = new ArrayList<>();
        String[] s = {
                "게임을 시작하기 전 당신의 직업을 선택 해 주세요.",
                "전사 : 적당한 체력과 적당한 공격력을 가지고 있습니다. 100골드를 가지고 시작합니다.",
                "마법사 : 낮은 체력과 강한 공격력을 가지고 있습니다. 150골드를 가지고 시작합니다."
        };
        int i = 0;
        for (String string : s) {
            conversations.add(new Pair<>(Integer.toString(i++), string));
        }
        List<SceneDecision> decisions = getSceneDecisions();

        return new StartScene(Identifier.ofDefault("start_scene"), selector, conversations, decisions);
    }

    private static @NotNull List<SceneDecision> getSceneDecisions() {
        List<SceneDecision> decisions = new ArrayList<>();
        SceneAction action1 = new SceneAction(ActionType.GIVE, Unit.HEALTH, "50");
        SceneAction action2 = new SceneAction(ActionType.GIVE, Unit.GOLD, "100");
        SceneDecision decision1 = new SceneDecision("전사를 택한다.", "무기를 얻습니다.", List.of(action1, action2));
        decisions.add(decision1);

        SceneAction action3 = new SceneAction(ActionType.GIVE, Unit.HEALTH, "-50");
        SceneAction action4 = new SceneAction(ActionType.GIVE, Unit.GOLD, "150");
        SceneDecision decision2 = new SceneDecision("마법사를 택한다.", "한번의 공격, 한번의 순간.", List.of(action3, action4));
        decisions.add(decision2);
        return decisions;
    }
}
