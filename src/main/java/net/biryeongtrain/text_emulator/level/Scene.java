package net.biryeongtrain.text_emulator.level;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.entity.Player;
import net.biryeongtrain.text_emulator.level.scene.Condition;
import net.biryeongtrain.text_emulator.level.scene.SceneDecision;
import net.biryeongtrain.text_emulator.level.scene.SceneSelector;
import net.biryeongtrain.text_emulator.utils.collections.DefaultedList;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.Objects;

public final class Scene {
    public static Codec<Scene> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(Scene::id),
            SceneSelector.CODEC.fieldOf("selector").forGetter(Scene::selector),
            Codec.compoundList(Codec.INT, Codec.STRING).fieldOf("conversations").forGetter(Scene::conversations),
            SceneDecision.CODEC.listOf().fieldOf("decision").forGetter(Scene::decision)
    ).apply(instance, Scene::new));

    private final Identifier id;
    private final SceneSelector selector;
    private final List<Pair<Integer,String>> conversations;
    private final List<String> conversationArrayList;
    private final List<SceneDecision> decision;

    public Scene(Identifier id, SceneSelector selector, List<Pair<Integer,String>> conversations, List<SceneDecision> decision) {
        this.id = id;
        this.selector = selector;
        this.conversations = conversations;
        this.decision = decision;
        this.conversationArrayList = DefaultedList.ofSize(conversations.size(), Strings.EMPTY);
        conversations.forEach(pair -> {
            conversationArrayList.set(pair.getFirst(), pair.getSecond());
        });
    }

    public Identifier id() {
        return id;
    }

    private SceneSelector selector() {
        return selector;
    }
    public boolean isAlways() {
        return this.selector.condition() == Condition.ALWAYS;
    }

    private List<Pair<Integer,String>> conversations() {
        return ImmutableList.copyOf(conversations);
    }

    public String getConversation(int index) {
        return conversationArrayList.get(index);
    }

    public ImmutableList<String> getConversations() {
        return ImmutableList.copyOf(this.conversationArrayList);
    }

    public List<SceneDecision> decision() {
        return decision;
    }

    public boolean canSelected(Player player) {
        return selector.checkCondition(player);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Scene) obj;
        return Objects.equals(this.selector, that.selector) &&
                Objects.equals(this.conversations, that.conversations) &&
                Objects.equals(this.decision, that.decision);
    }


}
