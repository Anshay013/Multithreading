package Main1;

import java.math.BigInteger;

public class MultiThreadFactorial {
   // when smaller nos. factorial is called normal code takes less time but if we handle bigger nos. MT code performs better.
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis(); // currentTimeMillis gives us the time in milli sec from jan 1st 1970.
        int[] arr = {100000, 20000, 30000, 50000};
        MyThread[] threads = new MyThread[arr.length]; // we create an array of objects of MyThread class.
                                              // it will also store the arr values with their factorial values.

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new MyThread(arr[i]); // this is assigning of value of num = arr[i] and result = 1 in constructor MyThread
            threads[i].start(); // now it stores the value of result for different num i.e arr[i].
           // -->  threads[i].calculate(); // now this wont be called multiThreading because MT is that part which runs in our run fun.(i.e MT is that
            // part which is called via .start() ) So to make this work this we simply call calculate from run function.
        }


        for(int i=0;i<threads.length;i++){
            threads[i].join(); // say while calculating factorial of a no. in MT the calculation takes place in different threads to ease up time so in the
                               // end we need to join all those threads to get our ans.
        }
        // note if we don't use threads we might gets ans = 1 (seen practically) that means we printed the result before the threads were joined, hence we
        // need to join them first to get the correct ans.



        for(int i = 0;i < threads.length ; i++){
            System.out.println("for no " + threads[i].num + "  result --> " + threads[i].result);
        }

        long end = System.currentTimeMillis();

        System.out.println("time taken to find factorials " + (end - start));
    }

    public static class MyThread extends Thread { // writing code using multi-threading hence extending the fun. to Thread.

        private int num;
        private BigInteger result;

        public MyThread(int num) {  // call this constructor from any class to assign value for num and set value of result = 1.(this constructor is just
            this.num = num;            // for initialising).
            this.result = BigInteger.valueOf(1);
        }

        @Override
        public void run() {
         calculate(); // for MT every thing must be part of run function but writing more code in run fun. makes it very big, therefore we can create fun.
            // elsewhere and call it form run function.(Now this becomes MT as calculate fun. is executed from the new thread i.e called via run fun.)
        }

        public void calculate() {

            for(int i = 2; i<=num; i++){
                this.result = result.multiply(BigInteger.valueOf(i));  // multiply is an inbuilt fun. in BigInteger class (note --> a bigInteger can only be multiplied by
                // bigInteger).
            }
        }


    }
}
