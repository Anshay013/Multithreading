package Main1;

import java.math.BigInteger;

public class Factorial {

    public static void main(String[] args){
        long start = System.currentTimeMillis(); // currentTimeMillis gives us the time in milli sec from jan 1st 1970.
        int []arr = {100000, 20000, 30000, 50000};

        for (int i = 0;i<arr.length; i++){
           System.out.println(calculate(arr[i]) + "\n"); 
        }
        long end = System.currentTimeMillis();

         System.out.println("time taken to find factorials " + (end - start));
    }

    public  static BigInteger calculate(int num){   
           BigInteger bi = BigInteger.valueOf(1);   
        for(int i = 2; i<=num; i++){
            bi = bi.multiply(BigInteger.valueOf(i));  
        }
        return bi;
          }
}
