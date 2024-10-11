import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestMain {
    @Test
    @DisplayName("TestMain")
    public void testMain() {
        Identifier correct = Identifier.of("you-can-do-like-this", "or_this_1_2");
        Identifier incorrect = Identifier.of("you-can-do-like-this", "or_this_1_2");
        if (correct.equals(incorrect)) {
            System.out.println("Correct");
        } else {
            System.out.println("Incorrect");
            Assertions.fail();
        }
    }
}
