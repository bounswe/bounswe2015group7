import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathematicalOperationsTest {

    static MathematicalOperations mathematicalOperations;

    @BeforeClass
    public static void init() {
        mathematicalOperations = new MathematicalOperations();
    }

    @Test
    public void testBinaryPlus() {
        assertEquals(5,mathematicalOperations.binaryPlus(3,2));
        assertEquals(0,mathematicalOperations.binaryPlus(-3,3));
        assertEquals(100,mathematicalOperations.binaryPlus(64,36));
    }

    @Test
    public void testBinaryMinus(){
        assertEquals(3,mathematicalOperations.binaryMinus(7,4));
        assertEquals(-7,mathematicalOperations.binaryMinus(-2,5));
        assertEquals(0,mathematicalOperations.binaryMinus(5,5));
    }
    @Test
    public void testTimes(){
    	assertEquals(21,mathematicalOperations.times(7,3));
        assertEquals(-18,mathematicalOperations.times(-6,3));
        assertEquals(0,mathematicalOperations.times(-123,0));
    }
    
    private static final double DELTA = 1e-5;
    @Test
    public void testDivide(){
        assertEquals(46.142857,mathematicalOperations.divide(323,7), DELTA);
        assertEquals(2,mathematicalOperations.divide(4,2),DELTA);
        assertEquals(-4,mathematicalOperations.divide(-1024,256),DELTA);
    }
    @Test(expected = ArithmeticException.class)
    public void testDivideByZero(){
        assertEquals(2,mathematicalOperations.divide(4,0), DELTA);
    }
    
 @Test
    public void testPower(){
        assertEquals(8,mathematicalOperations.power(2,3));
        assertEquals(1,mathematicalOperations.power(10,0));
        assertEquals(0.000001,mathematicalOperations.power(10,-6));
    }

    @Test
    public void testInverseDivide(){
        assertEquals(8,mathematicalOperations.inverseDivide(1, 31));
        assertEquals(1,mathematicalOperations.inverseDivide(0,10));
        assertEquals(0.000001,mathematicalOperations.inverseDivide(10,-6));
    }
    
    
}
