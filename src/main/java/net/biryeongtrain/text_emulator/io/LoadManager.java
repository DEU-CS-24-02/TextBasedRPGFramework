package net.biryeongtrain.text_emulator.io;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.biryeongtrain.text_emulator.io.loader.ContentsLoader;

import java.util.HashSet;
import java.util.Set;

public class LoadManager {
    private static final Set<ContentsLoader<?>> LOADERS = new ObjectArraySet<>();
    public synchronized static void load() {

    }
    static {
        // TODO : ADD LOADERS
    }
}
