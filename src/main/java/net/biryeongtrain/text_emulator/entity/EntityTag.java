package net.biryeongtrain.text_emulator.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.biryeongtrain.text_emulator.utils.collections.StringIdentifiable;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class EntityTag {
    public static Codec<EntityTag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(entityTag -> entityTag.name),
            Identifier.CODEC.fieldOf("id").forGetter(entityTag -> entityTag.id),
            Type.CODEC.fieldOf("type").forGetter(entityTag -> entityTag.type)
    ).apply(instance, EntityTag::new));

    public final String name;
    public final Identifier id;
    public final Type type;


    private EntityTag(String name, Identifier id, Type type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public enum Type implements StringIdentifiable {
        PLAYER,
        NON_PLAYER,
        ENTITY;

        public static EnumCodec<Type> CODEC = StringIdentifiable.createCodec(Type::values);
        @Override
        public String asString() {
            return this.name().toLowerCase();
        }
    }
}
