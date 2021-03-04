package Main1;

import java.math.BigInteger;

public class MultiThreadFactorial {
   // when smaller nos. factorial is called normal code takes less time but if we handle bigger nos. MT code performs better.
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis(); 
        int[] arr = {100000, 20000, 30000, 50000};
        MyThread[] threads = new MyThread[arr.length]; /

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new MyThread(arr[i]); 
            threads[i].start(); 
        }


        for(int i=0;i<threads.length;i++){
            threads[i].join(); 
        }
       



        for(int i = 0;i < threads.length ; i++){
            System.out.println("for no " + threads[i].num + "  result --> " + threads[i].result);
        }

        long end = System.currentTimeMillis();

        System.out.println("time taken to find factorials " + (end - start));
    }

    public static class MyThread extends Thread { 

        private int num;
        private BigInteger result;

        public MyThread(int num) {  
            this.num = num;          
            this.result = BigInteger.valueOf(1);
        }

        @Override
        public void run() {
         calculate();
        }

        public void calculate() {

            for(int i = 2; i<=num; i++){
                this.result = result.multiply(BigInteger.valueOf(i));  
                
            }
        }


    }
}
