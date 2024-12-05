package net.biryeongtrain.text_emulator.io.loader;

import net.biryeongtrain.text_emulator.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface ContentsLoader<T> {
    default void load(Path path) {
        this.throwIfFrozen();
        this.loadData(path);
    }

    /**
     * 해당 데이터 종류 로드를 시도합니다. 만약 해당 데이터를 로드하는데 문제가 생겼다면 (다만 단일 파일에 대한 로드는 제외) false를 반환합니다.
     * @param path
     * @return
     */
    boolean loadData(Path path);
    @NotNull
    Registry<T> getRegistry();

    default void throwIfFrozen() {
        if (getRegistry().isFrozen()) {
            throw new IllegalStateException("Registry is frozen. maybe loaded already? : %s ".formatted(getRegistry().getKey()));
        }
    }
}
