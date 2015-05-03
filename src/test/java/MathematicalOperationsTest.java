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


    /**
     * Performs a valid subtraction.
     * @result The subtraction is performed without error.
     */
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
        assertEquals(8,mathematicalOperations.power(2,3),0.00000001);
        assertEquals(1,mathematicalOperations.power(10,0),0.00000001);
        assertEquals(0.000001,mathematicalOperations.power(10,-6),0.00000001);
    }

    @Test
    public void testInverseDivide(){
        assertEquals(46.142857,mathematicalOperations.inverseDivide(7, 323), DELTA);
        assertEquals(2,mathematicalOperations.inverseDivide(2, 4),DELTA);
        assertEquals(-4,mathematicalOperations.inverseDivide(256, -1024),DELTA);
        
    }
    @Test(expected = ArithmeticException.class)
    public void testInverseDivideByZero(){
        assertEquals(2,mathematicalOperations.inverseDivide(0, 4), DELTA);
    }
    
    @Test
    public void testRemainder() {
    	assertEquals(0, mathematicalOperations.remainder(45, 15));
    	assertEquals(7, mathematicalOperations.remainder(7, 12));
    	assertEquals(2, mathematicalOperations.remainder(9, 7));
    }

    @Test
    public void unaryPlus() {
    	assertEquals(79,mathematicalOperations.unaryPlus(79));
        assertEquals(0,mathematicalOperations.unaryPlus(0));
        assertEquals(-17,mathematicalOperations.unaryPlus(-17));
    }
    
    @Test
    public void unaryMinus(){
    	assertEquals(-547,mathematicalOperations.unaryMinus(547));
        assertEquals(0,mathematicalOperations.unaryMinus(0));
        assertEquals(757,mathematicalOperations.unaryMinus(-757));
    }
    @Test
    public void negation(){
        assertEquals(false,mathematicalOperations.negation(true));
        assertEquals(true,mathematicalOperations.negation(false));
    }
    
    /**
     * Performs a valid factorial.
     * @result The factorial is performed without error.
     */
    @Test
    public void testFactorial() {
    	assertEquals(1, mathematicalOperations.factorial(0));
    	assertEquals(1, mathematicalOperations.factorial(1));
    	assertEquals(5040, mathematicalOperations.factorial(7));
    }
    /**
     * Performs an unvalid factorial.
     * @result The undefined factorial exception.
     */
    @Test(expected = ArithmeticException.class)
    public void testNegativeFactorial(){
        assertEquals(0,mathematicalOperations.factorial(-1));
    }

}
