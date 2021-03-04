package Main1;

import java.util.logging.Logger;

public class Back_Sync {
    /
    public  static void main(String[] args) throws InterruptedException {

        // here both the objects are accounts in Bank.
        Bank obj1 = new Bank(1,300);
        Bank obj2 = new Bank(2,100);


        MyThread[] threads = new MyThread[6]; // creating 6 threads

        /* related to obj1 */

        threads[0] = new MyThread(obj1 , true,50); // it deposits (true)
        threads[1] = new MyThread(obj1,false,100);  // it withdraws (false)
        threads[2] = new MyThread(obj1,true,150);

        /* related to obj2 */

        threads[3] = new MyThread(obj2 , false,1000);
        threads[4] = new MyThread(obj2,true,300);
        threads[5] = new MyThread(obj2,false,150);

        for(int i = 0;i<6;i++){  
            threads[i].start();   
        }
        for(int i=0;i<6;i++) threads[i].join();


            System.out.println("bank balance of obj 1 " + obj1.getBal());
            System.out.println("bank balance of obj 2 " + obj2.getBal());

    }

    public static class MyThread extends Thread{    

        private Bank obj; 

        public boolean isDeposit; 
        private int amount; 

        public MyThread(Bank obj, boolean isDeposit, int amount) {
            this.obj = obj; // it contains both acc. no and balance.
            this.isDeposit = isDeposit;
            this.amount = amount;
        }

        @Override
        public void run() {
            
            try {
                if (this.isDeposit)
                    depsoit();
                else withdraw();
            }
            catch(Exception e){
                
            }
        }
        public void depsoit() throws InterruptedException {    

            synchronized (this.obj) {

                System.out.println("In start of deposit fun. of thread : " + currentThread().getName() +" "+ this.obj);
                //Thread.sleep(10000); 
                obj.setBal(obj.getBal() + amount);
                System.out.println("In end of deposit fun. of thread : " + currentThread().getName() +" "+ this.obj);
                }
        }
        public void withdraw() throws InterruptedException {
            synchronized (this.obj) {
                System.out.println("In start of withdraw fun. of thread : " + currentThread().getName() +" "+ this.obj);
                //Thread.sleep(10000);
                try {
                    if (obj.getBal() - amount < 0)
                        throw new InterruptedException();
                    else
                        obj.setBal(obj.getBal() - amount);
                }
                catch (InterruptedException exc){ // if balance < 0 then Interrupted exception is caught and statement is printed.
                    System.out.println("insufficient balance " + obj.getAcc_no());
                }
                System.out.println("In end of withdraw fun. of thread : " + currentThread().getName() +" "+ this.obj);
            }
        }

    }
}
