package net.biryeongtrain.text_emulator.item.itemtype;

import net.biryeongtrain.text_emulator.entity.Slot;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class item {
    private String name; // 이름
    private Identifier id; // 식별자 Id
    private int atk; // 공격력
    private int dep; // 방어력
    private Slot slot; // 착용부위
    private int dur; // 내구도

    public item(String name, Identifier id, int atk, int dep, Slot slot, int dur){
        this.name = name;
        this.id = id;
        this.atk = atk;
        this.dep = dep;
        this.slot = slot;
        this.dur = dur;
    }

    public String getName(){
        return name;
    }

    public Identifier getId(){
        return id;
    }

    public int getAtk(){
        return atk;
    }

    public int getDep(){
        return dep;
    }

    public Slot getSlot() {
        return slot;
    }

    public int getDur() {
        return dur;
    }
}
