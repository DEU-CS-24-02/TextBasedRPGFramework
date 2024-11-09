package net.biryeongtrain.text_emulator.utils;

import org.jetbrains.annotations.Nullable;

public interface Clearable {
    void clear();

    static void clear(@Nullable Object o) {
        if (o instanceof Clearable) {
            ((Clearable) o).clear();
            //매개변수 o는 @Nullable 어노테이션이 붙어 있어, null일 수도 있음을 나타냅니다. 즉, null이 전달될 경우에는 아무 작업도 하지 않습니다.
        }
    }
}
