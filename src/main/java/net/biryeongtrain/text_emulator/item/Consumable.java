package net.biryeongtrain.text_emulator.item;

public interface Consumable {
    int getUsage();
    void onComsumed();
}
