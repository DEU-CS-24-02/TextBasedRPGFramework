import com.mojang.serialization.JsonOps;
import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.item.ItemStack;
import net.biryeongtrain.text_emulator.item.Items;
import net.biryeongtrain.text_emulator.utils.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class IOTest {
    @Test
    @DisplayName("PathTest")
    public void pathTest() throws ExecutionException, InterruptedException {
        String strPath = "./test";
        Path path = Path.of(strPath);

        ItemStack stack = new ItemStack(Items.AIR);
        Future<Integer> result = Util.IOExecutor.submit(() -> {
            try {
                Files.createDirectories(path);

                var json = ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).result().get();
                Files.writeString(path.resolve("test.json"), json.toString(), StandardCharsets.UTF_8);
            } catch (Throwable t) {
                Main.LOGGER.error(t.getMessage(), t);
            }
            return 1;
        });

        if (result.get() == 1) {
            Main.LOGGER.info("DONE!");
        }

        Util.shutdownExecutors();
    }

}
