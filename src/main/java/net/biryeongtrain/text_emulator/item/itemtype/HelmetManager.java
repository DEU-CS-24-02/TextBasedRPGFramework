package net.biryeongtrain.text_emulator.item.itemtype;

import net.biryeongtrain.text_emulator.utils.identifier.Identifier;

import java.util.HashMap;
import java.util.Map;

public class HelmetManager {
    private static HelmetManager instance;

    private Map<Identifier,wood_helmet> helmetMap = new HashMap<>();

    private HelmetManager(){}

    public static HelmetManager getInstance(){
        if (instance == null){
            instance=new HelmetManager();
        }
        return instance;
    }

    public void addHelmet(wood_helmet helmet){
        helmetMap.put(helmet.getId(),helmet);
    }
    public wood_helmet getHelmetById(Identifier id){
        return helmetMap.get(id);
    }
}
