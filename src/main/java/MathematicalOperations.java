


public class MathematicalOperations {


    public int binaryPlus(int x,int  y){
        return x+y;
    }

    public int binaryMinus(int x, int y) {
        return x-y;
    }

    public int times(int x, int y){ // returns x*y;
    	return x*y;
    }
    public int divide (int x, int y){// returns x/y
        return x/y;
    }

     public double power (double x, int y){// returns x^y
       double result=1;

       if(y==0){
        return 1;
       }
       if(y<0){
        x=1/x;
       }

        for(int i=0; i<y; i++){
            result = result*x;
        }

        return result;
    }
    
    public float inverseDivide(int x, int y){
        //returns y/x
        
        return y/x;
    
    }
}
