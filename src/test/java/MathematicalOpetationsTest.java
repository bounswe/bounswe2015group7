import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathematicalOpetationsTest {

    static MathematicalOperations mathematicalOperations;

    @BeforeClass
    public static void init() {
        mathematicalOperations = new MathematicalOperations();
    }

    @Test
    public void testBinarPlus() {
        assertEquals(5,mathematicalOperations.binaryPlus(3,2));
        assertEquals(0,mathematicalOperations.binaryPlus(-3,3));
        assertEquals(100,mathematicalOperations.binaryPlus(64,36));
    }

}
