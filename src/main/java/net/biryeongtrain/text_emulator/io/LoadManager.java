package net.biryeongtrain.text_emulator.io;

import net.biryeongtrain.text_emulator.io.loader.ContentsLoader;

import java.util.HashSet;
import java.util.Set;

public class LoadManager {
    private final Set<ContentsLoader<?>> LOADERS = new HashSet<>();

}
