package Main1;
import java.util.logging.Logger;
public class Bank_sync1 {


// this class is same as bank_sync but here we take lock on the whole bank class rather than an object of bank class(taking lock on the whole class means
// taking lock on every object of the Bank class, this can be achieved in a single step by simply taking a lock on the whole Bank class rather than taking
    // lock on every object one by one)
        public  static void main(String[] args) throws InterruptedException {

            // here both the objects are accounts in Bank.
            Bank obj1 = new Bank(1,300);
            Bank obj2 = new Bank(2,100);


            Main1.Back_Sync.MyThread[] threads = new Main1.Back_Sync.MyThread[6]; // creating 6 threads(array containing obj of MyThread class.)

            /* related to obj1 */

            threads[0] = new Main1.Back_Sync.MyThread(obj1 , true,50); // it deposits (true)
            threads[1] = new Main1.Back_Sync.MyThread(obj1,false,100);  // it withdraws (false)
            threads[2] = new Main1.Back_Sync.MyThread(obj1,true,150);

            /* related to obj2 */

            threads[3] = new Main1.Back_Sync.MyThread(obj2 , false,1000);
            threads[4] = new Main1.Back_Sync.MyThread(obj2,true,300);
            threads[5] = new Main1.Back_Sync.MyThread(obj2,false,150);

            for(int i = 0;i<6;i++){   // even if we change the order of running the thread reversing it or putting it into ant arbitrary order the order of picking
                threads[i].start();    // up the thread may change but the overall ans will always be the same.
            }
            for(int i=0;i<6;i++) threads[i].join();


            System.out.println("bank balance of obj 1 " + obj1.getBal());
            System.out.println("bank balance of obj 2 " + obj2.getBal());

        }

        public static class MyThread extends Thread{    // since this class extends to thread we can apply all the functions inside Thread class to the objects
            //    of MyhThread class.

            private Bank obj; // obj is object of Bank class. we declare it here because we need to take lock at one acc.(i.e while a thread accesses a acc.
            //  no other threads should access this acc.because that account shares same resources with other thread as well, which will return us incorrect
            //  responses(i.e race condition) if multiple threads access it at same time.So a thread must access single acc. at a time.(synchronization,
            //  happening in critical section.)

            public boolean isDeposit; // it will be true when we want to deposit and false if we want to withdraw.
            private int amount; // the amount that we want to transact.(deposit or withdraw)

            public MyThread(Bank obj, boolean isDeposit, int amount) {
                this.obj = obj; // it contains both acc. no and balance.
                this.isDeposit = isDeposit;
                this.amount = amount;
            }

            @Override
            public void run() {
                // currentThread().setPriority(); --> this is how we set priority to a thread i.e the order in which the thread executes first.
                // priority(p) is inbuilt fun. in thread.java it checks  if( p > max(p) || p<min(p)) returns exc. else it reorder our thread group(thread
                // are basically the group in which the the threads are run)  default priority given to all the threads is = 5. if we specify the priority it
                // overrides the parent one i.e 5.
                try {
                    if (this.isDeposit)
                        depsoit();
                    else withdraw();
                }
                catch(Exception e){
                    // no need to print the exc its already been printed when we called the functions.
                }
            }
            public void depsoit() throws InterruptedException {    // note --> the code under synchronized are only synchronized other are not.

                synchronized (Bank.class){// this isn how we take the lock on the whole class synchronized(class_name(on which we want to take lock).class)

                    System.out.println("In start of deposit fun. of thread : " + currentThread().getName() +" "+ this.obj);
                    Thread.sleep(10000); // sleep can also be used for debugging  (it also gives us an idea which threads execute at the same time and which
                    // at at different --> enable it set time to 10sec and we will get the glimpse that is shows us that time gap of print statement which
                    // rectifies our statement written.
                    obj.setBal(obj.getBal() + amount);
                    System.out.println("In end of deposit fun. of thread : " + currentThread().getName() +" "+ this.obj);
                }
            }
            public void withdraw() throws InterruptedException {
                synchronized (Bank.class) {
                    System.out.println("In start of withdraw fun. of thread : " + currentThread().getName() +" "+ this.obj);
                    Thread.sleep(10000);
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


