package net.biryeongtrain.text_emulator.item.itemtype;

import net.biryeongtrain.text_emulator.entity.Slot;
import net.biryeongtrain.text_emulator.item.Equipable;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

public class wood_helmet extends item {
    private static final Identifier wood_helmet = new Identifier("my_scenario","wood_helmet");
    private Identifier id;
    public wood_helmet(String name, Identifier id, int atk, int dep, Slot slot, int dur){
        super("가죽 모자",wood_helmet,atk,dep,slot,dur);
    }

    public String getName(){
        return "가죽 모자";
    }

    public Identifier getId(){
        return this.id;
    }

    public int getAtk(){
        return 0;
    }

    public int getDep(){
        return 3;
    }

    public Slot getSlot(){
        return Slot.HEAD;
    }

    public int getDur(){
        return 15;
    }
}

