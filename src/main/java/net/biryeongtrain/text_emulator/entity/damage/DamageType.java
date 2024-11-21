package net.biryeongtrain.text_emulator.entity.damage;

import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class DamageType {
    public static final DamageType PLAYER = new DamageType(Identifier.ofDefault("player"), "%s이(가) %s에게 %s의 피해를 입혔습니다.");
    public static final DamageType ENTITY = new DamageType(Identifier.ofDefault("entity"), "%s이(가) %s에게 %s의 피해를 입혔습니다.");
    public static final DamageType SELF_DAMAGE = new DamageType(Identifier.ofDefault("self_damage"), "%s이(가) %s의 자해 피해를 입었습니다.");

    public final Identifier id;
    public final String damageDisplayFormat;
    private DamageType(Identifier id, String damageDisplayFormat) {
        this.id = id;
        this.damageDisplayFormat = damageDisplayFormat;
    }
}
