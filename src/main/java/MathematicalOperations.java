


public class MathematicalOperations {


    public int binaryPlus(int x,int  y){
        return x+y;
    }

    /**
     * Performs integer subtraction operation
     * @param x The minuend
     * @param y The subtrahend
     * @return The difference
     */
    public int binaryMinus(int x, int y) {
        return x-y;
    }

    public int times(int x, int y){ // returns x*y;
    	return x*y;
    }


    /**
     * Performs division operation
     * @param x dividend
     * @param y divisor
     * @return division of x and y (x/y)
     * Implemented by Emre Miraç Paksoy
     *
     */
    public float divide (int x, int y){// returns x/y as a float
        if(y == 0){
            throw new ArithmeticException("divided by 0");
        }
        return (float)(x)/(float)(y);
    }

    double power(double x, int n){
        if(n==0)
            return 1;
        
        if(n<0){
            x = 1.0/x;
            n = -n;
        }
        double ret = power(x,n/2);
        ret = ret * ret;
        if(n%2!=0)
            ret = ret * x;
        return ret;
    }

    public float inverseDivide(int x, int y){
        //returns y/x
        
        if(x == 0){
            throw new ArithmeticException("divided by 0");
        }
        return (float)(y)/(float)(x);
    }
   
    /**
     * Performs modulo operation
     * @param x dividend
     * @param y divisor
     * @return remainder of x and y (x%y)
     * Implemented by Cenk Temeloğlu
     *
     */
    public int remainder(int x, int y) {
    	if(y == 0) {
    		throw new ArithmeticException("divided by 0");
    	}
    	return x % y;
    }

    /*
    * performs unary plus operation
    * @param x is the operand
    * @return the value of the x
    */
    public int unaryPlus(int x) {
    	return x;
    }

    /*
    * performs unary minus operation
    * @param x is the operand
    * @return the unary negation of the x
    */
    public int unaryMinus(int x) {
    	return -x;
    }

    public boolean negation(boolean x) {
        if(x == true){
            return false;
        }else{
            return true;
        }
    }
    
    /**
     * Performs factorial operation
     * @param x The number to factorize
     * @return The factorial of x
     */
    public int factorial(int x){
        if(x<0){
            throw new ArithmeticException("Undefined factorial");
        }else if(x==0 || x==1){
            return 1;
        }else{
            return factorial(x-1)*x;
        }
    }
    /**
     * Takes the absolute value of an integer and returns the result
     * Added by Mustafa Feyzioglu
     */
    public static int absolute(int a) {
        if(a<0) a = (a * -1);
        return a;
    }

}
